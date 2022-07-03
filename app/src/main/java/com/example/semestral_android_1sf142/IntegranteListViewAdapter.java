package com.example.semestral_android_1sf142;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IntegranteListViewAdapter extends BaseAdapter
{
    private final ArrayList<String[]> integrantesData;
    private final LayoutInflater layoutInflater;

    public IntegranteListViewAdapter(ArrayList<String[]> integrantesData, Context context)
    {
        this.integrantesData = integrantesData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return this.integrantesData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.integrantesData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        IntegranteListViewAdapter.CustomViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = this.layoutInflater.inflate(R.layout.integrante_item, null);
            viewHolder = new IntegranteListViewAdapter.CustomViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (CustomViewHolder) convertView.getTag();
        }

        viewHolder.nombreTv.setText(this.integrantesData.get(position)[0]);
        viewHolder.cedulaTv.setText(this.integrantesData.get(position)[1]);

        return convertView;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        final TextView nombreTv;
        final TextView cedulaTv;

        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.nombreTv = itemView.findViewById(R.id.integrante_nombre_tv);
            this.cedulaTv = itemView.findViewById(R.id.integrante_cedula_tv);
        }
    }
}
