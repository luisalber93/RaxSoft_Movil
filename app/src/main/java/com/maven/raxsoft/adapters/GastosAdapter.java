package com.maven.raxsoft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.maven.raxsoft.R;
import com.maven.raxsoft.models.Movimiento;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luisarriaga on 16/11/2015.
 */
public class GastosAdapter extends BaseExpandableListAdapter{

    //Componentes que se obtienen en el constructor.
    private Context context;
    private List<Movimiento> entradas;

    //Componentes que se generan al construir el adaptador.
    private List<Movimiento> groupMovimientos;
    private Map<Integer,List<Movimiento>> detailMovimientos;

    public GastosAdapter(Context context, List<Movimiento> entradas) {
        this.context = context;
        this.entradas = entradas;
        initComponents();
    }


    @Override
    public int getGroupCount() {
        return groupMovimientos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Se obtiene el id del grupo (ID de la materia prima agrupada).
        int id = groupMovimientos.get(groupPosition).getId();
        //Se obtiene la lista asociada para regresar su tamaño.
        List<Movimiento> children = detailMovimientos.get(id);
        return children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupMovimientos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Se obtiene el id del grupo (ID de la materia prima agrupada).
        int id = groupMovimientos.get(groupPosition).getId();
        //Se obtiene la lista asociada a este id para retornar el elemento.
        List<Movimiento> children = detailMovimientos.get(id);
        return children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupMovimientos.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        //Se obtiene el id del grupo (ID de la materia prima agrupada).
        int id = groupMovimientos.get(groupPosition).getId();
        //Se obtiene la lista asociada a este id para retornar el elemento.
        List<Movimiento> children = detailMovimientos.get(id);
        return children.get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //Se obtiene el objeto de grupo asociado.
        Movimiento headerMovimiento = (Movimiento) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gastos_group,null);
        }

        //Se obtienen las referencias de los componentes del layout.
        TextView materia = (TextView)convertView.findViewById(R.id.gastosGroupMateria);
        TextView costo = (TextView)convertView.findViewById(R.id.gastosGroupCosto);
        TextView cantidad = (TextView) convertView.findViewById(R.id.gastosGroupCantidad);

        //Se asignan los valores.
        //Recordando que el nombre de la materia se guardó en el usuario.
        materia.setText(headerMovimiento.getUsuario());
        costo.setText("$"+Double.toString(headerMovimiento.getCosto()));
        cantidad.setText(Integer.toString(headerMovimiento.getCantidad()));


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Se obtiene el objeto hijo.
        Movimiento detailMovimiento = (Movimiento) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gastos_detail,null);
        }

        //Se obtienen las referencias a los componentes del layout.
        TextView usuario = (TextView) convertView.findViewById(R.id.gastosDetailUser);
        TextView proveedor = (TextView) convertView.findViewById(R.id.gastosDetailProveedor);
        TextView costo = (TextView) convertView.findViewById(R.id.gastosDetailCosto);
        TextView fecha = (TextView) convertView.findViewById(R.id.gastosDetailTiempo);
        TextView cantidad = (TextView) convertView.findViewById(R.id.gastosDetailCantidad);


        //Se asignan los valores.
        usuario.setText(detailMovimiento.getUsuario());
        proveedor.setText(detailMovimiento.getMateria().getProveedores().get(0).getNombre());
        costo.setText("$"+Double.toString(detailMovimiento.getCosto()));
        fecha.setText(formatDate(detailMovimiento.getFecha()));
        cantidad.setText(Integer.toString(detailMovimiento.getCantidad()));

        return convertView;


    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    /**
     * Método que se encarga de agrupar las materias primas y construir las estructuras de datos para el ExpandableListView.
     */
    private void initComponents() {
        groupMovimientos = new ArrayList();
        detailMovimientos = new HashMap();

        //Se recorren las entradas para agruparlas.
        for (Movimiento entrada: entradas) {
            //Se obtiene el id de la materia prima y se busca en el hashMap.
            int idMateria = entrada.getMateria().getId();
            int index = groupsContainId(idMateria);
            if(index!=-1){
                //La lista contiene la clave de  materiaPrima.
                updateEntry(index,entrada);


            }else{
                //El mapa no contiene la clave de materiaPrima.
                createNewEntry(idMateria,entrada);
            }
        }


    }

    private void updateEntry(int index,Movimiento entrada){
        //En primer lugar, se actualiza el costo  y cantidad en la lista de grupos.
        Movimiento headerMovimiento = groupMovimientos.get(index);
        double costo = headerMovimiento.getCosto();
        costo+=entrada.getCosto();
        int cantidad = headerMovimiento.getCantidad();
        cantidad+=entrada.getCantidad();


        headerMovimiento.setCosto(costo);
        headerMovimiento.setCantidad(cantidad);

        //A continuación, se obtiene la lista de detalles y se agrega el movimiento.
        List<Movimiento> movimientosGrupo = detailMovimientos.get(headerMovimiento.getId());
        movimientosGrupo.add(entrada);
    }


    private void createNewEntry(int idMateria,Movimiento entrada){
        //Se crea un objeto de tipo movimiento que irá en el encabezado del grupo.
        Movimiento headerMovimiento = new Movimiento();
        //Se le asigna el id de la materia como id al movimiento.
        headerMovimiento.setId(idMateria);
        //Se inicializa el costo del encabezado del grupo.
        headerMovimiento.setCosto(entrada.getCosto());
        //Se inicializa la cantidad del grupo.
        headerMovimiento.setCantidad(entrada.getCantidad());
        //Se utiliza el campo usuario como contenedor  para el nombre de la materiaPrima.
        headerMovimiento.setUsuario(entrada.getMateria().getNombre());

        //Se agrega el movimiento a la lista.
        groupMovimientos.add(headerMovimiento);


        //Se crea el ArrayList para el detalle de cada grupo.
        List<Movimiento> movimientosGrupo = new ArrayList();
        //Se agrega el movimiento a la lista.
        movimientosGrupo.add(entrada);
        //Se crea la entrada en el mapa de detalle.
        detailMovimientos.put(idMateria, movimientosGrupo);
    }

    /**
     * Método que valida si un determinado id de materia prima ya existe en la lista de grupos.
     * Si existe, retorna la posición
     */

    private int groupsContainId(int materiaID){
        int retorno = -1;
        int index = 0;
        for (Movimiento grupo: groupMovimientos) {


            if(grupo.getId() == materiaID){
                retorno = index;
                break;
            }

            index++;

        }

        return retorno;
    }

    /**
     * Método que formatea la fecha recuperada de la BD a un formato comprensible.
     */
    private String formatDate(String fecha){
        String retorno = "";

        //En un array temporal se realiza la primera división entre fecha y hora usando el espacio en blanco.
        String[] temporal = fecha.split(" ");
        //Se asignan las cadenas a variables adecuadas.
        String date = temporal[0];
        String hora = temporal[1];

        //Se realiza la división de la fecha usando el mismo array temporal.
        //Se usa el - para obtener 0.- Año, 1.- Mes, 2.- Día.
        temporal = date.split("-");

        date = temporal[2]+"/"+temporal[1]+"/"+temporal[0];

        //Se asignan las variables en el retorno.
        retorno = date+"\n"+hora;

        return retorno;
    }

}
