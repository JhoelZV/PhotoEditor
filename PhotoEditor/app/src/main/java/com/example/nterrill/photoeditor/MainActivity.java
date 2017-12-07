package com.example.nterrill.photoeditor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_THUMBNAIL_IMAGE = 2;
    private static final int  MY_PERMISSION_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 0;

    Button b_filter, b_filter2, b_filter3, b_filter4;
    ImageButton b_share, b_save, b_load, b_camera;
    ImageView imageView, imageFilter;
    String currentImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            //Nothing
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        imageFilter = (ImageView) findViewById(R.id.imageFilter);

        b_load = (ImageButton) findViewById(R.id.b_load);
        b_save = (ImageButton) findViewById(R.id.b_save);
        b_filter = (Button) findViewById(R.id.b_filter);
        b_filter2 = (Button) findViewById(R.id.b_filter2);
        b_filter3 = (Button) findViewById(R.id.b_filter3);
        b_filter4 = (Button) findViewById(R.id.b_filter4);
        b_share = (ImageButton) findViewById(R.id.b_share);
        b_camera = (ImageButton) findViewById(R.id.b_camera);

        b_filter.setEnabled(false);
        b_filter2.setEnabled(false);
        b_filter3.setEnabled(false);
        b_filter4.setEnabled(false);
        b_save.setEnabled(false);
        b_share.setEnabled(false);


        b_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_THUMBNAIL_IMAGE);
            }
        });



        b_load.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        b_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                View content = findViewById(R.id.lay);
                Bitmap bitmap = getScreenShot(content);
                currentImage = "image" + System.currentTimeMillis() + ".png";
                store(bitmap, currentImage);
                b_share.setEnabled(true);
            }
        });


        //Filters
        b_filter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imageFilter.setImageResource(R.drawable.spring_filter);
                b_save.setEnabled(true);
            }
        });
        b_filter2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imageFilter.setImageResource(R.drawable.summer_filter);
                b_save.setEnabled(true);
            }
        });
        b_filter3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imageFilter.setImageResource(R.drawable.fall_filter);
                b_save.setEnabled(true);
            }
        });
        b_filter4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imageFilter.setImageResource(R.drawable.winter_filter);
                b_save.setEnabled(true);
            }
        });

        b_share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                shareImage(currentImage);
            }
        });
    }

    private static Bitmap getScreenShot (View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void store(Bitmap bm, String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FILTEREDIMAGES";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dirPath,fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/FILTEREDIMAGES";
        Uri uri = Uri.fromFile(new File(dirPath, fileName));
        Intent intent  = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT,"");
        intent.putExtra(Intent.EXTRA_TEXT,"");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try{
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            Toast.makeText(this, "No sharing app found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            b_filter.setEnabled(true);
            b_filter2.setEnabled(true);
            b_filter3.setEnabled(true);
            b_filter4.setEnabled(true);
        }
        
//Loading Picture from camera
        if (requestCode == REQUEST_THUMBNAIL_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            b_filter.setEnabled(true);
            b_filter2.setEnabled(true);
            b_filter3.setEnabled(true);
            b_filter4.setEnabled(true);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        //Nothing
                    }
                } else {
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
