package com.dharamshi.noteitdownv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // TODO : Implement Admob : ca-app-pub-5513150209080770/7091709500
    // Test Ad : ca-app-pub-3940256099942544/6300978111

    ConstraintLayout mainLayout;

    public static ArrayList<String> titleList = new ArrayList<>();
    public static ArrayList<String> notesList = new ArrayList<>();
    public static ArrayList<Integer> idList = new ArrayList<>();

    //TODO: Inisitalize AdMob Variables.
//    private AdView mAdView;
//    private InterstitialAd mInterstitialAd;

    public static ArrayAdapter sArrayAdapter;

    ListView notesListView;
    ImageButton searchCancel;
    EditText searchET;
    FloatingActionButton addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchCancel = findViewById(R.id.searchCancel);
        searchET = findViewById(R.id.searchET);
        addNote = findViewById(R.id.addNoteFAB);

        //TODO: Initialize and implement admob.
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
//        MobileAds.initialize(this, "ca-app-pub-5513150209080770~1883205736");
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                mAdView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int error) {
//                mAdView.setVisibility(View.GONE);
//            }
//        });
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        Toast.makeText(this, "Developed by Femin Dharamshi", Toast.LENGTH_SHORT).show();

        titleList.clear();
        notesList.clear();
        idList.clear();

        mainLayout = findViewById(R.id.constraintLayout);
        notesListView = findViewById(R.id.notesList);

        final SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("Notes" , MODE_PRIVATE, null);

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM notes", null);

        if (c != null) {

        final int titleIndex = c.getColumnIndex("title");
        int idIndex = c.getColumnIndex("id");
        int messageIndex = c.getColumnIndex("message");


        //c.moveToFirst();


            while(c.moveToNext()) {
                //Log.i("id", Integer.toString(c.getInt(idIndex)));
                idList.add(c.getInt(idIndex));
                //Log.i("Title", c.getString(titleIndex));
                if(c.getString(titleIndex) != null)
                    titleList.add(c.getString(titleIndex));
                else
                    titleList.add("No Title");
                //Log.i("Message", c.getString(messageIndex));
                if(c.getString(messageIndex) != null)
                    notesList.add(c.getString(messageIndex));
                else
                    notesList.add("");

            }
        }

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("noteID", -1);
                startActivity(intent);
            }
        });

        sArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titleList);

        notesListView.setAdapter(sArrayAdapter);

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchET.setText("");
                refreshList(searchET);
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idList.clear();
                notesList.clear();
                titleList.clear();

                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("Notes" , MODE_PRIVATE, null);

                String sql = "SELECT * FROM notes WHERE title LIKE '%" + charSequence.toString() + "%'";

                Cursor c = sqLiteDatabase.rawQuery(sql, null);

                if (c != null) {

                    int titleIndex = c.getColumnIndex("title");
                    int idIndex = c.getColumnIndex("id");
                    int messageIndex = c.getColumnIndex("message");


                    //c.moveToFirst();


                    while (c.moveToNext()) {
                        //Log.i("id", Integer.toString(c.getInt(idIndex)));
                        idList.add(c.getInt(idIndex));
                        //Log.i("Title", c.getString(titleIndex));
                        if (c.getString(titleIndex) != null)
                            titleList.add(c.getString(titleIndex));
                        else
                            titleList.add("No Tittle");
                        //Log.i("Message", c.getString(messageIndex));
                        if (c.getString(messageIndex) != null)
                            notesList.add(c.getString(messageIndex));
                        else
                            notesList.add("");
                    }
                }

                

                sArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }

                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("noteID", i);

                startActivity(intent);

            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int deleteNoteId = i;

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Are you sure ?")
                        .setMessage("Are you sure that you want to delete the note with title \""+ titleList.get(i) +"\" ?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //SQLiteDatabase myDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

                                String sql = "DELETE FROM notes WHERE id="+ String.valueOf(idList.get(deleteNoteId)); //<<<< CHANGED
                                sqLiteDatabase.execSQL(sql);

                                notesList.remove(deleteNoteId);
                                titleList.remove(deleteNoteId);
                                idList.remove(deleteNoteId);

                                sArrayAdapter.notifyDataSetChanged();


                                showSnack(findViewById(R.id.constraintLayout), "Note Deleted!");


                            }
                        })
                        .show();
                return true;
            }
        });

    }

    public void refreshList(View view){


        idList.clear();
        notesList.clear();
        titleList.clear();

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("Notes" , MODE_PRIVATE, null);

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM notes", null);

        if (c != null) {

        int titleIndex = c.getColumnIndex("title");
        int idIndex = c.getColumnIndex("id");
        int messageIndex = c.getColumnIndex("message");


        //c.moveToFirst();


            while(c.moveToNext()){
                //Log.i("id", Integer.toString(c.getInt(idIndex)));
                idList.add(c.getInt(idIndex));
                //Log.i("Title", c.getString(titleIndex));
                if(c.getString(titleIndex) != null)
                    titleList.add(c.getString(titleIndex));
                else  {

                    if(c.getString(messageIndex) == null){
                        String sql = "DELETE FROM notes WHERE id="+ String.valueOf(c.getInt(idIndex)); //<<<< CHANGED
                        sqLiteDatabase.execSQL(sql);
                    }else {
                        titleList.add("No Title");
                    }

                }


                //Log.i("Message", c.getString(messageIndex));
                if(c.getString(messageIndex) != null)
                    notesList.add(c.getString(messageIndex));
                else
                    notesList.add("");

            }
        }

        sArrayAdapter.notifyDataSetChanged();




    }

//    public void addNewNote(View view){
//
//
//
//    }

    @Override
    public void onResume(){
        super.onResume();

        refreshList(searchET);

    }

    public void showSnack(View v, String msg){
        Snackbar snackbar = Snackbar
                .make(v, msg, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

}
