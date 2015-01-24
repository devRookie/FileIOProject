package com.devrookie.fileioproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Omie Kue on 1/24/2015.
 */
public class Setting extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener{

    CheckBox cbx;
    Button btnOk;
    boolean checked = false;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cbx_layoout);

        this.btnOk = (Button)findViewById(R.id.button);

        this.cbx = (CheckBox)findViewById(R.id.checkbox);
        this.cbx.setOnCheckedChangeListener(this);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.checked = prefs.getBoolean("cbxSetting", false);

        this.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            this.checked = true;
        }else
            this.checked = false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("cbxSetting", this.checked);
        editor.apply();
    }
}
