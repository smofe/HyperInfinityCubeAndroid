package com.michelklappert.hyperinfinitycube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.larswerkman.holocolorpicker.ColorPicker;

public class FadingFragment extends Fragment {

    private ColorPicker colorPicker;
    private Button addColorButton;
    private LinearLayout buttonContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_fading, null);

        colorPicker = (ColorPicker) root.findViewById(R.id.fading_colorpicker);
        addColorButton = (Button) root.findViewById(R.id.fading_button_addColor);
        buttonContainer = (LinearLayout) root.findViewById(R.id.fading_buttonContainer);

        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = new Button(getContext());
                buttonContainer.addView(button);
            }
        });

        return root;
    }
}
