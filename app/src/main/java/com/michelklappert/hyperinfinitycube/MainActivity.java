package com.michelklappert.hyperinfinitycube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();
    final DatabaseReference power = dbRef.child("power");
    final DatabaseReference mode = dbRef.child("mode");

    PowerButton powerButton;
    Spinner ledmodeDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (PowerButton)findViewById(R.id.button_power);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerButton.toggleButton();
                power.setValue(powerButton.getPowerOn());
            }
        });


        ledmodeDropdown = (Spinner)findViewById(R.id.dropdown_ledmode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.ledmodes_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ledmodeDropdown.setAdapter(adapter);
        ledmodeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode.setValue(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        power.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(Boolean.class);
                Log.d("file","Value: " + value);
                powerButton.setPowerOn(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("file","Failed to read value", databaseError.toException());
            }
        });


    }
}
