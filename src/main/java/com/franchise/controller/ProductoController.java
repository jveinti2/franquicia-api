package com.franchise.controller;

import com.franchise.dto.request.ActualizarStockRequest;
import com.franchise.dto.request.CrearProductoRequest;
import com.franchise.dto.response.ProductoMayorStockResponse;
import com.franchise.dto.response.ProductoResponse;
import com.franchise.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos")
    public ResponseEntity<ProductoResponse> crearProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @Valid @RequestBody CrearProductoRequest request) {
        log.info("Petición para crear producto: {} en sucursal: {} de franquicia: {}",
                request.getNombre(), sucursalId, franquiciaId);

        try {
            ProductoResponse response = productoService.crearProducto(franquiciaId, sucursalId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos")
    public ResponseEntity<List<ProductoResponse>> obtenerProductosPorSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Petición para obtener todos los productos de sucursal: {} en franquicia: {}", sucursalId,
                franquiciaId);

        try {
            List<ProductoResponse> productos = productoService.obtenerProductosPorSucursal(franquiciaId, sucursalId);
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        log.info("Petición para obtener producto con ID: {} de sucursal: {} en franquicia: {}",
                productoId, sucursalId, franquiciaId);

        try {
            ProductoResponse producto = productoService.obtenerProductoPorId(franquiciaId, sucursalId, productoId);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @Valid @RequestBody ActualizarStockRequest request) {
        log.info("Petición para actualizar stock de producto ID: {} en sucursal: {} de franquicia: {} a: {}",
                productoId, sucursalId, franquiciaId, request.getStock());

        try {
            ProductoResponse response = productoService.actualizarStock(franquiciaId, sucursalId, productoId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        log.info("Petición para eliminar producto con ID: {} de sucursal: {} en franquicia: {}",
                productoId, sucursalId, franquiciaId);

        try {
            productoService.eliminarProducto(franquiciaId, sucursalId, productoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/productos/mayor-stock")
    public ResponseEntity<List<ProductoMayorStockResponse>> obtenerProductosConMayorStockPorSucursal() {
        log.info("Petición para obtener productos con mayor stock por sucursal por franquicia");

        try {
            List<ProductoMayorStockResponse> productos = productoService.obtenerProductosConMayorStockPorSucursal();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            log.error("Error al obtener productos con mayor stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
