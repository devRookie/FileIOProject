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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class MainActivity2 extends ActionBarActivity implements AdapterView.OnItemClickListener {


    private Button addBtn;
    private ListView mainListView;
    Context ctx;

    ArrayAdapter<String> fileAdapter;
    private List<String> fileListArray = new ArrayList<>();
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_layout);

        ctx = this;
    //Button
        this.addBtn = (Button)findViewById(R.id.btnAdd);
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
        editFile(position);
    }

    private void editFile(final int position) {

        final String fileName = (String) mainListView.getItemAtPosition(position);

        AlertDialog.Builder editAlert = new AlertDialog.Builder(this);
        editAlert.setIcon(R.drawable.ic_launcher);
        editAlert.setCancelable(true);
        editAlert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Start new intent to file content
                final String fileName = (String) mainListView.getItemAtPosition(position);

                prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
                final String password = prefs.getString(fileName, "");

                if (password.equals("")){
                    Toast.makeText(MainActivity2.this, "Check password if*", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(getApplicationContext(), FileContent.class);
                    myIntent.putExtra("fileName", fileName); //placing filename into a key to grab again for use in file content
                    startActivity(myIntent);
                }else{
                    LayoutInflater inf = getLayoutInflater();
                    AlertDialog.Builder d = new AlertDialog.Builder(MainActivity2.this);
                    View v = inf.inflate(R.layout.password_layout_d, null);
                    d.setView(v);
                    d.setTitle("Please enter password");

                    final EditText etEntPass = (EditText)v.findViewById(R.id.etEnterPassword);

                    d.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final String usrPassword = etEntPass.getText().toString();
                            if (usrPassword.equals(password)){
                                Intent myIntent = new Intent(getApplicationContext(), FileContent.class);
                                myIntent.putExtra("fileName", fileName); //placing filename into a key to grab again for use in file content
                                startActivity(myIntent);
                                return;
                            }else
                            Toast.makeText(MainActivity2.this, "Wrong password please try again", Toast.LENGTH_SHORT).show();
                            etEntPass.getText().clear();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

                }//else

            }
        })

                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { //Negative pops up another dialog
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
                        final String pwStr = prefs.getString(fileName, "");

                        if (pwStr.equals("")) { //Means there is a password
                            deleteDialog(position);
                        } else {
                            LayoutInflater inf = getLayoutInflater();
                            AlertDialog.Builder d = new AlertDialog.Builder(MainActivity2.this);
                            View v = inf.inflate(R.layout.password_layout_d, null);
                            d.setView(v);
                            d.setTitle("Please enter password");

                            final EditText etEntPass = (EditText)v.findViewById(R.id.etEnterPassword);

                            d.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final String usrPassword = etEntPass.getText().toString();
                                    if (usrPassword.equals(pwStr)){
                                        deleteDialog(position);
                                        return;
                                    }else
                                        Toast.makeText(MainActivity2.this, "Wrong password please try again", Toast.LENGTH_SHORT).show();
                                        etEntPass.getText().clear();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    }//OnClickDelete
                }).show();
    }//editFile



    private void createOriginalFiles() {
        try {
            OutputStreamWriter outNote = new OutputStreamWriter(openFileOutput("Notes",MODE_PRIVATE));
            outNote.write("Class registration starts next week on July 25th. ");
            outNote.close();


            OutputStreamWriter outToDo = new OutputStreamWriter(openFileOutput("ToDo",MODE_PRIVATE));
            outToDo.write("Finish science project \nWash the car \nPay the water bills ");
            outToDo.close();


            OutputStreamWriter outList = new OutputStreamWriter(openFileOutput("Shopping List",MODE_PRIVATE));
            outList.write("Eggs, Bread, Ham, Cheese, Milk ");
            outList.close();

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

        final SharedPreferences prefsnf = PreferenceManager.getDefaultSharedPreferences(this);

        LayoutInflater inflateDialog = getLayoutInflater();
        final View dialogAddView = inflateDialog.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder setUpAlert = new AlertDialog.Builder(this);
        setUpAlert.setView(dialogAddView);
        setUpAlert.setTitle("Adding new file.");

        final EditText dialogTitle = (EditText)dialogAddView.findViewById(R.id.etDialogTitle); //Title
        final EditText diaglogPassword = (EditText)dialogAddView.findViewById(R.id.etPassword);

        setUpAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String myTitle = dialogTitle.getText().toString();
                String myPass = diaglogPassword.getText().toString();

                if (myTitle.equals("")) {
                    Toast.makeText(getApplicationContext(), "Empty title, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fileListArray.add(myTitle);
                fileAdapter.notifyDataSetChanged();

                if (myPass.equals("")){
                    AlertDialog.Builder d = new AlertDialog.Builder(MainActivity2.this);
                    d.setTitle("Heads up!");
                    d.setMessage("You did not assgin a password");
                    d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

                SharedPreferences.Editor editor = prefsnf.edit();
                editor.putString(myTitle, myPass);
                editor.apply();
                addFileToData(myTitle);



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
            Toast.makeText(this, "Unable to add file", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_settings:

                break;
            case R.id.action_help:
                //TODO Open small dialog to read a help me
                AlertDialog.Builder help = new AlertDialog.Builder(this);
                help.setIcon(R.drawable.ic_launcher);
                help.setTitle("Instructions");
                help.setMessage("  1. Click \"Add\" to add a new file.\n" +
                        "  2. Input a title for your new file (password is optional) and press Ok.\n" +
                        "  3. Your file is now created, click your file to begin");
                help.setPositiveButton("Ok, I got it thanks.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.action_about:
                Dialog about = new Dialog(this);
                about.setContentView(R.layout.activity_about);
                about.setCancelable(true);
                about.show();
                break;
        }
        return true;
    }

    public void deleteDialog(final int position){
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
                        Toast.makeText(getApplicationContext(), "Cannot delete. There is no text to delete.", Toast.LENGTH_LONG).show();
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
    }

}//TODO: END OF MAIN CLASS
