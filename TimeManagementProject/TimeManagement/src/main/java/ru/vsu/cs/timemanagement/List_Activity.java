package ru.vsu.cs.timemanagement;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.List;
import java.util.Objects;

public class List_Activity extends Activity {

    private boolean important;
    private boolean urgent;
    private ListView list;

    private boolean all;

    private int kost = 0;
    private final int IDD_LIST = 1;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_);

        Bundle b = getIntent().getExtras();
        all = b.getBoolean("all");

        important = b.getBoolean("import");
        urgent = b.getBoolean("urg");
        if (!all) {
            if (important && urgent)
                setTitle("Важные и срочные");
            if (important && !urgent)
                setTitle("Важные и не срочные");
            if (!important && urgent)
                setTitle("Не важные и срочные");
            if (!important && !urgent)
                setTitle("Не важные и не срочные");
        }
        else {
            setTitle("Все задания");
        }
        list = (ListView) findViewById(R.id.list);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (kost == 0) {
                    Data item = (Data) list.getAdapter().getItem(position);

                    Intent i = new Intent(List_Activity.this, Edit_Activity.class);
                    int flag = 1;
                    i.putExtra("flag", flag);
                    i.putExtra("name", item.name);
                    i.putExtra("descr", item.description);
                    i.putExtra("import", item.important);
                    i.putExtra("urg", item.urgent);
                    i.putExtra("coordX", item.coordX);
                    i.putExtra("coordY", item.coordY);
                    i.putExtra("path", item.path);
                    startActivity(i);
                    // Toast.makeText(getApplicationContext(),l.name, Toast.LENGTH_SHORT).show();
                }
                else {
                    kost = 0;
                }
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                kost = 1;
                showDialog(IDD_LIST);
                return false;
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_LIST:

                final String[] methods ={"Удалить"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выбрать действие"); // заголовок для диалога

                builder.setItems(methods, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Data i = (Data) list.getAdapter().getItem(item);
                        Data.delete(i.name, i.description, List_Activity.this);
                        onStart();
                    }
                });
                builder.setCancelable(true);
                return builder.create();

            default:
                return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!all) {
            new TakeFromDBTask().execute(important, urgent);
            //list.setAdapter(new CustomAdapter(Data.returnData(important,urgent,this),this));
        }
        else {
            new TakeFromDBTask().execute();
            //list.setAdapter(new CustomAdapter(Data.returnAll(this), this));
        }
    }

    private class TakeFromDBTask extends AsyncTask<Object, Void, List<Data>> {

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(List_Activity.this);
            progress.setMessage("Загрузка...");
        }

        @Override
        protected List<Data> doInBackground(Object... params) {
            boolean loc_important;
            boolean loc_urgent;

            if (params.length > 0) {
                loc_important = (Boolean) params[0];
                loc_urgent = (Boolean) params[1];
                return Data.returnData(loc_important,loc_urgent,List_Activity.this);
            }
            else {
                return Data.returnAll(List_Activity.this);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progress.show();
        }

        @Override
        protected void onPostExecute(List<Data> datas) {
            super.onPostExecute(datas);
            list.setAdapter(new CustomAdapter(datas,List_Activity.this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.add_task) :
                Intent i = new Intent(List_Activity.this, Edit_Activity.class);
                int flag = 2;
                i.putExtra("flag", flag);
                i.putExtra("import", important);
                i.putExtra("urg", urgent);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
