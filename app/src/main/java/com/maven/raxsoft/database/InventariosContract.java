package com.maven.raxsoft.database;

import android.provider.BaseColumns;

/**
 * Created by Luisarriaga on 01/11/2015.
 * Clase Contrato de la Base de Datos. Define las tablas, su estructura y creación en base a constantes
 * que permitan una fácil manipulación de los objetos modificando los datos en un sólo lugar.
 */
public class InventariosContract {

    //Constantes de definición de la base de datos (Nombre y Versión).
    public static final String DATABASE_NAME = "InventariosDB";
    public static final int DATABASE_VERSION = 1;




    //Constantes de definición de tipos de datos.
    public static final String TEXT_TYPE = " TEXT ";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String REAL_TYPE=" REAL ";
    public static final String COMMA_SEPARATOR=",";
    public static final String NOT_NULL_DEF = "NOT NULL";
    public static final String UNIQUE_DEF = " UNIQUE";
    public static final String PRIMARY_KEY_DEF = "PRIMARY KEY AUTOINCREMENT";

    //Se evita que se instancie esta clase con un constructor vacío.
    public InventariosContract(){

    }

    //Clase Interna con la definición de la tabla Proveedor.
    public static abstract class ProveedorTable implements BaseColumns {
        public static final String TABLE_NAME = "proveedor";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_CALLE = "calle";
        public static final String COLUMN_NAME_NUMERO = "numero";
        public static final String COLUMN_NAME_COLONIA = "colonia";
        public static final String COLUMN_NAME_ESTADO = "estado";
        public static final String COLUMN_NAME_MUNICIPIO = "municipio";
        public static final String COLUMN_NAME_TELEFONO = "telefono";
        public static final String COLUMN_NAME_GIRO = "giro";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_USO = "uso";

        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_NOMBRE+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_CALLE+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_NUMERO+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_COLONIA+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_ESTADO+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_MUNICIPIO+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_TELEFONO+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_GIRO+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_EMAIL+TEXT_TYPE+NOT_NULL_DEF+UNIQUE_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_USO+INTEGER_TYPE+"CHECK ("+COLUMN_NAME_USO+" = 0 OR "+COLUMN_NAME_USO+ " = 1 ))"; //La columna uso es para implementar un borrado lógico. El 0 indica borrado. El 1 no borrado.

        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;

    }

    //Clase interna con la definición de la tabla MateriaPrima.

    public static abstract class MateriaPrimaTable implements BaseColumns{
        public static final String TABLE_NAME = "materia_prima";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_DESCRIPCION = "descripcion";
        public static final String COLUMN_NAME_UNIDAD = "unidad";
        public static final String COLUMN_NAME_FECHA_CREACION = "fecha_creacion";
        public static final String COLUMN_NAME_EXISTENCIAS_MINIMAS = "existencias_minimas";
        public static final String COLUMN_NAME_EXISTENCIAS_MAXIMAS = "existencias_maximas";
        public static final String COLUMN_NAME_USO = "uso";

        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_NOMBRE+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_DESCRIPCION+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_UNIDAD+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_FECHA_CREACION+TEXT_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_EXISTENCIAS_MINIMAS+INTEGER_TYPE+"CHECK ("+COLUMN_NAME_EXISTENCIAS_MINIMAS+">0)"+COMMA_SEPARATOR+
                COLUMN_NAME_EXISTENCIAS_MAXIMAS+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_USO+INTEGER_TYPE+"CHECK ("+COLUMN_NAME_USO+" = 0 OR "+COLUMN_NAME_USO+ " = 1 ))"; //La columna uso es para implementar un borrado lógico. El 0 indica borrado. El 1 no borrado.


        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
    }


    //Clase interna con la definición de la tabla Proveedor_Materia
    public static abstract class ProveedorMateriaTable implements BaseColumns{
        public static final String TABLE_NAME = "proveedor_materia";
        public static final String COLUMN_NAME_ID_PROVEEDOR = "id_proveedor";
        public static final String COLUMN_NAME_ID_MATERIA = "id_materia";


        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_ID_PROVEEDOR+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_ID_MATERIA+INTEGER_TYPE+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_PROVEEDOR+") REFERENCES "+ProveedorTable.TABLE_NAME+"("+ProveedorTable._ID+")"+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_MATERIA+") REFERENCES "+MateriaPrimaTable.TABLE_NAME+"("+MateriaPrimaTable._ID+"))";

        ;

        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    //Clase interna con la definición de la tabla STOCK.
    public static abstract class StockTable implements BaseColumns{
        public static final String TABLE_NAME = "stock";
        public static final String COLUMN_NAME_ID_MATERIA = "id_materia";
        public static final String COLUMN_NAME_EXISTENCIAS= "existencias";


        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_ID_MATERIA+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_EXISTENCIAS+INTEGER_TYPE+"CHECK ("+COLUMN_NAME_EXISTENCIAS+" >= 0 )"+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_MATERIA+") REFERENCES "+MateriaPrimaTable.TABLE_NAME+"("+MateriaPrimaTable._ID+"))";

        ;

        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
    }


    //Clase interna con la definición de la tabla Usuario.
    public static abstract class UsuarioTable implements BaseColumns{
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME_USUARIO = "usuario";
        public static final String COLUMN_NAME_PASSWORD= "password";
        public static final String COLUMN_NAME_ACCESO= "acceso";
        public static final String COLUMN_NAME_MAC_ADD= "mac";


        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_USUARIO+TEXT_TYPE+NOT_NULL_DEF+UNIQUE_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_PASSWORD+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_ACCESO+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_MAC_ADD+TEXT_TYPE+NOT_NULL_DEF+")";


        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;


    }

    //Clase interna con la definicón de la tabla Historial.
    public static abstract class HistorialTable implements BaseColumns{
        public static final String TABLE_NAME = "historial";
        public static final String COLUMN_NAME_ID_MATERIA = "id_materia";
        public static final String COLUMN_NAME_CANTIDAD= "cantidad";
        public static final String COLUMN_NAME_FECHA= "fecha";
        public static final String COLUMN_NAME_TIPO_MOV= "tipo_mov";
        public static final String COLUMN_NAME_USUARIO= "usuario";


        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_ID_MATERIA+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_CANTIDAD+INTEGER_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_FECHA+TEXT_TYPE+NOT_NULL_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_TIPO_MOV+INTEGER_TYPE+NOT_NULL_DEF+" CHECK ("+COLUMN_NAME_TIPO_MOV+" = 1 OR "+COLUMN_NAME_TIPO_MOV+ " = 2  OR "+COLUMN_NAME_TIPO_MOV+" = 3)"+COMMA_SEPARATOR+ //El tipo de movimiento 1 es Entrada, el 2 es salida, el 3 es Ajuste.
                COLUMN_NAME_USUARIO+TEXT_TYPE+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_MATERIA+") REFERENCES "+MateriaPrimaTable.TABLE_NAME+"("+MateriaPrimaTable._ID+")"+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_USUARIO+") REFERENCES "+UsuarioTable.TABLE_NAME+"("+UsuarioTable.COLUMN_NAME_USUARIO+"))";



        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
    }


    //Clase interna con la definicón de la tabla Detalle Entrada.
    public static abstract class DetalleEntradaTable implements BaseColumns{
        public static final String TABLE_NAME = "detalle_entrada";
        public static final String COLUMN_NAME_ID_MOVIMIENTO = "id_movimiento";
        public static final String COLUMN_NAME_ID_PROVEEDOR= "id_proveedor";
        public static final String COLUMN_NAME_COSTO= "costo";



        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+
                _ID+INTEGER_TYPE+PRIMARY_KEY_DEF+COMMA_SEPARATOR+
                COLUMN_NAME_ID_MOVIMIENTO+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_ID_PROVEEDOR+INTEGER_TYPE+COMMA_SEPARATOR+
                COLUMN_NAME_COSTO+REAL_TYPE+NOT_NULL_DEF+" CHECK ("+COLUMN_NAME_COSTO+"> 0)"+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_MOVIMIENTO+") REFERENCES "+HistorialTable.TABLE_NAME+"("+HistorialTable._ID+")"+COMMA_SEPARATOR+
                "FOREIGN KEY ("+COLUMN_NAME_ID_PROVEEDOR+") REFERENCES "+ProveedorTable.TABLE_NAME+"("+ProveedorTable._ID+"))";

        ;

        public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
    }

}
