package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.maven.raxsoft.models.ErrorDB;
import com.maven.raxsoft.models.Proveedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luisarriaga on 01/11/2015.
 */
public class ProveedorDAO extends GenericDAO {


    public ProveedorDAO(Context context) {
        super(context);
    }


    /**
     * Método que inserta un Proveedor en la tabla.
     */

    public ErrorDB insertProveedor(Proveedor proveedor){
        //Se obtienen los values del objeto.
        ContentValues values = getValuesFromProveedor(proveedor,true);
        //Se abre la base de datos para realizar la inserción.
        abrir();
        //Se realiza la inserción y se obtiene el resultado.
        boolean success = (database.insert(InventariosContract.ProveedorTable.TABLE_NAME,null,values)!=-1);
        //Se cierra la base de datos.
        cerrar();
        String mensaje = (success)?"Proveedor registrado exitosamente":"Ocurrió un error al insertar el nuevo proveedor. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG,mensaje);
        return new ErrorDB(success,mensaje);
    }


    /**
     * Método que realiza el borrado lógico de un proveedor de la BD en base a su identificador.
     */
    public ErrorDB deleteProveedor(int id){

        //Como se realizará un borrado lógico se hará un update de un registro.

        //Se crea el whereClause para el update.
        String whereClause = InventariosContract.ProveedorTable._ID+" = ?";
        //Se crea el whereArgs para el update.
        String [] whereArgs = new String[]{Integer.toString(id)};
        //Se crea el content values para el update.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_USO, 0); //El cero represena el borrado lógico del proveedor.
        //Se abre la base de datos.
        abrir();
        boolean success = (database.update(InventariosContract.ProveedorTable.TABLE_NAME, values, whereClause, whereArgs)>0);
        //Se cierra la base de datos.
        cerrar();
        String mensaje = (success)?"Proveedor eliminado exitosamente":"Ocurrió un error al eliminar el proveedor. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG, mensaje);

        return new ErrorDB(success,mensaje);
    }

    /**
     * Método que realiza la actualización de un registro.
     */

    public ErrorDB updateProveedor(Proveedor proveedor, int id){
        //Se obtiene el contentValues del proveedor.
        ContentValues values = getValuesFromProveedor(proveedor,false);
        //Se crea el whereClause.
        String whereClause = InventariosContract.ProveedorTable._ID+ " = ?";
        String [] whereArgs = new String[]{Integer.toString(id)};
        //Se realiza la operación de base de datos.
        //Se abre la base de datos.
        abrir();
        boolean success = (database.update(InventariosContract.ProveedorTable.TABLE_NAME, values, whereClause, whereArgs)>0);
        //Se cierra la base de datos.
        cerrar();
        String mensaje = (success)?"Proveedor actualizado exitosamente":"Ocurrió un error al actualizar el proveedor. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG, mensaje);
        return new ErrorDB(success,mensaje);
    }

    /**
     * Método que consulta la base de datos para obtener todos los proveedores registrados o un sólo proveedor en base a su id.
     */

    public List<Proveedor> fetchProveedores(int id){

        List<Proveedor> proveedores = null;
        Cursor cursor = null;
        //Se definen las columnas a consultar.
        String[] columns = new String[]{InventariosContract.ProveedorTable._ID,
                InventariosContract.ProveedorTable.COLUMN_NAME_NOMBRE,
                InventariosContract.ProveedorTable.COLUMN_NAME_CALLE,
                InventariosContract.ProveedorTable.COLUMN_NAME_NUMERO,
                InventariosContract.ProveedorTable.COLUMN_NAME_COLONIA,
                InventariosContract.ProveedorTable.COLUMN_NAME_ESTADO,
                InventariosContract.ProveedorTable.COLUMN_NAME_MUNICIPIO,
                InventariosContract.ProveedorTable.COLUMN_NAME_TELEFONO,
                InventariosContract.ProveedorTable.COLUMN_NAME_GIRO,
                InventariosContract.ProveedorTable.COLUMN_NAME_EMAIL,
        };

        String whereClause = InventariosContract.ProveedorTable.COLUMN_NAME_USO+" = ?";
        String [] whereArgs;
        //Si el id recibido es mayor que cero implica que se está consultando por un registro específico.
        //Se agregan los parámetros adecuados al whereArgs y al whereClause.
        if(id>0){
            whereClause+=" AND "+InventariosContract.ProveedorTable._ID+"= ?";
            whereArgs = new String[]{Integer.toString(1),Integer.toString(id)};
        }else{
            //Si no es mayor a cero implica que se busan todos los registros.
            whereArgs =  new String[]{Integer.toString(1)};
        }

        //Se abre la base de datos.
        abrir();
        //Se realiza la consulta.
        cursor = database.query(InventariosContract.ProveedorTable.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);
        proveedores = formatProveedorCursor(cursor);
        //Se cierra la base de datos.
        cerrar();


        return proveedores;

    }

    /**
     * Método que procesa un cursor y retorna una lista con Proveedores.
     */

    private List<Proveedor> formatProveedorCursor(Cursor cursor){
        List<Proveedor> proveedores = new ArrayList();

        if(cursor.moveToFirst()){
            do{
                Proveedor proveedor = new Proveedor();
                proveedor.setId(cursor.getInt(0));
                proveedor.setNombre(cursor.getString(1));
                proveedor.setCalle(cursor.getString(2));
                proveedor.setNumero(cursor.getString(3));
                proveedor.setColonia(cursor.getString(4));
                proveedor.setEstado(cursor.getString(5));
                proveedor.setMunicipio(cursor.getString(6));
                proveedor.setTelefono(cursor.getString(7));
                proveedor.setGiro(cursor.getString(8));
                proveedor.setEmail(cursor.getString(9));
                proveedores.add(proveedor);
            }while(cursor.moveToNext());
        }

        return proveedores;

    }

    /**
     * Método que retorna el Content Values a partir de un objeto tipo proveedor.
     */

    private ContentValues getValuesFromProveedor(Proveedor proveedor,boolean insert){
        ContentValues values = new ContentValues();
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_NOMBRE,proveedor.getNombre());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_CALLE,proveedor.getCalle());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_NUMERO,proveedor.getNumero());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_COLONIA,proveedor.getColonia());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_ESTADO,proveedor.getEstado());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_MUNICIPIO,proveedor.getMunicipio());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_TELEFONO,proveedor.getTelefono());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_GIRO,proveedor.getGiro());
        values.put(InventariosContract.ProveedorTable.COLUMN_NAME_EMAIL,proveedor.getEmail());
        if(insert){
            //Si la variable insert es true implica que se va a usar el ContentValues en una inserción por lo que se incluye el uso.
            values.put(InventariosContract.ProveedorTable.COLUMN_NAME_USO,proveedor.getUso());
        }


        return values;
    }


}
