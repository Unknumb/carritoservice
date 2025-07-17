package com.perfulandia.carritoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    private long usuarioId;
    private String mensaje;
    private long carritoId;
}