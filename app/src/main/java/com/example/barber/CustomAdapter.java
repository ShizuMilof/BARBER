// CustomAdapter.java

package com.example.barber;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barber.model.Radnik;
import com.squareup.picasso.Picasso;

import java.util.List;

// CustomAdapter.java
public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private List<Radnik> dataList;
    private final List<Radnik> originalList; // Dodano za podršku pretraživanju

    public CustomAdapter(Context context, List<Radnik> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.originalList = dataList; // Dodano za podršku pretraživanju
    }

    // Dodajte ovu metodu za ažuriranje liste
    public void updateList(List<Radnik> newList) {
        this.dataList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        }

        Object item = getItem(position);

        if (item instanceof Radnik) {
            Radnik radnik = (Radnik) item;

            TextView textView = convertView.findViewById(android.R.id.text1);
            ImageView imageView = convertView.findViewById(R.id.profileImageView);

            textView.setText(radnik.getIme() );

            String imageUrl = radnik.getIme();
            Log.d("PicassoDebug", "Image URL: " + imageUrl);

            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(imageView);
            } else {
                Picasso.get().load(R.drawable.avatar).into(imageView);
            }
        }

        return convertView;
    }
}
