package cl.duoc.usuario_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.usuario_service.client.DomicilioClient;
import cl.duoc.usuario_service.client.NombreClient;
import cl.duoc.usuario_service.dto.DomicilioResponse;
import cl.duoc.usuario_service.dto.NombreResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.mapper.UsuarioMapper;
import cl.duoc.usuario_service.model.Usuario;
import cl.duoc.usuario_service.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private DomicilioClient domicilioClient;

    @Autowired
    private NombreClient nombreClient;

    @Autowired
    private UsuarioMapper mapper;

    public List<UsuarioResponse> obtenerTodos() {
        log.info("Consultando usuarios...");
        return repository.findAll().stream().map(usuario -> {
            DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(usuario.getIdDomicilio());
            NombreResponse nombre = nombreClient.buscarNombrePorId(usuario.getIdNombre());
            return mapper.toResponse(usuario, domicilio, nombre);
        }).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return repository.findById(id)
                .map(usuario -> {
                    DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(usuario.getIdDomicilio());
                    NombreResponse nombre = nombreClient.buscarNombrePorId(usuario.getIdNombre());
                    return mapper.toResponse(usuario, domicilio, nombre);
                })
                .orElseThrow(() -> new NoSuchElementException("No se encontró usuario con id: " + id));
    }

    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        log.info("Creando nuevo usuario: {}", request.getIdNombre());
        Usuario usuario = mapper.toEntity(request);

        DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(request.getIdDomicilio());
        if (domicilio == null) {
            log.warn("Domicilio con id: {} no encontrado", request.getIdDomicilio());
            throw new NoSuchElementException("Domicilio con id " + request.getIdDomicilio() + " no encontrado.");
        }

        NombreResponse nombre = nombreClient.buscarNombrePorId(request.getIdNombre());
        if (nombre == null) {
            log.warn("Nombre con id: {} no encontrado", request.getIdNombre());
            throw new NoSuchElementException("Nombre con id " + request.getIdNombre() + " no encontrado");
        }

        return mapper.toResponse(repository.save(usuario), domicilio, nombre);
    }

    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        log.info("Actualizando usuario con id: {}", id);

        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró usuario con id " + id));

        DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(request.getIdDomicilio());
        if (domicilio == null) {
            log.warn("Domicilio con id: {} no encontrado", request.getIdDomicilio());
            throw new NoSuchElementException("Domicilio con id " + request.getIdDomicilio() + " no encontrado.");
        }

        NombreResponse nombre = nombreClient.buscarNombrePorId(request.getIdNombre());
        if (nombre == null) {
            log.warn("Nombre con id: {} no encontrado", request.getIdNombre());
            throw new NoSuchElementException("Nombre con id " + request.getIdNombre() + " no encontrado");
        }

        usuarioExistente.setIdNombre(nombre.getId());
        usuarioExistente.setRut(request.getRut());
        usuarioExistente.setEdad(request.getEdad());
        usuarioExistente.setIdDomicilio(domicilio.getId());
        usuarioExistente.setFechaNacimiento(request.getFechaNacimiento());

        Usuario usuarioActualizado = repository.save(usuarioExistente);
        return mapper.toResponse(usuarioActualizado, domicilio, nombre);
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró usuario con id: " + id);
        }
        repository.deleteById(id);
    }

    public List<UsuarioResponse> buscarUsuariosPorIdDomicilio(Long idDomicilio) {
        log.info("Buscando usuarios de domicilio con id: {}", idDomicilio);
        return repository.findByIdDomicilio(idDomicilio).stream()
                .map(usuario -> {
                    NombreResponse nombre = nombreClient.buscarNombrePorId(usuario.getIdNombre());
                    DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(usuario.getIdDomicilio());
                    return mapper.toResponse(usuario, domicilio, nombre);
                })
                .toList();
    }

    public List<UsuarioResponse> buscarUsuarioPorIdNombre(Long idNombre) {
        log.info("Buscando usuarios de nombre con id: {}", idNombre);
        return repository.findByIdNombre(idNombre).stream()
                .map(usuario -> {
                    NombreResponse nombre = nombreClient.buscarNombrePorId(usuario.getIdNombre());
                    DomicilioResponse domicilio = domicilioClient.buscarDomicilioPorId(usuario.getIdDomicilio());
                    return mapper.toResponse(usuario, domicilio, nombre);
                })
                .toList();
    }

}
