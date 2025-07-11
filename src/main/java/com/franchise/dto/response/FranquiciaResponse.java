package com.franchise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaResponse {

    private String id;
    private String nombre;
    private Instant fechaCreacion;
    private Boolean activo;

    public static FranquiciaResponse fromModel(com.franchise.model.Franquicia franquicia) {
        FranquiciaResponse response = new FranquiciaResponse();
        response.setId(franquicia.getIdLimpio());
        response.setNombre(franquicia.getNombre());
        response.setFechaCreacion(franquicia.getFechaCreacion());
        response.setActivo(franquicia.getActivo());
        return response;
    }
}