package com.project.librefit.librefit2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class show_fullscreen_img extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fullscreen_img);
        ImageView img=(ImageView)findViewById(R.id.img_view);
        String url=getIntent().getStringExtra("profile");
        Picasso.get().load(url).into(img);
        Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

    }


    public void back(View view) {
        startActivity(new Intent(show_fullscreen_img.this,profiledemo.class));
        finish();
    }
}
