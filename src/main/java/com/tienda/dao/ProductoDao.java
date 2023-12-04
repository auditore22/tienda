package com.tienda.dao;

import com.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoDao extends JpaRepository <Producto,Long> {

    public List<Producto> findByPrecioBetweenOrderByDescripcion(double precioInf, double precioSup);
}
