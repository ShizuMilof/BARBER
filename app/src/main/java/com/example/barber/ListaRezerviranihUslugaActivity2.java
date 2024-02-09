package com.example.barber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.barber.model.RezerviraniTerminModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaRezerviranihUslugaActivity2 extends Activity {
    private ListView listView;
    private ArrayList<RezerviraniTerminModel> listaRezervacija = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rezerviranih_usluga);
        Button nazadButton = findViewById(R.id.nazad);
        nazadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(ListaRezerviranihUslugaActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listViewRezervacije);

        CustomAdapter3 adapter = new CustomAdapter3(this, listaRezervacija);
        listView.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent != null) {
            String loggedInUsername;
            String ime = intent.getStringExtra("ime");

            loggedInUsername = ime ;


            DatabaseReference rezervacijeReference = FirebaseDatabase.getInstance().getReference().child("termini");
            rezervacijeReference.orderByChild("ime").equalTo(loggedInUsername).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    RezerviraniTerminModel rezervacija = dataSnapshot.getValue(RezerviraniTerminModel.class);
                    if (rezervacija != null) {
                        rezervacija.setKey(dataSnapshot.getKey());
                        listaRezervacija.add(rezervacija);
                        adapter.notifyDataSetChanged();
                    }
                }






                @Override
                public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    Log.e("MyApp", "Error: " + error.getMessage());
                }

                // Implement other ChildEventListener methods as needed
            });
        }





        }
    }
