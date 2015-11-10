package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.maven.raxsoft.models.ErrorDB;
import com.maven.raxsoft.models.MateriaPrima;
import com.maven.raxsoft.models.Proveedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 02/11/2015.
 */
public class MateriaPrimaDAO extends GenericDAO {


    public MateriaPrimaDAO(Context context) {
        super(context);
    }

    /**
     * Método que inserta una materia prima en la tabla.
     * Se requiere la materia prima a insertar y el proveedor que la abastece.
     */

    public ErrorDB insertMateriaPrima(MateriaPrima materia) {

        //Se obtienen los values del objeto.
        ContentValues values = getValuesFromMateria(materia, true);
        //Se abre la base de datos para realizar la inserción.
        abrir();
        //Se realiza la inserción en la tabla de Materias Primas como en la tabla de Proveedor_Materia y se obtiene el id asignado al registro.
        long materiaID = database.insert(InventariosContract.MateriaPrimaTable.TABLE_NAME, null, values);
        //Se cierra la base de datos.
        cerrar();
        Log.i(SQLiteHelper.LOG_TAG,"****Inserción del registro en tabla materia prima***");
        boolean success = false;
        if (materiaID != -1) { //La inserción del registro de materia prima fue exitosa, de manera que se procede a insertar en la tabla Proveedor_Materia.
            abrir();
            success = insertProveedorMateria(materiaID, materia.getProveedores(), false);
            Log.i(SQLiteHelper.LOG_TAG,"****Inserción de los proveedores***");
            //De igual modo se inserta en la tabla de Stock.
            success = createStock(materiaID);
            Log.i(SQLiteHelper.LOG_TAG,"****Inserción del registro en stock***");
            cerrar();

        }

        String mensaje = (success) ? "Materia Prima registrada con éxito." : "Ocurrió un error al registrar la materia prima. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG, mensaje);
        return new ErrorDB(success, mensaje);
    }

    /**
     * Método que realiza el borrado lógico de una materia prima.
     */

    public ErrorDB deleteMateriaPrima(int id) {

        //Como se realizará un borrado lógico se hará un update de un registro.

        //Se crea el whereClause para el update.
        String whereClause = InventariosContract.MateriaPrimaTable._ID + " = ?";
        //Se crea el whereArgs para el update.
        String[] whereArgs = new String[]{Integer.toString(id)};
        //Se crea el content values para el update.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_USO, 0); //El cero represena el borrado lógico de la materia prima.
        //Se abre la base de datos.
        abrir();
        boolean success = (database.update(InventariosContract.MateriaPrimaTable.TABLE_NAME, values, whereClause, whereArgs) > 0);
        //Se cierra la base de datos.
        cerrar();
        String mensaje = (success) ? "Materia prima dada de baja  exitosamente" : "Ocurrió un error al dar de baja la materia prima. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG, mensaje);

        return new ErrorDB(success, mensaje);

    }

    /**
     * Método que actualiza una materia prima.
     */

    public ErrorDB updateMateriaPrima(MateriaPrima materia, int idMateria, boolean proveedoresChanged) {
        //Se obtiene el contentValues de la materia.
        ContentValues values = getValuesFromMateria(materia, false);
        //Se crea el whereClause.
        String whereClause = InventariosContract.MateriaPrimaTable._ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(idMateria)};
        //Se realiza la operación de base de datos.
        //Se abre la base de datos.
        abrir();
        boolean success = (database.update(InventariosContract.MateriaPrimaTable.TABLE_NAME, values, whereClause, whereArgs) > 0);
        //Se cierra la base de datos.
        cerrar();

        if (proveedoresChanged) {
            //Se actualizan los registros en la tabla Proveedor_Materia.
            abrir();
            success = insertProveedorMateria(idMateria, materia.getProveedores(), true);
            cerrar();
        }


        String mensaje = (success) ? "Materia prima actualizada correctamente" : "Ocurrió un error al actualizar la materia prima. Por favor inténtelo de nuevo.";
        //Se registra el resultado.
        Log.i(SQLiteHelper.LOG_TAG, mensaje);
        return new ErrorDB(success, mensaje);
    }

    /**
     * Método que consulta una o todas las materias primas registradas.
     */

    public List<MateriaPrima> fetchMaterias(int id) {

        List<MateriaPrima> materias = null;
        Cursor cursor = null;

        //Se definen las columnas a consultar.
        String[] columns = new String[]{InventariosContract.MateriaPrimaTable._ID,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_NOMBRE,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_DESCRIPCION,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_UNIDAD,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_FECHA_CREACION,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_EXISTENCIAS_MINIMAS,
                InventariosContract.MateriaPrimaTable.COLUMN_NAME_EXISTENCIAS_MAXIMAS,
        };

        String whereClause = InventariosContract.MateriaPrimaTable.COLUMN_NAME_USO + " = ?";
        String[] whereArgs;

        //Si el id recibido es mayor que cero implica que se consulta una materia prima específica.
        //Se agregan las condiciones necesarias al WhereArgs y al WhereClause.
        if (id > 0) {
            whereClause += " AND " + InventariosContract.MateriaPrimaTable._ID + " = ?";
            whereArgs = new String[]{Integer.toString(1), Integer.toString(id)};
        } else {
            //Si no es mayor a cero implica que se buscan todos los registros.
            whereArgs = new String[]{Integer.toString(1)};
        }

        //Se abre la base de datos para realizar la consulta.
        abrir();
        cursor = database.query(InventariosContract.MateriaPrimaTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        materias = formatMateriaPrimaCursor(cursor);
        //Se cierra la base de datos.
        cerrar();

        //Se agregan las existencias actuales a cada materia prima.
        fetchCurrentExistences(materias);

        //implica que se consulta una materia prima específica por lo que se deben agregar los proveedores.
        if (id > 0) {
            queryProveedoresMateria(materias.get(0));
        }

        return materias;


    }

    /**
     * Método que consulta las existencias actuales para cada materia prima, haciendo uso del DAO de movimiento.
     */

    private void fetchCurrentExistences(List<MateriaPrima> materias){
        //Se crea el DAO de movimiento
        MovimientoDAO movDAO = new MovimientoDAO(context);
        for (MateriaPrima materia: materias) {
            materia.setExistenciasActuales(movDAO.getExistenciasActuales(materia.getId()));
        }
    }


    /**
     * Método que consulta los proveedores de una determinada materia prima y los agrega.
     */

    private void queryProveedoresMateria(MateriaPrima materia) {
        //Se consultarán los id´s de los proveedores asociados a la materia prima.
        String whereClause = InventariosContract.ProveedorMateriaTable.COLUMN_NAME_ID_MATERIA + " = ?";
        String[] whereArgs = new String[]{Integer.toString(materia.getId())};
        String[] columns = new String[]{InventariosContract.ProveedorMateriaTable.COLUMN_NAME_ID_PROVEEDOR};
        abrir();
        Cursor cursor = database.query(InventariosContract.ProveedorMateriaTable.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
        //Se procesa el ccursor.
        ArrayList<Integer> proveedoresIDs = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                //Cada id de proveedor consultado se agrega a la lista.
                proveedoresIDs.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        //Se cierra la base de datos.
        cerrar();

        //A continuación, se obtiene (usando el ProveedorDAO) el objeto asociado a cada id de Proveedor en la lista.
        ProveedorDAO provDAO = new ProveedorDAO(context);
        List<Proveedor> proveedores = new ArrayList();
        for (int proveedorID : proveedoresIDs) {
            //Se recupera el proveedor de la primera posición de la lista retornada por el DAO.
            proveedores.add(provDAO.fetchProveedores(proveedorID).get(0));
        }

        //Una vez recuperados todos los proveedores se asignan a la materia prima.
        materia.setProveedores(proveedores);


    }


    /**
     * Método que procesa un cursor y retorna una lista con Materias Primas.
     */

    private List<MateriaPrima> formatMateriaPrimaCursor(Cursor cursor) {
        List<MateriaPrima> materias = new ArrayList();

        if (cursor.moveToFirst()) {
            do {
                MateriaPrima materia = new MateriaPrima();
                materia.setId(cursor.getInt(0));
                materia.setNombre(cursor.getString(1));
                materia.setDescripcion(cursor.getString(2));
                materia.setUnidad(cursor.getString(3));
                materia.setFechaCreacion(cursor.getString(4));
                materia.setExistenciasMin(cursor.getInt(5));
                materia.setExistenciasMax(cursor.getInt(6));
                materias.add(materia);
            } while (cursor.moveToNext());
        }

        return materias;

    }


    /**
     * Método para insertar proveedores y materias en la tabla de Proveedor_Materia.
     */

    private boolean insertProveedorMateria(long materiaID, List<Proveedor> proveedores, boolean update) {
        int insertedProv = 0;



        if (update) {
            //Si la variable update es verdadera, implica que antes de insertar los nuevos proveedores es necesario borrar los que existían.
            String whereClause = InventariosContract.ProveedorMateriaTable.COLUMN_NAME_ID_MATERIA + " = ?";
            String[] whereArgs = new String[]{Long.toString(materiaID)};
            database.delete(InventariosContract.ProveedorMateriaTable.TABLE_NAME, whereClause, whereArgs);
        }

        //Se registra cada proveedor asociado a la materia prima.
        for (Proveedor proveedor : proveedores) {
            insertedProv += (database.insert(InventariosContract.ProveedorMateriaTable.TABLE_NAME, null, getValuesForProveedorMateria(materiaID, proveedor.getId())) != -1) ? 1 : 0;
        }

        return (insertedProv == proveedores.size());
    }


    /**
     * Método para obtener el ContentValues de un objeto MateriaPrima.
     */

    private ContentValues getValuesFromMateria(MateriaPrima materia, boolean insert) {

        //Se crea el contentValues.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_NOMBRE, materia.getNombre());
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_DESCRIPCION, materia.getDescripcion());
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_UNIDAD, materia.getUnidad());
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_FECHA_CREACION, materia.getFechaCreacion());
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_EXISTENCIAS_MINIMAS, materia.getExistenciasMin());
        values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_EXISTENCIAS_MAXIMAS, materia.getExistenciasMax());
        if (insert) {
            //Si la variable insert es true. Implica que se está creando un ContentValues para insertar en la tabla.
            //De modo que se añade la columna de uso.
            values.put(InventariosContract.MateriaPrimaTable.COLUMN_NAME_USO, materia.getUso());
        }

        return values;
    }

    /**
     * Método que inserta el registro del Stock cuando se inserta una nueva materia prima.
     */


    private boolean createStock(long materiaID) {
        //Se crea el content values
        ContentValues values = new ContentValues();
        values.put(InventariosContract.StockTable.COLUMN_NAME_ID_MATERIA, materiaID);
        values.put(InventariosContract.StockTable.COLUMN_NAME_EXISTENCIAS, 30);
        boolean retorno = (database.insert(InventariosContract.StockTable.TABLE_NAME, null, values) != -1);

        return retorno;
    }

    /**
     * Método que genera un ContentValues para la inserción en la tabla de Proveedor_Materia.
     */

    private ContentValues getValuesForProveedorMateria(long materiaID, int proveedorID) {
        //Se crea el ContentValues.
        ContentValues values = new ContentValues();
        values.put(InventariosContract.ProveedorMateriaTable.COLUMN_NAME_ID_MATERIA, materiaID);
        values.put(InventariosContract.ProveedorMateriaTable.COLUMN_NAME_ID_PROVEEDOR, proveedorID);
        return values;
    }


}
