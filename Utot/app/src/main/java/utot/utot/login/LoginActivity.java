package utot.utot.login;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.customobjects.CustomFrameLayout;
import utot.utot.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     // Picasso.with(this).load(R.drawable.aa_2).into((CustomFrameLayout) findViewById(R.id.mainwindow));
        Picasso.with(this).load(R.drawable.aa_13).into((ImageView)findViewById(R.id.utotLogo));


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
    }
}
