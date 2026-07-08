package cl.duoc.usuario_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.usuario_service.client.DomicilioClient;
import cl.duoc.usuario_service.client.NombreClient;
import cl.duoc.usuario_service.dto.DomicilioResponse;
import cl.duoc.usuario_service.dto.NombreResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.mapper.UsuarioMapper;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private DomicilioClient domicilioClient;

    @Mock
    private NombreClient nombreClient;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioRequest request;
    private UsuarioResponse response;
    private DomicilioResponse domicilio;
    private NombreResponse nombre;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L).idNombre(2L).rut("12345678-9").edad(40L)
                .idDomicilio(3L).fechaNacimiento(LocalDate.of(1985, 6, 20)).build();

        request = UsuarioRequest.builder()
                .idNombre(2L).rut("12345678-9").edad(40L)
                .idDomicilio(3L).fechaNacimiento(LocalDate.of(1985, 6, 20)).build();

        nombre = new NombreResponse(2L, "Juan", "Pablo", "Perez", "Soto");
        domicilio = new DomicilioResponse(3L, "Av. Siempre Viva 742", "Santiago", "Chile");

        response = UsuarioResponse.builder()
                .id(1L).nombre(nombre).rut("12345678-9").edad(40L)
                .domicilio(domicilio).fechaNacimiento(LocalDate.of(1985, 6, 20)).build();
    }

    @Test
    @DisplayName("obtenerTodos arma la respuesta consultando domicilio y nombre de cada usuario")
    void obtenerTodos_combinaConClientes() {
        
        when(repository.findAll()).thenReturn(List.of(usuario));
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(mapper.toResponse(usuario, domicilio, nombre)).thenReturn(response);

        List<UsuarioResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        verify(domicilioClient).buscarDomicilioPorId(3L);
        verify(nombreClient).buscarNombrePorId(2L);
    }

    @Test
    @DisplayName("buscarPorId devuelve el usuario enriquecido cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(mapper.toResponse(usuario, domicilio, nombre)).thenReturn(response);

        UsuarioResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando el usuario no existe")
    void buscarPorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
        verifyNoInteractions(domicilioClient, nombreClient);
    }

    @Test
    @DisplayName("crearUsuario guarda cuando domicilio y nombre existen")
    void crearUsuario_ok() {
        
        when(mapper.toEntity(request)).thenReturn(usuario);
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario, domicilio, nombre)).thenReturn(response);

        UsuarioResponse resultado = service.crearUsuario(request);

        assertNotNull(resultado);
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("crearUsuario lanza excepcion si el domicilio no existe")
    void crearUsuario_domicilioNulo() {
        
        when(mapper.toEntity(request)).thenReturn(usuario);
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.crearUsuario(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearUsuario lanza excepcion si el nombre no existe")
    void crearUsuario_nombreNulo() {
        
        when(mapper.toEntity(request)).thenReturn(usuario);
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.crearUsuario(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarUsuario modifica el usuario cuando todo es valido")
    void actualizarUsuario_ok() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(any(Usuario.class), eq(domicilio), eq(nombre))).thenReturn(response);

        UsuarioResponse resultado = service.actualizarUsuario(1L, request);

        assertNotNull(resultado);
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("actualizarUsuario lanza excepcion si el domicilio no existe")
    void actualizarUsuario_domicilioNulo_lanzaExcepcion() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.actualizarUsuario(1L, request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarUsuario lanza excepcion si el nombre no existe")
    void actualizarUsuario_nombreNulo_lanzaExcepcion() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.actualizarUsuario(1L, request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarUsuario lanza excepcion cuando el usuario no existe")
    void actualizarUsuario_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarUsuario(99L, request));
        verify(repository, never()).save(any());
        verifyNoInteractions(domicilioClient, nombreClient);
    }

    @Test
    @DisplayName("eliminarUsuario borra cuando existe")
    void eliminarUsuario_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarUsuario(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarUsuario lanza excepcion cuando no existe")
    void eliminarUsuario_inexistente() {
        
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarUsuario(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarUsuariosPorIdDomicilio devuelve los usuarios del domicilio")
    void buscarUsuariosPorIdDomicilio() {
        
        when(repository.findByIdDomicilio(3L)).thenReturn(List.of(usuario));
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(mapper.toResponse(usuario, domicilio, nombre)).thenReturn(response);

        List<UsuarioResponse> resultado = service.buscarUsuariosPorIdDomicilio(3L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdDomicilio(3L);
    }

    @Test
    @DisplayName("buscarUsuarioPorIdNombre devuelve los usuarios del nombre indicado")
    void buscarUsuarioPorIdNombre() {
        
        when(repository.findByIdNombre(2L)).thenReturn(List.of(usuario));
        when(nombreClient.buscarNombrePorId(2L)).thenReturn(nombre);
        when(domicilioClient.buscarDomicilioPorId(3L)).thenReturn(domicilio);
        when(mapper.toResponse(usuario, domicilio, nombre)).thenReturn(response);

        List<UsuarioResponse> resultado = service.buscarUsuarioPorIdNombre(2L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdNombre(2L);
    }
}