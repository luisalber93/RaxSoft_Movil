package com.maven.raxsoft.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.ProveedorDAO;
import com.maven.raxsoft.models.ErrorDB;
import com.maven.raxsoft.models.Proveedor;
import com.maven.raxsoft.models.Validaciones;

import java.util.HashMap;
import java.util.Map;

public class RegistrarProveedor extends AppCompatActivity {

    //Componentes de la GUI.
    private EditText razonSocial;
    private EditText calle;
    private EditText numero;
    private EditText colonia;
    private Spinner estado;
    private Spinner municipio;
    private EditText telefono;
    private EditText mail;
    private EditText giro;
    private Button guardar;

    //String arrays
    private String [] estados;
    private String [] municipios;

    //Id del proveedor que se está actualizando (Cuando el activity se usa para actualizar).
    private int idActual;

    //Variable booleana para evitar que el escuchador del spinner de estado se dispare al setear el estado por primera vez.
    private boolean firstTime;

    //HashMap que almacena los mensajes de las validaciones.
    private Map<Integer,String> mensajesValidacion;

    //Array con el control de las validaciones.
    private boolean [] validationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_proveedor);
        //Se obtienen las referencias de los componentes.
        initComponents();
        //Se obtiene la acción a realizar del bundle.
        int accion = getIntent().getExtras().getInt("accion");
        switch(accion){
            case 1: //Se prepara el Activity para realizar una inserción de proveedores.
                prepareForInsert();
                break;
            case 2:
                //Se prepara el Activity para la edición de un proveedor.
                prepareForUpdate();
                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Sólo se agrega el menú si se está editando el registro.
        int accion = getIntent().getExtras().getInt("accion");
        if(accion==2){
            getMenuInflater().inflate(R.menu.menu_registrar_proveedor, menu);
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
            case R.id.action_deleteProveedor:
                //Se genera un diálogo de Confirmación.
                new AlertDialog.Builder(RegistrarProveedor.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmar")
                        .setMessage("¿En verdad desea eliminar este proveedor?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProveedor();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponents(){
        razonSocial = (EditText)findViewById(R.id.idEdTeRazonSocial);
        calle = (EditText)findViewById(R.id.idEdTeCalle);
        numero = (EditText)findViewById(R.id.idEdTeNum);
        colonia = (EditText)findViewById(R.id.idEdTeColonia);
        estado = (Spinner) findViewById(R.id.spEstado);
        municipio = (Spinner)findViewById(R.id.spMunicipio);
        telefono = (EditText)findViewById(R.id.idEdTeTelofono);
        mail = (EditText)findViewById(R.id.idEdTeCorreoElectronico);
        giro = (EditText)findViewById(R.id.idEdTeGiro);
        guardar = (Button) findViewById(R.id.idBtnGuardarProveedor);
        estados = getResources().getStringArray(R.array.estados);
        municipios = getResources().getStringArray(R.array.municipiosMexico);
        firstTime = true;

        //Se llenan los mensajes de validación.
        mensajesValidacion = new HashMap();
        mensajesValidacion.put(0,"La razón social no puede estar vacía.");
        mensajesValidacion.put(1,"La calle no puede estar vacía ni contener caracteres especiales.");
        mensajesValidacion.put(2,"El número no puede ir vacío.");
        mensajesValidacion.put(3,"La colonia no puede estar vacía ni contener caracteres especiales.");
        mensajesValidacion.put(4,"El teléfono debe tener una longitud de entre 7 y 10 dígitos.");
        mensajesValidacion.put(5,"El correo debe seguir el formato: algo@server.com");
        mensajesValidacion.put(6,"El giro no debe contener caracteres especiales y debe ser menor a 140 caracteres.");

        //Validation Control
        validationControl = new boolean[7];


    }

    /**
     * Métodos relacionados con la actualización.
     */

    private void prepareForUpdate(){
        //Se recupera del Bundle el id del proveedor a editar.
        setTitle("Actualización de Proveedores");
        idActual = getIntent().getExtras().getInt("id");
        Proveedor proovedorActual = fetchSelectedProveedor();
        setValuesForFields(proovedorActual);
        buttonForUpdate();

    }

    //Se setean los elemenos de la GUI en base a los datos seleccionados.
    private void setValuesForFields(Proveedor proveedor){
        //Se setean los editText.
        razonSocial.setText(proveedor.getNombre());
        calle.setText(proveedor.getCalle());
        numero.setText(proveedor.getNumero());
        colonia.setText(proveedor.getColonia());
        telefono.setText(proveedor.getTelefono());
        mail.setText(proveedor.getEmail());
        giro.setText(proveedor.getGiro());

        //Se llena el spinner de estado.
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados);
        estado.setAdapter(adapter);

        //Se setean los spinners.
        estado.setSelection(((ArrayAdapter) estado.getAdapter()).getPosition(proveedor.getEstado()));
        //Se setea el spinner de nombres en base a lo que se encuentre en el Spinner de Clasificación.
        changeMunicipiosOnSelection(estado.getSelectedItem().toString());
        municipio.setSelection(((ArrayAdapter) municipio.getAdapter()).getPosition(proveedor.getMunicipio()));

        //Se agrega el escuchador al spinner de estado.
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!firstTime) {
                    String selection = (String) parent.getItemAtPosition(position);
                    changeMunicipiosOnSelection(selection); //Se cambia el contenido del spinner en base a lo que elija el usuario.
                }
                firstTime = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing bruh!!!
            }
        });


    }

    private  void buttonForUpdate(){

        guardar.setText("Guardar Cambios");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se crea un diálogo de confirmación.
                new AlertDialog.Builder(RegistrarProveedor.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmar")
                        .setMessage("¿En verdad desea modificar los datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateProveedor();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    private void updateProveedor(){
        //Insertar if con validación de campos.

        if(validateFields()){
            //Se crea el DAO de proveedor para realizar la actualización.
            ProveedorDAO provDAO = new ProveedorDAO(this);
            ErrorDB result = provDAO.updateProveedor(retrieveProveedorFromFields(false), idActual);
            Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
            if(result.isSuccess()){
                finishAndRefreshList();
            }
        }



    }


    private Proveedor fetchSelectedProveedor(){
        //HAce uso del DAO de proveedor para regresar los datos del proveedor seleccionado.
        ProveedorDAO provDAO = new ProveedorDAO(this);
        Proveedor proveedor = provDAO.fetchProveedores(idActual).get(0);

        return proveedor;
    }

    /***
     * Métodos Relacionados con la inserción.
     */

    private void prepareForInsert() {
        setTitle("Registro de Proveedores");
        //Se llenan los spinners correspondientes.
        fillSpinnersForInsert();
        //Se prepara el botón para la acción de inserción.
        buttonForInsert();
    }

    private void fillSpinnersForInsert() {

        //Se llena el spinner de estado.
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados);
        estado.setAdapter(adapter);

        //Se llena el spinner de municipios.
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,municipios);
        municipio.setAdapter(adapter);

        //Se agrega el escuchador al spinner de estado.
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                changeMunicipiosOnSelection(selection); //Se cambia el contenido del spinner en base a lo que elija el usuario.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing bruh!!!
            }
        });

    }

    private void buttonForInsert(){
        guardar.setText("Registrar Proveedor");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProveedor();
            }
        });

    }

    private void insertProveedor(){

        //Incluir if para la validación de campos.

        if(validateFields()){
            //Se crea el DAO para la inserción del proveedor.
            ProveedorDAO proveedorDAO = new ProveedorDAO(this);
            ErrorDB result = proveedorDAO.insertProveedor(retrieveProveedorFromFields(true));
            Toast.makeText(getBaseContext(),result.getMensaje(), Toast.LENGTH_SHORT).show();
            if(result.isSuccess()){
                finishAndRefreshList();
            }
        }




    }



    /***
     * Fin Métodos Relacionados con la inserción.
     */

    /**
     * Métodos de utilidades.
     */


    private void finishAndRefreshList(){
        Intent provsIntent = new Intent(RegistrarProveedor.this,Proveedores.class);
        startActivity(provsIntent);
        finish();
    }

    private void deleteProveedor(){
        //Se crea el DAO para eliminar el proveedor.
        ProveedorDAO provDAO = new ProveedorDAO(this);
        ErrorDB result = provDAO.deleteProveedor(idActual);
        Toast.makeText(getBaseContext(),result.getMensaje(),Toast.LENGTH_SHORT).show();
        if(result.isSuccess()){
            finishAndRefreshList();
        }
    }

    private Proveedor retrieveProveedorFromFields(boolean insert){
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(razonSocial.getText().toString());
        proveedor.setCalle(calle.getText().toString());
        proveedor.setNumero(numero.getText().toString());
        proveedor.setColonia(colonia.getText().toString());
        proveedor.setEstado(estado.getSelectedItem().toString());
        proveedor.setMunicipio(municipio.getSelectedItem().toString());
        proveedor.setTelefono(telefono.getText().toString());
        proveedor.setGiro(giro.getText().toString());
        proveedor.setEmail(mail.getText().toString());
        if(insert){//Si es una inserción, entonces se crea con uso = 1.
            proveedor.setUso(1);
        }
        return proveedor;
    }

    private void changeMunicipiosOnSelection(String selectedEstado){
        int id = -1;
        switch(selectedEstado){
            case "Estado de México":
                id = R.array.municipiosMexico;
                break;
            case "Querétaro":
                id = R.array.municipiosQueretaro;
                break;
        }

        municipios = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,municipios);
        municipio.setAdapter(adapter);
    }

    private boolean validateFields(){
        //Se realizan las validaciones respectivas a cada campo y se asignan al array de validationControl.
        //Razón Social.
        validationControl[0]= Validaciones.validarTextoVacio(razonSocial.getText().toString());
        //Calle
        String cadenaCalle = calle.getText().toString();
        validationControl[1] = Validaciones.validarTextoVacio(cadenaCalle)&&Validaciones.validarCaracteres(cadenaCalle);
        //Numero
        validationControl[2] = Validaciones.validarTextoVacio(numero.getText().toString());
        //Colonia
        String cadenaColonia = colonia.getText().toString();
        validationControl[3] = Validaciones.validarTextoVacio(cadenaColonia)&&Validaciones.validarCaracteres(cadenaColonia);
        //Teléfono.
        validationControl[4]=Validaciones.validarTelefono(telefono.getText().toString());
        //Correo.
        validationControl[5] = Validaciones.validarCorreoElectronico(mail.getText().toString());
        //Giro.
        String cadenaGiro = giro.getText().toString();
        validationControl[6]=Validaciones.validarTextoVacio(cadenaGiro)&&
                             Validaciones.validarCaracteres(cadenaGiro)&&
                             Validaciones.validarLongitudCadena(cadenaGiro);

        //Se crea la variable de retorno.
        boolean valid = true;
        //Se recorre el arreglo de validation control en busca de una validación fallida.
        for(int i=0;i<validationControl.length;i++){

            //Si se encuentra un false en alguna posición...
            if(!validationControl[i]){
                valid=false; ///Falla la validación de campos.
                Toast.makeText(getBaseContext(),mensajesValidacion.get(i),Toast.LENGTH_SHORT).show(); //Se presenta un mensaje.
                break;//Se interrumpe la iteración del arreglo.
            }

        }

        return valid;

    }





}


