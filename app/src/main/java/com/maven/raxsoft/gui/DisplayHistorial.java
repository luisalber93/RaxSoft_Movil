package com.maven.raxsoft.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.adapters.GastosAdapter;
import com.maven.raxsoft.adapters.HistorialAdapter;
import com.maven.raxsoft.database.HistorialDAO;
import com.maven.raxsoft.models.Movimiento;

import java.util.List;

public class DisplayHistorial extends AppCompatActivity {

    private ExpandableListView reportesExpandable;
    private List<Movimiento> movimientosData;
    private HistorialAdapter historialAdapter;
    private GastosAdapter gastosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_historial);
        //Se obtiene el tipo de reporte del Bundle y se inicializan los componentes acorde.
        int tipoReporte = getIntent().getExtras().getInt("tipoReporte");
        switch(tipoReporte){
            case 1: //El tipoReporte = 1 es el historial de movimientos.
                initComponentesForHistorial();
                break;
            case 2: //El tipoReporte = 2 es el reporte de gastos.
                initComponentesForGastos();
                break;

        }

    }


    private void initComponentesForHistorial(){
        setTitle("Historial de Movimientos");
        //Se obtiene la referencia al expandable ListView.
        reportesExpandable =(ExpandableListView)findViewById(R.id.reportesExpandable);
        //Se obtiene la lista de Movimientos.
        movimientosData = movimientosFactory(true);
        if(!movimientosData.isEmpty()){
            //Si la lista de movimientos contiene entradas.
            //Se crea el adaptador y luego se asigna al ListView.
            historialAdapter = new HistorialAdapter(this,movimientosData);
            reportesExpandable.setAdapter(historialAdapter);
        }else{
            //Si la lista está vacía, se envía un mensaje al usuario y se finaliza el aCtivity.
            Toast.makeText(getBaseContext(),"No hay movimientos registrados para este período",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initComponentesForGastos(){
        setTitle("Gastos Realizados");
        //Se obtiene la referencia al expandable ListView.
        reportesExpandable =(ExpandableListView)findViewById(R.id.reportesExpandable);
        //Se obtiene la lista de Movimientos.
        movimientosData = movimientosFactory(false);
        if(!movimientosData.isEmpty()){
            //SE crea el adapter.
            gastosAdapter = new GastosAdapter(this,movimientosData);
            //Se asigna el adapter al ListView.
            reportesExpandable.setAdapter(gastosAdapter);
        }else{
            //Si la lista está vacía, se envía un mensaje al usuario y se finaliza el aCtivity.
            Toast.makeText(getBaseContext(),"No hay movimientos registrados para este período",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private List<Movimiento> movimientosFactory(boolean historial){
        List<Movimiento> movimientos;
        //Se obtienen del bundle las fechas de inicio y fin para la consulta.
        String fechaInicio = getIntent().getExtras().getString("fechaInicio");
        String fechaFin = getIntent().getExtras().getString("fechaFin");
        //Se crea el DAO para la consulta.
        HistorialDAO histDAO = new HistorialDAO(this);
        movimientos = histDAO.fetchMovimientos(fechaInicio,fechaFin,historial);
        return movimientos;
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_display_historial, menu);
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
