package com.maven.raxsoft.models;

import java.util.HashMap;

/**
 * Created by Luis on 09/11/2015.
 */
public class Validaciones {


    public static boolean validarTextoVacio(String texto) {

        if (!texto.isEmpty()) {

            return true;
        } else {

            return false;
        }
    }

    public static boolean validarCaracteres(String texto) {
        if (texto.matches("^([A-Z,a-z,0-9,/\\s/ ]*)")) {

            return true;
        } else {

            return false;
        }
    }

    public static boolean validarLongitudCadena(String texto) {

        if (texto.length() < 140) {

            return true;
        } else {

            return false;
        }
    }

    public static boolean validarCorreoElectronico(String texto) {
        if (texto.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validarTelefono(String texto) {
        if (texto.matches("[0-9]{7,10}")) {

            return true;
        } else {

            return false;
        }
    }

}
