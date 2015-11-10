package com.maven.raxsoft.gui;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.adapters.ProveedoresAdapter;
import com.maven.raxsoft.database.ProveedorDAO;
import com.maven.raxsoft.models.Proveedor;

import java.util.ArrayList;
import java.util.List;

public class Proveedores extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerContent;
    ExpandableListView proveedoresList;
    List<Proveedor> proveedoresData;
    ProveedoresAdapter proveedoresAdapter;
    String[] menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        initComponents();

        drawerMenuEvents();

        expandableListEvents();


    }

    private void expandableListEvents()
    {
        proveedoresList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                //Se obtiene el proveedor seleccionado.
                Proveedor selectedProveedor = proveedoresData.get(groupPosition);
                //Se crea un bundle para enviar los datos al Activity de edición.
                Bundle extras = new Bundle();
                extras.putInt("accion",2); //La accion 2 quiere decir Editar.
                extras.putInt("id",selectedProveedor.getId());
                //Se crea el intent.
                Intent editIntent = new Intent(Proveedores.this,RegistrarProveedor.class);
                editIntent.putExtras(extras);
                startActivity(editIntent);
                finish();
                return false;
            }
        });
    }

    private void drawerMenuEvents() {
        //Se agregan los escuchadores de eventos.
        drawerContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                switch (menu[arg2]) {
                    case "Agregar Nuevo":
                        Intent opcionProveedores = new Intent(Proveedores.this, RegistrarProveedor.class);
                        //Se crea el Bundle para indicar al Activity que será creación de proveedor.
                        Bundle extras = new Bundle();
                        extras.putInt("accion", 1);//La acción 1 indica registro.
                        opcionProveedores.putExtras(extras);
                        startActivity(opcionProveedores);
                        finish();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_proveedores, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void initComponents(){
        //Se obtiene la referencia al expandable ListView.
        proveedoresList = (ExpandableListView)findViewById(R.id.proveedoresExpandable);
        //Se inicializa el drawer menu.
        drawerContent = (ListView) findViewById(R.id.drawerProveedores);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu = getResources().getStringArray(R.array.submenu);
        drawerContent.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                menu));
        //Se inicializa el expandableListView
        proveedoresData = proveedorFactory();
        if(!proveedoresData.isEmpty()){ //Si existen proveedores a recuperar.
            //Se crea el adapter.
            proveedoresAdapter = new ProveedoresAdapter(this,proveedoresData);
            //Se asigna el adaptador
            proveedoresList.setAdapter(proveedoresAdapter);
        }else{
            Toast.makeText(getBaseContext(),"¡Vaya!, No hay proveedores registrados",Toast.LENGTH_SHORT).show();
        }


    }

    private List<Proveedor> proveedorFactory(){
        //Se crea el DAO de proveedores para la consulta.
        ProveedorDAO proveedorDAO = new ProveedorDAO(this);
        List<Proveedor> fetchedData = proveedorDAO.fetchProveedores(-1);//Se envía el -1 para recuperar a todos los proveedores.
        return fetchedData;

    }
}
