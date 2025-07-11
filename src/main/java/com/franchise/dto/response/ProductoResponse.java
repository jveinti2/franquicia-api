package com.franchise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private String id;
    private String franquiciaId;
    private String sucursalId;
    private String nombre;
    private Integer stock;
    private Instant fechaCreacion;
    private Boolean activo;

    public static ProductoResponse fromModel(com.franchise.model.Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getProductoIdLimpio());
        response.setFranquiciaId(producto.getFranquiciaId().replace("FRANQUICIA#", ""));
        response.setSucursalId(producto.getSucursalIdLimpio());
        response.setNombre(producto.getNombre());
        response.setStock(producto.getStock());
        response.setFechaCreacion(producto.getFechaCreacion());
        response.setActivo(producto.getActivo());
        return response;
    }
}