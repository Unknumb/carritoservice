package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Notificacion;
import com.perfulandia.carritoservice.service.CarritoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;
    private final RestTemplate restTemplate;

    public CarritoController(CarritoService carritoService, RestTemplate restTemplate) {
        this.carritoService = carritoService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Carrito> obtenerTodos() {
        return carritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Carrito obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id);
    }

    @PostMapping
    public Carrito guardar(@RequestBody Carrito carrito) {
        Carrito savedCarrito = carritoService.guardar(carrito);


        String mensaje = "Se creó tu carrito #" + savedCarrito.getId() + " con total $" + savedCarrito.getTotal();
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(savedCarrito.getUsuarioId());
        notificacion.setCarritoId(savedCarrito.getId()); // <-- Añadir esta línea
        notificacion.setMensaje(mensaje);
        restTemplate.postForObject("https://notificacionservice.onrender.com/api/notificaciones", notificacion, Notificacion.class);

        return savedCarrito;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        carritoService.eliminar(id);
    }
}