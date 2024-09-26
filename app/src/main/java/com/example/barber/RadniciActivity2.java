package com.example.barber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barber.model.Radnik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RadniciActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextSearch;
    private ListView listView;

    private ArrayList<Radnik> filteredList = new ArrayList<>();
    private ArrayList<Radnik> originalList = new ArrayList<>();








    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnici2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        filteredList = new ArrayList<>();
        originalList = new ArrayList<>();
        listView = findViewById(R.id.listView);



        ArrayList<Radnik> list = new ArrayList<>();


        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            String loggedInUsername = intent.getStringExtra("username");



            CustomAdapter adapter = new CustomAdapter(this, list);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Radnik selectedRadnik = list.get(position);


                    Intent intent = new Intent(RadniciActivity2.this, ListaRezerviranihUslugaActivity4.class);

                    intent.putExtra("ime", selectedRadnik.getIme());



                    startActivity(intent);
                }
            });


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("radnici");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    Radnik radnik = dataSnapshot.getValue(Radnik.class);
                    if (radnik != null) {
                        list.add(radnik);
                        originalList.add(radnik); // Dodajte radnika u originalList
                        adapter.notifyDataSetChanged();
                        Log.d("DataDebug", "Radnik dodan: " + radnik.getIme() );
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


        }

    }}


