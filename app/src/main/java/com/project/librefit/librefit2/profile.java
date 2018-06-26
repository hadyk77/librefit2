package com.project.librefit.librefit2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.net.URL;

public class profile extends AppCompatActivity {
    ImageView img;
    TextView username;
    Profile  var_profile;
    TextView logout;
    ImageView cover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intial_items();

             var_profile = Profile.getCurrentProfile();
            // username.setText(var_profile.getName());
            // Picasso.get().load(var_profile.getProfilePictureUri(img.getMaxWidth(),img.getMaxHeight()).toString()).into(img);

              /* logout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       LoginManager.getInstance().logOut();
                       startActivity(new Intent(profile.this, login_without_anim.class));
                       finish();
                       overridePendingTransition(R.anim.slidein, R.anim.slideout);

                   }
               });
*/


    }
    public  void intial_items() {
       // img = (ImageView) findViewById(R.id.profile_image);
        username=(TextView)findViewById(R.id.username);
        //logout=(TextView)findViewById(R.id.logout);
        //cover = (ImageView) findViewById(R.id.cover_image);

    }
    public Transformation round(){
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
        return transformation;

    }

}
