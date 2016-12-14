package utot.utot.alarm;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.ImageButton;


import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.poem.SavedPoemsFragment;

public class TabbedAlarm extends AppCompatActivity {

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
                    transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
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
                    if(cur==2) transaction.setCustomAnimations(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
                    if(cur==0) transaction.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left);
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
                    transaction.setCustomAnimations(R.animator.pull_in_right, R.animator.push_out_left);
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


}
