package com.maven.raxsoft.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Luisarriaga on 01/11/2015.
 */
public class GenericDAO {

    protected SQLiteHelper helper;
    protected SQLiteDatabase database;

    public GenericDAO(Context context){
        this.helper= new SQLiteHelper(context);
    }

    /**
     * Método para abrir la base de datos.
     */

    protected void abrir(){
        this.database = helper.getWritableDatabase();
    }


    /**
     * Método para cerrar la base de datos.
     */
    protected void cerrar(){
        this.helper.close();
    }



}
