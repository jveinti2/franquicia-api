package com.franchise.service;

import com.franchise.dto.request.CrearSucursalRequest;
import com.franchise.dto.response.SucursalResponse;
import com.franchise.model.Sucursal;
import com.franchise.repository.FranquiciaRepository;
import com.franchise.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public SucursalResponse crearSucursal(String franquiciaId, CrearSucursalRequest request) {
        log.info("Creando nueva sucursal: {} para franquicia: {}", request.getNombre(), franquiciaId);

        if (!franquiciaRepository.existsById(franquiciaId)) {
            throw new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId);
        }

        String fullFranquiciaId = "FRANQUICIA#" + franquiciaId;
        Sucursal sucursal = Sucursal.crear(fullFranquiciaId, request.getNombre());

        Sucursal sucursalGuardada = sucursalRepository.save(sucursal);

        log.info("Sucursal creada exitosamente con ID: {}", sucursalGuardada.getIdLimpio());

        return SucursalResponse.fromModel(sucursalGuardada);
    }

    public List<SucursalResponse> obtenerSucursalesPorFranquicia(String franquiciaId) {
        log.info("Obteniendo todas las sucursales de la franquicia: {}", franquiciaId);

        if (!franquiciaRepository.existsById(franquiciaId)) {
            throw new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId);
        }

        List<Sucursal> sucursales = sucursalRepository.findByFranquiciaId(franquiciaId);

        return sucursales.stream()
                .map(SucursalResponse::fromModel)
                .collect(Collectors.toList());
    }

    public SucursalResponse obtenerSucursalPorId(String franquiciaId, String sucursalId) {
        log.info("Obteniendo sucursal con ID: {} de franquicia: {}", sucursalId, franquiciaId);

        Sucursal sucursal = sucursalRepository.findById(franquiciaId, sucursalId)
                .orElseThrow(() -> new RuntimeException(
                        "Sucursal no encontrada con ID: " + sucursalId + " en franquicia: " + franquiciaId));

        return SucursalResponse.fromModel(sucursal);
    }

    public SucursalResponse actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        log.info("Actualizando nombre de sucursal ID: {} en franquicia: {} a: {}", sucursalId, franquiciaId,
                nuevoNombre);

        Sucursal sucursal = sucursalRepository.findById(franquiciaId, sucursalId)
                .orElseThrow(() -> new RuntimeException(
                        "Sucursal no encontrada con ID: " + sucursalId + " en franquicia: " + franquiciaId));

        sucursal.setNombre(nuevoNombre);

        Sucursal sucursalActualizada = sucursalRepository.save(sucursal);

        log.info("Nombre de sucursal actualizado exitosamente");

        return SucursalResponse.fromModel(sucursalActualizada);
    }

    public void eliminarSucursal(String franquiciaId, String sucursalId) {
        log.info("Eliminando sucursal con ID: {} de franquicia: {}", sucursalId, franquiciaId);

        if (!sucursalRepository.existsById(franquiciaId, sucursalId)) {
            throw new RuntimeException(
                    "Sucursal no encontrada con ID: " + sucursalId + " en franquicia: " + franquiciaId);
        }

        sucursalRepository.delete(franquiciaId, sucursalId);

        log.info("Sucursal eliminada exitosamente");
    }
}
