package com.maven.raxsoft.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.MateriaPrimaDAO;
import com.maven.raxsoft.database.ProveedorDAO;
import com.maven.raxsoft.models.ErrorDB;
import com.maven.raxsoft.models.MateriaPrima;
import com.maven.raxsoft.models.Proveedor;
import com.maven.raxsoft.models.Validaciones;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarMateriaPrima extends AppCompatActivity {

    //Componentes de la Interfaz de Usuario.
    private EditText nombreMateria;
    private EditText descripcionMateria;
    private Spinner unidadMateria;
    private Spinner proveedorSpinner;
    private EditText existenciaMinima;
    private EditText existenciaMaxima;
    private Button guardar;

    //Arrays para los Spinners.
    String[] nombresProveedores;
    String[] nombresUnidades;

    //Lista de Proveedores.
    List<Proveedor> listProveedores;

    //Variables que controlan las existencias de las materiasPrimas.
    int controlMinima;
    int controlMaxima;
    int controlActual;

    //Varaible que controla el id de la Materia que se está editando.
    //Cuando el Activity se utiliza para actualización.
    int idActual;
    //Varaible para controlar el cambio en el proveedor.
    int idProveedorActual;

    //Mapa con los mensajes de las validaciones.
    private Map<Integer,String> mensajesValidaciones;

    //Array que controla las validaciones.
    private boolean[] validationControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_materia_prima);
        //Se obtienen las referencias y se inicializan los componentes.
        initComponents();
        int accion = getIntent().getExtras().getInt("accion");
        //En base a la acción a realizar se prepara el Activity para insertar o borrar materias primas.
        switch (accion){
            case 1:
                prepareForInsert();
                break;
            case 2:
                prepareForUpdate();
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Sólo se agrega el menú si se está editando el registro.
        int accion = getIntent().getExtras().getInt("accion");
        if (accion == 2) {
            getMenuInflater().inflate(R.menu.menu_registrar_materia_prima, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_deleteMateria:
                //Se genera un diálogo de Confirmación.
                new AlertDialog.Builder(RegistrarMateriaPrima.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmar")
                        .setMessage("¿En verdad desea eliminar esta materia Prima?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMateriaPrima();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponents() {
        nombreMateria = (EditText) findViewById(R.id.idEdTeNombreProducto);
        descripcionMateria = (EditText) findViewById(R.id.idEdTeDescripcionProducto);
        unidadMateria = (Spinner) findViewById(R.id.idSpinnerUnidades);
        proveedorSpinner = (Spinner) findViewById(R.id.idSpProveedor);
        existenciaMinima = (EditText) findViewById(R.id.idEdTeExMin);
        existenciaMaxima = (EditText) findViewById(R.id.idEdTeExMax);
        guardar = (Button) findViewById(R.id.idBtnRegistrarProducto);

        nombresProveedores = proveedoresForSpinnerFactory();
        nombresUnidades = getResources().getStringArray(R.array.strSpinnerUnidades);

        //Antes de inicializar los spinners se valida que el array de los proveedores no sea nulo.
        //Si el array es nulo, significa que no hay proveedores para asignar. Por lo que el activity no se abre.
        if (nombresProveedores == null) {
            Toast.makeText(getBaseContext(),"Por favor registre proveedores para poder registrar materias primas.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Si hay proveedores que registrar, entonces se inicializan los spinners.
            //Se llena el spinner de unidades.
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresUnidades);
            unidadMateria.setAdapter(adapter);

            //Se llena el spinner de proveedores.
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresProveedores);
            proveedorSpinner.setAdapter(adapter);

            //Se inicializa el mapa de mensajes.
            mensajesValidaciones = new HashMap();
            mensajesValidaciones.put(0,"El nombre no puede estar vacío ni tener caracteres especiales.");
            mensajesValidaciones.put(1,"La descripción debe ser menor a 140 caracteres y no debe contener caracteres especiales.");
            mensajesValidaciones.put(2,"Las existencias mínimas no pueden estar vacías.");
            mensajesValidaciones.put(3,"Las existencias máximas no pueden estar vacías.");
            mensajesValidaciones.put(4,"Las existencias mínimas deben ser menores a las existencias máximas.");
            mensajesValidaciones.put(5,"Las existencias mínimas deben ser menores a 30 y las existencias máximas deben ser mayores a 30.");

            //Se inicializa el validationControl
            validationControl = new boolean[6];

        }


    }

    /**
     * Métodos relacionados con la inserción.
     */
    private void prepareForInsert(){
        setTitle("Registro de Materias Primas");
        //Se prepara el botón para la inserción.
        buttonForInsert();
    }

    private void buttonForInsert(){
        guardar.setText("Registrar Materia Prima");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMateriaPrima();
            }
        });
    }

    private void insertMateriaPrima(){
        //Insertar if de validaciones de campos.

        if(validateFields()){
            //Se obtiene la materia prima de los campos.
            MateriaPrima materia = retrieveMateriaFromFields(true,true);
            //Se crea el DAO de materia prima para insertar los datos.

            MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
            ErrorDB result = materiaDAO.insertMateriaPrima(materia);
            Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
            if(result.isSuccess()){
                finishAndRefreshList();
            }
        }

    }



    /**
     * Métodos Relacionados con la actualización.
     */
    private void prepareForUpdate(){

        setTitle("Actualización de Materias Primas");
        //Se obtiene del Bundle el id de la materia Prima a consultar.
        idActual = getIntent().getExtras().getInt("id");
        //Se obtienen los datos de la materia prima a consultar.

        MateriaPrima selectedMateria = fetchSelectedMateria(idActual);

        setValuesForFields(selectedMateria);
        buttonForUpdate();



    }

    private void setValuesForFields(MateriaPrima materia){
        nombreMateria.setText(materia.getNombre());
        descripcionMateria.setText(materia.getDescripcion());
        existenciaMinima.setText(Integer.toString(materia.getExistenciasMin()));
        existenciaMaxima.setText(Integer.toString(materia.getExistenciasMax()));

        //Se setean los valores de los spinner.
        unidadMateria.setSelection(((ArrayAdapter) unidadMateria.getAdapter()).getPosition(materia.getUnidad()));

        //Se obtiene el nombre del proveedor registrado de la materia para usarlo al setear el spinner.
        String nombreProv = materia.getProveedores().get(0).getNombre();
        proveedorSpinner.setSelection(((ArrayAdapter) proveedorSpinner.getAdapter()).getPosition(nombreProv));

    }

    private void buttonForUpdate(){
        guardar.setText("Guardar Cambios");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se crea un diálogo de confirmación.
                new AlertDialog.Builder(RegistrarMateriaPrima.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmar")
                        .setMessage("¿En verdad desea modificar los datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateMateriaPrima();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void updateMateriaPrima(){
        //Insertar if de validación de campos.

        if(validateFields()){
            boolean proveedorChanged = checkForProveedorChange();
            //Se recupera la materia Prima de los campos.
            MateriaPrima materia = retrieveMateriaFromFields(false,proveedorChanged);
            //Se crea el DAO para realizar la actualización.
            MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
            ErrorDB result = materiaDAO.updateMateriaPrima(materia,idActual,proveedorChanged);
            Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
            if(result.isSuccess()){
                finishAndRefreshList();
            }
        }


    }

    private boolean checkForProveedorChange(){
        boolean retorno = false;
        int selectedProveedorID = fetchProveedoresFromSpinner().get(0).getId();
        if(selectedProveedorID != idProveedorActual){
            retorno = true;
        }

        return retorno;
    }


    private MateriaPrima fetchSelectedMateria(int id){
        //Se crea el DAO para obtener los datos de la materia prima.
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
        MateriaPrima materia = materiaDAO.fetchMaterias(id).get(0);
        //Se inicializan los valores de control.
        controlMinima = materia.getExistenciasMin();
        controlMaxima = materia.getExistenciasMax();
        controlActual = materia.getExistenciasActuales();
        idProveedorActual = materia.getProveedores().get(0).getId();
        //Se retorna la materia prima.
        return materia;

    }



    /**
     * Métodos de Utilidades.
     */

    private void deleteMateriaPrima(){
        //Se crea el DAO para el borrado.
        MateriaPrimaDAO materiaDAO = new MateriaPrimaDAO(this);
        ErrorDB result = materiaDAO.deleteMateriaPrima(idActual);
        Toast.makeText(getBaseContext(),result.getMensaje(), Toast.LENGTH_SHORT).show();
        if(result.isSuccess()){
            finishAndRefreshList();
        }
    }

    private String[] proveedoresForSpinnerFactory(){
        String [] provsNombres;
        //SE consultan los proveedores existentes en la BD.
        ProveedorDAO proveedorDAO = new ProveedorDAO(this);
        listProveedores = proveedorDAO.fetchProveedores(-1);
        if(!listProveedores.isEmpty()){
            //Si la lista contiene proveedores, entonces se llena el spinner.
            provsNombres = new String[listProveedores.size()]; //Se crea el array del mismo tamaño que la lista de proveedores.
            int index = 0 ;
            for (Proveedor proveedor:listProveedores) {
                provsNombres[index] = proveedor.getNombre();
                index++;
            }


        }else{
            //Si la lista está vacía, se retorna nulo para indicarle al activity que no se permite el registro de materias primas.
            provsNombres = null;
        }

        return provsNombres;

    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(calendar.getTime());
    }

    private List<Proveedor> fetchProveedoresFromSpinner(){
        List<Proveedor> retorno = new ArrayList();
        int selectedIndex = proveedorSpinner.getSelectedItemPosition();
        Proveedor selectedProveedor = listProveedores.get(selectedIndex);
        retorno.add(selectedProveedor);
        return retorno;
    }

    private void finishAndRefreshList(){
        Intent provsIntent = new Intent(RegistrarMateriaPrima.this,MateriasPrimas.class);
        startActivity(provsIntent);
        finish();
    }

    private MateriaPrima retrieveMateriaFromFields(boolean insert,boolean proveedorChanged){
        MateriaPrima materiaPrima = new MateriaPrima();
        materiaPrima.setNombre(nombreMateria.getText().toString());
        materiaPrima.setDescripcion(descripcionMateria.getText().toString());
        materiaPrima.setUnidad(unidadMateria.getSelectedItem().toString());
        materiaPrima.setFechaCreacion(getCurrentTime());
        materiaPrima.setExistenciasMin(Integer.parseInt(existenciaMinima.getText().toString()));
        materiaPrima.setExistenciasMax(Integer.parseInt(existenciaMaxima.getText().toString()));
        if(proveedorChanged){
            materiaPrima.setProveedores(fetchProveedoresFromSpinner());
        }


        if(insert){
            materiaPrima.setUso(1);
        }
        return materiaPrima;
    }


    private boolean validateFields(){
        //Se realizan las validaciones y se almacenan en el validationControl.
        //Nombre
        String cadenaNombre = nombreMateria.getText().toString();
        validationControl[0] = Validaciones.validarTextoVacio(cadenaNombre)&&Validaciones.validarCaracteres(cadenaNombre);
        //Descripcion.
        String cadenaDescripcion = descripcionMateria.getText().toString();
        validationControl[1] = Validaciones.validarTextoVacio(cadenaDescripcion)&&Validaciones.validarCaracteres(cadenaDescripcion)&&
                Validaciones.validarLongitudCadena(cadenaDescripcion);
        //Existencias Minimas.
        String cadenaMinimas = existenciaMinima.getText().toString();
        validationControl[2] = Validaciones.validarTextoVacio(cadenaMinimas);
        //Existencias máximas.
        String cadenaMaximas = existenciaMaxima.getText().toString();
        validationControl[3] = Validaciones.validarTextoVacio(cadenaMaximas);

        //Validar existenciasMin<existenciasMax.
        boolean validExistences = false;
        try{
            int minimas = Integer.parseInt(cadenaMinimas);
            int maximas = Integer.parseInt(cadenaMaximas);
            validExistences = (minimas<maximas);
        }catch(NumberFormatException ex){
            validExistences = false;
        }
        validationControl[4] =  validExistences;

        //Validar existenciasMin<existenciasMax.
        validExistences = false;
        try{
            int minimas = Integer.parseInt(cadenaMinimas);
            int maximas = Integer.parseInt(cadenaMaximas);
            validExistences = (minimas<30)&&(maximas>30);
        }catch(NumberFormatException ex){
            validExistences = false;
        }
        validationControl[5]=validExistences;

        //Se crea la variable de retorno.
        boolean valid = true;

        //Se recorre el array en busca de una valdiación fallida.
        for(int i=0;i<validationControl.length;i++){
            //Se busca algún false en el array.
            if(!validationControl[i]){
                valid=false;
                Toast.makeText(getBaseContext(),mensajesValidaciones.get(i),Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return valid;
    }
}
