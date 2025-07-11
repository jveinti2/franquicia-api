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
public class Sucursal {

    private String franquiciaId; // FRANQUICIA#{id}
    private String id; // SUCURSAL#{id}
    private String nombre;
    private Instant fechaCreacion;
    private Boolean activo;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getFranquiciaId() {
        return franquiciaId;
    }

    public void setFranquiciaId(String franquiciaId) {
        this.franquiciaId = franquiciaId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    // Constructor helper
    public static Sucursal crear(String franquiciaId, String nombre) {
        Sucursal sucursal = new Sucursal();
        sucursal.setFranquiciaId(franquiciaId);
        sucursal.setId("SUCURSAL#" + java.util.UUID.randomUUID().toString());
        sucursal.setNombre(nombre);
        sucursal.setFechaCreacion(Instant.now());
        sucursal.setActivo(true);
        return sucursal;
    }

    // Helper para obtener el ID limpio
    public String getIdLimpio() {
        return this.id.replace("SUCURSAL#", "");
    }
}