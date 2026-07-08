package cl.duoc.turnos_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
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

import cl.duoc.turnos_service.client.BibliotecarioClient;
import cl.duoc.turnos_service.dto.BibliotecarioResponse;
import cl.duoc.turnos_service.dto.TurnoRequest;
import cl.duoc.turnos_service.dto.TurnoResponse;
import cl.duoc.turnos_service.mapper.TurnoMapper;
import cl.duoc.turnos_service.model.TipoTurno;
import cl.duoc.turnos_service.model.Turno;
import cl.duoc.turnos_service.repository.TurnoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de TurnoService")
class TurnoServiceTest {

    @Mock
    private TurnoRepository repository;

    @Mock
    private TurnoMapper mapper;

    @Mock
    private BibliotecarioClient bibliotecarioClient;

    @InjectMocks
    private TurnoService service;

    private Turno turno;
    private TurnoRequest request;
    private TurnoResponse response;
    private BibliotecarioResponse bibliotecario;

    @BeforeEach
    void setUp() {
        turno = Turno.builder()
                .id(1L).idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(13, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();

        request = TurnoRequest.builder()
                .idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(13, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();

        bibliotecario = BibliotecarioResponse.builder()
                .id(5L).nombre("Ana Soto").edad(35)
                .fechaIngreso(LocalDate.of(2020, 1, 15)).build();

        response = TurnoResponse.builder()
                .id(1L).bibliotecario(bibliotecario)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(13, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();
    }

    @Test
    @DisplayName("obtenerTodos arma la respuesta consultando el bibliotecario de cada turno")
    void obtenerTodos_combinaConBibliotecario() {
       
        when(repository.findAll()).thenReturn(List.of(turno));
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        List<TurnoResponse> resultado = service.obtenerTodos();

        assertEquals(1, resultado.size());
        verify(bibliotecarioClient).buscarPorId(5L);
    }

    @Test
    @DisplayName("buscarPorId devuelve el turno cuando existe")
    void buscarPorId_existente() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(turno));
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        TurnoResponse resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarPorId lanza excepcion cuando no existe")
    void buscarPorId_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.buscarPorId(99L));
        verifyNoInteractions(bibliotecarioClient);
    }

    @Test
    @DisplayName("buscarPorBibliotecario valida el bibliotecario y mapea sus turnos")
    void buscarPorBibliotecario() {
        
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(repository.findByIdBibliotecario(5L)).thenReturn(List.of(turno));
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        List<TurnoResponse> resultado = service.buscarPorBibliotecario(5L);

        assertEquals(1, resultado.size());
        verify(repository).findByIdBibliotecario(5L);
    }

    @Test
    @DisplayName("crearTurno guarda cuando el horario es valido")
    void crearTurno_ok() {
        
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(mapper.toEntity(request)).thenReturn(turno);
        when(repository.save(turno)).thenReturn(turno);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        TurnoResponse resultado = service.crearTurno(request);

        assertNotNull(resultado);
        verify(repository).save(turno);
    }

    @Test
    @DisplayName("crearTurno lanza excepcion si la hora de fin no es posterior a la de inicio")
    void crearTurno_horarioInvalido() {
        
        TurnoRequest invalido = TurnoRequest.builder()
                .idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(13, 0))
                .horaFin(LocalTime.of(9, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.crearTurno(invalido));
        
        verify(repository, never()).save(any());
        verifyNoInteractions(bibliotecarioClient);
    }

    @Test
    @DisplayName("actualizarTurno modifica el turno cuando existe y el horario es valido")
    void actualizarTurno_ok() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(turno));
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(repository.save(any(Turno.class))).thenReturn(turno);
        when(mapper.toResponse(any(Turno.class), eq(bibliotecario))).thenReturn(response);

        TurnoResponse resultado = service.actualizarTurno(1L, request);

        assertNotNull(resultado);
        verify(repository).save(turno);
    }

    @Test
    @DisplayName("actualizarTurno lanza excepcion cuando el turno no existe")
    void actualizarTurno_inexistente() {
        
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.actualizarTurno(99L, request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("eliminarTurno borra cuando existe")
    void eliminarTurno_existente() {
        
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarTurno(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarTurno lanza excepcion cuando no existe")
    void eliminarTurno_inexistente() {
        
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.eliminarTurno(99L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarPorFecha devuelve los turnos de la fecha indicada")
    void buscarPorFecha() {
    
        when(repository.findByFechaTurno(LocalDate.of(2026, 7, 1))).thenReturn(List.of(turno));
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        List<TurnoResponse> resultado = service.buscarPorFecha(LocalDate.of(2026, 7, 1));

        assertEquals(1, resultado.size());
        verify(repository).findByFechaTurno(LocalDate.of(2026, 7, 1));
    }

    @Test
    @DisplayName("buscarPorTipo devuelve los turnos del tipo indicado")
    void buscarPorTipo() {
        
        when(repository.findByTipoTurno(TipoTurno.MANANA)).thenReturn(List.of(turno));
        when(bibliotecarioClient.buscarPorId(5L)).thenReturn(bibliotecario);
        when(mapper.toResponse(turno, bibliotecario)).thenReturn(response);

        List<TurnoResponse> resultado = service.buscarPorTipo(TipoTurno.MANANA);

        assertEquals(1, resultado.size());
        verify(repository).findByTipoTurno(TipoTurno.MANANA);
    }

    @Test
    @DisplayName("crearTurno lanza excepcion si la hora de fin es igual a la de inicio")
    void crearTurno_horaIgual_lanzaExcepcion() {
        
        TurnoRequest invalido = TurnoRequest.builder()
                .idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(9, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.crearTurno(invalido));
        verify(repository, never()).save(any());
        verifyNoInteractions(bibliotecarioClient);
    }

    @Test
    @DisplayName("actualizarTurno lanza excepcion si la hora de fin no es posterior a la de inicio")
    void actualizarTurno_horarioInvalido_lanzaExcepcion() {
        
        TurnoRequest invalido = TurnoRequest.builder()
                .idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(13, 0))
                .horaFin(LocalTime.of(9, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(turno));

        assertThrows(IllegalArgumentException.class, () -> service.actualizarTurno(1L, invalido));
        verify(repository, never()).save(any());
        verifyNoInteractions(bibliotecarioClient);
    }

    @Test
    @DisplayName("actualizarTurno lanza excepcion si la hora de fin es igual a la de inicio")
    void actualizarTurno_horaIgual_lanzaExcepcion() {
        
        TurnoRequest invalido = TurnoRequest.builder()
                .idBibliotecario(5L)
                .fechaTurno(LocalDate.of(2026, 7, 1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(9, 0))
                .tipoTurno(TipoTurno.MANANA)
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(turno));

        assertThrows(IllegalArgumentException.class, () -> service.actualizarTurno(1L, invalido));
        verify(repository, never()).save(any());
        verifyNoInteractions(bibliotecarioClient);
    }
}