package com.example.barber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.barber.model.Radnik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BrisanjeRadnikaActivity extends Activity {
    private FirebaseAuth mAuth;
    private ListView listView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brisanje_radnika);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);


        ArrayList<Radnik> list1 = new ArrayList<>();


        CustomAdapter adapter = new CustomAdapter(this, list1);
        listView.setAdapter(adapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("radnici");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Radnik radnik = dataSnapshot.getValue(Radnik.class);
                if (radnik != null) {
                    list1.add(radnik);
                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Dohvati odabrani element iz liste
                final Radnik selectedRadnik = list1.get(position);

                // Koristi AlertDialog za potvrdu brisanja
                AlertDialog.Builder builder = new AlertDialog.Builder(BrisanjeRadnikaActivity.this);
                builder.setTitle("Potvrda brisanja");
                builder.setMessage("Jeste li sigurni da želite obrisati " + selectedRadnik.getImePrezime() + "?");

                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dohvati referencu na bazu podataka
                        DatabaseReference uslugeReference = FirebaseDatabase.getInstance().getReference().child("radnici");

                        // Pronađi ključ odabrane usluge u bazi podataka
                        String selectedRadnikKey = selectedRadnik.getImePrezime();

                        uslugeReference.child(selectedRadnikKey).removeValue();
                        list1.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }}