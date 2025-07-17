package com.perfulandia.carritoservice.repository;

import com.perfulandia.carritoservice.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Puedes agregar métodos personalizados si los necesitas.
}
