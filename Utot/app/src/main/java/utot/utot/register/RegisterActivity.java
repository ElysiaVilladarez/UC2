package utot.utot.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.customviews.ButtonPlus;
import utot.utot.login.LoginActivity;
import utot.utot.login.LoginSplashScreen;

public class RegisterActivity extends Activity {

    private CallbackManager callbackManager;
    private LoginButton registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Picasso.with(this).load(R.drawable.utotlogo1).into((ImageView)findViewById(R.id.utotLogo));

        callbackManager = CallbackManager.Factory.create();
        registerButton = (LoginButton)findViewById(R.id.register_fb);

        registerButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //              "User ID: "
//                        + loginResult.getAccessToken().getUserId()
//                        + "\n" +
//                        "Auth Token: "
//                        + loginResult.getAccessToken().getToken()
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginSplashScreen.class));
                RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(RegisterActivity.this, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(RegisterActivity.this, "An error has occurred during login", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.fb_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButton.performClick();
            }
        });

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                RegisterActivity.this.overridePendingTransition(R.anim.left_to_right_slide, R.anim.right_to_left_slide);

            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check confirm password
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String confirmPassword = ((EditText)findViewById(R.id.confirmPassword)).getText().toString();
                //additionally, check if username or phone already exists in the database
                if(password.equals(confirmPassword)) {
                    // register user to server
                    // log in automatically
                    RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginSplashScreen.class));
                    finish();
                    RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Please confirm password", Toast.LENGTH_SHORT).show();
                } else{
                    // if user already exist
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
