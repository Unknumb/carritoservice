package com.perfulandia.carritoservice.controller;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.Notificacion;
import com.perfulandia.carritoservice.service.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarritoService service;

    @MockBean
    private RestTemplate restTemplate;

    private Carrito sample;

    @BeforeEach
    void setUp() {
        sample = Carrito.builder()
                .id(1L)
                .usuarioId(10L)
                .productoIds(Collections.emptyList())
                .total(99.99)
                .build();
    }

    @Test
    void GET_api_carritos_deberiaRetornarLista() throws Exception {
        when(service.obtenerTodos()).thenReturn(Collections.singletonList(sample));

        mvc.perform(get("/api/carritos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].total").value(99.99));

        verify(service, times(1)).obtenerTodos();
    }

    @Test
    void GET_api_carritos_id_deberiaRetornarEntidad() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(sample);

        mvc.perform(get("/api/carritos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(10))
                .andExpect(jsonPath("$.total").value(99.99));

        verify(service, times(1)).obtenerPorId(1L);
    }

    @Test
    void POST_api_carritos_deberiaCrearYNotificar() throws Exception {
        // Prepara el servicio y el RestTemplate
        when(service.guardar(any(Carrito.class))).thenReturn(sample);
        when(restTemplate.postForObject(anyString(), any(Notificacion.class), eq(Notificacion.class)))
                .thenReturn(new Notificacion());

        String body = "{"
                + "\"usuarioId\":10,"
                + "\"productoIds\":[],"
                + "\"total\":99.99"
                + "}";

        mvc.perform(post("/api/carritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        // Verifica que se llamó al servicio
        verify(service, times(1)).guardar(any(Carrito.class));
        // Verifica que se envió la notificación
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(restTemplate, times(1))
                .postForObject(eq("http://localhost:8084/api/notificaciones"), captor.capture(), eq(Notificacion.class));

        Notificacion sent = captor.getValue();
        assert sent.getCarritoId() == 1L;
        assert sent.getUsuarioId() == 10L;
    }

    @Test
    void DELETE_api_carritos_id_deberiaEliminar() throws Exception {
        mvc.perform(delete("/api/carritos/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).eliminar(1L);
    }
}

