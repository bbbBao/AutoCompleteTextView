package com.example.autocompletetextview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private DatabaseManager dbManager;

    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // for database operations
    DatabaseManager databaseH;

    // just to add some initial value
    String[] item = new String[] {"Please search..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            // instantiate database handler
            databaseH = new DatabaseManager(MainActivity.this);

            // put sample data to database
            insertSampleData();

            // autocompletetextview is in activity_main.xml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    public void insertSampleData(){



        databaseH.create( new Email("tianyi@bu.edu") );
        databaseH.create( new Email("lzivan@bu.edu") );
        databaseH.create( new Email("gcj@bu.edu") );
        databaseH.create( new Email("lycc@bu.edu") );
        databaseH.create( new Email("rec@bu.edu") );

    }

    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        ArrayList<Email> emails = databaseH.read(searchTerm);
        int rowCount = emails.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (Email email : emails) {

            item[x] = email.objectName;
            Log.w("MainActivity", "Item is: " + item);
            x++;
        }

        return item;
    }


}