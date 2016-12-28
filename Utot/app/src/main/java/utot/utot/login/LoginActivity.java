package utot.utot.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.asynctasks.CheckingStart;
import utot.utot.asynctasks.LoginTask;
import utot.utot.customviews.ButtonPlus;
import utot.utot.helpers.CheckInternet;
import utot.utot.helpers.FinalVariables;
import utot.utot.helpers.LoginCommon;
import utot.utot.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Glide.with(this).load(R.mipmap.utotlogo1).into((ImageView) findViewById(R.id.utotLogo));

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_fb);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        loginButton.registerCallback(callbackManager, LoginCommon.facebookCallback(LoginActivity.this, true));

        Button loginButton2 = (Button) findViewById(R.id.fb_login);
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
                String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill up the given fields", Toast.LENGTH_SHORT).show();
                } else {
                        LoginTask loggingIn = new LoginTask(getBaseContext(), LoginActivity.this, username, password);
                        loggingIn.execute();
                }
            }
        });

        //Help buttons
        final ButtonPlus helpButton = (ButtonPlus) findViewById(R.id.helpCenterButton);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final ButtonPlus forgotButton = (ButtonPlus) findViewById(R.id.forgetPasswordButton);

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
