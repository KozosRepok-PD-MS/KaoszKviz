/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.kaoszkviz.server.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dominik
 */
@RestController
@RequestMapping("/")
public class MainController {
    
    @GetMapping(name = "")
    public String index() {
        return "KaoszKviz - API";
    }
}
