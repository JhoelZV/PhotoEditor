package com.example.nterrill.photoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button mCameraButton;
    Button mGalleryButton;
    ImageView mLogo;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton = (Button)findViewById(R.id.CameraButton);
        mGalleryButton = (Button)findViewById(R.id.GalleryButton);
        mLogo = (ImageView)findViewById(R.id.LogoImageView);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void takePicture(){
        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(CameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(CameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_CANCELED){
            Bundle pictureData = data.getExtras();
            Bitmap takenPicture = (Bitmap) pictureData.get("data");
            mLogo.setImageBitmap(takenPicture);
        }
    }
}
