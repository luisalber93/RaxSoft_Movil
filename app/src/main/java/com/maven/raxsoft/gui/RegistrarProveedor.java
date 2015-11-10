package com.maven.raxsoft.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maven.raxsoft.R;

public class RegistrarProveedor extends AppCompatActivity {

    EditText razonSocial;
    EditText calle;
    EditText numero;
    EditText colonia;
    EditText telefono;
    EditText correoElectronico;
    EditText giro;
    Button btnRegistrarProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_proveedor);

        razonSocial = (EditText) findViewById(R.id.idEdTeRazonSocial);
        calle = (EditText) findViewById(R.id.idEdTeCalle);
        numero = (EditText) findViewById(R.id.idEdTeNumero);
        colonia = (EditText) findViewById(R.id.idEdTeColonia);
        telefono = (EditText) findViewById(R.id.idEdTeTelofono);
        correoElectronico = (EditText) findViewById(R.id.idEdTeCorreoElectronico);
        giro = (EditText) findViewById(R.id.idEdTeGiro);
        btnRegistrarProveedor = (Button) findViewById(R.id.idBtnRegistrarProveedor);

        btnRegistrarProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Validacion en Razon Social, valida: texto vacio
                Validaciones.ValidarTextoVacio(razonSocial.getText().toString());
//        Validacion en calle, valida: texto vacio, caracteres ilegales
                Validaciones.ValidarTextoVacio(calle.getText().toString());
                Validaciones.ValidarCaracteres(calle.getText().toString());
//        Validar numero, valida: texto vacio
                Validaciones.ValidarTextoVacio(numero.getText().toString());
//        Validacion colonia, valida: texto vacio, caracteres ilegales
                Validaciones.ValidarTextoVacio(colonia.getText().toString());
                Validaciones.ValidarCaracteres(colonia.getText().toString());
//        Validacion telefono, valido: texto vacio, longitud de 7 a 10 digitos;
                Validaciones.ValidarTextoVacio(telefono.getText().toString());
                Validaciones.ValidarTelefono(telefono.getText().toString());
//        Validacion correo electronico, valida: texto vacio, formato de correo electronico
                Validaciones.ValidarTextoVacio(correoElectronico.getText().toString());
                Validaciones.ValidarCorreoElectronico(correoElectronico.getText().toString());
//        Validacion de giro, valida: texto vacio, caracteres ilegales, longitud menor a 140 caracteres
                Validaciones.ValidarTextoVacio(giro.getText().toString());
                Validaciones.ValidarLogitudCadena(giro.getText().toString());

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar_proveedor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
