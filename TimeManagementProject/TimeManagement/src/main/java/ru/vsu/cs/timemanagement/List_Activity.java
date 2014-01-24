package ru.vsu.cs.timemanagement;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class List_Activity extends Activity {

    private boolean important;
    private boolean urgent;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_);

        Bundle b = getIntent().getExtras();
        important = b.getBoolean("import");
        urgent = b.getBoolean("urg");
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                Data item = (Data) list.getAdapter().getItem(position);

                Intent i = new Intent(List_Activity.this, Edit_Activity.class);
                int flag = 1;
                i.putExtra("flag", flag);
                i.putExtra("name", item.name);
                i.putExtra("descr", item.description);
                i.putExtra("import", item.important);
                i.putExtra("urg", item.urgent);
                startActivity(i);
                // Toast.makeText(getApplicationContext(),l.name, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        list.setAdapter(new CustomAdapter(Data.returnData(important,urgent,this),this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_, menu);
        return true;
    }

}
