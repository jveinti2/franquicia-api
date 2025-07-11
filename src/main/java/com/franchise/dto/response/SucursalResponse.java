package com.franchise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponse {

    private String id;
    private String franquiciaId;
    private String nombre;
    private Instant fechaCreacion;
    private Boolean activo;

    public static SucursalResponse fromModel(com.franchise.model.Sucursal sucursal) {
        SucursalResponse response = new SucursalResponse();
        response.setId(sucursal.getIdLimpio());
        response.setFranquiciaId(sucursal.getFranquiciaId().replace("FRANQUICIA#", ""));
        response.setNombre(sucursal.getNombre());
        response.setFechaCreacion(sucursal.getFechaCreacion());
        response.setActivo(sucursal.getActivo());
        return response;
    }
}