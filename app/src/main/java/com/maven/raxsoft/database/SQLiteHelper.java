package com.maven.raxsoft.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Luisarriaga on 01/11/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    //Se definen las sentencias de creación para las tablas.
    private String[] createStatements = new String[]{InventariosContract.ProveedorTable.CREATE_TABLE,
            InventariosContract.MateriaPrimaTable.CREATE_TABLE,
            InventariosContract.ProveedorMateriaTable.CREATE_TABLE,
            InventariosContract.StockTable.CREATE_TABLE,
            InventariosContract.UsuarioTable.CREATE_TABLE,
            InventariosContract.HistorialTable.CREATE_TABLE,
            InventariosContract.DetalleEntradaTable.CREATE_TABLE};

    //Array de Sentencias para el borrado de las tablas se invierte el orden para no violar la integridad referencial.
    private String[] deleteStatements = new String[]{InventariosContract.DetalleEntradaTable.DROP_TABLE,
            InventariosContract.HistorialTable.DROP_TABLE,
            InventariosContract.UsuarioTable.DROP_TABLE,
            InventariosContract.StockTable.DROP_TABLE,
            InventariosContract.ProveedorMateriaTable.DROP_TABLE,
            InventariosContract.MateriaPrimaTable.DROP_TABLE,
            InventariosContract.ProveedorTable.DROP_TABLE,
            };

    //Se define el tag para los mensajes del log.
    public static final String LOG_TAG = "RaxSoftDB";


    public SQLiteHelper(Context context) {
        super(context, InventariosContract.DATABASE_NAME, null, InventariosContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Se ejecutan todas las sentencias de creación de las tablas.
        for (String createStatement: createStatements) {
            Log.i(SQLiteHelper.LOG_TAG,"Sentencia de Creación: "+createStatement);
            sqLiteDatabase.execSQL(createStatement);
        }
        Log.i(SQLiteHelper.LOG_TAG,"Se ha creado la base de datos exitosamente.");

        //Incluir en este apartado sentencias para poblar las tablas requeridas.

        long idAdmin = sqLiteDatabase.insert(InventariosContract.UsuarioTable.TABLE_NAME,null,getUsuariosValues("RaxAdmin","R4xS0ft4dmin","admin","9c:a9:e4:c0:f8:fb"));
        long idEmp = sqLiteDatabase.insert(InventariosContract.UsuarioTable.TABLE_NAME,null,getUsuariosValues("RaxEmp", "R4xS0ft3mp", "emp", "9c:a9:e4:c0:f8:fb"));

        Log.i(SQLiteHelper.LOG_TAG, "Se han insertado las filas de población preliminar de tablas: "+idAdmin+","+idEmp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Se verifica que la nueva versión sea más grande que la vieja versión.
        if(newVersion > oldVersion){
            //Se procede a borrar todas las tablas de la base de datos.
            for (String deleteStatement:deleteStatements) {
                sqLiteDatabase.execSQL(deleteStatement);
            }
            Log.i(SQLiteHelper.LOG_TAG,"Se han borrado las tablas de la BD por actualización.");
            //Se ejecuta el método onCreate para recrear la base de datos.
            onCreate(sqLiteDatabase);
        }
    }

    private ContentValues getUsuariosValues(String usuario,String password, String acceso, String mac){

        ContentValues values = new ContentValues();
        values.put(InventariosContract.UsuarioTable.COLUMN_NAME_USUARIO,usuario);
        values.put(InventariosContract.UsuarioTable.COLUMN_NAME_PASSWORD,password);
        values.put(InventariosContract.UsuarioTable.COLUMN_NAME_ACCESO,acceso);
        values.put(InventariosContract.UsuarioTable.COLUMN_NAME_MAC_ADD,mac);

        return values;

    }
}
