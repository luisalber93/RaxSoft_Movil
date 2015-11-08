package com.maven.raxsoft.models;

/**
 * Created by Luisarriaga on 01/11/2015.
 */
public class ErrorDB {

    private boolean success;
    private String mensaje;

    public ErrorDB(boolean success, String mensaje){
        this.success=success;
        this.mensaje=mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
