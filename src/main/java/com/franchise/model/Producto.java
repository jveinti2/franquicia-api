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
public class Producto {

    private String franquiciaId; // FRANQUICIA#{id}
    private String id; // SUCURSAL#{sucursal_id}#PRODUCTO#{producto_id}
    private String nombre;
    private Integer stock;
    private Instant fechaCreacion;
    private Boolean activo;
    private String sucursalId;

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

    @DynamoDbAttribute("stock")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    // GSI para buscar productos por sucursal
    @DynamoDbSecondaryPartitionKey(indexNames = "SucursalIndex")
    @DynamoDbAttribute("sucursalId")
    public String getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(String sucursalId) {
        this.sucursalId = sucursalId;
    }

    // Constructor helper
    public static Producto crear(String franquiciaId, String sucursalId, String nombre, Integer stock) {
        Producto producto = new Producto();
        producto.setFranquiciaId(franquiciaId);
        producto.setSucursalId(sucursalId);

        String productoId = java.util.UUID.randomUUID().toString();
        producto.setId(sucursalId + "#PRODUCTO#" + productoId);

        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setFechaCreacion(Instant.now());
        producto.setActivo(true);
        return producto;
    }

    // Helper para obtener el ID limpio del producto
    public String getProductoIdLimpio() {
        String[] parts = this.id.split("#PRODUCTO#");
        return parts.length > 1 ? parts[1] : "";
    }

    // Helper para obtener el ID limpio de la sucursal
    public String getSucursalIdLimpio() {
        return this.sucursalId.replace("SUCURSAL#", "");
    }
}