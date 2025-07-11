package com.franchise.controller;

import com.franchise.dto.request.CrearSucursalRequest;
import com.franchise.dto.response.SucursalResponse;
import com.franchise.service.SucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/franquicias/{franquiciaId}/sucursales")
@RequiredArgsConstructor
@Slf4j
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<SucursalResponse> crearSucursal(
            @PathVariable String franquiciaId,
            @Valid @RequestBody CrearSucursalRequest request) {
        log.info("Petición para crear sucursal: {} en franquicia: {}", request.getNombre(), franquiciaId);

        try {
            SucursalResponse response = sucursalService.crearSucursal(franquiciaId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> obtenerSucursalesPorFranquicia(@PathVariable String franquiciaId) {
        log.info("Petición para obtener todas las sucursales de franquicia: {}", franquiciaId);

        try {
            List<SucursalResponse> sucursales = sucursalService.obtenerSucursalesPorFranquicia(franquiciaId);
            return ResponseEntity.ok(sucursales);
        } catch (RuntimeException e) {
            log.error("Franquicia no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener sucursales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{sucursalId}")
    public ResponseEntity<SucursalResponse> obtenerSucursalPorId(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Petición para obtener sucursal con ID: {} de franquicia: {}", sucursalId, franquiciaId);

        try {
            SucursalResponse sucursal = sucursalService.obtenerSucursalPorId(franquiciaId, sucursalId);
            return ResponseEntity.ok(sucursal);
        } catch (RuntimeException e) {
            log.error("Sucursal no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{sucursalId}")
    public ResponseEntity<SucursalResponse> actualizarNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @Valid @RequestBody CrearSucursalRequest request) {
        log.info("Petición para actualizar nombre de sucursal ID: {} en franquicia: {} a: {}",
                sucursalId, franquiciaId, request.getNombre());

        try {
            SucursalResponse response = sucursalService.actualizarNombreSucursal(franquiciaId, sucursalId,
                    request.getNombre());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Sucursal no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{sucursalId}")
    public ResponseEntity<Void> eliminarSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Petición para eliminar sucursal con ID: {} de franquicia: {}", sucursalId, franquiciaId);

        try {
            sucursalService.eliminarSucursal(franquiciaId, sucursalId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Sucursal no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar sucursal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
