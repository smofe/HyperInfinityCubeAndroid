package com.michelklappert.hyperinfinitycube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RacingFragment extends Fragment {

    private ColorpickerFragment colorPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_racing, null);

        /* Load the colorPicker fragment */
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        colorPicker = new ColorpickerFragment();
        colorPicker.setArguments(getActivity().getIntent().getExtras());
        transaction.replace(R.id.racing_colorPickerContainer,colorPicker).commit();

        return root;
    }
}
