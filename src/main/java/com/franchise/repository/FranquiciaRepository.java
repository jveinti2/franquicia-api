package com.franchise.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.franchise.model.Franquicia;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FranquiciaRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private static final String TABLE_NAME = "franquicia-data";

    private DynamoDbTable<Franquicia> getTable() {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Franquicia.class));
    }

    public Franquicia save(Franquicia franquicia) {
        log.info("Guardando franquicia: {}", franquicia.getNombre());
        try {
            getTable().putItem(franquicia);
            log.info("Franquicia guardada exitosamente con ID: {}", franquicia.getIdLimpio());
            return franquicia;
        } catch (Exception e) {
            log.error("Error al guardar franquicia: {}", e.getMessage());
            throw new RuntimeException("Error al guardar franquicia", e);
        }
    }

    public Optional<Franquicia> findById(String id) {
        log.info("Buscando franquicia con ID: {}", id);
        try {
            String fullId = "FRANQUICIA#" + id;
            Franquicia franquicia = getTable().getItem(r -> r.key(k -> k.partitionValue(fullId).sortValue(fullId)));
            return Optional.ofNullable(franquicia);
        } catch (Exception e) {
            log.error("Error al buscar franquicia: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Franquicia> findAll() {
        log.info("Obteniendo todas las franquicias");
        try {

            software.amazon.awssdk.enhanced.dynamodb.Expression filterExpression = software.amazon.awssdk.enhanced.dynamodb.Expression
                    .builder()
                    .expression("attribute_exists(nombre)")
                    .build();

            ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                    .filterExpression(filterExpression)
                    .build();

            return getTable().scan(scanRequest)
                    .items()
                    .stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener franquicias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener franquicias", e);
        }
    }

    public void delete(String id) {
        log.info("Eliminando franquicia con ID: {}", id);
        try {
            String fullId = "FRANQUICIA#" + id;
            getTable().deleteItem(r -> r.key(k -> k.partitionValue(fullId).sortValue(fullId)));
            log.info("Franquicia eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar franquicia: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar franquicia", e);
        }
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }
}