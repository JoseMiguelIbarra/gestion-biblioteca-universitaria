package cl.duoc.estante_service.service;

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

import cl.duoc.estante_service.client.BodegaClient;
import cl.duoc.estante_service.dto.BodegaResponse;
import cl.duoc.estante_service.dto.EstanteRequest;
import cl.duoc.estante_service.dto.EstanteResponse;
import cl.duoc.estante_service.mapper.EstanteMapper;
import cl.duoc.estante_service.model.Estante;
import cl.duoc.estante_service.repository.EstanteRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de EstanteService")
class EstanteServiceTest {

    @Mock
    private EstanteRepository repository;

    @Mock
    private BodegaClient bodegaClient;

    @Mock
    private EstanteMapper mapper;

    @InjectMocks
    private EstanteService service;

    private Estante estante;
    private EstanteRequest request;
    private EstanteResponse response;
    private BodegaResponse bodega;

    @BeforeEach
    void setUp() {
        estante = Estante.builder().id(1L).numero(10L).idBodega(2L).capacidad(100L).build();
        request = EstanteRequest.builder().numero(10L).idBodega(2L).capacidad(100L).build();
        bodega = new BodegaResponse(2L, "Bodega Central", "Subterraneo", 500L);
        response = EstanteResponse.builder().id(1L).numero(10L).bodega(bodega).capacidad(100L).build();
    }

    @Test
    @DisplayName("obtenerTodos arma la respuesta consultando la bodega de cada estante")
    void obtenerTodos_combinaConBodega() {
        // Given
        when(repository.findAll()).thenReturn(List.of(estante));
        when(bodegaClient.buscarPorId(2L)).thenReturn(bodega);
        when(mapper.toResponse(estante, bodega)).thenReturn(response);

        List<EstanteResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        verify(bodegaClient).buscarPorId(2L);
    }

    @Test
    @DisplayName("buscarEstantePorId devuelve el estante enriquecido cuando existe")
    void buscarEstantePorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(estante));
        when(bodegaClient.buscarPorId(2L)).thenReturn(bodega);
        when(mapper.toResponse(estante, bodega)).thenReturn(response);

        EstanteResponse resultado = service.buscarEstantePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarEstantePorId lanza excepcion cuando no existe")
    void buscarEstantePorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarEstantePorId(99L));
        verifyNoInteractions(bodegaClient);
    }

    @Test
    @DisplayName("crearEstante guarda cuando la bodega existe")
    void crearEstante_ok() {
        
        when(mapper.toEntity(request)).thenReturn(estante);
        when(bodegaClient.buscarPorId(2L)).thenReturn(bodega);
        when(repository.save(estante)).thenReturn(estante);
        when(mapper.toResponse(estante, bodega)).thenReturn(response);

        EstanteResponse resultado = service.crearEstante(request);

        assertNotNull(resultado);
        verify(repository).save(estante);
    }

    @Test
    @DisplayName("crearEstante lanza excepcion si la bodega no existe")
    void crearEstante_bodegaNula() {
        
        when(mapper.toEntity(request)).thenReturn(estante);
        when(bodegaClient.buscarPorId(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.crearEstante(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEstante modifica el estante cuando todo es valido")
    void actualizarEstante_ok() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(estante));
        when(bodegaClient.buscarPorId(2L)).thenReturn(bodega);
        when(repository.save(any(Estante.class))).thenReturn(estante);
        when(mapper.toResponse(any(Estante.class), eq(bodega))).thenReturn(response);

        EstanteResponse resultado = service.actualizarEstante(1L, request);

        assertNotNull(resultado);
        verify(repository).save(estante);
    }

    @Test
    @DisplayName("actualizarEstante lanza excepcion si la bodega no existe")
    void actualizarEstante_bodegaNula() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(estante));
        when(bodegaClient.buscarPorId(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> service.actualizarEstante(1L, request));
        verify(repository, never()).save(any());
    }
    @Test
    @DisplayName("actualizarEstante lanza excepcion cuando el estante no existe")
    void actualizarEstante_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarEstante(99L, request));
        verify(repository, never()).save(any());
        verifyNoInteractions(bodegaClient);
    }

    @Test
    @DisplayName("eliminarEstante borra cuando existe")
    void eliminarEstante_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarEstante(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarEstante lanza excepcion cuando no existe")
    void eliminarEstante_inexistente() {
        
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarEstante(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarEstantePorIdBodega devuelve los estantes de la bodega")
    void buscarEstantePorIdBodega() {
        
        when(repository.findByIdBodega(2L)).thenReturn(List.of(estante));
        when(bodegaClient.buscarPorId(2L)).thenReturn(bodega);
        when(mapper.toResponse(estante, bodega)).thenReturn(response);

        List<EstanteResponse> resultado = service.buscarEstantePorIdBodega(2L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdBodega(2L);
    }
}