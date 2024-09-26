package com.example.barber;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.Radnik;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RadnikAdapter1 extends RecyclerView.Adapter<RadnikAdapter1.RadnikViewHolder> {
    private List<Radnik> radnici;
    private OnItemClickListener listener;
    private Context context;
    private OkHttpClient okHttpClient;

    public RadnikAdapter1(Context context, List<Radnik> radnici) {
        this.radnici = radnici;
        this.context = context;
        initializeUnsafeHttpClient();
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
    public RadnikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new RadnikViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadnikViewHolder holder, int position) {
        Radnik radnik = radnici.get(position);

        holder.name.setText(radnik.getIme() + " " + radnik.getPrezime());
        holder.email.setText(radnik.getEmail());
        holder.verificationStatus.setText(radnik.isVerificiran() ? "Potvrden račun" : "Račun treba odobrenje admina");

        String imageUrl = radnik.getUrlSlike();
        Log.d("RadnikAdapter1", "Loading image from URL: " + imageUrl);

        // Load the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.avatar) // Optional: placeholder while loading
                .error(R.drawable.avatar) // Optional: error image if loading fails
                .into(holder.imageProfile);

        // Set check mark color based on verification status
        holder.verificationCheck.setImageResource(radnik.isVerificiran() ? R.drawable.correct : R.drawable.correct1);

        // Set background color based on verification status
        int backgroundColor = radnik.isVerificiran() ? ContextCompat.getColor(context, R.color.white) : ContextCompat.getColor(context, R.color.red);
        holder.itemView.setBackgroundColor(backgroundColor);

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(radnik);
            }
        });

        // Set click listener for the profile image to enlarge it
        holder.imageProfile.setOnClickListener(v -> {
            showImageDialog(imageUrl);
        });

        // Set click listener for the check mark
        holder.verificationCheck.setOnClickListener(v -> {
            if (!radnik.isVerificiran()) {
                verifyUser(radnik, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return radnici.size();
    }

    public static class RadnikViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, verificationStatus;
        CircleImageView imageProfile;
        ImageView verificationCheck;

        public RadnikViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image); // Make sure this ID matches an ImageView in your XML
            name = itemView.findViewById(R.id.text_name); // Ensure this ID matches a TextView
            email = itemView.findViewById(R.id.text_email); // Ensure this ID matches a TextView
            verificationStatus = itemView.findViewById(R.id.text_verification_status); // Ensure this ID matches a TextView
            verificationCheck = itemView.findViewById(R.id.verification_check); // Ensure this ID matches an ImageView
        }
    }

    private void initializeUnsafeHttpClient() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyUser(Radnik radnik, RadnikViewHolder holder) {
        String url = "https://172.20.10.3:7194/api/Radnici/VerifyUser"; // Replace with your actual API endpoint
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = String.valueOf(radnik.getId()); // Sending the user ID as a plain number
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("RadnikAdapter1", "Verification failed: " + e.getMessage());

                // Show toast on the main UI thread
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Update the Radnik object and the UI
                    radnik.setVerificiran(true);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.verificationCheck.setImageResource(R.drawable.correct);
                        holder.verificationStatus.setText("Verified");
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white)); // Update background color
                        Toast.makeText(context, "KORISNIK JE POTVRĐEN!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.e("RadnikAdapter1", "Verification failed: " + response.message());
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Verification failed: " + response.message(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void showImageDialog(String imageUrl) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_image);

        ImageView enlargedImage = dialog.findViewById(R.id.enlarged_image);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(enlargedImage);

        dialog.show();
    }
}
