package com.perfulandia.carritoservice.service;

import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoRepository repo;

    @InjectMocks
    private CarritoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeCarritos() {
        // dado
        Carrito c1 = Carrito.builder().id(1L).usuarioId(10L).total(100.0).build();
        Carrito c2 = Carrito.builder().id(2L).usuarioId(20L).total(200.0).build();
        when(repo.findAll()).thenReturn(Arrays.asList(c1, c2));

        // cuando
        List<Carrito> resultado = service.obtenerTodos();

        // entonces
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(c1) && resultado.contains(c2));
        verify(repo, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarCarrito() {
        // dado
        Carrito esperado = Carrito.builder().id(5L).usuarioId(99L).total(500.0).build();
        when(repo.findById(5L)).thenReturn(Optional.of(esperado));

        // cuando
        Carrito resultado = service.obtenerPorId(5L);

        // entonces
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(99L, resultado.getUsuarioId());
        verify(repo, times(1)).findById(5L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNull() {
        // dado
        when(repo.findById(123L)).thenReturn(Optional.empty());

        // cuando
        Carrito resultado = service.obtenerPorId(123L);

        // entonces
        assertNull(resultado);
        verify(repo, times(1)).findById(123L);
    }

    @Test
    void guardar_deberiaPersistirYRetornarEntidad() {
        // dado
        Carrito input = Carrito.builder().usuarioId(33L).total(330.0).build();
        Carrito saved = Carrito.builder().id(7L).usuarioId(33L).total(330.0).build();
        when(repo.save(input)).thenReturn(saved);

        // cuando
        Carrito resultado = service.guardar(input);

        // entonces
        assertNotNull(resultado);
        assertEquals(7L, resultado.getId());
        verify(repo, times(1)).save(input);
    }

    @Test
    void eliminar_deberiaInvocarDeleteById() {
        // cuando
        service.eliminar(42L);

        // entonces
        verify(repo, times(1)).deleteById(42L);
    }
}

