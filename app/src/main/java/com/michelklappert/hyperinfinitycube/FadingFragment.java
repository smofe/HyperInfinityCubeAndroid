package com.michelklappert.hyperinfinitycube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.michelklappert.hyperinfinitycube.helper.SimpleItemTouchHelperCallback;

public class FadingFragment extends Fragment {

    private Button addColorButton;
    private RecyclerView selectedColors;
    private ColorpickerFragment colorPicker;

    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_fading, null);
        addColorButton = (Button) root.findViewById(R.id.fading_button_addColor);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        colorPicker = new ColorpickerFragment();
        colorPicker.setArguments(getActivity().getIntent().getExtras());
        transaction.replace(R.id.fading_colorPickerContainer,colorPicker).commit();

        final RecyclerListAdapter adapter = new RecyclerListAdapter();
        selectedColors = (RecyclerView) root.findViewById(R.id.fading_selectedColorList);
        selectedColors.setHasFixedSize(false);
        selectedColors.setAdapter(adapter);
        selectedColors.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(selectedColors);

        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(colorPicker.getColor());
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}


