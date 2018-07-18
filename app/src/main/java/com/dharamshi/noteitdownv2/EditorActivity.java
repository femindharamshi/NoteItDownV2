package com.dharamshi.noteitdownv2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    static int noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        final Intent intent = getIntent();



        final SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editNote = findViewById(R.id.editNote);

        noteID = intent.getIntExtra("noteID", -1);

        if(noteID != -1)
        {

            editTitle.setText(HomeActivity.titleList.get(noteID));
            editNote.setText(HomeActivity.notesList.get(noteID));

        }
        else{

            HomeActivity.titleList.add("");
            HomeActivity.notesList.add("");

            noteID = HomeActivity.titleList.size() - 1;

            SQLiteDatabase myDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY, title TEXT, message TEXT)");

            myDatabase.execSQL("INSERT INTO notes (title, message) VALUES (NULL , NULL)");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM notes", null);

            int idIndex = c.getColumnIndex("id");

            c.moveToLast();

            HomeActivity.idList.add(c.getInt(idIndex));

        }

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String sql = "UPDATE notes SET title = '" + charSequence.toString() + "' WHERE id="+ String.valueOf(HomeActivity.idList.get(noteID)); //<<<< CHANGED
                sqLiteDatabase.execSQL(sql);

                //Toast.makeText(EditorActivity.this, "UPDATE notes SET title = '" + charSequence.toString() + "' WHERE id = "+ HomeActivity.idList.get(noteID), Toast.LENGTH_SHORT).show();
                HomeActivity.titleList.set(noteID, charSequence.toString());
                HomeActivity.sArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sql = "UPDATE notes SET message = '" + charSequence.toString() + "' WHERE id="+ String.valueOf(HomeActivity.idList.get(noteID)); //<<<< CHANGED
                sqLiteDatabase.execSQL(sql);

                //Toast.makeText(EditorActivity.this, "UPDATE notes SET title = '" + charSequence.toString() + "' WHERE id = "+ HomeActivity.idList.get(noteID), Toast.LENGTH_SHORT).show();
                HomeActivity.notesList.set(noteID, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


}
