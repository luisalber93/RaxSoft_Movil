package com.maven.raxsoft.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.HistorialDAO;
import com.maven.raxsoft.database.MateriaPrimaDAO;
import com.maven.raxsoft.models.MateriaPrima;
import com.maven.raxsoft.models.Proveedor;

import java.util.List;

public class Movimiento extends AppCompatActivity {

    //Componentes de la GUI.
    NumberPicker npCantidad;
    CheckBox ckAjuste;
    Spinner spProveedor;
    EditText txtCosto;
    TextView labelProveedor;
    TextView labelCosto;
    Button btnActualizar;



    //Variable que controla el id de la materia para la que se realizará el movimiento.
    private int idMateria;

    //Variables que controla las existencias actuales, minimas y máximas de una materia prima.
    private int actuales;
    private int minimas;
    private int maximas;

    //Lista con los proveedores registrados de una materia prima.
    List<Proveedor> proveedores;

    //Variable que controla el tipo de movimiento.

    private int movimientoTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);
        //Se obtienen las referencias de los componentes de la GUI, se inicializan sus valores.
        initComponents();
        //Se consulta la materia prima seleccionada y en base a sus datos se llenan los componentes.
        fillComponents();

        //Se ocultan aquellos componentes que no deben verse.
        toggleProveedorVisibility(false);

        //Eventos para el numberPicker.
        numberPickerEvents();

        //Evento del checkBox.
        toggleAjuste();


        btnActualizar.setVisibility(View.GONE);

    }

    private void initComponents(){
        npCantidad = (NumberPicker)findViewById(R.id.npCantidad);
        ckAjuste = (CheckBox)findViewById(R.id.ckAjuste);
        spProveedor =(Spinner)findViewById(R.id.spProvedor);
        txtCosto = (EditText)findViewById(R.id.txtCosto);
        labelProveedor = (TextView) findViewById(R.id.labelProveedor);
        labelCosto = (TextView)findViewById(R.id.labelCosto);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        movimientoTipo = -1;
    }

    private void fillComponents(){
        idMateria = getIntent().getExtras().getInt("id");
        //Se crea el DAO para consultar.
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
        //Se realiza la consulta.
        MateriaPrima selectedMateria = materiaDAO.fetchMaterias(idMateria).get(0);


        //Se inicializan valores de control.
        actuales = selectedMateria.getExistenciasActuales();
        minimas = selectedMateria.getExistenciasMin();
        maximas = selectedMateria.getExistenciasMax();

        proveedores = selectedMateria.getProveedores();

        //Se llena el spinner de proveedor.
        fillSpinner();

        //Configura el numberPicker.
        setUpNumberPickerMovement();
    }

    private void setUpNumberPickerMovement(){

        npCantidad.setMinValue(minimas);
        npCantidad.setMaxValue(maximas);
        npCantidad.setValue(actuales);
        npCantidad.setWrapSelectorWheel(false);

    }

    private void fillSpinner(){

        String [] proveedoresNombres = new String[proveedores.size()];
        int index=0;
        for (Proveedor proveedor:proveedores) {
            proveedoresNombres[index] = proveedor.getNombre();
            index++;
        }

        //Se crea el adapter para el spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,proveedoresNombres);
        spProveedor.setAdapter(adapter);

    }

    private void toggleProveedorVisibility(boolean visible){
        int visibility = (visible)? View.VISIBLE:View.GONE;
        labelProveedor.setVisibility(visibility);
        labelCosto.setVisibility(visibility);
        spProveedor.setVisibility(visibility);
        txtCosto.setVisibility(visibility);
    }

    private void numberPickerEvents(){
        npCantidad.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if(movimientoTipo!=HistorialDAO.MOV_AJUSTE){
                    if (newVal > actuales) {
                        //Se actualiza la GUI y las variables para soportar el movimiento de entrada.
                        updateToEntrada();
                    } else {
                        if (newVal < actuales) {
                            //Se actualiza la GUI y las variables para soportar el movimiento de salida.
                            updateToSalida();
                        } else {
                            //Si es igual a las existencias, no se realiza ninguna operación.
                            movimientoTipo = -1;

                        }
                    }
                }


            }
        });
    }


    private void updateToEntrada(){
        //Se presentan al usuario los datos del proveedor.
        toggleProveedorVisibility(true);
        movimientoTipo = HistorialDAO.MOV_ENTRADA;


    }

    private void updateToSalida(){

        toggleProveedorVisibility(false);
        movimientoTipo = HistorialDAO.MOV_SALIDA;

    }

    private void toggleAjuste(){
        ckAjuste.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Implica que es un ajuste de inventario, se actualiza la GUI para ello.
                    toggleProveedorVisibility(false);
                    movimientoTipo = HistorialDAO.MOV_AJUSTE;
                    npCantidad.setMinValue(0);
                    npCantidad.setMaxValue(500);
                    npCantidad.setValue(actuales);

                }else{
                    //Se desactivo el ajuste de inventario.
                    toggleProveedorVisibility(false);
                    movimientoTipo = -1;
                    npCantidad.setMinValue(minimas);
                    npCantidad.setMaxValue(maximas);
                    npCantidad.setValue(actuales);
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_movimiento, menu);
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
