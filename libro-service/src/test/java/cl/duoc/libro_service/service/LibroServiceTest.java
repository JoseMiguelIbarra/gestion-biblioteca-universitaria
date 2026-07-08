package cl.duoc.libro_service.service;

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

import cl.duoc.libro_service.client.AutorClient;
import cl.duoc.libro_service.client.GeneroClient;
import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.mapper.LibroMapper;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.repository.LibroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de LibroService")
class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @Mock
    private AutorClient autorClient;

    @Mock
    private GeneroClient generoClient;

    @Mock
    private LibroMapper mapper;

    @InjectMocks
    private LibroService service;

    private Libro libro;
    private LibroRequest request;
    private LibroResponse response;
    private AutorResponse autor;
    private GeneroResponse genero;

    @BeforeEach
    void setUp() {
        libro = Libro.builder()
                .id(1L)
                .titulo("Cien anios de soledad")
                .isbn("978-0307474728")
                .idGenero(2L)
                .idAutor(3L)
                .estado(true)
                .build();

        request = LibroRequest.builder()
                .titulo("Cien anios de soledad")
                .isbn("978-0307474728")
                .idGenero(2L)
                .idAutor(3L)
                .estado(true)
                .build();

        autor = new AutorResponse(3L, "Gabriel Garcia Marquez");
        genero = new GeneroResponse(2L, "Realismo magico");

        response = LibroResponse.builder()
                .id(1L)
                .titulo("Cien anios de soledad")
                .isbn("978-0307474728")
                .genero(genero)
                .autor(autor)
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("obtenerTodos arma la respuesta consultando autor y genero de cada libro")
    void obtenerTodos_combinaDatosDeClientes() {
       
        when(repository.findAll()).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(mapper.toResponse(libro, genero, autor)).thenReturn(response);

        List<LibroResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Cien anios de soledad", resultado.get(0).getTitulo());
        verify(autorClient).buscarPorId(3L);
        verify(generoClient).buscarPorId(2L);
    }

    @Test
    @DisplayName("buscarPorId devuelve el libro enriquecido cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(mapper.toResponse(libro, genero, autor)).thenReturn(response);

        LibroResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Gabriel Garcia Marquez", resultado.getAutor().getNombre());
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando el libro no existe")
    void buscarPorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
        
        verifyNoInteractions(autorClient, generoClient);
    }

    @Test
    @DisplayName("crearLibro guarda cuando autor y genero existen")
    void crearLibro_ok() {
        
        when(mapper.toEntity(request)).thenReturn(libro);
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(repository.save(libro)).thenReturn(libro);
        when(mapper.toResponse(libro, genero, autor)).thenReturn(response);

        LibroResponse resultado = service.crearLibro(request);

        assertNotNull(resultado);
        assertEquals("Cien anios de soledad", resultado.getTitulo());
        verify(repository).save(libro);
    }

    @Test
    @DisplayName("crearLibro lanza excepcion si el autor no existe")
    void crearLibro_autorNulo_lanzaExcepcion() {
  
        when(mapper.toEntity(request)).thenReturn(libro);
        when(autorClient.buscarPorId(3L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.crearLibro(request));
     
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearLibro lanza excepcion si el genero no existe")
    void crearLibro_generoNulo_lanzaExcepcion() {
        
        when(mapper.toEntity(request)).thenReturn(libro);
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.crearLibro(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarLibro modifica el libro cuando todo es valido")
    void actualizarLibro_ok() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(repository.save(any(Libro.class))).thenReturn(libro);
        when(mapper.toResponse(any(Libro.class), eq(genero), eq(autor))).thenReturn(response);

        LibroResponse resultado = service.actualizarLibro(1L, request);

        assertNotNull(resultado);
        verify(repository).save(libro);
    }
    @Test
    @DisplayName("actualizarLibro lanza excepcion si el autor no existe")
    void actualizarLibro_autorNulo_lanzaExcepcion() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.actualizarLibro(1L, request));
        verify(repository, never()).save(any());
}

    @Test
    @DisplayName("actualizarLibro lanza excepcion si el genero no existe")
    void actualizarLibro_generoNulo_lanzaExcepcion() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(null);

        
        assertThrows(NoSuchElementException.class, () -> service.actualizarLibro(1L, request));
        verify(repository, never()).save(any());
}

    @Test
    @DisplayName("actualizarLibro lanza excepcion cuando el libro no existe")
    void actualizarLibro_inexistente_lanzaExcepcion() {
       
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarLibro(99L, request));
        verify(repository, never()).save(any());
        verifyNoInteractions(autorClient, generoClient);
    }

    @Test
    @DisplayName("eliminarLibro borra cuando el libro existe")
    void eliminarLibro_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarLibro(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarLibro lanza excepcion cuando el libro no existe")
    void eliminarLibro_inexistente() {
       
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarLibro(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarLibroPorAutor devuelve los libros del autor indicado")
    void buscarLibroPorAutor() {
        
        when(repository.findByIdAutor(3L)).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(mapper.toResponse(libro, genero, autor)).thenReturn(response);

        List<LibroResponse> resultado = service.buscarLibroPorAutor(3L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdAutor(3L);
    }
    @Test
    @DisplayName("buscarLibroPorGenero devuelve los libros del genero indicado")
    void buscarLibroPorGenero() {
        
        when(repository.findByIdGenero(2L)).thenReturn(List.of(libro));
        when(autorClient.buscarPorId(3L)).thenReturn(autor);
        when(generoClient.buscarPorId(2L)).thenReturn(genero);
        when(mapper.toResponse(libro, genero, autor)).thenReturn(response);

        List<LibroResponse> resultado = service.buscarLibroPorGenero(2L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdGenero(2L);
}
}