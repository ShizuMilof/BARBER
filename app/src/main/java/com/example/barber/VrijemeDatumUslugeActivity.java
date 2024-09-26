package com.example.barber;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.barber.model.Appointment;
import com.example.barber.model.Usluge;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class VrijemeDatumUslugeActivity extends AppCompatActivity {

private Button datePickerButton;
private Button nextButton;
private String selectedDateRange;
private String selectedTimeSlot;
private List<Usluge> selectedServices;
private String username;
private String frizerIme;

private String urlSlike;

private ApiService apiService;
        Long startday;

// Variables to store appointment details
private String bookedDate;
private String bookedTime;
private String bookedHairdresser;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrijeme_datum_usluge);

        // Retrieve data from Intent
        username = getIntent().getStringExtra("username");
        frizerIme = getIntent().getStringExtra("frizer_ime");
        selectedServices = getIntent().getParcelableArrayListExtra("selectedServices");

        if (selectedServices != null) {
        for (Usluge service : selectedServices) {
        Log.d("VrijemeDatumUslugeActivity", "Selected : " + service.getNaziv() + ", Price: " + service.getCijena() + ", Duration: " + service.getTrajanje());
        }
        }

        datePickerButton = findViewById(R.id.datePickerButton);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setVisibility(View.GONE);
        // Disable the next button initially
        nextButton.setEnabled(false);

        nextButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        // Proceed with these selected services
        Intent intent = new Intent(VrijemeDatumUslugeActivity.this, PregledOdabranihUslugaActivity.class);
        // Pass the selected services to the next activity if needed
        intent.putExtra("username", username); // Pass the username
        intent.putExtra("frizer_ime", frizerIme); // Pass the frizer_ime
        intent.putParcelableArrayListExtra("selectedServices", (ArrayList<? extends Parcelable>) selectedServices);
        intent.putExtra("selectedDateRange", selectedDateRange);
        intent.putExtra("selectedTimeSlot", selectedTimeSlot);
        // Pass the appointment details
        intent.putExtra("bookedDate", bookedDate);
        intent.putExtra("bookedTime", bookedTime);
        intent.putExtra("bookedHairdresser", bookedHairdresser);
        startActivity(intent);
        }
        });

        datePickerButton.setOnClickListener(view -> showDatePicker());
        OkHttpClient client = getUnsafeOkHttpClient();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://172.20.10.3:7194/") // Use appropriate base URL
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
        apiService = retrofit.create(ApiService.class);
        }

private OkHttpClient getUnsafeOkHttpClient() {
        try {
// Create a trust manager that does not validate certificate chains
final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
@Override
public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

@Override
public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

@Override
public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
        }
        }
        };

// Install the all-trusting trust manager
final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
// Create an ssl socket factory with our all-trusting manager
final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
@Override
public boolean verify(String hostname, SSLSession session) {
        return true;
        }
        });

        return builder.build();
        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        }

private void showDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
@Override
public void onPositiveButtonClick(Pair<Long, Long> selection) {
        Long startDate = selection.first;
        Long endDate = selection.second;
        startday = selection.first;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        if (startDate != null && endDate != null) {
        String formattedStartDate = format.format(new Date(startDate));
        String formattedEndDate = format.format(new Date(endDate));

        selectedDateRange = " "+ formattedStartDate + "  -  " + formattedEndDate;
        Log.d("VrijemeDatumUslugeActivity", "Selected Date Range: " + selectedDateRange);

        // Initialize startCal and endCal
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endDate);

        // Show time slot selection dialog
        showTimeSlotDialog(startCal, endCal);
        }
        }
        });
        }

private void showTimeSlotDialog(Calendar startCal, Calendar endCal) {
// Define the time slots
final String[] timeSlots = new String[]{"UJUTRO","PRIJEPODNE", "TIJEKOM DANA", "POSLIJEPODNE"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Odaberite termin vremena")
        .setItems(timeSlots, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        selectedTimeSlot = timeSlots[which];
        String finalSelection = selectedDateRange + "  " + selectedTimeSlot;

        // Provide feedback to the user

        // Update the button text or any other UI element
        datePickerButton.setText("OBRADA PODATAKA U TIJEKU...");

        // Enable the next button
        nextButton.setEnabled(true);

        // Proceed to the next activity with the selected details
        findAndBookAppointment(startCal, endCal);
        }
        });
        builder.create().show();
        }

private void findAndBookAppointment(Calendar startCal, Calendar endCal) {
        // Define time slot ranges
        int startHour = 0;
        int endHour = 0;
        switch (selectedTimeSlot) {
        case "UJUTRO":
        startHour = 8;
        endHour = 11;
        break;
        case "PRIJEPODNE":
        startHour = 11;
        endHour = 13;
        break;
        case "TIJEKOM DANA":
        startHour = 13;
        endHour = 16;
        break;
        case "POSLIJEPODNE":
        startHour = 16;
        endHour = 19;
        break;
        }

        // Fetch existing appointments for the selected date range
        List<Appointment> allAppointments = new ArrayList<>();
        fetchAppointmentsForDateRange(startCal, endCal, startHour, endHour, allAppointments);
        }

private void fetchAppointmentsForDateRange(Calendar startCal, Calendar endCal, int startHour, int endHour, List<Appointment> allAppointments) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startCal.getTime());

        apiService.getAppointments().enqueue(new Callback<List<Appointment>>() {
@Override
public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
        if (response.isSuccessful() && response.body() != null) {
        allAppointments.addAll(response.body());
        } else {
        Toast.makeText(VrijemeDatumUslugeActivity.this, "ne mozemo dohvatiti podatke", Toast.LENGTH_SHORT).show();
        Log.e("API", "Response error: " + response.code() + ", " + response.message());
        }

        startCal.add(Calendar.DATE, 1);
        if (startCal.after(endCal)) {
        // All dates processed, now book the next available appointment
        bookNextAvailableAppointment(startHour, endHour, allAppointments, endCal);
        } else {
        fetchAppointmentsForDateRange(startCal, endCal, startHour, endHour, allAppointments);
        }
        }

@Override
public void onFailure(Call<List<Appointment>> call, Throwable t) {
        Log.e("API", "Failure: " + t.getMessage());
        Toast.makeText(VrijemeDatumUslugeActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
        startCal.add(Calendar.DATE, 1);
        if (startCal.after(endCal)) {
        // All dates processed, now book the next available appointment
        bookNextAvailableAppointment(startHour, endHour, allAppointments, endCal);
        } else {
        fetchAppointmentsForDateRange(startCal, endCal, startHour, endHour, allAppointments);
        }
        }
        });
        }

        private void bookNextAvailableAppointment(int startHour, int endHour, List<Appointment> appointments, Calendar endCal) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure UTC timezone

                try {
                        // Sort appointments by start time
                        appointments.sort((a1, a2) -> {
                                String a1Start = a1.getTrajanjeUsluge().split("-")[0];
                                String a2Start = a2.getTrajanjeUsluge().split("-")[0];
                                return a1Start.compareTo(a2Start);
                        });

                        Calendar currentCal = Calendar.getInstance();
                        Calendar startCal = Calendar.getInstance();
                        startCal.setTimeInMillis(startday);
                        currentCal.setTime(startCal.getTime()); // Ensure we start from the selected start date
                        currentCal.set(Calendar.HOUR_OF_DAY, startHour);
                        currentCal.set(Calendar.MINUTE, 0);
                        currentCal.set(Calendar.SECOND, 0);
                        currentCal.set(Calendar.MILLISECOND, 0);

                        boolean timeSlotFound = false;

                        // Calculate the total service duration
                        final int totalServiceDuration = calculateTotalServiceDuration();
                        final String serviceNames = getServiceNames();

                        // Correct the end date to include the entire end day
                        Calendar correctedEndCal = (Calendar) endCal.clone();
                        correctedEndCal.set(Calendar.HOUR_OF_DAY, 23);
                        correctedEndCal.set(Calendar.MINUTE, 59);
                        correctedEndCal.set(Calendar.SECOND, 59);
                        correctedEndCal.set(Calendar.MILLISECOND, 999);

                        // Loop through dates and times to find the next available slot
                        while (!currentCal.after(correctedEndCal)) {
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentCal.getTime());

                                // Check if the user already has an appointment on this date
                                for (Appointment appointment : appointments) {
                                        if (appointment.getKorisnik().equals(username) && appointment.getDatum().startsWith(date)) {
                                                // Postavite tekst na gumb "OBRADA PODATAKA U TIJEKU" na novu poruku
                                                datePickerButton.setText("NIJE MOGUĆE REZERVIRATI VIŠE TERMINA ZA TAJ DAN");
                                                // Ostavite gumb aktivnim bez onemogućavanja
                                                return;
                                        }
                                }

                                Calendar slotStartCal = (Calendar) currentCal.clone();
                                Calendar slotEndCal = (Calendar) slotStartCal.clone();
                                slotEndCal.add(Calendar.MINUTE, totalServiceDuration);
                                String slotStart = timeFormat.format(slotStartCal.getTime());
                                String slotEnd = timeFormat.format(slotEndCal.getTime());

                                boolean slotAvailable = true;
                                for (Appointment appointment : appointments) {
                                        if (appointment.getFrizer().equals(frizerIme)) {
                                                String appointmentDateStr = appointment.getDatum().split("T")[0];
                                                String appointmentTimeRange = appointment.getTrajanjeUsluge();

                                                if (appointmentDateStr.equals(date) && isTimeRangeOverlap(slotStart + "-" + slotEnd, appointmentTimeRange)) {
                                                        slotAvailable = false;
                                                        break;
                                                }
                                        }
                                }

                                if (slotAvailable) {
                                        // Ask the user if they want to book the slot
                                        showBookingConfirmationDialog(slotStart, slotEnd, date, dateTimeFormat, serviceNames, totalServiceDuration, slotEndCal);
                                        return; // Exit the loop once a slot is found
                                } else {
                                        // Move to the end time of the latest overlapping appointment
                                        for (Appointment appointment : appointments) {
                                                if (appointment.getFrizer().equals(frizerIme)) {
                                                        String appointmentDateStr = appointment.getDatum().split("T")[0];
                                                        if (appointmentDateStr.equals(date)) {
                                                                String[] appointmentTimeRange = appointment.getTrajanjeUsluge().split("-");
                                                                Calendar appointmentEndCal = Calendar.getInstance();
                                                                appointmentEndCal.setTime(slotStartCal.getTime());
                                                                appointmentEndCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(appointmentTimeRange[1].split(":")[0]));
                                                                appointmentEndCal.set(Calendar.MINUTE, Integer.parseInt(appointmentTimeRange[1].split(":")[1]));

                                                                if (appointmentEndCal.after(slotStartCal)) {
                                                                        slotStartCal.setTime(appointmentEndCal.getTime());
                                                                        slotStartCal.add(Calendar.MINUTE, 5); // Move to the next minute after the appointment end time
                                                                        break;
                                                                }
                                                        }
                                                }
                                        }

                                        // Update currentCal to the next possible time slot
                                        currentCal.setTime(slotStartCal.getTime());
                                        if (currentCal.get(Calendar.HOUR_OF_DAY) >= endHour) {
                                                currentCal.add(Calendar.DATE, 1);
                                                currentCal.set(Calendar.HOUR_OF_DAY, startHour);
                                                currentCal.set(Calendar.MINUTE, 0);
                                                currentCal.set(Calendar.SECOND, 0);
                                                currentCal.set(Calendar.MILLISECOND, 0);
                                        }
                                }
                        }

                        if (!timeSlotFound) {
                                Toast.makeText(VrijemeDatumUslugeActivity.this, "NAŽALOST NE POSTOJE SLOBODNI TERMINI ZA VAŠE ODABIRE", Toast.LENGTH_LONG).show();
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }




        private void showBookingConfirmationDialog(String slotStart, String slotEnd, String date, SimpleDateFormat dateTimeFormat, String serviceNames, int totalServiceDuration, Calendar slotEndCal) {
                // Format the date to the desired format
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String formattedDate = displayDateFormat.format(parseDate(date, dateTimeFormat)); // Parse and format the date

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Potvrda termina");
                builder.setMessage("Želite li rezervirati termin u vremenu od: " + slotStart + " - " + slotEnd + " na datum " + formattedDate + "?");
                builder.setPositiveButton("Da", (dialog, which) -> {
                        generateAndUploadQRCode(slotStart, slotEnd, date, dateTimeFormat, serviceNames, totalServiceDuration, slotEndCal);
                });
                builder.setNegativeButton("Ne", (dialog, which) -> {
                        datePickerButton.setText("ŽELIM ODABRATI DRUGI TERMIN");

                        dialog.dismiss();
                });
                builder.show();
        }

        // Helper method to parse the date string
        private Date parseDate(String date, SimpleDateFormat dateTimeFormat) {
                try {
                        return dateTimeFormat.parse(date + "T00:00:00Z");
                } catch (Exception e) {
                        e.printStackTrace();
                        return new Date(); // Return current date as a fallback
                }
        }

        private void generateAndUploadQRCode(String slotStart, String slotEnd, String date, SimpleDateFormat dateTimeFormat, String serviceNames, int totalServiceDuration, Calendar slotEndCal) {
                // Formatirajte datum u oblik "dd.MM.yyyy"
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String formattedDate = displayDateFormat.format(parseDate(date, dateTimeFormat)); // Parse and format the date

                // Kreirajte tekst za QR kod koristeći novi format datuma
                String qrCodeText = String.format("Datum termina: %s\nVrijeme termina: %s - %s\nFrizer: %s\nOdabrane usluge: %s",
                        formattedDate, slotStart, slotEnd, frizerIme, serviceNames);

                Bitmap qrCodeBitmap = generateQRCode(qrCodeText);

                if (qrCodeBitmap != null) {
                        uploadQRCodeToFirebase(qrCodeBitmap, slotStart, slotEnd, date, dateTimeFormat, serviceNames, totalServiceDuration, slotEndCal);
                }
        }



private void bookAppointment(String slotStart, String slotEnd, String date, SimpleDateFormat dateTimeFormat, String serviceNames, int totalServiceDuration, Calendar slotEndCal, String qrCodeUrl) {
        Appointment newAppointment = new Appointment();
        String dateTime = date + "T" + slotStart + ":00Z";
        try {
        newAppointment.setDatum(dateTimeFormat.format(dateTimeFormat.parse(dateTime)));
        } catch (Exception e) {
        e.printStackTrace();
        }
        newAppointment.setVrijemeUsluge(String.format("%02d:%02d:00", totalServiceDuration / 60, totalServiceDuration % 60));
        newAppointment.setKorisnik(username);
        newAppointment.setFrizer(frizerIme);
        newAppointment.setUsluge(serviceNames);
        newAppointment.setTrajanjeUsluge(slotStart + "-" + slotEnd);
        newAppointment.setUrlSlike(qrCodeUrl);

        // Log the JSON payload
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(newAppointment);
        Log.d("API Request", jsonPayload);

        apiService.bookAppointment(newAppointment).enqueue(new Callback<ResponseBody>() {
@Override
public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
        Toast.makeText(VrijemeDatumUslugeActivity.this, "USPJEŠNO REZERVIRAN TERMIN  ", Toast.LENGTH_SHORT).show();

        // Store booked appointment details
        bookedDate = date;
        bookedTime = slotStart + "-" + slotEnd;
        bookedHairdresser = frizerIme;

        // Proceed to the next activity with the booked appointment details
        proceedToNextActivity(qrCodeUrl);
        } else {
        Toast.makeText(VrijemeDatumUslugeActivity.this, "TERMIN JE ODBAČEN", Toast.LENGTH_SHORT).show();
        Log.e("API", "Response error: " + response.code() + ", " + response.message());
        try {
        Log.e("API", "Response body: " + response.errorBody().string());
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        }

@Override
public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(VrijemeDatumUslugeActivity.this, "API call failed", Toast.LENGTH_SHORT).show();
        Log.e("API", "Failure: " + t.getMessage());
        }
        });
        }

private void proceedToNextActivity(String qrCodeUrl) {
        Intent intent = new Intent(VrijemeDatumUslugeActivity.this, PregledOdabranihUslugaActivity.class);
        // Pass the selected services to the next activity if needed
        intent.putExtra("username", username); // Pass the username
        intent.putExtra("frizer_ime", frizerIme); // Pass the frizer_ime
        intent.putParcelableArrayListExtra("selectedServices", (ArrayList<? extends Parcelable>) selectedServices);
        intent.putExtra("selectedDateRange", selectedDateRange);
        intent.putExtra("selectedTimeSlot", selectedTimeSlot);
        // Pass the appointment details
        intent.putExtra("bookedDate", bookedDate);
        intent.putExtra("bookedTime", bookedTime);
        intent.putExtra("bookedHairdresser", bookedHairdresser);
        intent.putExtra("qrCodeUrl", qrCodeUrl); // Proslijedi URL QR koda

        startActivity(intent);
        }

private int calculateTotalServiceDuration() {
        int totalServiceDuration = 0;
        for (Usluge service : selectedServices) {
        totalServiceDuration += Integer.parseInt(service.getTrajanje());
        }
        return totalServiceDuration;
        }

private String getServiceNames() {
        StringBuilder serviceNames = new StringBuilder("");
        for (int i = 0; i < selectedServices.size(); i++) {
        if (i > 0) {
        serviceNames.append(", ");
        }
        serviceNames.append(selectedServices.get(i).getNaziv());
        }
        return serviceNames.toString();
        }





private boolean isTimeRangeOverlap(String newRange, String existingRange) {
        String[] newRangeSplit = newRange.split("-");
        String[] existingRangeSplit = existingRange.split("-");
        return !(newRangeSplit[1].compareTo(existingRangeSplit[0]) <= 0 || newRangeSplit[0].compareTo(existingRangeSplit[1]) >= 0);
        }

private Bitmap generateQRCode(String text) {
        try {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(new String(text.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat.QR_CODE, 300, 300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
        }


private void uploadQRCodeToFirebase(Bitmap qrCodeBitmap, String slotStart, String slotEnd, String date, SimpleDateFormat dateTimeFormat, String serviceNames, int totalServiceDuration, Calendar slotEndCal) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Generiraj jedinstveni naziv koristeći timestamp
        String uniqueName = System.currentTimeMillis() + ".png";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference qrCodeRef = storageRef.child("qrcodes/" + uniqueName);

        UploadTask uploadTask = qrCodeRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
        Log.e("FirebaseStorage", "Upload failed: " + exception.getMessage());
        }).addOnSuccessListener(taskSnapshot -> {
        Log.d("FirebaseStorage", "Upload success: " + taskSnapshot.getMetadata().getPath());
        qrCodeRef.getDownloadUrl().addOnSuccessListener(uri -> {
        String qrCodeUrl = uri.toString();
        Log.d("FirebaseStorage", "QR Code URL: " + qrCodeUrl);
        // Pozovite bookAppointment s URL-om QR koda
        bookAppointment(slotStart, slotEnd, date, dateTimeFormat, serviceNames, totalServiceDuration, slotEndCal, qrCodeUrl);
        });
        });
        }}
