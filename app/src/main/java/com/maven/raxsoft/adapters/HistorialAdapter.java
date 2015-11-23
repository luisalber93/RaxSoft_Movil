package com.maven.raxsoft.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maven.raxsoft.R;
import com.maven.raxsoft.database.HistorialDAO;
import com.maven.raxsoft.models.Movimiento;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Luisarriaga on 16/11/2015.
 */
public class HistorialAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<Movimiento> movimientos;

    //Constantes para el color de la cantidad según el tipo de movimiento.
    private final String SALIDA;
    private final String AJUSTE;
    private final String ENTRADA;

    public HistorialAdapter(Context context, List<Movimiento> movimientos) {
        this.context = context;
        this.movimientos = movimientos;
        this.SALIDA ="#e50000";
        this.AJUSTE = "#018def";
        this.ENTRADA = "#198c19";
    }


    @Override
    public int getGroupCount() {
        return movimientos.size();
    }

    @Override
    public int getChildrenCount(int groupPositon) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return movimientos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return movimientos.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return movimientos.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Movimiento headerMovimiento = (Movimiento)getGroup(groupPosition);
        //Si el view es nulo, se obtiene con el Layout Inflater.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movimiento_group,null);
        }

        //Se obtienen las referencias a los componentes y se asignan sus valores.
        TextView materia = (TextView)convertView.findViewById(R.id.movimientoGroupMateria);
        TextView fecha = (TextView)convertView.findViewById(R.id.movimientoGroupFecha);
        TextView hora = (TextView)convertView.findViewById(R.id.movimientoGroupHora);
        TextView cantidad = (TextView)convertView.findViewById(R.id.movimientoGroupCantidad);
        ImageView imagen = (ImageView)convertView.findViewById(R.id.movimientoGroupImage);


        //Se asignan los valores.
        materia.setText(headerMovimiento.getMateria().getNombre());
        //Se determina el color de la cantidad en base al tipo de movimiento.
        String colorCode = chooseColorCode(headerMovimiento.getTipoMov());
        //Se determina la imagen en base al tipo de movimiento.
        int imageResource = getImageFromMovimiento(headerMovimiento.getTipoMov());
        cantidad.setText(Integer.toString(headerMovimiento.getCantidad()));
        cantidad.setTextColor(Color.parseColor(colorCode));

        //Se obtiene la fecha y hora formateadas:
        String[] formattedTime = formatDate(headerMovimiento.getFecha());
        fecha.setText(formattedTime[0]);
        hora.setText(formattedTime[1]);
        imagen.setImageResource(imageResource);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Movimiento detailMovimiento = (Movimiento)getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movimiento_detail,null);
        }

        //Se obtienen las referencias a los componentes del layout.
        TextView user = (TextView)convertView.findViewById(R.id.movimientoDetailUser);
        TextView proveedor = (TextView)convertView.findViewById(R.id.movimientoDetailProveedor);
        TextView costo = (TextView)convertView.findViewById(R.id.movimientoDetailCosto);


        //Se asignan los valores a los componentes.
        user.setText(detailMovimiento.getUsuario());
        if(detailMovimiento.getTipoMov() == HistorialDAO.MOV_ENTRADA){
            proveedor.setText(detailMovimiento.getMateria().getProveedores().get(0).getNombre());
            costo.setText("$"+Double.toString(detailMovimiento.getCosto()));
        }else{
            proveedor.setText("N/A");
            costo.setText("N/A");
        }


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /**
     * Método que formatea la fecha recuperada de la BD a un formato comprensible.
     */
    private String[] formatDate(String fecha){
        String[] retorno = new String[2]; //En la posición 0 va la fecha, en la 1 la hora.

        //En un array temporal se realiza la primera división entre fecha y hora usando el espacio en blanco.
        String[] temporal = fecha.split(" ");
        //Se asignan las cadenas a variables adecuadas.
        String date = temporal[0];
        String hora = temporal[1];

        //Se realiza la división de la fecha usando el mismo array temporal.
        //Se usa el - para obtener 0.- Año, 1.- Mes, 2.- Día.
        temporal = date.split("-");

        date = temporal[2]+"/"+temporal[1]+"/"+temporal[0];

        //Se asignan las variables a su posición en el array de retorno.
        retorno[0]=date;
        retorno[1]=hora;

        return retorno;
    }

    /**
     * Método que determina el color de la cantidad según el tipo de movimiento realizado.
     * @param tipoMov
     * @return
     */

    private String chooseColorCode(int tipoMov){
        String colorCode = "";
        switch (tipoMov){
            case HistorialDAO.MOV_SALIDA:
                colorCode = SALIDA;
                break;
            case HistorialDAO.MOV_ENTRADA:
                colorCode = ENTRADA;
                break;
            case HistorialDAO.MOV_AJUSTE:
                colorCode = AJUSTE;
                break;
        }

        return colorCode;

    }

    private int getImageFromMovimiento(int tipoMov){
        int retorno = -1;
        switch(tipoMov){
            case HistorialDAO.MOV_ENTRADA:
                retorno = R.drawable.entry;
                break;
            case HistorialDAO.MOV_SALIDA:
                retorno= R.drawable.out;
                break;
            case HistorialDAO.MOV_AJUSTE:
                retorno= R.drawable.adjust;
                break;
        }

        return retorno;


    }

}
