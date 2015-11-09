package com.maven.raxsoft.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maven.raxsoft.R;

public class RegistrarMateriaPrima extends AppCompatActivity {

    Button btnMateriaPrima;
    TextView btnNombreProducto;
    TextView btnDescripcionProducto;
    TextView btnProductoMinimo;
    TextView btnProductoMaximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_materia_prima);

        btnMateriaPrima = (Button) findViewById(R.id.idBtnRegistrarProducto);
        btnNombreProducto = (TextView) findViewById(R.id.idEdTeNombreProducto);
        btnDescripcionProducto = (TextView) findViewById(R.id.idEdTeDescripcionProducto);
        btnProductoMinimo = (TextView) findViewById(R.id.idEdTeMinimo);
        btnProductoMaximo = (TextView) findViewById(R.id.idEdTeMaximo);

        btnMateriaPrima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Validacion en Nombre de materia primar, valida: texto vacio, caracteres ilegales
                Validaciones.ValidarTextoVacio(btnNombreProducto.getText().toString());
                Validaciones.ValidarCaracteres(btnNombreProducto.getText().toString());
//                Validacion en descripcion de materia prima, valida: texto vacio, longitud menor a 140 caracteres, caracteres ilegales
                Validaciones.ValidarTextoVacio(btnDescripcionProducto.getText().toString());
                Validaciones.ValidarCaracteres(btnDescripcionProducto.getText().toString());
                Validaciones.ValidarLogitudCadena(btnDescripcionProducto.getText().toString());
//                Validacion en minimo en materia prima, valida: texto vacio
                Validaciones.ValidarTextoVacio(btnProductoMinimo.getText().toString());
//                Validacion en maximo en materia prima, valida: texto vacio
                Validaciones.ValidarTextoVacio(btnProductoMaximo.getText().toString());


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar_materia_prima, menu);
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
