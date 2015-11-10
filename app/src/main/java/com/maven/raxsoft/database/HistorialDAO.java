package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.maven.raxsoft.models.MateriaPrima;
import com.maven.raxsoft.models.Movimiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Luisarriaga on 03/11/2015.
 */
public class HistorialDAO extends GenericDAO {


    public static final int MOV_ENTRADA = 1;
    public static final int MOV_SALIDA = 2;
    public static final int MOV_AJUSTE = 3;


    public HistorialDAO(Context context) {
        super(context);
    }


    /**
     * Método que registra una salida en el historial en el Inventario.
     */

    public boolean registrarSalida(int materiaID, int cantidadModificada) {
        //Se genera el contentValues para el la inserción en historial.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA, materiaID);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD, cantidadModificada);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_FECHA, getCurrentTime());
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV, MOV_SALIDA);
        //values.put(InventariosContract.HistorialTable.COLUMN_NAME_USUARIO,user);

        //Se realiza la inserción:
        abrir();
        boolean retorno = (database.insert(InventariosContract.HistorialTable.TABLE_NAME, null, values) != -1);
        cerrar();

        return retorno;
    }

    /**
     * Método que registra una entrada en el almacén.
     */

    public boolean registraEntrada(int materiaID, int cantidadModificada, int proveedorID, double costo) {
        //Se genera el contentValues para el la inserción en historial.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA, materiaID);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD, cantidadModificada);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_FECHA, getCurrentTime());
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV, MOV_ENTRADA);
        //values.put(InventariosContract.HistorialTable.COLUMN_NAME_USUARIO,user);

        //Se realiza la inserción:
        abrir();
        long idMovimiento = database.insert(InventariosContract.HistorialTable.TABLE_NAME, null, values);
        cerrar();

        boolean retorno = false;
        if (idMovimiento != -1) {
            //Si la inserción en el historial fue exitosa, entonces se inserta en la Tabla de Detalle_Entrada.
            //Se genera el ContentValues.
            ContentValues detalleValues = new ContentValues();
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_ID_MOVIMIENTO, idMovimiento);
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_ID_PROVEEDOR, proveedorID);
            detalleValues.put(InventariosContract.DetalleEntradaTable.COLUMN_NAME_COSTO, costo);

            //Se realiza la inserción.
            abrir();
            retorno = (database.insert(InventariosContract.DetalleEntradaTable.TABLE_NAME, null, detalleValues) != -1);
            cerrar();
        }


        return retorno;
    }

    /**
     * Método que registra un ajuste al inventario.
     */

    public boolean registrarAjuste(int materiaID, int nvaExistencia) {

        //Se genera el ContentValues para la inserción.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA, materiaID);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD, nvaExistencia);
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_FECHA, getCurrentTime());
        values.put(InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV, MOV_AJUSTE);
        //values.put(InventariosContract.HistorialTable.COLUMN_NAME_USUARIO,user);

        //Se realiza la inserción.
        abrir();
        boolean retorno = (database.insert(InventariosContract.HistorialTable.TABLE_NAME, null, values) != -1);
        cerrar();

        return retorno;
    }

    /**
     * Método que retorna todos los movimientos efectuados entre dos intervalos de tiempo.
     */

    public List<Movimiento> fetchMovimientos(String fechaInicio, String fechaFin) {


        List<Movimiento> movimientos;
        Cursor cursor = null;

        String whereClause = InventariosContract.HistorialTable.COLUMN_NAME_FECHA + " BETWEEN ? AND ?";
        String[] whereArgs = new String[]{fechaInicio + " 00:00", fechaFin + " 23:59"};
        String[] columns = new String[]{InventariosContract.HistorialTable._ID,
                InventariosContract.HistorialTable.COLUMN_NAME_ID_MATERIA,
                InventariosContract.HistorialTable.COLUMN_NAME_CANTIDAD,
                InventariosContract.HistorialTable.COLUMN_NAME_FECHA,
                InventariosContract.HistorialTable.COLUMN_NAME_TIPO_MOV};
        //,InventariosContract.HistorialTable.COLUMN_NAME_USUARIO};

        //Se realiza la consulta
        abrir();
        cursor = database.query(InventariosContract.HistorialTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        movimientos = formatHistorialCursor(cursor);
        cerrar();

        //El siguiente paso en la consulta consiste en obtener la información de cada materia prima. Se utiliza el DAO de materia prima.
        completeMateriaInfo(movimientos);

        //El último paso en la consulta consiste en obtener el costo de para cada operación de entrada.
        setCostoToMovimiento(movimientos);

        return movimientos;


    }

    /**
     * Método que formatea el cursor de la consulta a la tabla Historial
     */

    private List<Movimiento> formatHistorialCursor(Cursor cursor) {
        List<Movimiento> movimientos = new ArrayList();

        if (cursor.moveToFirst()) {
            do {
                Movimiento movimiento = new Movimiento();
                movimiento.setId(cursor.getInt(0));
                movimiento.getMateria().setId(cursor.getInt(1));
                movimiento.setCantidad(cursor.getInt(2));
                movimiento.setFecha(cursor.getString(3));
                movimiento.setTipoMov(cursor.getInt(4));
                //movimiento.setUsuario(cursor.getString(5));
                movimientos.add(movimiento);
            } while (cursor.moveToNext());
        }

        return movimientos;
    }

    /**
     * Método que obtiene el ArrayList  de la consulta a la tabla Historial y completa la información de cada materia prima.
     */

    private void completeMateriaInfo(List<Movimiento> movimientos) {

        //Se crea el DAO de materias primas
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(context);

        //Se recorre el ArrayList de movimientos para completar la materia prima de cada uno.
        for (Movimiento movimiento : movimientos) {

            MateriaPrima materiaTemp = materiaDAO.fetchMaterias(movimiento.getMateria().getId()).get(0);
            movimiento.setMateria(materiaTemp);
        }

    }

    /**
     * Método que consulta, en la tabla de Detalle Entrada el costo de una entrada en base al id de movimiento.
     */

    private double fetchCosto(int movimientoID) {

        double retorno = -1;
        String whereClause = InventariosContract.DetalleEntradaTable.COLUMN_NAME_ID_MOVIMIENTO + " = ?";
        String[] whereArgs = new String[]{Integer.toString(movimientoID)};
        String[] columns = new String[]{InventariosContract.DetalleEntradaTable.COLUMN_NAME_COSTO};

        Cursor cursor = database.query(InventariosContract.DetalleEntradaTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                retorno = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        return retorno;


    }

    /**
     *Método que recorre el ArrayList de la consulta a la tabla Historial y agrega el costo a cada movimiento.
     */

    private void setCostoToMovimiento(List<Movimiento> movimientos){
        abrir(); //Se abre la base de datos.
        for (Movimiento movimiento:movimientos) {
            if(movimiento.getTipoMov() == MOV_ENTRADA){
                //Se recorre el ArrayList y a cada movimiento de Entrada se le asigna su costo.
                movimiento.setCosto(fetchCosto(movimiento.getId()));
            }

        }

        cerrar(); //Se cierra la base de DAtos.
    }

    /**
     * Método que obtiene la fecha actual del sistema y la retorna en una cadena
     * formateada para ser almacenada en la base de datos SQLite.
     */

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(calendar.getTime());
    }
}
