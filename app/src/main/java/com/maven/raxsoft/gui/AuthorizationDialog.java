package com.maven.raxsoft.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.HistorialDAO;
import com.maven.raxsoft.database.MovimientoDAO;
import com.maven.raxsoft.database.UsuarioDAO;
import com.maven.raxsoft.models.*;

/**
 * Created by Luisarriaga on 15/11/2015.
 */
public class AuthorizationDialog  extends DialogFragment{

    //Componentes de la GUI.
    private EditText adminUser;
    private EditText adminPasswd;
    private View view;

    //Elementos para el ajuste.
    private int materiaID;
    private int nvaCantidad;


    //Context para los DAO.
    private Context context;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Se obtiene el inflater para el layout personalizado.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Se inicializa la variable view.
        view = inflater.inflate(R.layout.ajuste_autorizacion,null);
        //Se inicializan los valores de los EditText de la GUI.
        adminUser = (EditText) view.findViewById(R.id.adminUser);
        adminPasswd = (EditText) view.findViewById(R.id.adminPasswd);

        //Se expande y se setea el view del dialog.
        builder.setView(view)
        //Se agregan los botones.
        .setTitle("Se requiere autorización del administrador: ")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Autorizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //Se llama al método para registrar el Ajuste.
                        registerAjuste();
                    }
                })
                .setNegativeButton("Cancelar",null);

        return builder.create();

    }

    private void registerAjuste(){
        //Se obtienen los valores de los EditText.
        String testUser = adminUser.getText().toString();
        String testPasswd = adminPasswd.getText().toString();
        //Se crea el DAO para validar que el usuario sea administrador.
        UsuarioDAO userDAO = new UsuarioDAO(context);
        if(userDAO.isUserAdmin(testUser,testPasswd)){
            //Si el usuario es administrador, entonces se registra el ajuste con el usuario que lo autorizó.
            MovimientoDAO movDAO = new MovimientoDAO(context);
            ErrorDB result = movDAO.updateExistencias(materiaID,nvaCantidad,0, HistorialDAO.MOV_AJUSTE,0,0,testUser);
            Toast.makeText(context,result.getMensaje(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Este usuario no puede autorizar un ajuste.",Toast.LENGTH_SHORT).show();
        }


    }

    public void setMateriaID(int materiaID) {
        this.materiaID = materiaID;
    }

    public void setNvaCantidad(int nvaCantidad) {
        this.nvaCantidad = nvaCantidad;
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
