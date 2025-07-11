package com.franchise.service;

import com.franchise.dto.request.ActualizarStockRequest;
import com.franchise.dto.request.CrearProductoRequest;
import com.franchise.dto.response.ProductoMayorStockResponse;
import com.franchise.dto.response.ProductoResponse;
import com.franchise.model.Franquicia;
import com.franchise.model.Producto;
import com.franchise.model.Sucursal;
import com.franchise.repository.FranquiciaRepository;
import com.franchise.repository.ProductoRepository;
import com.franchise.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public ProductoResponse crearProducto(String franquiciaId, String sucursalId, CrearProductoRequest request) {
        log.info("Creando nuevo producto: {} en sucursal: {} de franquicia: {}",
                request.getNombre(), sucursalId, franquiciaId);

        if (!franquiciaRepository.existsById(franquiciaId)) {
            throw new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId);
        }

        if (!sucursalRepository.existsById(franquiciaId, sucursalId)) {
            throw new RuntimeException(
                    "Sucursal no encontrada con ID: " + sucursalId + " en franquicia: " + franquiciaId);
        }

        String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
        String fullSucursalId = "SUCURSAL#" + sucursalId;
        Producto producto = Producto.crear(fullFranquiciaId, fullSucursalId, request.getNombre(), request.getStock());

        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getProductoIdLimpio());

        return ProductoResponse.fromModel(productoGuardado);
    }

    public List<ProductoResponse> obtenerProductosPorSucursal(String franquiciaId, String sucursalId) {
        log.info("Obteniendo todos los productos de sucursal: {} en franquicia: {}", sucursalId, franquiciaId);

        if (!sucursalRepository.existsById(franquiciaId, sucursalId)) {
            throw new RuntimeException(
                    "Sucursal no encontrada con ID: " + sucursalId + " en franquicia: " + franquiciaId);
        }

        List<Producto> productos = productoRepository.findBySucursalId(franquiciaId, sucursalId);

        return productos.stream()
                .map(ProductoResponse::fromModel)
                .collect(Collectors.toList());
    }

    public ProductoResponse obtenerProductoPorId(String franquiciaId, String sucursalId, String productoId) {
        log.info("Obteniendo producto con ID: {} de sucursal: {} en franquicia: {}",
                productoId, sucursalId, franquiciaId);

        Producto producto = productoRepository.findById(franquiciaId, sucursalId, productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId +
                        " en sucursal: " + sucursalId + " de franquicia: " + franquiciaId));

        return ProductoResponse.fromModel(producto);
    }

    public ProductoResponse actualizarStock(String franquiciaId, String sucursalId, String productoId,
            ActualizarStockRequest request) {
        log.info("Actualizando stock de producto ID: {} en sucursal: {} de franquicia: {} a: {}",
                productoId, sucursalId, franquiciaId, request.getStock());

        Producto producto = productoRepository.findById(franquiciaId, sucursalId, productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId +
                        " en sucursal: " + sucursalId + " de franquicia: " + franquiciaId));

        producto.setStock(request.getStock());

        Producto productoActualizado = productoRepository.save(producto);

        log.info("Stock de producto actualizado exitosamente");

        return ProductoResponse.fromModel(productoActualizado);
    }

    public void eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        log.info("Eliminando producto con ID: {} de sucursal: {} en franquicia: {}",
                productoId, sucursalId, franquiciaId);

        if (!productoRepository.existsById(franquiciaId, sucursalId, productoId)) {
            throw new RuntimeException("Producto no encontrado con ID: " + productoId +
                    " en sucursal: " + sucursalId + " de franquicia: " + franquiciaId);
        }

        productoRepository.delete(franquiciaId, sucursalId, productoId);

        log.info("Producto eliminado exitosamente");
    }

    public List<ProductoMayorStockResponse> obtenerProductosConMayorStockPorSucursal() {
        log.info("Obteniendo productos con mayor stock por sucursal por franquicia");

        try {
            List<Producto> todosLosProductos = productoRepository.findAll();

            Map<String, Map<String, List<Producto>>> productosPorFranquiciaYSucursal = todosLosProductos.stream()
                    .collect(Collectors.groupingBy(
                            Producto::getFranquiciaId,
                            Collectors.groupingBy(Producto::getSucursalId)));

            List<ProductoMayorStockResponse> resultado = new ArrayList<>();

            for (Map.Entry<String, Map<String, List<Producto>>> franquiciaEntry : productosPorFranquiciaYSucursal
                    .entrySet()) {
                String franquiciaId = franquiciaEntry.getKey();

                // Obtener información de la franquicia
                String franquiciaIdLimpio = franquiciaId.replace("FRANQUICIA#", "");
                Optional<Franquicia> franquiciaOpt = franquiciaRepository.findById(franquiciaIdLimpio);
                String franquiciaNombre = franquiciaOpt.map(Franquicia::getNombre).orElse("Desconocida");

                for (Map.Entry<String, List<Producto>> sucursalEntry : franquiciaEntry.getValue().entrySet()) {
                    String sucursalId = sucursalEntry.getKey();
                    List<Producto> productos = sucursalEntry.getValue();

                    Optional<Producto> productoConMayorStock = productos.stream()
                            .filter(p -> p.getActivo())
                            .max(Comparator.comparing(Producto::getStock));

                    if (productoConMayorStock.isPresent()) {
                        Producto producto = productoConMayorStock.get();

                        String sucursalIdLimpio = sucursalId.replace("SUCURSAL#", "");
                        Optional<Sucursal> sucursalOpt = sucursalRepository.findById(franquiciaIdLimpio,
                                sucursalIdLimpio);
                        String sucursalNombre = sucursalOpt.map(Sucursal::getNombre).orElse("Desconocida");

                        ProductoMayorStockResponse response = ProductoMayorStockResponse.crear(
                                franquiciaIdLimpio,
                                franquiciaNombre,
                                sucursalIdLimpio,
                                sucursalNombre,
                                producto.getProductoIdLimpio(),
                                producto.getNombre(),
                                producto.getStock());

                        resultado.add(response);
                    }
                }
            }

            // Ordenar por franquicia y luego por sucursal
            resultado.sort(Comparator
                    .comparing(ProductoMayorStockResponse::getFranquiciaNombre)
                    .thenComparing(ProductoMayorStockResponse::getSucursalNombre));

            log.info("Se encontraron {} productos con mayor stock por sucursal", resultado.size());

            return resultado;

        } catch (Exception e) {
            log.error("Error al obtener productos con mayor stock: {}", e.getMessage());
            throw new RuntimeException("Error al obtener productos con mayor stock", e);
        }
    }
}
