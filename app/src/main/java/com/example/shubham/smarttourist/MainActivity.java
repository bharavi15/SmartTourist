package com.example.shubham.smarttourist;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.acl.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    ImageView img2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Context mContext=MainActivity.this;
    private static int RESULT_LOAD_IMAGE = 1;
    int flag=0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    public static Bitmap im, bitmap, croppedBitmap;
    public static int width;
    public static int height;
    public static boolean isImageSelected = false;

    private String inputNodeName = "conv_input_input";
    String mCurrentPhotoPath;
    private String outputNodeName = "main_output/Softmax";
    String result;


    private static final int REQUEST = 112;

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
              //  Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button imp = findViewById(R.id.Import);
         img2 =findViewById(R.id.tv2);
        Button captureButton = findViewById(R.id.CameraBT);
        this.imageView = (ImageView) this.findViewById(R.id.IMG);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2.setVisibility(View.INVISIBLE);
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED|| isStoragePermissionGranted()) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);

                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }


            }
        });

       imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });
        Button save=(Button)findViewById(R.id.saveIMG);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();

                result = classifyMedicine(bmap);
                Log.d("Test------------", result);
                SaveImage(bmap);
            }
        });


        //spinner
        final Spinner spinner = (Spinner) findViewById(R.id.Spin);
        Button next=(Button)findViewById(R.id.next);
       // spinner.setOnItemSelectedListener();

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("हिंदी");
        categories.add("मराठी");
        categories.add("தமிழ்");
        categories.add("ગુજરાતી");
        categories.add("ਪੰਜਾਬੀ");
        categories.add("తెలుగు");
        categories.add("മല്യാലം");
        categories.add("سنڌي");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,Display.class);
                intent.putExtra("data",String.valueOf(spinner.getSelectedItem()));
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }

        }
               super.onActivityResult(requestCode, resultCode, data);

               if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                   Uri selectedImage = data.getData();
                   String[] filePathColumn = { MediaStore.Images.Media.DATA };

                   Cursor cursor = getContentResolver().query(selectedImage,
                           filePathColumn, null, null, null);
                   cursor.moveToFirst();

                   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                   String picturePath = cursor.getString(columnIndex);
                   cursor.close();

                   img2.setVisibility(View.INVISIBLE);
                   imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                   result = classifyMedicine(BitmapFactory.decodeFile(picturePath));
                   Log.d("output", result);

               }





    }

    public void SaveImage(Bitmap showedImgae){

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/myCapturedImages");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "FILENAME-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            showedImgae.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    public String classifyMedicine(Bitmap image)
    {
        width = image.getWidth();
        height = image.getHeight();
        //isImageSelected = true;

        String outputNames[] = new String[]{outputNodeName};
        croppedBitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);

        frameToCropTransform = getTransformationMatrix(width, height, 64, 64, 0, true);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(image, frameToCropTransform, null);

        int[] pixels = new int[64 * 64];
        float[] floatValues = new float[64 * 64 * 3];
        Log.e("Test-----", String.valueOf(croppedBitmap.getWidth()) + " " + String.valueOf(croppedBitmap.getHeight()));
        croppedBitmap.getPixels(pixels, 0, 64, 0, 0, 64, 64);

        for (int i = 0; i < pixels.length; ++i)
        {
            final int val = pixels[i];
            floatValues[i * 3 + 0] = ((val >> 16) & 0xFF);
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF);
            floatValues[i * 3 + 2] = (val & 0xFF);
        }

        TensorFlowInferenceInterface tensorflow = new TensorFlowInferenceInterface(getAssets(), "frozen_protobuf1.pb");
        tensorflow.feed("conv2d_1_input", floatValues, 1, 64, 64, 3);
        tensorflow.run(outputNames, false);

        float[] output = new float[8];

        tensorflow.fetch("main_output/Softmax", output);
        for(int i = 0 ; i < 8 ; i++)
        {
            if(output[i] == 1)
            {
                switch (i)
                {
                    case 0: return "HawaMahal";
                    case 1: return "TajMahal";

                    default: break;
                }
            }
        }

        return null;
    }

    public static Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {

            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.
        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;

        final int inWidth = transpose ? srcHeight : srcWidth;
        final int inHeight = transpose ? srcWidth : srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        if (applyRotation != 0) {
            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;
    }
}
