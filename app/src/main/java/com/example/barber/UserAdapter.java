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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Radnik> radnici;
    private OnItemClickListener listener;

    public UserAdapter(List<Radnik> radnici) {
        this.radnici = radnici;
    }

    public interface OnItemClickListener {
        void onItemClick(Radnik radnik);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Radnik> newRadnici) {
        this.radnici = newRadnici;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Radnik radnik = radnici.get(position);

        holder.name.setText(radnik.getIme() + " " + radnik.getPrezime());
        holder.email.setText(radnik.getEmail());

        String imageUrl = radnik.getUrlSlike();
        Log.d("UserAdapter", "Loading image from URL: " + imageUrl);

        // Load the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.avatar) // Optional: placeholder while loading
                .error(R.drawable.avatar) // Optional: error image if loading fails
                .into(holder.imageProfile);

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(radnik);
            }
        });
    }

    @Override
    public int getItemCount() {
        return radnici.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        CircleImageView imageProfile;

        public UserViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image); // Make sure this ID matches an ImageView in your XML
            name = itemView.findViewById(R.id.text_name); // Ensure this ID matches a TextView
            email = itemView.findViewById(R.id.text_email); // Ensure this ID matches a TextView
        }
    }
}
