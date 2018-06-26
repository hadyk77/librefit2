package com.project.librefit.librefit2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.annotation.Target;

public class signup extends AppCompatActivity {
    Button ok;
    Button back;
    FirebaseAuth mAuth;
    ProgressBar pr;
    EditText name,Email,password,confirm_email,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initial_value();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(signup.this,login_without_anim.class);
                startActivity(intent);
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registar();
            }
        });
    }

    private void registar() {
        String Password=password.getText().toString().trim();
        final String email=Email.getText().toString().trim();
        final String Name=name.getText().toString().trim();
        if(validation()){
            return;
        }
        else {
            mAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Intent intent=new Intent(signup.this,profiledemo.class);
                   DatabaseReference reference=FirebaseDatabase.getInstance().getReference("user").child(mAuth.getCurrentUser().getUid());
                   account_info info=new account_info(Name,email,"empty",mAuth.getUid());
                   reference.setValue(info);
                   startActivity(intent);
                   finish();
               }
               else {
                   Toast.makeText(signup.this,"you are already registered",Toast.LENGTH_LONG).show();
               }
                }
            });
        }
    }

    private boolean validation() {
        String Name=name.getText().toString().trim();
        String email=Email.getText().toString().trim();
        String Confirm_email=confirm_email.getText().toString().trim();
        String Password=password.getText().toString().trim();
        String Confirm_password=confirm_password.getText().toString().trim();
        boolean catch_Error=false;
        if(Name.isEmpty()){
            name.setError("Name field is required");
            name.requestFocus();
            catch_Error=true;

        }

        else if(email.isEmpty()){
            Email.setError("Email field is required");
            Email.requestFocus();
            catch_Error=true;

        }
        else if(Password.isEmpty()){
            password.setError("password field is required");
            confirm_password.requestFocus();
            catch_Error=true;

        }
        else if(Confirm_password.isEmpty()){
            confirm_password.setError("confirm password field is required");
            confirm_password.requestFocus();
            catch_Error=true;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("please enter a valid email");
            Email.requestFocus();
            catch_Error=true;

        }
        if(Password.length()<6)
        {
            password.setError("Enter a combination of at least six numbers,digits");
            password.requestFocus();
            catch_Error=true;
        }
        if(!email.equals(Confirm_email)){
            confirm_email.setError("Your Emails Do not Match Try Again");
            confirm_email.requestFocus();
            catch_Error=true;
        }
        return catch_Error;
    }


    public  void initial_value(){
        ok=(Button) findViewById(R.id.ok);
        back=(Button) findViewById(R.id.back);
        name=(EditText)findViewById(R.id.yourname);
        Email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirm_email=(EditText)findViewById(R.id.confirmemail);
        confirm_password=(EditText)findViewById(R.id.confirmpassword);
        mAuth=FirebaseAuth.getInstance();
    }


}
