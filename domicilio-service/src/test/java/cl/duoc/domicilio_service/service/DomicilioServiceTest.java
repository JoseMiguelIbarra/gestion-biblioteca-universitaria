package cl.duoc.domicilio_service.service;

import cl.duoc.domicilio_service.dto.DomicilioRequest;
import cl.duoc.domicilio_service.dto.DomicilioResponse;
import cl.duoc.domicilio_service.mapper.DomicilioMapper;
import cl.duoc.domicilio_service.model.Domicilio;
import cl.duoc.domicilio_service.repository.DomicilioRepository;
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
@DisplayName("Pruebas unitarias de DomicilioService")
class DomicilioServiceTest {

    @Mock
    private DomicilioRepository repository;

    @Mock
    private DomicilioMapper mapper;

    @InjectMocks
    private DomicilioService service;

    private Domicilio domicilio;
    private DomicilioRequest request;
    private DomicilioResponse response;

    @BeforeEach
    void setUp() {
        domicilio = Domicilio.builder()
                .id(1L).direccion("Av. Siempre Viva 742").comuna("Santiago").pais("Chile").build();

        request = DomicilioRequest.builder()
                .direccion("Av. Siempre Viva 742").comuna("Santiago").pais("Chile").build();

        response = DomicilioResponse.builder()
                .id(1L).direccion("Av. Siempre Viva 742").comuna("Santiago").pais("Chile").build();
    }

    @Test
    @DisplayName("obtenerTodos devuelve la lista mapeada")
    void obtenerTodos_devuelveLista() {
       
        when(repository.findAll()).thenReturn(List.of(domicilio));
        when(mapper.toResponse(domicilio)).thenReturn(response);

        List<DomicilioResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getComuna());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorId devuelve el domicilio cuando existe")
    void buscarPorId_existente() {
       
        when(repository.findById(1L)).thenReturn(Optional.of(domicilio));
        when(mapper.toResponse(domicilio)).thenReturn(response);

        DomicilioResponse resultado = service.buscarPorId(1L);

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
        
        when(mapper.toEntity(request)).thenReturn(domicilio);
        when(repository.save(domicilio)).thenReturn(domicilio);
        when(mapper.toResponse(domicilio)).thenReturn(response);

        DomicilioResponse resultado = service.crear(request);

        assertNotNull(resultado);
        verify(repository).save(domicilio);
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
    @DisplayName("actualizar modifica el domicilio cuando existe")
    void actualizar_existente() {
        
        DomicilioRequest nuevo = DomicilioRequest.builder()
                .direccion("Calle Falsa 123").comuna("Providencia").pais("Chile").build();
        when(repository.findById(1L)).thenReturn(Optional.of(domicilio));
        when(repository.save(any(Domicilio.class))).thenReturn(domicilio);
        when(mapper.toResponse(any(Domicilio.class))).thenReturn(response);

        DomicilioResponse resultado = service.actualizar(1L, nuevo);

        assertNotNull(resultado);
        assertEquals("Calle Falsa 123", domicilio.getDireccion());
        assertEquals("Providencia", domicilio.getComuna());
        verify(repository).save(domicilio);
    }

    @Test
    @DisplayName("actualizar lanza excepcion cuando no existe")
    void actualizar_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizar(99L, request));
        verify(repository, never()).save(any());
    }
}