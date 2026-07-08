package cl.duoc.genero_service.service;

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

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.mapper.GeneroMapper;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.repository.GeneroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de GeneroService")
class GeneroServiceTest {

    @Mock
    private GeneroRepository repository;

    @Mock
    private GeneroMapper mapper;

    @InjectMocks
    private GeneroService service;

    private Genero genero;
    private GeneroRequest request;
    private GeneroResponse response;

    @BeforeEach
    void setUp() {
        genero = Genero.builder().id(1L).nombre("Realismo magico").build();
        request = GeneroRequest.builder().nombre("Realismo magico").build();
        response = GeneroResponse.builder().id(1L).nombre("Realismo magico").build();
    }

    @Test
    @DisplayName("obtenerTodos devuelve la lista de generos mapeada")
    void obtenerTodos_devuelveLista() {
        
        when(repository.findAll()).thenReturn(List.of(genero));
        when(mapper.toResponse(genero)).thenReturn(response);

        List<GeneroResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Realismo magico", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId devuelve el genero cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(genero));
        when(mapper.toResponse(genero)).thenReturn(response);

        GeneroResponse resultado = service.buscarPorId(1L);

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
    @DisplayName("crearGenero guarda y devuelve la respuesta")
    void crearGenero_ok() {
       
        when(mapper.toEntity(request)).thenReturn(genero);
        when(repository.save(genero)).thenReturn(genero);
        when(mapper.toResponse(genero)).thenReturn(response);

        GeneroResponse resultado = service.crearGenero(request);

        assertNotNull(resultado);
        verify(repository).save(genero);
    }

    @Test
    @DisplayName("eliminarGenero borra cuando existe")
    void eliminarGenero_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarGenero(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarGenero lanza excepcion cuando no existe")
    void eliminarGenero_inexistente() {
        
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarGenero(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("actualizar modifica el genero cuando existe")
    void actualizar_existente() {
        
        GeneroRequest nuevo = GeneroRequest.builder().nombre("Novela negra").build();
        when(repository.findById(1L)).thenReturn(Optional.of(genero));
        when(repository.save(any(Genero.class))).thenReturn(genero);
        when(mapper.toResponse(any(Genero.class))).thenReturn(response);

        GeneroResponse resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Novela negra", genero.getNombre());
        verify(repository).save(genero);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando no existe")
    void actualizar_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
    }
}