package com.demo.androidsample;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NextActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        if (savedInstanceState == null) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Button btn1;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_next, container, false);

            TextView text = (TextView) rootView.findViewById(R.id.print_message);
            text.setText(getArguments().getString(MainActivity.EXTRA_MESSAGE));

            btn1 = (Button) rootView.findViewById(R.id.save_message);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Getting message
                    EditText editText = (EditText) rootView.findViewById(R.id.new_message);
                    String message = editText.getText().toString();

                    //Saving message to Preferences
                    SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.saved_message), message);
                    editor.commit();

                    TextView system_message1 = (TextView) rootView.findViewById(R.id.system_message1);
                    system_message1.setText("Message saved in Preferences");

                    //Reading from preferences
                    SharedPreferences preferences2 = getActivity().getPreferences(Context.MODE_PRIVATE);
                    TextView final_output_message = (TextView) rootView.findViewById(R.id.final_output_message);
                    final_output_message.setText(preferences2.getString(getString(R.string.saved_message), "Nothing! =("));

                    TextView system_message2 = (TextView) rootView.findViewById(R.id.system_message2);
                    system_message2.setText("Message read from Preferences ");

                    //Saving message to DB
                    BookListDbHelper dbHelper = new BookListDbHelper(rootView.getContext());
                    SQLiteDatabase db1 = dbHelper.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(BookListContract.MessageEntry.COLUMN_NAME_TEXT, message);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId = db1.insert(BookListContract.MessageEntry.TABLE_NAME, null, values);

                    TextView system_message3 = (TextView) rootView.findViewById(R.id.system_message3);
                    system_message3.setText("Message inserted into DB");

                    //Reading message from DB
                    SQLiteDatabase db2 = dbHelper.getReadableDatabase();

                    // Define a projection that specifies which columns from the database
                    // you will actually use after this query.
                    String[] projection = {
                            BookListContract.MessageEntry._ID,
                            BookListContract.MessageEntry.COLUMN_NAME_TEXT
                    };

                    // Define 'where' part of query.
                    String selection = BookListContract.MessageEntry._ID + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { String.valueOf(newRowId) };

                    // How you want the results sorted in the resulting Cursor
                    String sortOrder =
                            BookListContract.MessageEntry.COLUMN_NAME_TEXT + " DESC";

                    Cursor cursor = db2.query(
                            BookListContract.MessageEntry.TABLE_NAME, // The table to query
                            projection,                               // The columns to return
                            selection,                                // The columns for the WHERE clause
                            selectionArgs,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            sortOrder                                 // The sort order
                    );

                    cursor.moveToFirst();
                    String message2 = cursor.getString(cursor.getColumnIndexOrThrow(BookListContract.MessageEntry.COLUMN_NAME_TEXT));

                    TextView db_output_message = (TextView) rootView.findViewById(R.id.db_output_message);
                    db_output_message.setText(message2);

                    TextView system_message4 = (TextView) rootView.findViewById(R.id.system_message4);
                    system_message4.setText("Message obtained from DB");


                }
            });

            return rootView;
        }
    }

}
