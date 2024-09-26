package com.example.barber;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class RadnikAdapter extends RecyclerView.Adapter<RadnikAdapter.RadnikViewHolder> {
    private List<Radnik> radnici;

    public RadnikAdapter(List<Radnik> radnici) {
        this.radnici = radnici;
    }

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Radnik radnik);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Radnik> newRadnici) {
        this.radnici = newRadnici;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    @NonNull
    @Override
    public RadnikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_radnik, parent, false);
        return new RadnikViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadnikViewHolder holder, int position) {
        Radnik radnik = radnici.get(position);

        holder.name.setText(radnik.getIme() + " " + radnik.getPrezime());

        // Postavljanje klika na cijeli redak
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(radnik);
            }
        });

        String imageUrl = radnik.getUrlSlike();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d("RadnikAdapter", "Loading image from URL: " + imageUrl);
            // Load the image using Picasso
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.de) // Optional: placeholder while loading
                    .error(R.drawable.avatar) // Optional: error image if loading fails
                    .into(holder.imageProfile);
        } else {
            Log.e("RadnikAdapter", "Image URL is null or empty for " + radnik.getIme() + " " + radnik.getPrezime());
            holder.imageProfile.setImageResource(R.drawable.avatar); // Set default image
        }
    }

    @Override
    public int getItemCount() {
        return radnici.size();
    }

    public static class RadnikViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname;
        CircleImageView imageProfile;

        public RadnikViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image2); // Make sure this ID matches an ImageView in your XML.
            name = itemView.findViewById(R.id.text_namee); // Ensure this ID matches a TextView.
        }
    }
}
