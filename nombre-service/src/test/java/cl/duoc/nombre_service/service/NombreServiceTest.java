package cl.duoc.nombre_service.service;

import cl.duoc.nombre_service.dto.NombreRequest;
import cl.duoc.nombre_service.dto.NombreResponse;
import cl.duoc.nombre_service.mapper.NombreMapper;
import cl.duoc.nombre_service.model.Nombre;
import cl.duoc.nombre_service.repository.NombreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de NombreService")
class NombreServiceTest {

    @Mock
    private NombreRepository repository;

    @Mock
    private NombreMapper mapper;

    @InjectMocks
    private NombreService service;

    private Nombre nombre;
    private NombreRequest request;
    private NombreResponse response;

    @BeforeEach
    void setUp() {
        nombre = Nombre.builder()
                .id(1L).nombre("Juan").segundoNombre("Pablo")
                .apPaterno("Perez").apMaterno("Soto").build();

        request = NombreRequest.builder()
                .nombre("Juan").segundoNombre("Pablo")
                .apPaterno("Perez").apMaterno("Soto").build();

        response = NombreResponse.builder()
                .id(1L).nombre("Juan").segundoNombre("Pablo")
                .apPaterno("Perez").apMaterno("Soto").build();
    }

    @Test
    @DisplayName("obtenerTodos devuelve la lista mapeada")
    void obtenerTodos_devuelveLista() {
        
        when(repository.findAll()).thenReturn(List.of(nombre));
        when(mapper.toResponse(nombre)).thenReturn(response);

        List<NombreResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId devuelve el nombre cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(nombre));
        when(mapper.toResponse(nombre)).thenReturn(response);

        NombreResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("crear guarda y devuelve la respuesta")
    void crear_ok() {
        
        when(mapper.toEntity(request)).thenReturn(nombre);
        when(repository.save(nombre)).thenReturn(nombre);
        when(mapper.toResponse(nombre)).thenReturn(response);

        NombreResponse resultado = service.crear(request);

        assertNotNull(resultado);
        verify(repository).save(nombre);
    }

    @Test
    @DisplayName("eliminar borra cuando existe")
    void eliminar_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepcion cuando no existe")
    void eliminar_inexistente() {
        
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("actualizar modifica el nombre cuando existe")
    void actualizar_existente() {
        
        NombreRequest nuevo = NombreRequest.builder()
                .nombre("Maria").segundoNombre("Jose")
                .apPaterno("Gonzalez").apMaterno("Rojas").build();
        when(repository.findById(1L)).thenReturn(Optional.of(nombre));
        when(repository.save(any(Nombre.class))).thenReturn(nombre);
        when(mapper.toResponse(any(Nombre.class))).thenReturn(response);

        NombreResponse resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Maria", nombre.getNombre());
        assertEquals("Gonzalez", nombre.getApPaterno());
        verify(repository).save(nombre);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando no existe")
    void actualizar_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
    }
}