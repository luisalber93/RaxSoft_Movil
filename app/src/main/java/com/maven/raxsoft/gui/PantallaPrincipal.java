package com.maven.raxsoft.gui;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.HistorialDAO;

public class PantallaPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerContent;
    private String[] menuPaginaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        initComponents();
        drawerMenuEvents();


    }

    private void initComponents(){
        initDrawerMenu();
    }

    private void initDrawerMenu(){
        drawerContent = (ListView) findViewById(R.id.drawerPantallaPrincipal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuPaginaPrincipal = createMainMenu();
        drawerContent.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                menuPaginaPrincipal));
    }

    private String[] createMainMenu(){
        Bundle extras = getIntent().getExtras();
        String role = extras.getString("role");
        int id=0;
        switch(role){
            case "admin":
                id=R.array.MenuPrincipalAdmin;
                break;
            case "emp":
                id=R.array.MenuPrincipalEmp;
                break;
        }

        String[] mainMenu = getResources().getStringArray(id);
        return mainMenu;

    }

    private void drawerMenuEvents(){
        drawerContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch (menuPaginaPrincipal[arg2]) {
                    case "Proveedores":
                        Intent opcionProveedores = new Intent(PantallaPrincipal.this, Proveedores.class);
                        startActivity(opcionProveedores);
                        break;
                    case "Materias Primas":
                        Intent opcionMateriasPrimas = new Intent(PantallaPrincipal.this, MateriasPrimas.class);
                        startActivity(opcionMateriasPrimas);
                        break;
                    case "Reportes":
                        Intent opcionReportes = new Intent(PantallaPrincipal.this,Reportes.class);
                        startActivity(opcionReportes);
                        break;
                    case "Movimientos":
                        //Se crea el bundle para enviar el nombre de usuario.
                        Bundle extras = new Bundle();
                        extras.putString("username",getIntent().getExtras().getString("username"));
                        Intent opcionMovimientos = new Intent(PantallaPrincipal.this,Existencias.class);
                        opcionMovimientos.putExtras(extras);
                        startActivity(opcionMovimientos);
                        break;
                    case "Acerca De":
                        //Se crea el Intent para el activity de acerca de.
                        Intent aboutIntent = new Intent(PantallaPrincipal.this,About.class);
                        startActivity(aboutIntent);
                        break;
                    case "Salir":
                        //Se finalizan todos los Activities.
                        Toast.makeText(getBaseContext(),"¡Hasta Luego!",Toast.LENGTH_SHORT).show();
                        finishAffinity();
                        break;

                }
                drawerLayout.closeDrawers();

            }
        });
    }


}
