package com.michelklappert.hyperinfinitycube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.michelklappert.hyperinfinitycube.helper.ColorObserver;

import java.util.ArrayList;
import java.util.List;

/*
    Colorpicker Fragment based on the Android Holo Colorpicker: https://github.com/LarsWerkman/HoloColorPicker
 */

public class ColorpickerFragment extends Fragment {

    private ColorPicker colorPicker;
    private SaturationBar saturationBar;

    private DatabaseReference dbColorRef;

    private List<ColorObserver> observerList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_colorpicker, null);

        colorPicker = (ColorPicker) root.findViewById(R.id.color_Colorpicker);
        saturationBar = (SaturationBar) root.findViewById(R.id.saturationbar);

        colorPicker.addSaturationBar(saturationBar);
        colorPicker.setShowOldCenterColor(false);

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                if (dbColorRef != null) dbColorRef.setValue(color);
                for (int i=0; i < observerList.size(); i++){
                    observerList.get(i).update(color);
                }
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

    public void setSelectedColor(int color){
        colorPicker.setColor(color);
    }

    public void attachColorChangeObserver(ColorObserver o){
        if (observerList == null) observerList = new ArrayList<ColorObserver>();
        observerList.add(o);
    }

}
