package com.devrookie.fileioproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    EditText etFileName;
    Button btnFileNew;
    TextView tvFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileName = (EditText) findViewById(R.id.etFileName);
        btnFileNew = (Button) findViewById(R.id.btnNewFile);
        tvFileContents = (TextView) findViewById(R.id.tvFileContent);


    }

    public void newFile(View v) {
        String fileName = etFileName.getText().toString();
        if (fileName.equals(""))
            return;

        createFile(fileName);
    }

    public void createFile(String fn) {
        try {

            OutputStreamWriter outFile = new OutputStreamWriter(openFileOutput(fn, MODE_PRIVATE));
            outFile.write("Line 1: " + fn + '\n');
            outFile.write("Line 2: " + fn);
            outFile.close();
            Toast.makeText(this, "file created: " + fn, Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Create Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void viewFile(View v) {
        String fileName = etFileName.getText().toString();
        readFile(fileName);
    }

    public void readFile(String fn) {
        try {
            InputStream in = openFileInput(fn);
            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str = null;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                }
                in.close();
                tvFileContents.setText(buf.toString());
            } else
                Toast.makeText(this, "Find Not Found", Toast.LENGTH_SHORT).show();
        } catch (java.io.FileNotFoundException e) {

        } catch (Throwable t) {
            Toast.makeText(this, "Read Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void removeFile(View v) {
        String fileName = etFileName.getText().toString();
        deleteMyFile(fileName);
    }

    public void deleteMyFile(String fn) {
        try {

            String dir = getFilesDir().getAbsolutePath();
            File myFile = new File(dir, fn);

            boolean ret = myFile.delete();
            if (!ret)
                Toast.makeText(getApplicationContext(), "Delete Error", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "File Deleted: " + fn, Toast.LENGTH_LONG).show();


        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(), "Delete Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void listFiles(View v) {

        String[] filenames = getApplicationContext().fileList();

        tvFileContents.setText("");
        String fn = "";
        int i;
        for (i = 0; i < filenames.length; i++) {
            fn = fn + filenames[i] + '\n';
        }
        tvFileContents.setText(fn);
    }
}
