package com.franchise.controller;

import com.franchise.dto.request.CrearFranquiciaRequest;
import com.franchise.dto.response.FranquiciaResponse;
import com.franchise.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/franquicias")
@RequiredArgsConstructor
@Slf4j
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    public ResponseEntity<FranquiciaResponse> crearFranquicia(@Valid @RequestBody CrearFranquiciaRequest request) {
        log.info("Petición para crear franquicia: {}", request.getNombre());

        try {
            FranquiciaResponse response = franquiciaService.crearFranquicia(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear franquicia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FranquiciaResponse>> obtenerTodasLasFranquicias() {
        log.info("Petición para obtener todas las franquicias");

        try {
            List<FranquiciaResponse> franquicias = franquiciaService.obtenerTodasLasFranquicias();
            return ResponseEntity.ok(franquicias);
        } catch (Exception e) {
            log.error("Error al obtener franquicias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FranquiciaResponse> obtenerFranquiciaPorId(@PathVariable String id) {
        log.info("Petición para obtener franquicia con ID: {}", id);

        try {
            FranquiciaResponse franquicia = franquiciaService.obtenerFranquiciaPorId(id);
            return ResponseEntity.ok(franquicia);
        } catch (RuntimeException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener franquicia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FranquiciaResponse> actualizarNombreFranquicia(
            @PathVariable String id,
            @Valid @RequestBody CrearFranquiciaRequest request) {
        log.info("Petición para actualizar nombre de franquicia ID: {} a: {}", id, request.getNombre());

        try {
            FranquiciaResponse response = franquiciaService.actualizarNombreFranquicia(id, request.getNombre());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar franquicia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFranquicia(@PathVariable String id) {
        log.info("Petición para eliminar franquicia con ID: {}", id);

        try {
            franquiciaService.eliminarFranquicia(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar franquicia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}