package com.maven.raxsoft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.maven.raxsoft.R;
import com.maven.raxsoft.models.Proveedor;

import java.util.List;

/**
 * Created by Luisarriaga on 07/11/2015.
 * Adaptador para el extenden ListView de
 */
public class ProveedoresAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Proveedor> proveedores;

    public ProveedoresAdapter(Context context, List<Proveedor> proveedores){
        this.context=context;
        this.proveedores = proveedores;

    }

    @Override
    public int getGroupCount() {
        return proveedores.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return proveedores.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return proveedores.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return proveedores.get(groupPosition).getId();
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
        Proveedor headerProveedor = (Proveedor) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.proveedor_group,null);
        }

        TextView proveedorNombre = (TextView) convertView.findViewById(R.id.proveedorGroupNombre);
        TextView proveedorGiro = (TextView) convertView.findViewById(R.id.proveedorGrupoGiro);

        proveedorNombre.setText(headerProveedor.getNombre());
        proveedorGiro.setText(headerProveedor.getGiro());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Proveedor detailProveedor = (Proveedor)getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.proveedor_detail,null);
        }

        TextView proveedorPhone = (TextView) convertView.findViewById(R.id.proveedorDetailPhone);
        TextView proveedorMail = (TextView) convertView.findViewById(R.id.proveedorDetailMail);
        TextView proveedorCalle = (TextView) convertView.findViewById(R.id.proveedorDetailCalleNum);
        TextView proveedorColonia = (TextView) convertView.findViewById(R.id.proveedorDetailColonia);
        TextView proveedorEstado = (TextView) convertView.findViewById(R.id.proveedorDetailEstadoMunicipio);


        proveedorPhone.setText("Teléfono: "+detailProveedor.getTelefono());
        proveedorMail.setText("E-mail: "+detailProveedor.getEmail());
        proveedorCalle.setText(detailProveedor.getCalle()+" #"+detailProveedor.getNumero());
        proveedorColonia.setText("Colonia: "+detailProveedor.getColonia());
        proveedorEstado.setText(detailProveedor.getMunicipio()+","+detailProveedor.getEstado());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
