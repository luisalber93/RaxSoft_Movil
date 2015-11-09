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
import com.maven.raxsoft.adapters.MateriasAdapter;
import com.maven.raxsoft.adapters.ProveedoresAdapter;
import com.maven.raxsoft.database.MateriaPrimaDAO;
import com.maven.raxsoft.models.MateriaPrima;
import com.maven.raxsoft.models.Proveedor;

import java.util.ArrayList;
import java.util.List;

public class MateriasPrimas extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerContent;
    ExpandableListView materiasList;
    List<MateriaPrima> materiasData;
    MateriasAdapter materiasAdapter;
    String[] menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias_primas);
        initComponents();
        drawerMenuEvents();
        expandableListEvents();


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_materias_primas, menu);
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

    private void drawerMenuEvents() {
        //Se agregan los escuchadores de eventos.
        drawerContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                switch (menu[arg2]) {
                    case "Agregar Nuevo":
                        Intent opcionProveedores = new Intent(MateriasPrimas.this, RegistrarMateriaPrima.class);
                        //Se crea el Bundle para indicar al Activity que será creación de proveedor.
                        Bundle extras = new Bundle();
                        extras.putInt("accion", 1);//La acción 1 indica registro.
                        opcionProveedores.putExtras(extras);
                        startActivity(opcionProveedores);
                        finish();
                        break;
                    case "Salir":

                        break;

                }
                drawerLayout.closeDrawers();

            }
        });
    }

    private void expandableListEvents()
    {
        materiasList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                //Se obtiene la materia prima seleccionada.
                MateriaPrima selectedMateria = materiasData.get(groupPosition);
                //Se crea un bundle para enviar los datos al Activity de edición.
                Bundle extras = new Bundle();
                extras.putInt("accion", 2); //La accion 2 quiere decir Editar.
                extras.putInt("id", selectedMateria.getId());
                //Se crea el intent.
                Intent editIntent = new Intent(MateriasPrimas.this, RegistrarMateriaPrima.class);
                editIntent.putExtras(extras);
                startActivity(editIntent);
                finish();
                return false;
            }
        });
    }



    //Se obtienen referencias y se les da valores a los componentes del Activity.
    private void initComponents(){
        //Se obtiene la referencia al expandable ListView.
        materiasList = (ExpandableListView)findViewById(R.id.materiasExpandable);
        //Se inicializa el drawer menu.
        drawerContent = (ListView) findViewById(R.id.drawerMaterias);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Se llena el menú y se asigna al drawer menu.
        menu = getResources().getStringArray(R.array.submenu);
        drawerContent.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                menu));

        //Se inicializa el expandableListView
        materiasData = materiasFactory();
        if(!materiasData.isEmpty()){ //Si existen materiasPrimas a recuperar.
            //Se crea el adapter.
            materiasAdapter = new MateriasAdapter(this,materiasData);
            //Se asigna el adaptador
            materiasList.setAdapter(materiasAdapter);
        }else{
            Toast.makeText(getBaseContext(), "¡Vaya!, No hay materias primas registradas", Toast.LENGTH_SHORT).show();
        }

    }

    private List<MateriaPrima> materiasFactory(){
        List<MateriaPrima> materias;
        //Se crea el DAO de consulta para las materias primas.
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
        materias = materiaDAO.fetchMaterias(-1); //Se envía el -1 para recuperar todas materias primas.
        return materias;


    }
}
