package com.maven.raxsoft.gui;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by axel on 09-Nov-15.
 */
public class  Validaciones {

//    String expresionRegular = [[a-z][0-9]];

    public static boolean ValidarTextoVacio(String texto){

        if (!texto.isEmpty()) {
            Log.i("Correcto", "si contiene texto " + texto.toString());
            return true;
        }
        else{
            Log.i("Error","Introduce algun valor");
            return false;
        }
    }

    public static boolean ValidarCaracteres(String texto){
        if (texto.matches("^([A-Z,a-z,0-9,/\\s/ ]*)")){
            Log.i("Correcto","Cadena valida " + texto);
            return true;
        }
        else{
            Log.i("Error","contiene caracteres ilegales " + texto);
            return false;
        }
    }

    public static boolean ValidarLogitudCadena(String texto){

        if(texto.length()<140){
            Log.i("Correcto","Tamaño valido " + texto.length());
            return true;
        }
        else{
            Log.i("Error","Exceso de longitud " + texto.length());
            return false;
        }
    }

    public static boolean ValidarCorreoElectronico(String texto){
        if(texto.matches("^([A-Z,a-z,0-9-_])+@[A-Z,a-z]+(.[A-Z,a-z]+)")){
            Log.i("Correcto","el correo cumple con las especificaciones " + texto);
            return true;
        }
        else{
            Log.i("Error", "Formato invalido " + texto);
            return false;
        }
    }

    public static boolean ValidarTelefono(String texto){
        if(texto.matches("[0-9]{7,10}")){
            Log.i("Correcto", "el numero cumple con la longitud requerida ");
            return true;
        }
        else{
            Log.i("Error","El numero telefonico no cumple con las especificaciones");
            return false;
        }
    }

}
