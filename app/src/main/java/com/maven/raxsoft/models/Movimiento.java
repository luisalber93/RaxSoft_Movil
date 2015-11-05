package com.maven.raxsoft.models;

/**
 * Created by Luisarriaga on 04/11/2015.
 */
public class Movimiento {

    private int id;
    private MateriaPrima materia;
    private int cantidad;
    private String fecha;
    private int tipoMov;
    private String usuario;

    //Atributos para los movimientos de Entrada.
    //El proveedor se obtiene de la consulta a la materia prima.

    private double costo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MateriaPrima getMateria() {
        return materia;
    }

    public void setMateria(MateriaPrima materia) {
        this.materia = materia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(int tipoMov) {
        this.tipoMov = tipoMov;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
