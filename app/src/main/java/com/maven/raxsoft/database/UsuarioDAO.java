package com.maven.raxsoft.database;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiManager;

import com.maven.raxsoft.models.ErrorDB;
import com.maven.raxsoft.models.Usuario;

/**
 * Created by Luisarriaga on 05/11/2015.
 */
public class UsuarioDAO extends GenericDAO {


    public UsuarioDAO(Context context) {
        super(context);
    }


    /**
     * Método que permite el acceso a un usuario a la aplicación.
     */

    public ErrorDB authenticateUser(String user, String passwd){
        //El tipo de retorno es un ErrorDB pues tiene una variable booleana (autenticación) y un mensaje (Transportar el role o bien la causa del fallo).
        //Se obtiene el objeto usuario de la BD (si es que existe).
        Usuario usuario = fetchUserData(user,passwd);
        String mensaje ="";
        boolean autorizacion = false;
        if(usuario!=null){

            //Comentar las siguientes dos líneas al probar en un teléfono real.
            mensaje = usuario.getRoleAcceso(); //Se pasa el role del usuario para ser manipulado.
            autorizacion = true; //Se permite el acceso.

            //Se obtiene la dirección MAC del dispositivo.
//            String devicesMAC = getMAcAddress();
//            if(devicesMAC != null){
//                    //Si la dirección MAC no es nula, se compara con la obtenida de la BD para este usuario.
//
//                    if(devicesMAC.equals(usuario.getMac())){
//                        mensaje = usuario.getRoleAcceso(); //Se pasa el role del usuario para ser manipulado.
//                        autorizacion = true; //Se permite el acceso.
//                    }else{
//                        mensaje = "Dispositivo no registrado. Acceso denegado.";
//                    }
//            }else{
//                mensaje = "Por favor active el Wi-Fi para autenticarse.";
//            }

        }else{
            mensaje = "Usuario y/o Contraseña Inválidos. Acceso Denegado.";
        }


        return new ErrorDB(autorizacion,mensaje);
    }

    /**
     * Método que consulta la información de usuario en la base de datos.
     */

    private Usuario fetchUserData(String user, String passwd) {

        Usuario  usuario = null;
        int numRegistros = 0;
        String whereClause = InventariosContract.UsuarioTable.COLUMN_NAME_USUARIO + " = ? AND " + InventariosContract.UsuarioTable.COLUMN_NAME_PASSWORD + " = ?";
        String [] whereArgs = new String[]{user,passwd};
        String [] columns = new String[]{InventariosContract.UsuarioTable.COLUMN_NAME_ACCESO,
                InventariosContract.UsuarioTable.COLUMN_NAME_MAC_ADD,
                InventariosContract.UsuarioTable._ID};
        abrir();
        Cursor cursor =database.query(InventariosContract.UsuarioTable.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);
        if(cursor.moveToFirst()){
            do{
                usuario = new Usuario();
                usuario.setRoleAcceso(cursor.getString(0));
                usuario.setMac(cursor.getString(1));
                usuario.setId(cursor.getInt(2));
                numRegistros++;
            }while(cursor.moveToNext());
        }

        if(numRegistros>1){ //Si existe más de un registro, entones se nulifica el objeto para negar el acceso.
            usuario = null;
        }

        return usuario;

    }

    /**
     * Método que obtiene la dirección MAC del dispositivo.
     *
     */

    private String getMAcAddress(){
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = "";
        //String macAddress = wimanager.getConnectionInfo().getMacAddress();
        return macAddress;
    }


}
