package com.tienda.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_producto")
    private long idProducto;
    private String descripcion;
    private String rutaImagen;
    private Boolean activo;

    public Producto() {
    }

    public Producto(String descripcion) {
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public boolean isActivo(){
        return activo;
    }
}
