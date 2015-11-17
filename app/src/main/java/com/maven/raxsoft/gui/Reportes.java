package com.maven.raxsoft.gui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.maven.raxsoft.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reportes extends AppCompatActivity {

    //Elementos de la GUI.
    private Spinner spTipoReporte;
    private TextView fechaInicial;
    private TextView fechaFinal;
    private Button btnConsultar;

    //Variables de año, mes y día para el datePicker de fecha inicio.
    private int iniYear;
    private int iniMonth;
    private int iniDay;





    //Variables de año, mes y día para el datePicker de fecha fin.
    private int endYear;
    private int endMonth;
    private int endDay;

    //Array con las opciones de reportes.
    private String [] reportOptions;

    //DatePicker Dialogs.
    private DatePickerDialog iniDateDialog;
    private DatePickerDialog endDateDialog;

    //Listeners para datePickers.
    //Fecha Inicial.
    private DatePickerDialog.OnDateSetListener iniDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            //Se actualizan los campos correspondientes
            iniYear = year;
            iniMonth = monthOfYear+1;
            iniDay =dayOfMonth;

            fechaInicial.setText(fetchDateForUI(iniDay,iniMonth,iniYear));

            DatePicker datePicker = endDateDialog.getDatePicker();
            String fecha = fetchDateForDB(iniDay,iniMonth,iniYear);
            datePicker.setMinDate(0);
            datePicker.setMinDate(parseDateFromString(fecha));


        }
    };

    //Fecha Final.
    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            //Se actualizan los campos correspondientes
            endYear = year;
            endMonth = monthOfYear+1;
            endDay =dayOfMonth;

            //Se actualiza la interfaz de usuario.
            fechaFinal.setText(fetchDateForUI(endDay,endMonth,endYear));




            DatePicker datePicker = iniDateDialog.getDatePicker();
            String dummy = fetchDateForDB(31,12,2100);
            String fecha = fetchDateForDB(endDay,endMonth,endYear);
            datePicker.setMaxDate(parseDateFromString(dummy));
            datePicker.setMaxDate(parseDateFromString(fecha));
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
        //Se obtienen las referencias de los componentes y se inicializan valores..
        initComponents();

        //Eventos de los textView para mostrar los diálogos
        datePickersEvent();

        //Evento del botón para realizar la consulta.
        buttonEvent();


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_reportes, menu);
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
        spTipoReporte = (Spinner)findViewById(R.id.spTipoReporte);
        fechaInicial = (TextView)findViewById(R.id.txtFechaDesde);
        fechaFinal = (TextView)findViewById(R.id.txtFechaHasta);
        btnConsultar = (Button)findViewById(R.id.btnConsultarReporte);

        //Se inicializan los editText con las fechas.
        initCurrentDates();

        //Se inicializa el spinner de opciones para reportes
        initSpinner();
    }

    private void datePickersEvent(){
        fechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se despliega el datepicker de la fecha inicial.
                iniDateDialog.show();
            }
        });

        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateDialog.show();
            }
        });
    }

    private void buttonEvent(){
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtienen las fechas de inicio y fin para ser enviadas a la BD.
                String fechaInicio = fetchDateForDB(iniDay,iniMonth,iniYear);
                String fechaFin = fetchDateForDB(endDay,endMonth,endYear);
                //Se determina el tipo de período.
                int tipoReporte = spTipoReporte.getSelectedItemPosition()+1;
                //Se crea el bundle para colocar los datos.
                Bundle extras = new Bundle();
                extras.putInt("tipoReporte",tipoReporte);
                extras.putString("fechaInicio", fechaInicio);
                extras.putString("fechaFin",fechaFin);

                //Se crea el Intent para llamar el Activity.
                Intent displayReporte = new Intent(Reportes.this, DisplayHistorial.class);
                displayReporte.putExtras(extras);
                startActivity(displayReporte);



            }
        });
    }

    private void initCurrentDates(){
        //Se inicia la fecha con la fecha actual
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int currMonth = cal.get(Calendar.MONTH)+1;
        int currDay = cal.get(Calendar.DAY_OF_MONTH);

        iniDay = currDay;
        endDay = currDay;

        iniMonth = currMonth;
        endMonth = currMonth;

        iniYear = currYear;
        endYear = currYear;

        //Se colocan las fechas actuales en los TextView.
        fechaInicial.setText(fetchDateForUI(iniDay, iniMonth, iniYear));
        fechaFinal.setText(fetchDateForUI(endDay, endMonth, endYear));

        //Se crean los dialogs.
        --currMonth;
        iniDateDialog = new DatePickerDialog(this,iniDateListener,iniYear,currMonth,iniDay);
        endDateDialog = new DatePickerDialog(this,endDateListener,endYear,currMonth,endDay);

        //Se ajustan los minDate y maxDate para evitar inconsistencias en la consulta.
        //Se empieza con el dialogo de fecha inicial.

        DatePicker datePicker = iniDateDialog.getDatePicker();
        String dummy = fetchDateForDB(31,12,2100);
        String fecha = fetchDateForDB(endDay,endMonth,endYear);
        datePicker.setMaxDate(parseDateFromString(dummy));
        datePicker.setMaxDate(parseDateFromString(fecha));

        //Se continua con la fecha final.
        datePicker = endDateDialog.getDatePicker();
        fecha = fetchDateForDB(iniDay, iniMonth, iniYear);
        datePicker.setMinDate(0);
        datePicker.setMinDate(parseDateFromString(fecha));


     }

    private void initSpinner(){
        reportOptions = getResources().getStringArray(R.array.strTiposReportes);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,reportOptions);
        spTipoReporte.setAdapter(adapter);
    }

    private String fetchDateForUI(int mDay,int mMonth,int mYear){
        String fecha = ((mDay<10)?"0"+mDay:mDay)+"/"+((mMonth<10)?"0"+mMonth:mMonth)+"/"+mYear;
        return fecha;
    }

    private String fetchDateForDB(int mDay,int mMonth,int mYear){

        String fecha= mYear+"-"+((mMonth<10)?"0"+mMonth:mMonth)+"-"+((mDay<10)?"0"+mDay:mDay);
        return fecha;

    }

    /**
     * Método que hace el parse de una cadena a un date y devuelve el long.
     *
     */

    private long parseDateFromString(String fecha){
        long retorno;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date formattedDate = format.parse(fecha);
            retorno = formattedDate.getTime();
        }catch(ParseException ex){
            retorno = -1;
        }

        return retorno;
    }




}
