package com.michelklappert.hyperinfinitycube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

/*
    Colorpicker Fragment based on the Android Holo Colorpicker: https://github.com/LarsWerkman/HoloColorPicker
 */

public class ColorpickerFragment extends Fragment {

    private ColorPicker colorPicker;
    private SaturationBar saturationBar;

    private DatabaseReference dbColorRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_colorpicker, null);

        colorPicker = (ColorPicker) root.findViewById(R.id.colorpicker);
        saturationBar = (SaturationBar) root.findViewById(R.id.saturationbar);

        colorPicker.addSaturationBar(saturationBar);
        colorPicker.setShowOldCenterColor(false);

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                dbColorRef.setValue(color);
            }
        });

        return root;
    }

    public int getColor(){
        return colorPicker.getColor();
    }

    public void setDbColorRef(DatabaseReference dbRef){
        this.dbColorRef = dbRef;
    }

    public DatabaseReference getDbColorRef(){
        return this.dbColorRef;
    }

}
