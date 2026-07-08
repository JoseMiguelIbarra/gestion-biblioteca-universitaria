package cl.duoc.usuario_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usuarios")
@Tag(
    name = "Usuarios",
    description = "Operaciones para la gestion de usuarios de la Biblioteca Universitaria"
)
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(
        summary = "Listar usuarios",
        description = "Obtiene todos los usuarios registrados en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        log.info("GET/usuarios");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(
        summary = "Buscar usuario por ID",
        description = "Obtiene un usuario segun su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {

        log.info("GET/usuarios/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Obtiene todos los usuarios asociados a un nombre especifico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
        @ApiResponse(responseCode = "404", description = "No existen usuarios asociados al nombre indicado")
    })
    @GetMapping("/nombre/{idNombre}")
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuariosPorIdNombre(
            @Parameter(description = "ID del nombre asociado", example = "1")
            @PathVariable Long idNombre) {

        log.info("GET/usuarios/nombre/{}", idNombre);
        return ResponseEntity.ok(service.buscarUsuarioPorIdNombre(idNombre));
    }

    @Operation(
        summary = "Buscar usuarios por domicilio",
        description = "Obtiene todos los usuarios asociados a un domicilio especifico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
        @ApiResponse(responseCode = "404", description = "No existen usuarios asociados al domicilio indicado")
    })
    @GetMapping("/domicilio/{idDomicilio}")
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuariosPorIdDomicilio(
            @Parameter(description = "ID del domicilio asociado", example = "1")
            @PathVariable Long idDomicilio) {

        log.info("GET/usuarios/domicilio/{}", idDomicilio);
        return ResponseEntity.ok(service.buscarUsuariosPorIdDomicilio(idDomicilio));
    }

    @Operation(
        summary = "Crear usuario",
        description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Valid @RequestBody UsuarioRequest request) {

        log.info("POST/usuarios");

        UsuarioResponse usuarioCreado = service.crearUsuario(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuarioCreado);
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza la informacion de un usuario existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {

        log.info("PUT/usuarios/{}", id);
        return ResponseEntity.ok(service.actualizarUsuario(id, request));
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema segun su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {

        log.info("DELETE/usuarios/{}", id);
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}