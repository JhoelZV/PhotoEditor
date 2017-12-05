package com.example.nterrill.photoeditor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    static final int REQUEST_IMAGE_GALLERY = 2;


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
                openGallery();
            }
        });
    }

    private void takePicture(){
        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(CameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(CameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery(){
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(imageGalleryIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_CANCELED){
            Bundle pictureData = data.getExtras();
            Bitmap takenPicture = (Bitmap) pictureData.get("data");
            mLogo.setImageBitmap(takenPicture);
        } else if (resultCode == 2){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);

            cursor.close();

            Bitmap userSlectedImage = BitmapFactory.decodeFile(filePath);
            mLogo.setImageBitmap(userSlectedImage);


        }
    }
}
