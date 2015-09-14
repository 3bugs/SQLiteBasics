package com.example.sqlitebasics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyHelper mHelper;
    private SQLiteDatabase mDatabase;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new MyHelper(this);
        mDatabase = mHelper.getWritableDatabase();

        Cursor cursor = readAllData();

        mAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{MyHelper.COL_NAME, MyHelper.COL_PHONE_NUMBER},
                new int[]{android.R.id.text1, android.R.id.text2},
                0
        );

        ListView listView = (ListView) findViewById(R.id.contacts_listview);
        listView.setAdapter(mAdapter);

        // กำหนดการทำงานของปุ่ม Add Contact
        Button insertButton = (Button) findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        // กำหนดการทำงานปุ่ม Delete Contact
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });
    }

    private void addContact() {
        ContentValues cv = new ContentValues();
        cv.put(MyHelper.COL_NAME, "bbbbb");
        cv.put(MyHelper.COL_PHONE_NUMBER, "333-333-3333");
        mDatabase.insert(MyHelper.TABLE_NAME, null, cv);

        Cursor cursor = readAllData();
        mAdapter.changeCursor(cursor);
    }

    private void deleteContact() {
/*
        mDatabase.delete(
                MyHelper.TABLE_NAME,
                MyHelper.COL_PHONE_NUMBER + " LIKE ?",
                new String[]{ "333%" }
        );
*/

        mDatabase.execSQL("DELETE FROM contacts WHERE phone_number LIKE '333%'");

        Cursor cursor = readAllData();
        mAdapter.changeCursor(cursor);
    }

    private Cursor readAllData() {
        String[] columns = {
                MyHelper.COL_ID,
                MyHelper.COL_NAME,
                MyHelper.COL_PHONE_NUMBER
        };

        return mDatabase.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null);
    }
}
