package com.project.librefit.librefit2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class facebook_class  {
    final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String email;
    public   facebook_class(CallbackManager callbackManager, final Context context){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, final GraphResponse response) {
                        try {
                            email= object.getString("email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //addToDataBase(object);

                            AuthCredential credential= FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                             signin(context);
                                    }
                                    else {
                                        Toast.makeText(context,"faild",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }



                });
                Bundle parameters=new Bundle();
                parameters.putString("fields","id,email,name,cover");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });





    }
    public void signin(Context context){
            Intent i = new Intent(context, profiledemo.class);
            context.startActivity(i);
            ((Activity) context).finish();
    }
}
