package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.maven.raxsoft.models.ErrorDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Luisarriaga on 02/11/2015.
 */
public class MovimientoDAO extends GenericDAO {

    public MovimientoDAO(Context context){
        super(context);
    }


    /**
     * Método que actualiza las existencias actuales de una materia prima.
     */

    public ErrorDB updateExistencias(int materiaID,int nuevaCantidad, int cantidadModificada, int tipoMov,int proveedorID, double costo,String user){
        //Los tipos de movimientos que se tienen:
        //1.- Entrada.
        //2.- Salida.
        //3.- Ajuste.

        //Se prepara el contentValues.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.StockTable.COLUMN_NAME_EXISTENCIAS,nuevaCantidad);

        //Se prepara la condición de actualización.
        String whereClause = InventariosContract.StockTable.COLUMN_NAME_ID_MATERIA+" = ?";
        String [] whereArgs = new String[]{Integer.toString(materiaID)};

        //Se realiza la actualización
        abrir();
        boolean success = (database.update(InventariosContract.StockTable.TABLE_NAME,values,whereClause,whereArgs)>0);
        cerrar();

        //Se registra el movimiento en el historial.

        boolean historyEntrySuccess = false;
        String mensaje="";
        HistorialDAO histDAO = new HistorialDAO(context);
        switch(tipoMov){
            //Se registra una entrada.
            case 1:
                historyEntrySuccess = histDAO.registraEntrada(materiaID,cantidadModificada,proveedorID,costo,user);
                mensaje = (success&&historyEntrySuccess)?"Registro de Entrada exitoso.":"El registro de entrada salió mal. Por favor inténtelo de nuevo.";
                break;
            //Se registra una salida.
            case 2:
                historyEntrySuccess = histDAO.registrarSalida(materiaID,cantidadModificada,user);
                mensaje = (success&&historyEntrySuccess)?"Registro de salida exitoso.":"El registro de salida salió mal. Por favor inténtelo de nuevo.";
                break;
            //Se registra un ajuste.
            case 3:
                historyEntrySuccess = histDAO.registrarAjuste(materiaID,nuevaCantidad,user);
                mensaje = (success&&historyEntrySuccess)?"Registro de ajuste de inventario exitoso.":"El registro del ajuste salió mal. Por favor inténtelo de nuevo.";
                break;
        }


        mensaje+="Para materia prima: "+materiaID;
        Log.i(SQLiteHelper.LOG_TAG,mensaje);

        return  new ErrorDB(success&&historyEntrySuccess,mensaje);

    }

    /**
     * Método que consulta las existencias actuales de una materia prima.
     */

    public int getExistenciasActuales(int materiaID){

        int existencias = -1;
        String whereClause = InventariosContract.StockTable.COLUMN_NAME_ID_MATERIA+"= ?";
        String [] whereArgs = new String[]{Integer.toString(materiaID)};
        String [] columns = new String[]{InventariosContract.StockTable.COLUMN_NAME_EXISTENCIAS};
        //Se abre la base de datos apra realizar la consulta
        abrir();
        Cursor cursor = database.query(InventariosContract.StockTable.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);
        //Se procesa el cursor.
        if(cursor.moveToFirst()){
            do{
                existencias = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        //Se cierra la bd
        cerrar();
        return existencias;
    }




}
