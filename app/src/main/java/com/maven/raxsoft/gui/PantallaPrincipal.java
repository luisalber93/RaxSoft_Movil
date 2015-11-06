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
    ListView listView;
    String[] menuPaginaPrincipal;
    private ActionBarDrawerToggle drawerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        listView = (ListView) findViewById(R.id.drawerPantallaPrincipal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuPaginaPrincipal = getResources().getStringArray(R.array.MenuPrincipal);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                menuPaginaPrincipal));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(PantallaPrincipal.this, "Item: " + menuPaginaPrincipal[arg2],
                        Toast.LENGTH_SHORT).show();

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

//        drawerLayout.setDrawerListener(drawerListener);
//        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Mostramos el botón en la barra de la aplicación

//       this. getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerListener.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(listView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(listView);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_pantalla_principal, menu);
        return true;
    }


}
