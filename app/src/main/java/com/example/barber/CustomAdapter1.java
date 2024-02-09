package com.example.barber;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.barber.model.Usluge;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter1 extends BaseAdapter {

    private final Context context;
    private final List<Usluge> dataList;
    private final List<Usluge> originalList;

    public CustomAdapter1(Context context, List<Usluge> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.originalList = new ArrayList<>(dataList);
    }

    // Clear all items in the adapter
    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items to the adapter
    public void addAll(List<Usluge> newData) {
        dataList.addAll(newData);
        notifyDataSetChanged();
    }


    public void updateList(List<Usluge> newList) {
        clear();
        addAll(newList);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item1, parent, false);
        }

        Object item = getItem(position);

        if (item instanceof Usluge) {
            Usluge usluge = (Usluge) item;

            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);

            ImageView imageView = convertView.findViewById(R.id.profileImageView);

            text1.setText( usluge.getImeUsluge() );

            text2.setText(  usluge.getCijenaUsluge() + "€"   );






            String imageUrl = usluge.getProfileImageUrl();
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
