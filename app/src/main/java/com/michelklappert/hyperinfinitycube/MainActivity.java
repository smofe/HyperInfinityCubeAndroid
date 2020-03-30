package com.michelklappert.hyperinfinitycube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.larswerkman.holocolorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();
    final DatabaseReference dbPower = dbRef.child("power");
    final DatabaseReference dbMode = dbRef.child("mode");
    final DatabaseReference dbColor = dbRef.child("color");

    PowerButton powerButton;
    Spinner ledmodeDropdown;

    Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (PowerButton)findViewById(R.id.button_power);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerButton.toggleButton();
                dbPower.setValue(powerButton.getPowerOn());
            }
        });


        ledmodeDropdown = (Spinner)findViewById(R.id.dropdown_ledmode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.ledmodes_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ledmodeDropdown.setAdapter(adapter);
        ledmodeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbMode.setValue(position);
                setActiveMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dbPower.addValueEventListener(new ValueEventListener() {
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

    private void setActiveMode(int id){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (id){
            case 0: {
                activeFragment = new ColorpickerFragment();
                ((ColorpickerFragment) activeFragment).setDbColorRef(dbColor);
                break;
            }
            case 1: {
                activeFragment = new FadingFragment();
                break;
            }
            case 2: {
                activeFragment = new EdgeSymmetryFragment();
                break;
            }
            case 3: {
                activeFragment = new RacingFragment();
                break;
            }
        }
        activeFragment.setArguments(getIntent().getExtras());
        transaction.replace(R.id.fragment_container,activeFragment).commit();
    }
}
