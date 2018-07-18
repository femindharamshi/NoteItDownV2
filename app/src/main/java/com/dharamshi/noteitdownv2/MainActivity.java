package com.dharamshi.noteitdownv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int mInstructionsID = 1;

    ConstraintLayout mConstraintLayout;
    Button mForwardButton;
    Button mBackButton;
    Button mLetsGo;
    LinearLayout mButtonLayout;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = this.getSharedPreferences(getPackageName(), MODE_PRIVATE );

        boolean firstTimeSetup = mSharedPreferences.getBoolean("FirstTimeSetup", false);

        if(firstTimeSetup == false) {


            initDatabase();

            mConstraintLayout = findViewById(R.id.mainLayout);
            mForwardButton = findViewById(R.id.forwardButton);
            mBackButton = findViewById(R.id.backButton);
            mButtonLayout = findViewById(R.id.buttonLayout);
            mLetsGo = findViewById(R.id.letsGo);

            mLetsGo.setVisibility(View.INVISIBLE);
            mButtonLayout.setVisibility(View.VISIBLE);

            updateInstructions();
            Toast.makeText(this, "Click on the right side of the screen to continue", Toast.LENGTH_LONG).show();

            mForwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInstructionsID <= 4) {
                        mInstructionsID++;
                        updateInstructions();
                    }

                }
            });

            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInstructionsID > 1) {
                        mInstructionsID--;
                        updateInstructions();
                    }
                }
            });

            mLetsGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    mSharedPreferences.edit().putBoolean("FirstTimeSetup", true).apply();


                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

        }else{
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }



    public void updateInstructions(){

        switch (mInstructionsID){

            case 1: mConstraintLayout.setBackgroundResource(R.drawable.noteitdownbgone);
                    break;

            case 2: mConstraintLayout.setBackgroundResource(R.drawable.noteitdownbgtwo);
                    break;

            case 3: mConstraintLayout.setBackgroundResource(R.drawable.noteitdownbgthree);
                    break;

            case 4: mConstraintLayout.setBackgroundResource(R.drawable.noteitdownbgfour);
                    break;


        }

        if(mInstructionsID > 4)
            mInstructionsID = 4;

        if(mInstructionsID == 4){

            mButtonLayout.setVisibility(View.INVISIBLE);
            mLetsGo.setVisibility(View.VISIBLE);

        }


    }

    public void initDatabase(){

        try{
            SQLiteDatabase myDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY, title TEXT, message TEXT)");

            myDatabase.execSQL("INSERT INTO notes (title, message) VALUES ('Example', 'This is a example note')");

            //Toast.makeText(this, "Database Created!", Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(e.getMessage())
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }


    }
}
