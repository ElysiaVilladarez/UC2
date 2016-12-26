package utot.utot.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import utot.utot.R;
import utot.utot.asynctasks.RegisterTask;
import utot.utot.customviews.ButtonPlus;
import utot.utot.helpers.LoginCommon;
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

        Glide.with(this).load(R.mipmap.utotlogo1).into((ImageView)findViewById(R.id.utotLogo));

        callbackManager = CallbackManager.Factory.create();
        registerButton = (LoginButton)findViewById(R.id.login_fb);
        registerButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        registerButton.registerCallback(callbackManager, LoginCommon.facebookCallback(RegisterActivity.this, false));

        Button registerButton2 = (Button)findViewById(R.id.fb_login);
        registerButton2.setText("Register with Facebook");
        registerButton2.setOnClickListener(new View.OnClickListener() {
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
                String username = ((EditText)findViewById(R.id.username)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password)).getText().toString().trim();
                String confirmPassword = ((EditText)findViewById(R.id.confirmPassword)).getText().toString().trim();
                //additionally, check if username or phone already exists in the database
                if(password.equals(confirmPassword)) {
                    new RegisterTask(getApplicationContext(), RegisterActivity.this, username, password, "", "","");
                } else if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill up the necessary fields", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Please confirm password", Toast.LENGTH_SHORT).show();
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
