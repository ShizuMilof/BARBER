package com.example.barber;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barber.model.TerminModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class VrijemeDatumUslugeActivity extends AppCompatActivity {


    private  String imePrezime ;

    private TimePicker timePicker;

    private String selectedDate;
    private DatePicker datePicker;
    private RecyclerView recyclerViewTermini;
    private TerminAdapter terminAdapter;
    private DatabaseReference terminiReference;

    private final TerminAdapter.OnItemClickListener onItemClickListener = new TerminAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String selectedTermin) {
            promijeniBojuTermina(selectedTermin);
        }

        @Override
        public int postaviBojuTermina(String vrijeme) {
            return 0;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vrijeme_datum_usluge);

        DatePicker datePicker = findViewById(R.id.datePicker);



        String imePrezime = getIntent().getStringExtra("imePrezime");
        String loggedInUsername = getIntent().getStringExtra("username");
        String imeUsluge = getIntent().getStringExtra("imeUsluge");
        String cijenaUsluge = getIntent().getStringExtra("cijenaUsluge");
        String trajanjeUsluge = getIntent().getStringExtra("trajanjeUsluge");
        String profileImageUrl = getIntent().getStringExtra("profileImageUrl");



        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.setMinDate(calendar.getTimeInMillis());

        timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });





        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%02d_%02d_%04d", dayOfMonth, monthOfYear + 1, year);

                // Očisti rezervacije od prethodnog datuma
                ocistiRezervacije();

                // Primijeni animaciju na recyclerViewTermini
                Animation animation = AnimationUtils.loadAnimation(VrijemeDatumUslugeActivity.this, R.anim.fade_in);
                recyclerViewTermini.startAnimation(animation);

                // Provjeri rezervacije za odabrani datum
                List<String> rezerviraniTermini = provjeriRezervacije();
                // Bojaj rezervirane termine automatski
                bojajRezerviraneTermine(rezerviraniTermini);
            }
        });


        terminiReference = FirebaseDatabase.getInstance().getReference("termini");
        timePicker = findViewById(R.id.timePicker);

        List<TerminModel> terminiList = generateTermini("termini");
        terminAdapter = new TerminAdapter(terminiList);
        terminAdapter.setOnItemClickListener(onItemClickListener);
        terminAdapter.setOnItemClickListener(new TerminAdapter.OnItemClickListener() {



            @Override
            public void onItemClick(String selectedTermin) {
                if (selectedDate != null) {
                    try {
                        // Format the selected date
                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault());
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault());

                        Date parsedSelectedDate = inputDateFormat.parse(selectedDate);
                        String formattedSelectedDate = outputDateFormat.format(parsedSelectedDate);


                        String keyToCheck = "korisnik:" + loggedInUsername + "_frizer:" + imePrezime +
                                "_termin:" + selectedTermin + "__" + formattedSelectedDate+"_rezerviran";

                        // Query the database to check if the key exists
                        terminiReference.child(keyToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    Toast.makeText(VrijemeDatumUslugeActivity.this, "Odabrani termin nije slobodan!", Toast.LENGTH_SHORT).show();
                                } else {




                                    try {

                                        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                                        Date parsedDate = inputFormat.parse(selectedTermin);
                                        String formattedTermin = outputFormat.format(parsedDate);

                                        // Postavljanje ključa (naziva) termina na formatirani termin
                                        boolean isReserved = checkReservationStatus(formattedTermin, formattedSelectedDate);

                                        String key = "korisnik:" + loggedInUsername + "_frizer:" + imePrezime +
                                                "_termin:" + formattedTermin + "__" + formattedSelectedDate;


                                        if (!isReserved) {
                                            key += "_rezerviran";
                                        }


                                        TerminFirebaseModel terminFirebaseModel = new TerminFirebaseModel(selectedDate, formattedTermin, imeUsluge, loggedInUsername, imePrezime);
                                        terminFirebaseModel.setRezerviran(true);
                                        terminiReference.child(key).setValue(terminFirebaseModel);


                                        Intent intent = new Intent(VrijemeDatumUslugeActivity.this, PregledOdabranihUslugaActivity.class);

                                        intent.putExtra("imePrezime", imePrezime);
                                        intent.putExtra("username", loggedInUsername);
                                        intent.putExtra("imeUsluge", imeUsluge);
                                        intent.putExtra("cijenaUsluge", cijenaUsluge);
                                        intent.putExtra("trajanjeUsluge", trajanjeUsluge);
                                        intent.putExtra("profileImageUrl", profileImageUrl);
                                        intent.putExtra("selectedDate", formattedSelectedDate);
                                        intent.putExtra("selectedTermin", formattedTermin);


                                        startActivity(intent);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(VrijemeDatumUslugeActivity.this, "Greška pri provjeri dostupnosti termina.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(VrijemeDatumUslugeActivity.this, "Odaberite datum prije spremanja termina!", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public int postaviBojuTermina(String vrijeme) {
                return 0;
            }
        });
    }


    private void bojajRezerviraneTermine(List<String> rezerviraniTermini) {
        for (String rezerviraniTermin : rezerviraniTermini) {
            terminAdapter.promijeniBojuTermina(rezerviraniTermin, Color.RED);
        }
    }

    private List<String> provjeriRezervacije() {
        List<String> rezerviraniTermini = new ArrayList<>();

        terminiReference.orderByChild("selectedDate").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot terminSnapshot : snapshot.getChildren()) {
                    TerminFirebaseModel rezerviraniTermin = terminSnapshot.getValue(TerminFirebaseModel.class);
                    if (rezerviraniTermin != null) {
                        String rezerviranoVrijeme = rezerviraniTermin.getSelectedTermin();
                        promijeniBojuTermina(rezerviranoVrijeme);
                        rezerviraniTermini.add(rezerviranoVrijeme);
                    }
                }

                updateColorsForAvailableTimeSlots();

                if (rezerviraniTermini.isEmpty()) {
                    prikaziZauzeteTermine(rezerviraniTermini, "Nema rezervacija za odabrani datum.");
                } else {

                    prikaziZauzeteTermine(rezerviraniTermini, "Zauzeti termini: ");

                    // Bojaj rezervirane termine automatski
                    bojajRezerviraneTermine(rezerviraniTermini);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VrijemeDatumUslugeActivity.this, "Greška pri dohvaćanju podataka iz baze.", Toast.LENGTH_SHORT).show();
            }
        });


        return rezerviraniTermini;
    }

    private void prikaziZauzeteTermine(List<String> zauzetiTermini, String porukaZaNemaTermina) {
        TextView recyclerViewTextView = findViewById(R.id.textViewRecyclerView);

        if (zauzetiTermini.isEmpty()) {
            recyclerViewTextView.setText(porukaZaNemaTermina);
        } else {
            StringBuilder message = new StringBuilder("Zauzeti termini: ");

            for (String termin : zauzetiTermini) {
                message.append(termin).append(", ");
            }

            message.setLength(message.length() - 2);

            recyclerViewTextView.setText(message.toString());
        }
    }



    public static void ukloniRezerviraneTermine(List<TerminModel> terminiList, List<String> rezerviraniTermini) {
        Iterator<TerminModel> iterator = terminiList.iterator();
        while (iterator.hasNext()) {
            TerminModel termin = iterator.next();
            if (rezerviraniTermini.contains(termin.getVrijeme())) {
                iterator.remove();
            }
        }
    }

 private void ocistiRezervacije() {
        terminAdapter.clearReservations();
        terminAdapter.clearColors();
    }






    private void updateColorsForAvailableTimeSlots() {
        // Iterate through all time slots and update colors based on reservation status
        for (TerminModel termin : terminAdapter.getTerminiList()) {
            String selectedTermin = termin.getVrijeme();
            boolean isReserved = checkReservationStatus(selectedTermin, selectedDate);

            if (!isReserved) {

            } else {
                terminAdapter.promijeniBojuTermina(selectedTermin, Color.RED);
            }
        }
    }






    private List<TerminModel> generateTermini(String selectedDate) {
        List<TerminModel> terminiList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (int i = 0; i < 18; i++) {
            String startTime = timeFormat.format(calendar.getTime());

            boolean isReserved = checkReservationStatus(startTime, selectedDate);

            // Set the initial color based on reservation status
            int initialColor = isReserved ? Color.RED : Color.RED;

            TerminModel terminModel = new TerminModel(startTime, selectedDate);
            terminModel.setReserved(isReserved);
            terminModel.setBoja(initialColor);

            terminiList.add(terminModel);

            calendar.add(Calendar.MINUTE, 30);
        }

        return terminiList;
    }


    private boolean checkReservationStatus(String selectedTermin, String selectedDate) {
        // Implement logic to check reservation status from the database
        // Use the provided information (frizer name, date, and time) to construct the key and query the database
        // Return true if reserved, false if not reserved
        // You may need to modify this method based on your actual database structure and logic
        // Example pseudocode:


        String keyToCheck = "frizer:" + imePrezime + "_termin:" + selectedTermin + "__" + selectedDate;
        return isTerminReservedInDatabase(keyToCheck);
    }
    private boolean isTerminReservedInDatabase(String keyToCheck) {
        final boolean[] isReserved = {false}; // Using an array to store the result as it needs to be final for the listener

        terminiReference.child(keyToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TerminFirebaseModel terminFirebaseModel = snapshot.getValue(TerminFirebaseModel.class);
                    if (terminFirebaseModel != null) {
                        isReserved[0] = terminFirebaseModel.isRezerviran();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return isReserved[0];
    }

    private void promijeniBojuTermina(String rezerviranoVrijeme) {
        terminAdapter.promijeniBojuTermina(rezerviranoVrijeme, Color.RED);
    }
}