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
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;

public class Login extends ActionBarActivity {

//    DrawerLayout drawerLayout;
//    ListView listView;
//    String[] menuPaginaPrincipal ;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Funcionamiento del boton para iniciar sesion
        btnIngresar=(Button) findViewById(R.id.btnIngresar);


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIngresar= new Intent(Login.this,PantallaPrincipal.class);
                startActivity(intentIngresar);
            }
        });




//        listView = (ListView) findViewById(R.id.list_view);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        menuPaginaPrincipal= getResources().getStringArray(R.array.menuPaginaPrincipal);
//        listView.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1,
//                menuPaginaPrincipal));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                Toast.makeText(Login.this, "Item: " + menuPaginaPrincipal[arg2],
//                        Toast.LENGTH_SHORT).show();
//                drawerLayout.closeDrawers();
//            }
//        });

        // Mostramos el botón en la barra de la aplicación
//       this. getActionBar().setDisplayHomeAsUpEnabled(true);

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
}
