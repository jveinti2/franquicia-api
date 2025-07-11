package com.franchise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FranchiseController {

    @GetMapping("/franchises")
    public String listarFranquicias() {
        return "Lista de franquicias";
    }

}
