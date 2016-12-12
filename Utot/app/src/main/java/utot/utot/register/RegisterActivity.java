package utot.utot.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.customobjects.CustomFrameLayout;
import utot.utot.login.LoginActivity;
import utot.utot.login.LoginSplashScreen;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

      // Picasso.with(this).load(R.drawable.aa_2).into((CustomFrameLayout) findViewById(R.id.mainwindow));
        Picasso.with(this).load(R.drawable.aa_13).into((ImageView)findViewById(R.id.utotLogo));


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
    }
}
