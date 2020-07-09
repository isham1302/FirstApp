package com.example.first;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Home extends AppCompatActivity {

   // private static final int PERMISSION_CODE = 1000;
   // private static final int IMAGE_CAPTURE_CODE = 1001;
    final int SEND_SMS_PERMISSION_REQUEST_CODE= 1;
    //Uri image_uri;

    ScrollView bg;
    ImageView logo;
    Button btn,btn1;

    public static final String MYPREFERENCES="nightModePref";
    public static final String MY_ISNIGHTMODE="isNightMode";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bg= findViewById(R.id.bg);
        logo= findViewById(R.id.logo);
        btn= findViewById(R.id.info);
        btn1= findViewById(R.id.nxt);
        registerForContextMenu(logo);

        sharedPreferences= getSharedPreferences(MYPREFERENCES,MODE_PRIVATE);

        checkNightModeActivated();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Home.this, recycle.class);
                startActivity(intent);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Home.this, horizontal.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mode:
                Toast.makeText(getApplicationContext(), "Mode is Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.background:
                Toast.makeText(getApplicationContext(), "Background is Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.contact:
                Toast.makeText(getApplicationContext(), "Contact Us", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.day:
                Toast.makeText(getApplicationContext(), "Day mode is Selected", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveNightModeState(false);
                recreate();
                return true;
            case R.id.night:
                Toast.makeText(getApplicationContext(), "Night Mode is Selected", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveNightModeState(true);
                recreate();
                return true;
            case R.id.camera:
                Toast.makeText(getApplicationContext(), "Click Picture from Camera", Toast.LENGTH_SHORT).show();
                /*if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission= {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }else {
                            openCamera();
                    }
                }else{
                        openCamera();
                }*/
                Intent i =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,101);
                return true;
            case R.id.gallery:
                Toast.makeText(getApplicationContext(), "Pick a Picture from Gallery", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return_data",true);
                startActivityForResult(intent,102);
                return true;
            case R.id.call:
                Toast.makeText(getApplicationContext(), "Make a call", Toast.LENGTH_SHORT).show();
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "9723555107"));
                    startActivity(callIntent);
                }
                return true;
            case R.id.sms:
                Toast.makeText(getApplicationContext(), "Text Us", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
                if (checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager= SmsManager.getDefault();
                    smsManager.sendTextMessage("9723555107",null,"Hi!! How may I help You",null,null);
                }else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.whatsapp:
                Toast.makeText(getApplicationContext(), "WhatsApp Us", Toast.LENGTH_SHORT).show();
                boolean installed= appInstalledOrNot("com.whatsapp");
                if (installed){
                    Intent whatsIntent= new Intent(Intent.ACTION_VIEW);
                    whatsIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone"+"+9723555107"+"&text="+"Hi!! How may I help you"));
                    startActivity(whatsIntent);
                }else {
                    Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.mail:
                String recipient="isham1302@gmail.com";
                String subject="Register enquiry";
                String message="Hi!! How may I help you.";
                Toast.makeText(getApplicationContext(), "Mail Us", Toast.LENGTH_SHORT).show();
                sendMail(recipient,subject,message);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendMail(String recipient, String subject, String message) {
        Intent mailIntent= new Intent(Intent.ACTION_SEND);
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.setType("text/plain");
        mailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{recipient});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        mailIntent.putExtra(Intent.EXTRA_TEXT,message);
        try {
                startActivity(mailIntent);
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager= getPackageManager();
        boolean app_installed;
        try {
                packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
                app_installed=true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed=false;
        }
        return app_installed;
    }

   /* private void openCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
        iv.setImageURI(image_uri);
    }*/

    private void saveNightModeState ( boolean nightMode){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MY_ISNIGHTMODE, nightMode);
        editor.apply();
    }
    public void checkNightModeActivated (){
        if (sharedPreferences.getBoolean(MY_ISNIGHTMODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            Bitmap b= (Bitmap) data.getExtras().get("data");
            bg.setBackground(new BitmapDrawable(getResources(), b));
        }else if(requestCode==102){
            Uri image=data.getData();
            Bitmap b;
            try {
                b = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                bg.setBackground(new BitmapDrawable(getResources(), b));
            }catch (Exception e){
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_1:
                Toast.makeText(this, "Logo download link", Toast.LENGTH_SHORT).show();
                Intent web= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=java+logo&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj2uMTKx7zqAhX0xzgGHdXyAf4Q_AUoAXoECA8QAw&biw=1280&bih=610&dpr=1.5#imgrc=bojPrPt0Xb0G7M"));
                startActivity(web);
                return true;
            case R.id.option_2:
                Toast.makeText(this, "Read more on Java", Toast.LENGTH_SHORT).show();
                Intent webIntent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.javatpoint.com/java-tutorial"));
                startActivity(webIntent);
                return true;
            case R.id.option_3:
                Toast.makeText(this, "View a video", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(this,webView.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
