package com.example.barber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class FrizerskeUslugeActivity extends Activity {
    private FirebaseAuth mAuth;
    private ListView listView;

    private EditText editTextSearchUsluge;

    private final ArrayList<Usluge> filteredList = new ArrayList<>();
    private final ArrayList<Usluge> originalList = new ArrayList<>();


    private void filterUslugeList(String searchText) {
        Log.d("FilterDebug", "Filtering Usluge with: " + searchText);

        filteredList.clear();
        for (Usluge usluge : originalList) {
            if (usluge.getImeUsluge().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(usluge);
                Log.d("FilterDebug", "Match found: " + usluge.getImeUsluge());
            }
        }

        ((CustomAdapter1) listView.getAdapter()).updateList(filteredList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frizerske_usluge);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);
        editTextSearchUsluge = findViewById(R.id.editTextSearchUsluge);

        Intent intent = getIntent();
        String imePrezime = getIntent().getStringExtra("imePrezime");

        String loggedInUsername = intent.getStringExtra("username");





        ArrayList<Usluge> list1 = new ArrayList<>();
        CustomAdapter1 adapter = new CustomAdapter1(this, list1);
        listView.setAdapter(adapter);



        editTextSearchUsluge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterUslugeList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });













        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("frizerskeusluge");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Usluge usluge = dataSnapshot.getValue(Usluge.class);
                if (usluge != null) {
                    list1.add(usluge);
                    originalList.add(usluge);
                    adapter.notifyDataSetChanged();
                    Log.d("DataDebug", "Usluge dodane: " + usluge.getImeUsluge() + " - Size: " + list1.size());
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
                // Handle item click here, for example, start a new activity
                Usluge selectedUsluge = list1.get(position);

                // Pass selectedUsluge data to the next activity
                Intent intent = new Intent(FrizerskeUslugeActivity.this, VrijemeDatumUslugeActivity.class);
                intent.putExtra("imeUsluge", selectedUsluge.getImeUsluge());
                intent.putExtra("cijenaUsluge", selectedUsluge.getCijenaUsluge());
                intent.putExtra("trajanjeUsluge", selectedUsluge.getTrajanjeUsluge());
                intent.putExtra("profileImageUrl", selectedUsluge.getProfileImageUrl());
                intent.putExtra("imePrezime", imePrezime);
                intent.putExtra("username", loggedInUsername);
                startActivity(intent);
            }
        });





    }



}