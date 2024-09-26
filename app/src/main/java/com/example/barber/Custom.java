package com.example.barber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.barber.model.Radnik;

import java.util.ArrayList;
public class Custom extends ArrayAdapter<Radnik> {
    private ArrayList<Radnik> radnikArrayList;
    private Context context;
    private int resource;

    public Custom(ArrayList<Radnik> radnikArrayList, Context context, int resource) {
        super(context, resource, radnikArrayList);
        this.radnikArrayList = radnikArrayList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return radnikArrayList.size(); // Vraćanje broja elemenata u listi radnikArrayList
    }

    @Override
    public Radnik getItem(int position) {
        return radnikArrayList.get(position); // Vraćanje Radnik objekta na određenoj poziciji
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //create view holder innter class
    private static class ViewHolder {
        TextView idtxt, titletxt, bodytxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context); // Preferirana metoda za dohvaćanje LayoutInflater-a
            convertView = layoutInflater.inflate(resource, parent, false);

            // Inicijalizacija ViewHolder-a
            viewHolder.idtxt = convertView.findViewById(R.id.imetxt);
            viewHolder.titletxt = convertView.findViewById(R.id.prezimetxt);
            viewHolder.bodytxt = convertView.findViewById(R.id.usernametxt);

            convertView.setTag(viewHolder); // Postavljanje taga na convertView
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); // Dobivanje ViewHolder-a iz convertView taga
        }

        // Dobivanje objekta Radnik iz liste radnikArrayList na osnovu trenutne pozicije
        Radnik radnik = getItem(position);

        // Postavljanje vrijednosti u TextView-ove
        viewHolder.idtxt.setText("Id:-" + radnik.getIme() + "\n");
        viewHolder.titletxt.setText("Title:-" + radnik.getPrezime() + "\n");
        viewHolder.bodytxt.setText("Body:-" + radnik.getUsername());

        return convertView;
    }
}
