package utot.utot.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import utot.utot.R;
import utot.utot.customviews.ClearTextView;
import utot.utot.register.RegisterActivity;

public class InitialScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        ClearTextView register = (ClearTextView)findViewById(R.id.registerButton);
        ClearTextView login = (ClearTextView)findViewById(R.id.loginButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitialScreen.this.startActivity(new Intent(InitialScreen.this, RegisterActivity.class));
                InitialScreen.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitialScreen.this.startActivity(new Intent(InitialScreen.this, LoginActivity.class));
                finish();
                InitialScreen.this.overridePendingTransition(R.anim.left_to_right_slide, R.anim.right_to_left_slide);

            }
        });

    }
}
