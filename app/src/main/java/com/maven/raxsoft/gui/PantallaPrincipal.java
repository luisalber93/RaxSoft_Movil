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
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;

public class PantallaPrincipal extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerContent;
    String[] menuPaginaPrincipal;
    private ActionBarDrawerToggle drawerListener;


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

                }
                drawerLayout.closeDrawers();

            }
        });
    }


}
