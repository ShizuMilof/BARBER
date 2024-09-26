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

import com.example.barber.model.Usluge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BrisanjeUslugaActivity extends Activity {
    private FirebaseAuth mAuth;
    private ListView listView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brisanje_usluga);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);


        ArrayList<Usluge> list1 = new ArrayList<>();




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("frizerskeusluge");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Usluge usluge = dataSnapshot.getValue(Usluge.class);
                if (usluge != null) {
                    list1.add(usluge);

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
                final Usluge selectedUsluga = list1.get(position);

                // Koristi AlertDialog za potvrdu brisanja
                AlertDialog.Builder builder = new AlertDialog.Builder(BrisanjeUslugaActivity.this);
                builder.setTitle("Potvrda brisanja");
                builder.setMessage("Jeste li sigurni da želite obrisati " + selectedUsluga.getNaziv() + "?");

                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dohvati referencu na bazu podataka
                        DatabaseReference uslugeReference = FirebaseDatabase.getInstance().getReference().child("frizerskeusluge");

                        // Pronađi ključ odabrane usluge u bazi podataka
                        String selectedUslugaKey = selectedUsluga.getNaziv();

                        // Briši odabrani element iz baze podataka
                        uslugeReference.child(selectedUslugaKey).removeValue();

                        // Osvježi listu nakon brisanja
                        list1.remove(position);

                    }
                });

                builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ako korisnik odabere "Ne", zatvori dijalog
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }}