package com.michelklappert.hyperinfinitycube;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.michelklappert.hyperinfinitycube.helper.ColorObserver;
import com.michelklappert.hyperinfinitycube.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FadingFragment extends Fragment implements ColorObserver {

    private DatabaseReference dbRef;
    private DatabaseReference dbColors;
    private DatabaseReference dbSpeed;

    private Button addColorButton;
    private RecyclerView selectedColors;
    private ColorpickerFragment colorPicker;
    private SeekBar speedSlider;

    private ItemTouchHelper itemTouchHelper;
    private RecyclerListAdapter adapter;

    private Button activeColor;


    public FadingFragment(DatabaseReference dbRef){
        try {
            setDbRef(dbRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_fading, null);

        initiateColorSelection(root);

        speedSlider = (SeekBar) root.findViewById(R.id.fading_speedSlider);
        dbSpeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int speed = dataSnapshot.getValue(Integer.class);
                speedSlider.setProgress(speed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dbSpeed.setValue(seekBar.getProgress());
            }
        });

        return root;
    }

    private void initiateColorSelection(ViewGroup root){
        /* Load the colorPicker fragment */
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        colorPicker = new ColorpickerFragment();
        colorPicker.setArguments(getActivity().getIntent().getExtras());
        transaction.replace(R.id.fading_colorPickerContainer,colorPicker).commit();

        /* Load the RecyclerView for displaying all selected colors */
        adapter = new RecyclerListAdapter();
        selectedColors = (RecyclerView) root.findViewById(R.id.fading_selectedColorList);
        selectedColors.setHasFixedSize(false);
        selectedColors.setAdapter(adapter);
        selectedColors.setLayoutManager(new LinearLayoutManager(getActivity()));

        /* Create and attach itemTouchHelper to allow drag/drog and swipe actions */
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(selectedColors);

        /* Load the add color button */
        addColorButton = (Button) root.findViewById(R.id.fading_button_addColor);
        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> colors = new ArrayList<Integer>();
                for (int i=0; i < selectedColors.getChildCount(); i++){
                    colors.add(((ColorDrawable)selectedColors.getChildAt(i).getBackground()).getColor());
                }
                colors.add(colorPicker.getColor());
                adapter.setItemList(colors);
                resetActiveColor();
            }
        });

        /* Load existing color configuration from Firebase DB */
        dbColors.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()){
                    adapter.addItem(it.next().getValue(Integer.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /* Observe item changes to update the color database */
        RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                dbColors.setValue(adapter.getColors());
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                dbColors.setValue(adapter.getColors());
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                dbColors.setValue(adapter.getColors());
            }
        };
        adapter.registerAdapterDataObserver(observer);

        adapter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                int color = ((ColorDrawable) button.getBackground()).getColor();
                if (activeColor != null && activeColor.equals(button)) {
                    resetActiveColor();
                }
                else {
                    resetActiveColor();
                    activeColor = button;
                    activeColor.setText("EDIT");
                }
                colorPicker.setSelectedColor(color);
            }
        });

        colorPicker.attachColorChangeObserver(this);

    }

    public void setDbRef(DatabaseReference ref) throws Exception {
        if (ref != null) {
            this.dbRef = ref;
            this.dbColors = ref.child("colors");
            this.dbSpeed = ref.child("speed");
        } else {
            throw new Exception("DatabaseReference is null");
        }
    }

    @Override
    public void update(int color) {
        if (activeColor != null) {
            activeColor.setBackgroundColor(color);

            //TODO: This seems pretty bad performance wise, but works for now.
            List<Integer> colors = new ArrayList<Integer>();
            for (int i=0; i < selectedColors.getChildCount(); i++){
                colors.add(((ColorDrawable)selectedColors.getChildAt(i).getBackground()).getColor());
            }
            adapter.setItemList(colors);

        }
    }

    private void resetActiveColor(){
        if (activeColor != null){
            activeColor.setText("");
            activeColor = null;
        }
    }
}



