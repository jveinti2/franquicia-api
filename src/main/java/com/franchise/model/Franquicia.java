package com.franchise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Franquicia {

    private String id; // FRANQUICIA#{id}
    private String sortKey; // FRANQUICIA#{id}
    private String nombre;
    private Instant fechaCreacion;
    private Boolean activo;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDbAttribute("nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @DynamoDbAttribute("fechaCreacion")
    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @DynamoDbAttribute("activo")
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // Constructor helper para crear una franquicia
    public static Franquicia crear(String nombre) {
        Franquicia franquicia = new Franquicia();
        String id = "FRANQUICIA#" + java.util.UUID.randomUUID().toString();
        franquicia.setId(id);
        franquicia.setSortKey(id);
        franquicia.setNombre(nombre);
        franquicia.setFechaCreacion(Instant.now());
        franquicia.setActivo(true);
        return franquicia;
    }

    // Helper para obtener el ID limpio
    public String getIdLimpio() {
        return this.id.replace("FRANQUICIA#", "");
    }
}