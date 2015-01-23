package com.devrookie.fileioproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Omie Kue on 1/20/2015.
 */
public class MainActivity2 extends ActionBarActivity implements AdapterView.OnItemClickListener {


    private Button addBtn, clearBtn;
    private ListView mainListView;
    Context ctx;

    ArrayAdapter<String> fileAdapter;
    private List<String> fileListArray = new ArrayList<String>();

    boolean startFiles = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_layout);

        ctx = this;
    //Button
        this.addBtn = (Button)findViewById(R.id.btnAdd);
        this.clearBtn = (Button)findViewById(R.id.btnClear);
    //ListView
        this.mainListView = (ListView)findViewById(R.id.mainListView);


        //TODO: Ask if user wants to create original files.
         startFileDialog();


        fileAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,fileListArray);
        this.mainListView.setAdapter(fileAdapter);

        this.mainListView.setOnItemClickListener(this);
        //The add button
        this.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpNewFile();
            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //viewFileInformation(position);
        editFile(position);
    }

    private void editFile(final int position) {

        AlertDialog.Builder editAlert = new AlertDialog.Builder(this);
        editAlert.setIcon(R.drawable.ic_launcher);
        editAlert.setCancelable(true);
        editAlert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Start new intent to file content
                String fileName = (String) mainListView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(getApplicationContext(), FileContent.class);
                myIntent.putExtra("fileName", fileName); //placing filename into a key to grab again for use in file content
                startActivity(myIntent);


            }
        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() { //Negative pops up another dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder alertDelete = new AlertDialog.Builder(ctx);
                alertDelete.setMessage("Are you sure you want to delete this file?");
                alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fn = (String) mainListView.getItemAtPosition(position);

                        try {

                            String dir = getFilesDir().getAbsolutePath();
                            Log.d("String Dir", dir);
                            File myFile = new File(dir, fn);
                            boolean checkIfDeleted = myFile.delete();
                            if (!checkIfDeleted)
                                Toast.makeText(getApplicationContext(), "Cannot delete. Error.", Toast.LENGTH_LONG).show();
                            else {
                                fileListArray.remove(position);
                                fileAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "File deleted", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                alertDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }//OnClickDelete
        }).show();
    }//editFile



    private void createOriginalFiles() {
        try {
            OutputStreamWriter outNote = new OutputStreamWriter(openFileOutput("Notes",MODE_PRIVATE));
            outNote.write("Class registration starts next week on July 25th. ");
            outNote.close();
            //Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();

            OutputStreamWriter outToDo = new OutputStreamWriter(openFileOutput("ToDo",MODE_PRIVATE));
            outToDo.write("Finish science project \nWash the car \nPay the water bills ");
            outToDo.close();
            //Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();

            OutputStreamWriter outList = new OutputStreamWriter(openFileOutput("Shopping List",MODE_PRIVATE));
            outList.write("Eggs, Bread, Ham, Cheese, Milk ");
            outList.close();
            //Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        }
        catch(Throwable t) {
            Toast.makeText(this, "Exception: "+ t.toString(), Toast.LENGTH_LONG).show();

        }
    }


    public void setUpNewFile(){
        //TODO
        //1. Open dialog to add a new file (Title / Description) [Done]
        //2. Get text and save to string to be passes
        //3. Apply method with the passed string of title / description

        LayoutInflater inflateDialog = getLayoutInflater();
        final View dialogAddView = inflateDialog.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder setUpAlert = new AlertDialog.Builder(this);
        setUpAlert.setView(dialogAddView);
        setUpAlert.setTitle("Adding new file.");

        final EditText dialogTitle = (EditText)dialogAddView.findViewById(R.id.etDialogTitle); //Title

        setUpAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String myTitle = dialogTitle.getText().toString();

                if (dialogTitle.equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty title, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fileListArray.add(myTitle);
                fileAdapter.notifyDataSetChanged();

                addFileToData(myTitle);
                Log.d("Main: ", myTitle);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }

    private void addFileToData(String title) {
        try {

            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(title,MODE_PRIVATE));
            out.write("Add your description...");
            out.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        }
        catch(Throwable t) {
            Toast.makeText(this, "Exception: "+ t.toString(), Toast.LENGTH_LONG).show();
        }
    }//addFileToData


    public void startFileDialog() {

        final String[][] myFiles = {getApplicationContext().fileList()}; //Hold the names of the files into a string

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean startOriFiles = prefs.getBoolean("startFile", false);
        if(!startOriFiles){

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Welcome")
                    .setMessage("Would you like to create starting files to understand how this Application works? ")
                    .setPositiveButton("Yes", new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("startFile", true);
                            editor.apply();
                            createOriginalFiles();
                            myFiles[0] = getApplicationContext().fileList();
                            Collections.addAll(fileListArray, myFiles[0]);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No thanks.", new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("startFile", true);
                            editor.apply();
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }else{
            Collections.addAll(fileListArray, myFiles[0]);
        }
    }






























}//TODO: END OF MAIN CLASS
