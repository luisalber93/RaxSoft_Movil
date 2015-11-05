package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Luisarriaga on 03/11/2015.
 */
public class HistorialDAO extends GenericDAO {


    private final int MOV_ENTRADA = 1;
    private final int MOV_SALIDA = 2;
    private final int MOV_AJUSTE = 3;


    public HistorialDAO(Context context) {
        super(context);
    }


    /**
     * Método que registra una salida en el historial en el Inventario.
     */

    public boolean registrarSalida(int materiaID, int cantidadModificada){
        //Se genera el contentValues para el la inserción en historial.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA,materiaID);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD,cantidadModificada);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_FECHA,getCurrentTime());
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV,MOV_SALIDA);
        //values.put(InventariosContract.HistorialTable.COLUMN_NAME_USUARIO,user);

        //Se realiza la inserción:
        abrir();
        boolean retorno = (database.insert(InventariosContract.HistorialTable.TABLE_NAME,null,values)!=-1);
        cerrar();

        return retorno;
    }

    /**
     * Método que registra una entrada en el almacén.
     */

    public boolean registraEntrada(int materiaID, int cantidadModificada, int proveedorID, double costo){
        //Se genera el contentValues para el la inserción en historial.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA,materiaID);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD,cantidadModificada);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_FECHA,getCurrentTime());
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV,MOV_ENTRADA);
        //values.put(InventariosContract.HistorialTable.COLUMN_NAME_USUARIO,user);

        //Se realiza la inserción:
        abrir();
        long idMovimiento = database.insert(InventariosContract.HistorialTable.TABLE_NAME,null,values);
        cerrar();

        boolean retorno = false;
        if(idMovimiento != -1){
            //Si la inserción en el historial fue exitosa, entonces se inserta en la Tabla de Detalle_Entrada.
            //Se genera el ContentValues.
            ContentValues detalleValues =  new ContentValues();
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_ID_MOVIMIENTO,idMovimiento);
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_ID_PROVEEDOR,proveedorID);
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_COSTO, costo);

            //Se realiza la inserción.
            abrir();
            retorno = (database.insert(InventariosContract.DetalleEntradaTable.TABLE_NAME,null,detalleValues)!=-1);
            cerrar();
        }


        return retorno;
    }

    public boolean registrarAjuste(int materiaID,int nvaExistencia){


        return true;
    }

    /**
     * Método que obtiene la fecha actual del sistema y la retorna en una cadena
     * formateada para ser almacenada en la base de datos SQLite.
     */

    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(calendar.getTime());
    }
}
