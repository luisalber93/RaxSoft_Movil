package com.maven.raxsoft.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
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
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.HistorialDAO;
import com.maven.raxsoft.database.MateriaPrimaDAO;
import com.maven.raxsoft.database.MovimientoDAO;
import com.maven.raxsoft.models.ErrorDB;
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

    //Variable que almacena el username de quien registra el movimiento.
    private String username;

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

        //Se agregan eventos al botón:
        buttonEvents();



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
        username =  getIntent().getExtras().getString("username");
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

                //Si la casilla de ajuste no está marcada.
                if (!ckAjuste.isChecked()) {
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
                } else {
                    //Si la casilla está marcada: Se valida que el nuevo valor sea diferente que el de existencias actuales para marcar el movimiento ocmo ajuste.
                    movimientoTipo = (newVal != actuales) ? HistorialDAO.MOV_AJUSTE : -1;
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
                if (isChecked) {
                    //Implica que es un ajuste de inventario, se actualiza la GUI para ello.
                    toggleProveedorVisibility(false);
                    movimientoTipo = -1;
                    npCantidad.setMinValue(minimas);
                    npCantidad.setMaxValue(maximas);
                    npCantidad.setValue(actuales);

                } else {
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

    private void buttonEvents(){

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //la operación a realizar varía en función del tipo de movimiento que esté activo:
                switch (movimientoTipo) {
                    case -1:
                        //No se ha realizado ningún movimiento. Se presenta un Toast con el mensaje correspondiente:
                        Toast.makeText(getBaseContext(), "Las existencias no han sido modificadas. No se registrará ningún movimiento", Toast.LENGTH_SHORT).show();
                        break;
                    case HistorialDAO.MOV_SALIDA:
                        registerSalida();
                        //Se invoca el método para registrar el movimiento de salida.
                        break;
                    case HistorialDAO.MOV_ENTRADA:
                        registerEntrada();
                        //Se invoca al método para registrar el movimiento de entrada.
                        break;
                    case HistorialDAO.MOV_AJUSTE:
                        registerAjuste();
                        //Se invoca al método para registrar el movimiento de ajuste.
                        break;
                }
            }
        });

    }


    private void registerSalida(){
        //Se genera un diálogo de Confirmación.
        new AlertDialog.Builder(Movimiento.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmar")
                .setMessage("La salida se registrará con el usuario: "+username+"\n¿Desea Continuar?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se registra la salida de mercancia con el DAO de movimiento.
                        updateExistencias(HistorialDAO.MOV_SALIDA);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void registerEntrada(){
        //Se genera un diálogo de Confirmación.
        new AlertDialog.Builder(Movimiento.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmar")
                .setMessage("La entrada se registrará con el usuario: "+username+"\n¿Desea Continuar?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se registra la salida de mercancia con el DAO de movimiento.
                        updateExistencias(HistorialDAO.MOV_ENTRADA);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void registerAjuste(){
        //Se instancia el Dialogo de confirmación.
        AuthorizationDialog autorizacion = new AuthorizationDialog();
        //Se setean los valores adecuados.
        autorizacion.setMateriaID(idMateria);
        autorizacion.setNvaCantidad(npCantidad.getValue());
        autorizacion.setContext(this);
        //Se muestra el dialog.
        autorizacion.show(getFragmentManager(),"Autorización");



    }

    private void updateExistencias(int tipoMov){
        //Se crea el DAO para la operación.
        MovimientoDAO movDAO = new MovimientoDAO(this);
        //Se obtiene el número seleccionado del numberPicker.
        int nvasExistencias = npCantidad.getValue();
        int cantidadModificada = 0;
        //Se prepara la variable que almacenará el id del prooveedor.
        int provId = 0;
        //Se prepara la variable que almacenará el costo.
        double costo = 0;
        //Variable booleana para autorizar la operación.
        boolean proceeed = false;
        switch(tipoMov){
            case HistorialDAO.MOV_SALIDA:
                //Se calcula la cantidad modificada restando de las existencias actuales la nueva cantidad.
                cantidadModificada = actuales-nvasExistencias;
                proceeed = true;
                break;
            case HistorialDAO.MOV_ENTRADA:
                //Se calcula la cantidad modificada restando de las nuevas existencias las actuales.
                cantidadModificada = nvasExistencias-actuales;
                //Se obtiene el id del proveedorSeleccionado.
                int selectedIndex = spProveedor.getSelectedItemPosition();
                provId = proveedores.get(selectedIndex).getId();
                //Se obtiene el costo registrado para la entrada.
                try{
                    costo = Double.parseDouble(txtCosto.getText().toString());
                    proceeed = true;
                }catch(NumberFormatException ex){
                    proceeed = false;
                }

                break;


        }

        if(proceeed){
            //Se realiza la operación en la BD.
            ErrorDB result = movDAO.updateExistencias(idMateria,nvasExistencias,cantidadModificada,tipoMov,provId,costo,username);
            Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
            if(result.isSuccess()){
                finish();
            }

        }else{
            Toast.makeText(getBaseContext(),"Por favor verifique que el costo sea un número decimal como: 200.50, 190.00 ó 175",Toast.LENGTH_SHORT).show();
        }

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
