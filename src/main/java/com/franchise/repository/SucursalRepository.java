package com.franchise.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.franchise.model.Sucursal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SucursalRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private static final String TABLE_NAME = "franquicia-data";

    private DynamoDbTable<Sucursal> getTable() {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Sucursal.class));
    }

    public Sucursal save(Sucursal sucursal) {
        log.info("Guardando sucursal: {} en franquicia: {}", sucursal.getNombre(), sucursal.getFranquiciaId());
        try {
            getTable().putItem(sucursal);
            log.info("Sucursal guardada exitosamente con ID: {}", sucursal.getIdLimpio());
            return sucursal;
        } catch (Exception e) {
            log.error("Error al guardar sucursal: {}", e.getMessage());
            throw new RuntimeException("Error al guardar sucursal", e);
        }
    }

    public Optional<Sucursal> findById(String franquiciaId, String sucursalId) {
        log.info("Buscando sucursal con ID: {} en franquicia: {}", sucursalId, franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
            String fullSucursalId = "SUCURSAL#" + sucursalId;

            Sucursal sucursal = getTable()
                    .getItem(r -> r.key(k -> k.partitionValue(fullFranquiciaId).sortValue(fullSucursalId)));
            return Optional.ofNullable(sucursal);
        } catch (Exception e) {
            log.error("Error al buscar sucursal: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Sucursal> findByFranquiciaId(String franquiciaId) {
        log.info("Obteniendo todas las sucursales de la franquicia: {}", franquiciaId);
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
                    .filter(sucursal -> sucursal.getId().startsWith("SUCURSAL#") && sucursal.getActivo())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener sucursales: {}", e.getMessage());
            throw new RuntimeException("Error al obtener sucursales", e);
        }
    }

    public void delete(String franquiciaId, String sucursalId) {
        log.info("Eliminando sucursal con ID: {} de franquicia: {}", sucursalId, franquiciaId);
        try {
            String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
            String fullSucursalId = "SUCURSAL#" + sucursalId;

            getTable().deleteItem(r -> r.key(k -> k.partitionValue(fullFranquiciaId).sortValue(fullSucursalId)));
            log.info("Sucursal eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar sucursal: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar sucursal", e);
        }
    }

    public boolean existsById(String franquiciaId, String sucursalId) {
        return findById(franquiciaId, sucursalId).isPresent();
    }
}
