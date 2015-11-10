package com.maven.raxsoft.gui;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.UsuarioDAO;
import com.maven.raxsoft.models.ErrorDB;

public class Login extends ActionBarActivity {

//    DrawerLayout drawerLayout;
//    ListView listView;
//    String[] menuPaginaPrincipal ;
    Button btnIngresar;
    EditText inpUsurio;
    EditText inpPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//      Creación de referencias para componentes de la GUI.
        initComponents();



        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });




    }

    /*
     * mostramos/ocultamos el menu al precionar el icono de la aplicacion
     * ubicado en la barra XXX
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (drawerLayout.isDrawerOpen(listView)) {
//                    drawerLayout.closeDrawers();
//                } else {
//                    drawerLayout.openDrawer(listView);
//                }
//                return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponents(){
        btnIngresar=(Button) findViewById(R.id.btnIngresar);
        inpUsurio = (EditText) findViewById(R.id.idEdTeUsuario);
        inpPasswd = (EditText) findViewById(R.id.idEdTeContraseña);

    }

    private void authenticateUser(){
        //SE crea el DAO para consultar el usuario.
        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        String usuario = inpUsurio.getText().toString();
        String passwd = inpPasswd.getText().toString();

        //Se realiza la consulta
        ErrorDB result=usuarioDAO.authenticateUser(usuario,passwd);

//        result.setSuccess(true);
//        result.setMensaje("emp");

        if(result.isSuccess()){
            //Se crea un bundle para enviar el role.
            Bundle bundle = new Bundle();
            bundle.putString("role",result.getMensaje());
            //Se crea el Intent para lanzar el activity.
            Intent principalIntent = new Intent(Login.this,PantallaPrincipal.class);
            principalIntent.putExtras(bundle);
            startActivity(principalIntent);
        }else{
            Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
            //Se limpian los campos
            inpUsurio.setText("");
            inpPasswd.setText("");
        }

    }
}
