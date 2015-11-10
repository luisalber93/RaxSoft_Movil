package com.maven.raxsoft.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.adapters.ExistenciasAdapter;
import com.maven.raxsoft.database.MateriaPrimaDAO;
import com.maven.raxsoft.models.MateriaPrima;

import java.util.List;

public class Existencias extends AppCompatActivity {

    ListView listExistencias;
    List<MateriaPrima> materiaPrimaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existencias);

        //Se obtiene la referencia al ListView de existencias.
        listExistencias = (ListView) findViewById(R.id.existenciasList);
        materiaPrimaData = materiaPrimaFactory();
        if(!materiaPrimaData.isEmpty()){
            //Se crea el adapter para el ListView.
            ExistenciasAdapter adapter = new ExistenciasAdapter(this,materiaPrimaData);
            listExistencias.setAdapter(adapter);
            //Se agrega el evento de click al ListView.
            listClickEvent();

        }else{
            Toast.makeText(getBaseContext(),"No hay materias primas registradas, imposible consultar existencias.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private List<MateriaPrima> materiaPrimaFactory(){
        //Se obtiene el DAO para realizar la consulta.
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
        List<MateriaPrima> materias = materiaDAO.fetchMaterias(-1);
        return materias;

    }

    private void listClickEvent(){
        listExistencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Se obtiene la materia prima seleccionada.
                MateriaPrima materia = materiaPrimaData.get(position);
                //SE crea un bundle para lanzar el Intent
                Bundle extras = new Bundle();
                extras.putInt("id",materia.getId());
                //SE crea el Intent
                Intent movimientoIntent = new Intent(Existencias.this,Movimiento.class);
                movimientoIntent.putExtras(extras);
                startActivity(movimientoIntent);
                //Se cierra el activity para control de la navegación.
                finish();
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_existencias, menu);
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
}
