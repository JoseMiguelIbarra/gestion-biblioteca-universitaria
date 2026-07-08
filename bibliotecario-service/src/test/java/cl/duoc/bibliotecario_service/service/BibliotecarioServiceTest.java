package cl.duoc.bibliotecario_service.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.mapper.BibliotecarioMapper;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.repository.BibliotecarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de BibliotecarioService")
class BibliotecarioServiceTest {

    @Mock
    private BibliotecarioRepository repository;

    @Mock
    private BibliotecarioMapper mapper;

    @InjectMocks
    private BibliotecarioService service;

    private Bibliotecario bibliotecario;
    private BibliotecarioRequest request;
    private BibliotecarioResponse response;

    @BeforeEach
    void setUp() {
        bibliotecario = Bibliotecario.builder()
                .id(1L).nombre("Ana Soto").edad(35)
                .fechaIngreso(LocalDate.of(2020, 1, 15)).build();

        request = BibliotecarioRequest.builder()
                .nombre("Ana Soto").edad(35)
                .fechaIngreso(LocalDate.of(2020, 1, 15)).build();

        response = BibliotecarioResponse.builder()
                .id(1L).nombre("Ana Soto").edad(35)
                .fechaIngreso(LocalDate.of(2020, 1, 15)).build();
    }

    @Test
    @DisplayName("obtenerTodos devuelve la lista mapeada")
    void obtenerTodos_devuelveLista() {
        
        when(repository.findAll()).thenReturn(List.of(bibliotecario));
        when(mapper.toResponse(bibliotecario)).thenReturn(response);

        List<BibliotecarioResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Ana Soto", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId devuelve el bibliotecario cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(bibliotecario));
        when(mapper.toResponse(bibliotecario)).thenReturn(response);

        BibliotecarioResponse resultado = service.buscarPorId(1L);

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
    
        when(mapper.toEntity(request)).thenReturn(bibliotecario);
        when(repository.save(bibliotecario)).thenReturn(bibliotecario);
        when(mapper.toResponse(bibliotecario)).thenReturn(response);

        BibliotecarioResponse resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("Ana Soto", resultado.getNombre());
        verify(repository).save(bibliotecario);
    }

    @Test
    @DisplayName("actualizar modifica el bibliotecario cuando existe")
    void actualizar_existente() {
       
        BibliotecarioRequest nuevo = BibliotecarioRequest.builder()
                .nombre("Ana Soto Reyes").edad(36)
                .fechaIngreso(LocalDate.of(2019, 5, 1)).build();
        when(repository.findById(1L)).thenReturn(Optional.of(bibliotecario));
        when(repository.save(any(Bibliotecario.class))).thenReturn(bibliotecario);
        when(mapper.toResponse(any(Bibliotecario.class))).thenReturn(response);

        BibliotecarioResponse resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Ana Soto Reyes", bibliotecario.getNombre());
        assertEquals(36, bibliotecario.getEdad());
        verify(repository).save(bibliotecario);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando no existe")
    void actualizar_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
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
}