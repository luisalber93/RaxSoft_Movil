package com.maven.raxsoft.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maven.raxsoft.R;
import com.maven.raxsoft.models.MateriaPrima;

import java.util.List;

/**
 * Created by Luis on 09/11/2015.
 */
public class ExistenciasAdapter extends BaseAdapter {

    private Context context;
    private List<MateriaPrima> materias;

    //Variables con los códigos hexadecimales para el color del semáforo de existencias actuales.
    private final String EXISTENCIAS_BAJA;
    private final String EXISTENCIAS_NORMAL;
    private final String EXISTENCIAS_ALTA;

    public ExistenciasAdapter(Context context, List<MateriaPrima> materias) {
        this.context = context;
        this.materias = materias;
        this.EXISTENCIAS_BAJA = "#e50000";
        this.EXISTENCIAS_NORMAL = "#018def";
        this.EXISTENCIAS_ALTA = "#198c19";
    }

    @Override
    public int getCount() {
        return materias.size();
    }

    @Override
    public Object getItem(int position) {
        return materias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return materias.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MateriaPrima headerMateria = (MateriaPrima) getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.materia_group, null);
        }

        //Se obtienen los elementos del layout de grupo materia para asignarles su valor.
        TextView materiaNombre = (TextView) convertView.findViewById(R.id.materiaGroupNombre);
        TextView materiaMinima = (TextView) convertView.findViewById(R.id.materiaGroupMinima);
        TextView materiaMaxima = (TextView) convertView.findViewById(R.id.materiaGroupMaxima);
        TextView materiaActual = (TextView) convertView.findViewById(R.id.materiaGroupActual);

        //Se asignan los valores en base a los datos del objeto.
        materiaNombre.setText(headerMateria.getNombre());
        materiaMinima.setText(Integer.toString(headerMateria.getExistenciasMin()));
        materiaMaxima.setText(Integer.toString(headerMateria.getExistenciasMax()));
        materiaActual.setText(Integer.toString(headerMateria.getExistenciasActuales()));

        //Se utiliza el siguiente método para darle color al TextView de existencias actuales en base al estado de las existencias.
        String colorCode = chooseColor(headerMateria.getExistenciasMin(), headerMateria.getExistenciasMax(), headerMateria.getExistenciasActuales());
        materiaActual.setTextColor(Color.parseColor(colorCode));


        return convertView;
    }

    //Método para determinar el color que debe adoptar lel indicador de exsitencias de la materia prima.
    private String chooseColor(int minimas, int maximas, int actuales) {
        String retorno = EXISTENCIAS_BAJA;

        //Se calcula el intervalo que existe entre existencias maximas y minimas, se divide en tres segmentos.
        int diff = (maximas - minimas) / 3;


        //Se verifica en que rango caen las existencias actuales para devolver el color en base a eso.
        if (actuales > (minimas + (2 * diff))) {
            retorno = EXISTENCIAS_ALTA;
        } else {
            if (actuales > (minimas + diff)) {
                retorno = EXISTENCIAS_NORMAL;
            }
        }

        return retorno;


    }
}
