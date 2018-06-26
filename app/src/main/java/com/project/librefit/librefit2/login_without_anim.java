package com.project.librefit.librefit2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class login_without_anim extends AppCompatActivity implements View.OnClickListener{

    TextView registar;
    TextView forget_password;
    LoginButton default_bt;
    Button facebook,google,login;
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions gso;
    EditText email,password;
    private static final int googleRequest_Code=999;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(login_without_anim.this);
        setContentView(R.layout.activity_login_without_anim);
        intial_value();
        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login_without_anim.this,signup.class);
                startActivity(intent);
                finish();
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login_without_anim.this,forgetpassword.class);
                startActivity(intent);
                finish();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                default_bt.performClick();
            }
        });
        facebook_class fb=new facebook_class(callbackManager,login_without_anim.this);
        google=(Button) findViewById(R.id.google);
        google.setOnClickListener(this);

    }

    private void signin() {
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
                        startActivity(new Intent(login_without_anim.this,profiledemo.class));
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
        registar=(TextView)findViewById(R.id.register);
        forget_password=(TextView)findViewById(R.id.forgetpass);
        default_bt=(LoginButton)findViewById(R.id.login_button);
        facebook=(Button)findViewById(R.id.facebook);
        default_bt.setReadPermissions(Arrays.asList("public_profile","email"));
        callbackManager=CallbackManager.Factory.create();
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleApiClient=new GoogleApiClient.
                Builder(this).enableAutoManage(this,null)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        mAuth=FirebaseAuth.getInstance();
        login=(Button)findViewById(R.id.login);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
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
                    MainActivity mainActivity=new MainActivity();
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

