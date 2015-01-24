package com.devrookie.fileioproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileContent extends ActionBarActivity{
    Button btnSave;
    TextView tvFileName;
    EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_layout);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");

        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.tvFileName = (TextView) findViewById(R.id.tvFileTitle);
        this.etDescription = (EditText) findViewById(R.id.etDescriptionBox);

        tvFileName.setText(fileName);
        read(fileName); //Read the file name


    }

    public void read(String fileName) {
        try {
            InputStream in = openFileInput(fileName);
            if(in != null)
            {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuilder buf = new StringBuilder();
                while((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                }
                in.close();
                etDescription.setText(buf.toString());
            }
        }

        catch(java.io.FileNotFoundException e) {

        }
        catch(Throwable t) {
            Toast.makeText(this, "Unable to read file",Toast.LENGTH_LONG).show();
        }

    }

    public void done(View v) {
        save();
        finish();
    }

    public void cancel(View v) {
        finish();
    }

    public void save() {
        try {

            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(tvFileName.getText().toString(),MODE_PRIVATE));
            out.write(etDescription.getText().toString());
            out.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        }
        catch(Throwable t) {
            Toast.makeText(this, "Unable to save", Toast.LENGTH_LONG).show();
        }
    }

}
