package ru.vsu.cs.timemanagement;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void addNewTask(View view) {
        Intent i = new Intent(this, Edit_Activity.class);
        int flag = 0;
        i.putExtra("flag", flag);
        startActivity(i);
       // startActivity(new Intent(this, Edit_Activity.class));
    }

    public void viewImpUrg(View view) {
        startNewActivity(true,true);
    }

    public void viewImpNotUrg(View view) {
        startNewActivity(true,false);
    }

    public void viewUnimpUrg(View view) {
        startNewActivity(false,true);
    }

    public void viewUnimpNotUrg(View view) {
        startNewActivity(false,false);
    }

    public void startNewActivity(boolean _import, boolean _urg) {
        Intent i = new Intent(this, List_Activity.class);
        i.putExtra("import", _import);
        i.putExtra("urg", _urg);
        startActivity(i);
    }

}
