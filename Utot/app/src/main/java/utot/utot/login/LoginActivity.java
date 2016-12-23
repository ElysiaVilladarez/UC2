package utot.utot.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.customviews.ButtonPlus;
import utot.utot.helpers.LoginCommon;
import utot.utot.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        AccessToken facebookAccessToken = AccessToken.getCurrentAccessToken();
        boolean sessionExpired;
        if(facebookAccessToken != null){
            sessionExpired = facebookAccessToken.isExpired();
        }else{
            sessionExpired = true;
        }

        if(!sessionExpired){
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoginSplashScreen.class));
            LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        Picasso.with(this).load(R.mipmap.utotlogo1).into((ImageView)findViewById(R.id.utotLogo));


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_fb);

        loginButton.registerCallback(callbackManager, LoginCommon.facebookCallback(LoginActivity.this));

        Button loginButton2 = (Button)findViewById(R.id.fb_login);
        loginButton2.setText("Login with Facebook");
        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                LoginActivity.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();

            }
        });



        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // authenticate user
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                boolean authenticated = true;
                if(authenticated){
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, LoginSplashScreen.class));
                    LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                } else{
                    Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Help buttons
        final ButtonPlus helpButton = (ButtonPlus)findViewById(R.id.helpCenterButton);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final ButtonPlus forgotButton = (ButtonPlus)findViewById(R.id.forgetPasswordButton);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
