package com.franchise.service;

import com.franchise.dto.request.CrearFranquiciaRequest;
import com.franchise.dto.response.FranquiciaResponse;
import com.franchise.model.Franquicia;
import com.franchise.repository.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaResponse crearFranquicia(CrearFranquiciaRequest request) {
        log.info("Creando nueva franquicia: {}", request.getNombre());
        Franquicia franquicia = Franquicia.crear(request.getNombre());
        Franquicia franquiciaGuardada = franquiciaRepository.save(franquicia);
        log.info("Franquicia creada exitosamente con ID: {}", franquiciaGuardada.getIdLimpio());
        return FranquiciaResponse.fromModel(franquiciaGuardada);
    }

    public List<FranquiciaResponse> obtenerTodasLasFranquicias() {
        log.info("Obteniendo todas las franquicias");

        List<Franquicia> franquicias = franquiciaRepository.findAll();

        return franquicias.stream()
                .map(FranquiciaResponse::fromModel)
                .collect(Collectors.toList());
    }

    public FranquiciaResponse obtenerFranquiciaPorId(String id) {
        log.info("Obteniendo franquicia con ID: {}", id);

        Franquicia franquicia = franquiciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada con ID: " + id));

        return FranquiciaResponse.fromModel(franquicia);
    }

    public FranquiciaResponse actualizarNombreFranquicia(String id, String nuevoNombre) {
        log.info("Actualizando nombre de franquicia ID: {} a: {}", id, nuevoNombre);

        Franquicia franquicia = franquiciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada con ID: " + id));

        franquicia.setNombre(nuevoNombre);

        Franquicia franquiciaActualizada = franquiciaRepository.save(franquicia);

        log.info("Nombre de franquicia actualizado exitosamente");

        return FranquiciaResponse.fromModel(franquiciaActualizada);
    }

    public void eliminarFranquicia(String id) {
        log.info("Eliminando franquicia con ID: {}", id);

        if (!franquiciaRepository.existsById(id)) {
            throw new RuntimeException("Franquicia no encontrada con ID: " + id);
        }

        franquiciaRepository.delete(id);

        log.info("Franquicia eliminada exitosamente");
    }
}