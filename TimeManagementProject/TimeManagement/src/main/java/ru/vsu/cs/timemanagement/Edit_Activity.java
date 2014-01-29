package ru.vsu.cs.timemanagement;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Edit_Activity extends Activity {

    private CheckBox ch_import;
    private CheckBox ch_urg;
    private EditText et_name;
    private EditText et_descr;

    private int fl;
    private String name;
    private String name_search;
    private String description;
    private boolean important;
    private boolean urgent;
    private float x;
    private float y;

    private boolean is_map = false;
    //test
    ImageView capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);

        ch_import = (CheckBox)findViewById(R.id.ch_import);
        ch_urg = (CheckBox)findViewById(R.id.ch_urg);
        et_name = (EditText)findViewById(R.id.editText_name);
        et_descr = (EditText)findViewById(R.id.editText_descr);

        Bundle b = getIntent().getExtras();
        fl = b.getInt("flag");
        if (fl == 1) {
            name = b.getString("name");
            name_search = name;
            description = b.getString("descr");
            important = b.getBoolean("import");
            urgent = b.getBoolean("urg");
            x = b.getFloat("coordX");
            y = b.getFloat("coordY");

            et_name.setText(name);
            et_descr.setText(description);
            ch_import.setChecked(important);
            ch_urg.setChecked(urgent);
        }

        capturedImage = (ImageView) findViewById(R.id.imageView);

        /*startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
               // Toast.makeText(Edit_Activity.this,"destroy",Toast.LENGTH_SHORT).show();
                //TODO mycode, save,
                 finish();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_, menu);
        return true;
    }

    public void addPlace(View view) {
        is_map = true;
        Intent i = new Intent(this, Map_activity.class);
        if (fl == 1) {
            i.putExtra("draw", true);
            i.putExtra("coordX",x);
            i.putExtra("coordY",y);
        }
        else {
            i.putExtra("draw", false);
        }
        startActivityForResult(i, 1);
    }

    /////////////////////////////////

    private String getFileName() {
        String path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
        String timeStamp = "String";//new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return path + "/" + timeStamp;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public Intent photoCapture(String file) {
        Uri uri = Uri.fromFile(new File(file));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public void addPhoto(View view) throws IOException {
        Intent captureIntent = photoCapture(getFileName());
        startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE);
    }


    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromFile(String fileName, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //distinguish!!
        if (is_map) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                x = data.getFloatExtra("x",0);
                y = data.getFloatExtra("y",0);
            }
            is_map = false;
        }
        else {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    String fileName = getFileName();
                    Bitmap bitmap = decodeSampledBitmapFromFile(fileName, capturedImage.getWidth(), capturedImage.getHeight());
                    capturedImage.setImageBitmap(bitmap);

                }
            }
        }
    }

    //////////////////////////////

    private void pushNew() {
        Data data = new Data();
        data.name = et_name.getText().toString();
        data.description = et_descr.getText().toString();
        if (ch_import.isChecked() == true)
            data.important = true;
        else
            data.important = false;

        if (ch_urg.isChecked() == true)
            data.urgent = true;
        else
            data.urgent = false;
        data.coordX = x;
        data.coordY = y;
        data.save(this);
        finish();
    }

    private void changeOld() {
        name  = et_name.getText().toString();
        description = et_descr.getText().toString();
        important = ch_import.isChecked();
        urgent = ch_urg.isChecked();
        Data.updateField(name_search, name, description, important, urgent, this);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                //push in DB
                if (fl == 1)
                    changeOld();
                else
                    pushNew();
        }
        return super.onOptionsItemSelected(item);
    }
}
