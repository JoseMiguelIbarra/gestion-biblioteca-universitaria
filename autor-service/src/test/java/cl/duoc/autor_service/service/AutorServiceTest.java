package cl.duoc.autor_service.service;

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

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de AutorService")
class AutorServiceTest {

    @Mock
    private AutorRepository repository;

    @Mock
    private AutorMapper mapper;

    @InjectMocks
    private AutorService service;

    private Autor autor;
    private AutorRequest request;
    private AutorResponse response;

    @BeforeEach
    void setUp() {
        autor = Autor.builder()
                .id(1L)
                .nombre("Gabriel Garcia Marquez")
                .fecha_nac(LocalDate.of(1927, 3, 6))
                .build();

        request = AutorRequest.builder()
                .nombre("Gabriel Garcia Marquez")
                .fecha_nac(LocalDate.of(1927, 3, 6))
                .build();

        response = AutorResponse.builder()
                .id(1L)
                .nombre("Gabriel Garcia Marquez")
                .fecha_nac(LocalDate.of(1927, 3, 6))
                .build();
    }

    @Test
    @DisplayName("obtenerTodos devuelve la lista de autores mapeada")
    void obtenerTodos_devuelveListaMapeada() {
        
        when(repository.findAll()).thenReturn(List.of(autor));
        when(mapper.toResponse(autor)).thenReturn(response);
        
        List<AutorResponse> resultado = service.obtenerTodos();
        
        assertEquals(1, resultado.size());
        assertEquals("Gabriel Garcia Marquez", resultado.get(0).getNombre());
        verify(repository).findAll();
        verify(mapper).toResponse(autor);
    }

    @Test
    @DisplayName("buscarPorId devuelve el autor cuando existe")
    void buscarPorId_existente_devuelveAutor() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(autor));
        when(mapper.toResponse(autor)).thenReturn(response);
        
        AutorResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando el autor no existe")
    void buscarPorId_inexistente_lanzaExcepcion() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
        verify(repository).findById(99L);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("crear guarda el autor y devuelve la respuesta")
    void crear_guardaYDevuelveRespuesta() {
        
        when(mapper.toEntity(request)).thenReturn(autor);
        when(repository.save(autor)).thenReturn(autor);
        when(mapper.toResponse(autor)).thenReturn(response);

        AutorResponse resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("Gabriel Garcia Marquez", resultado.getNombre());
        verify(repository).save(autor);
    }

    @Test
    @DisplayName("eliminar borra cuando el autor existe")
    void eliminar_existente_borra() {

        when(repository.existsById(1L)).thenReturn(true);
        
        service.eliminar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepcion cuando el autor no existe")
    void eliminar_inexistente_lanzaExcepcion() {
        
        when(repository.existsById(99L)).thenReturn(false);
        
        assertThrows(NoSuchElementException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("actualizar modifica y devuelve el autor cuando existe")
    void actualizar_existente_actualiza() {
        
        AutorRequest nuevosDatos = AutorRequest.builder()
                .nombre("Mario Vargas Llosa")
                .fecha_nac(LocalDate.of(1936, 3, 28))
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(autor));
        when(repository.save(any(Autor.class))).thenReturn(autor);
        when(mapper.toResponse(any(Autor.class))).thenReturn(response);

        
        AutorResponse resultado = service.actualizar(1L, nuevosDatos);

        assertNotNull(resultado);

        assertEquals("Mario Vargas Llosa", autor.getNombre());
        assertEquals(LocalDate.of(1936, 3, 28), autor.getFecha_nac());
        verify(repository).save(autor);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando el autor no existe")
    void actualizar_inexistente_lanzaExcepcion() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
    }
}