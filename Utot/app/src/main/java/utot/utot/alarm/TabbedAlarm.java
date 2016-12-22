package utot.utot.alarm;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.SavedPoemsFragment;
import utot.utot.settings.SettingsFragment;

public class TabbedAlarm extends AppCompatActivity {
    public static CallbackManager callbackManager;

    private ImageButton savedPoemsButton, alarmButton, settingsButton;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int cur;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm);
        Picasso.with(this).load(R.mipmap.ic_import_contacts_black_36dp).into((ImageButton) findViewById(R.id.savedPoemsButton));
        Picasso.with(this).load(R.mipmap.ic_access_alarms_black_36dp).into((ImageButton) findViewById(R.id.alarmButton));
        Picasso.with(this).load(R.mipmap.ic_settings_black_36dp).into((ImageButton) findViewById(R.id.settingsButton));

        int action = getIntent().getIntExtra(FinalVariables.ACTION_DONE, -1);
        if(action == FinalVariables.POEM_SAVE){
            Toast.makeText(this, "Poem saved!", Toast.LENGTH_SHORT).show();
        } else if(action == FinalVariables.POEM_DISCARD){
            Toast.makeText(this, "Poem discarded!", Toast.LENGTH_SHORT).show();
        } else if(action == FinalVariables.POEM_SHARE){
            Toast.makeText(this, "Poem shared successfully!", Toast.LENGTH_SHORT).show();
        }
        cur = 1; // currently in alarmFragment

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, AlarmFragment.newInstance());
        transaction.commit();


        alarmButton = (ImageButton) findViewById(R.id.alarmButton);
        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        savedPoemsButton = (ImageButton) findViewById(R.id.savedPoemsButton);

        changeButtonBG(alarmButton, savedPoemsButton, settingsButton);

        savedPoemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to SavedPoemsFragment
                if(cur!=0) {
                    callbackManager = CallbackManager.Factory.create();
                    transaction = manager.beginTransaction();
                   // transaction.setCustomAnimations(R.anim.left_to_right_slide_slow, R.anim.right_to_left_slide_slow);
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.container, SavedPoemsFragment.newInstance()); // newInstance() is a static factory method.
                    transaction.commit();

                    changeButtonBG(savedPoemsButton, settingsButton, alarmButton);
                    cur = 0;
                }
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to AlarmFragment
                if(cur!=1) {
                    transaction = manager.beginTransaction();
                    if(cur==2) {
                      //  transaction.setCustomAnimations(R.anim.left_to_right_slide_slow, R.anim.right_to_left_slide_slow);
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                    if(cur==0) {
                      //  transaction.setCustomAnimations(R.anim.pull_in_right_slow, R.anim.push_out_left_slow);
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                    transaction.replace(R.id.container, AlarmFragment.newInstance());
                    transaction.commit();

                    changeButtonBG(alarmButton, settingsButton, savedPoemsButton);

                    cur = 1;
                }
            }
        });


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to SettingsFragment
                if(cur!=2) {
                    transaction = manager.beginTransaction();
                   // transaction.setCustomAnimations(R.anim.pull_in_right_slow, R.anim.push_out_left_slow);
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.container, SettingsFragment.newInstance());
                    transaction.commit();

                    changeButtonBG(settingsButton, alarmButton, savedPoemsButton);
                    cur =2;
                }
            }
        });

    }

    protected void changeButtonBG(ImageButton b1, ImageButton b2, ImageButton b3){
        b1.setBackgroundColor(Color.parseColor("#bababa"));
        b2.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
        b3.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
