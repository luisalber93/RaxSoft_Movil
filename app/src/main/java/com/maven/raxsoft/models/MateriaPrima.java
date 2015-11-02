package com.maven.raxsoft.models;

import java.util.List;

/**
 * Created by Luis on 02/11/2015.
 */
public class MateriaPrima {

    private int id;
    private String nombre;
    private String descripcion;
    private String unidad;
    private String fechaCreacion;
    private int existenciasMin;
    private int existenciasMax;
    private int uso;
    private List<Proveedor> proveedores;

    /**
     * Getters y Setters.
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getExistenciasMin() {
        return existenciasMin;
    }

    public void setExistenciasMin(int existenciasMin) {
        this.existenciasMin = existenciasMin;
    }

    public int getExistenciasMax() {
        return existenciasMax;
    }

    public void setExistenciasMax(int existenciasMax) {
        this.existenciasMax = existenciasMax;
    }

    public int getUso() {
        return uso;
    }

    public void setUso(int uso) {
        this.uso = uso;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
