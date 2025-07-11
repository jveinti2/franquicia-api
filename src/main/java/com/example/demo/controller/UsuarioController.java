package com.example.demo.controller;

import com.example.demo.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private Map<Long, Usuario> usuarios = new HashMap<>();
    private Long secuenciaId = 1L;

    @GetMapping
    public Collection<Usuario> listar() {
        return usuarios.values();
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return usuarios.get(id);
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        usuario.setId(secuenciaId++);
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario usuario = usuarios.get(id);
        if (usuario != null) {
            usuario.setNombre(datos.getNombre());
            usuario.setCorreo(datos.getCorreo());
        }
        return usuario;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarios.remove(id);
    }
}
