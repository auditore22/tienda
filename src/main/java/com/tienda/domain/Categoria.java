package com.tienda.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Data
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categoria")
    private long idCategoria;
    private String descripcion;
    private String rutaImagen;
    private Boolean activo;

    public Categoria() {
    }

    public Categoria(String descripcion) {
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public boolean isActivo(){
        return activo;
    }
}
