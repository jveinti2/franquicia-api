package com.franchise.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.franchise.model.Producto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductoRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private static final String TABLE_NAME = "franquicia-data";
    private static final String SUCURSAL_INDEX = "SucursalIndex";

    private DynamoDbTable<Producto> getTable() {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Producto.class));
    }

    public Producto save(Producto producto) {
        log.info("Guardando producto: {} en sucursal: {}", producto.getNombre(), producto.getSucursalId());
        try {
            getTable().putItem(producto);
            log.info("Producto guardado exitosamente con ID: {}", producto.getProductoIdLimpio());
            return producto;
        } catch (Exception e) {
            log.error("Error al guardar producto: {}", e.getMessage());
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    public Optional<Producto> findById(String franquiciaId, String sucursalId, String productoId) {
        log.info("Buscando producto con ID: {} en sucursal: {} de franquicia: {}", productoId, sucursalId,
                franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
            String fullSucursalId = "SUCURSAL#" + sucursalId;
            String fullProductoId = fullSucursalId + "#PRODUCTO#" + productoId;

            Producto producto = getTable()
                    .getItem(r -> r.key(k -> k.partitionValue(fullFranquiciaId).sortValue(fullProductoId)));
            return Optional.ofNullable(producto);
        } catch (Exception e) {
            log.error("Error al buscar producto: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Producto> findBySucursalId(String franquiciaId, String sucursalId) {
        log.info("Obteniendo todos los productos de la sucursal: {} en franquicia: {}", sucursalId, franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
            String fullSucursalId = "SUCURSAL#" + sucursalId;

            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(k -> k.partitionValue(fullFranquiciaId));

            QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return getTable().query(queryRequest)
                    .items()
                    .stream()
                    .filter(producto -> producto.getId().startsWith(fullSucursalId + "#PRODUCTO#")
                            && producto.getActivo())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener productos", e);
        }
    }

    public List<Producto> findByFranquiciaId(String franquiciaId) {
        log.info("Obteniendo todos los productos de la franquicia: {}", franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;

            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(k -> k.partitionValue(fullFranquiciaId));

            QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return getTable().query(queryRequest)
                    .items()
                    .stream()
                    .filter(producto -> producto.getId().contains("#PRODUCTO#") && producto.getActivo())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener productos de franquicia: {}", e.getMessage());
            throw new RuntimeException("Error al obtener productos de franquicia", e);
        }
    }

    public List<Producto> findAll() {
        log.info("Obteniendo todos los productos");
        try {
            return getTable().scan()
                    .items()
                    .stream()
                    .filter(producto -> producto.getId().contains("#PRODUCTO#") && producto.getActivo())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todos los productos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todos los productos", e);
        }
    }

    public void delete(String franquiciaId, String sucursalId, String productoId) {
        log.info("Eliminando producto con ID: {} de sucursal: {} en franquicia: {}", productoId, sucursalId,
                franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
            String fullSucursalId = "SUCURSAL#" + sucursalId;
            String fullProductoId = fullSucursalId + "#PRODUCTO#" + productoId;

            getTable().deleteItem(r -> r.key(k -> k.partitionValue(fullFranquiciaId).sortValue(fullProductoId)));
            log.info("Producto eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    public boolean existsById(String franquiciaId, String sucursalId, String productoId) {
        return findById(franquiciaId, sucursalId, productoId).isPresent();
    }
}
