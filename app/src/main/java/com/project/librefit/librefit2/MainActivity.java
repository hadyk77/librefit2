package com.project.librefit.librefit2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    RelativeLayout relativeLayout;
    EditText email,password;
    Button facebook_custom,google;
    TextView registar,forget_password;
    Handler handler=new Handler();
    Runnable runnable;
    ImageView logo;
    LoginButton faceBookDefulat;
    CallbackManager callbackManager;
    Animation animation;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions gso;
    Button login;
 private static final int googleRequest_Code=777;
 private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        intial_value();
        logo.startAnimation(animation);

        pullup_animation();
        handler.postDelayed(runnable, 3000);
        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
                finish();
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, forgetpassword.class);
                startActivity(intent);
                finish();
            }
        });

        facebook_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Profile.getCurrentProfile()!=null){
                    LoginManager.getInstance().logOut();
                }
                faceBookDefulat.performClick();
            }
        });

     
        facebook_class fb=new facebook_class(callbackManager,MainActivity.this);
        google=(Button) findViewById(R.id.google);
        google.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });


    }

    public void signin() {
        final String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(validation(Email,Password)){
            return;
        }
        else {
            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this,profiledemo.class));
                        finish();
                    }
                    else {
                        email.setError("your email is not Registered");
                        email.requestFocus();
                        return;
                    }
                }
            });
        }
    }

    public  void intial_value(){
        password=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email);
        facebook_custom=(Button) findViewById(R.id.fb);
        relativeLayout=(RelativeLayout)findViewById(R.id.rela);
        logo=(ImageView)findViewById(R.id.logo);
        registar=(TextView)findViewById(R.id.register);
        animation= AnimationUtils.loadAnimation(this,R.anim.fadein);
        forget_password=(TextView)findViewById(R.id.forgetpass);
        faceBookDefulat=(LoginButton) findViewById(R.id.login_button);
        faceBookDefulat.setReadPermissions(Arrays.asList("public_profile"));
        callbackManager=CallbackManager.Factory.create();
        mAuth=FirebaseAuth.getInstance();
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleApiClient=new GoogleApiClient.
                Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
login= findViewById(R.id.login);


    }
    public void pullup_animation(){

        runnable=new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "translationY", -50f);
                animator.setDuration(500);
                animator.start();
                relativeLayout.setVisibility(View.VISIBLE);
                logo.setImageResource(R.drawable.logggowhite);
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==googleRequest_Code){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account=result.getSignInAccount();
            if(result.isSuccess()){
                googleAuth(account);
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,googleRequest_Code);

    }
    private void googleAuth(GoogleSignInAccount account){
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   startActivity(new Intent(getApplicationContext(),profiledemo.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"faild",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
private boolean validation(String Email,String Password){
        boolean Catch_error=false;

          if(Email.isEmpty()){
        email.setError("Email field is required");
        email.requestFocus();
        Catch_error=true;

    }
    else if(Password.isEmpty()){
        password.setError("password field is required");
        password.requestFocus();
        Catch_error=true;

    }
    if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
        email.setError("please enter a valid email");
        email.requestFocus();
        Catch_error=true;

    }
    return Catch_error;
}

}
