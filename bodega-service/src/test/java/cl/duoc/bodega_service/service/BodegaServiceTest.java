package cl.duoc.bodega_service.service;

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

import cl.duoc.bodega_service.dto.BodegaRequest;
import cl.duoc.bodega_service.dto.BodegaResponse;
import cl.duoc.bodega_service.mapper.BodegaMapper;
import cl.duoc.bodega_service.model.Bodega;
import cl.duoc.bodega_service.repository.BodegaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de BodegaService")
class BodegaServiceTest {

    @Mock
    private BodegaRepository repository;

    @Mock
    private BodegaMapper mapper;

    @InjectMocks
    private BodegaService service;

    private Bodega bodega;
    private BodegaRequest request;
    private BodegaResponse response;

    @BeforeEach
    void setUp() {
        bodega = Bodega.builder()
                .id(1L).nombre("Bodega Central").ubicacion("Subterraneo")
                .capacidad(500L).build();

        request = BodegaRequest.builder()
                .nombre("Bodega Central").ubicacion("Subterraneo")
                .capacidad(500L).build();

        response = BodegaResponse.builder()
                .id(1L).nombre("Bodega Central").ubicacion("Subterraneo")
                .capacidad(500L).build();
    }

    @Test
    @DisplayName("obtenerTodas devuelve la lista mapeada")
    void obtenerTodas_devuelveLista() {
        
        when(repository.findAll()).thenReturn(List.of(bodega));
        when(mapper.toResponse(bodega)).thenReturn(response);

        List<BodegaResponse> resultado = service.obtenerTodas();

        assertEquals(1, resultado.size());
        assertEquals("Bodega Central", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarBodegaPorId devuelve la bodega cuando existe")
    void buscarBodegaPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(bodega));
        when(mapper.toResponse(bodega)).thenReturn(response);

        BodegaResponse resultado = service.buscarBodegaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarBodegaPorId lanza excepcion cuando no existe")
    void buscarBodegaPorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarBodegaPorId(99L));
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("crear guarda y devuelve la respuesta")
    void crear_ok() {
        
        when(mapper.toEntity(request)).thenReturn(bodega);
        when(repository.save(bodega)).thenReturn(bodega);
        when(mapper.toResponse(bodega)).thenReturn(response);

        BodegaResponse resultado = service.crear(request);

        assertNotNull(resultado);
        verify(repository).save(bodega);
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
    @DisplayName("actualizar modifica la bodega cuando existe")
    void actualizar_existente() {
        
        BodegaRequest nuevo = BodegaRequest.builder()
                .nombre("Bodega Norte").ubicacion("Piso 2").capacidad(800L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(bodega));
        when(repository.save(any(Bodega.class))).thenReturn(bodega);
        when(mapper.toResponse(any(Bodega.class))).thenReturn(response);

        BodegaResponse resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Bodega Norte", bodega.getNombre());
        assertEquals(800L, bodega.getCapacidad());
        verify(repository).save(bodega);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando no existe")
    void actualizar_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
    }
}