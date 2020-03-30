package com.michelklappert.hyperinfinitycube;

import android.content.Context;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatImageButton;

public class PowerButton extends AppCompatImageButton {

    private boolean powerOn = false;

    public PowerButton(Context context) {
        super(context);
    }

    public PowerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PowerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean toggleButton(){
        this.setPowerOn(!this.getPowerOn());
        return this.getPowerOn();
    }

    public boolean getPowerOn(){
        return this.powerOn;
    }

    public void setPowerOn(boolean state){
        this.powerOn = state;
        if (this.powerOn) this.setImageResource(R.drawable.ic_power_on);
        else this.setImageResource(R.drawable.ic_power_off);
    }
}
