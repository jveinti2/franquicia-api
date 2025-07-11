package com.franchise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMayorStockResponse {

    private String franquiciaId;
    private String franquiciaNombre;
    private String sucursalId;
    private String sucursalNombre;
    private String productoId;
    private String productoNombre;
    private Integer stock;

    public static ProductoMayorStockResponse crear(
            String franquiciaId, String franquiciaNombre,
            String sucursalId, String sucursalNombre,
            String productoId, String productoNombre, Integer stock) {
        return new ProductoMayorStockResponse(
                franquiciaId, franquiciaNombre,
                sucursalId, sucursalNombre,
                productoId, productoNombre, stock);
    }
}