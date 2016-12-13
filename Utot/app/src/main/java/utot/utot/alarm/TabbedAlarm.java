package utot.utot.alarm;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;


import com.squareup.picasso.Picasso;

import utot.utot.R;
import utot.utot.poem.SavedPoemsFragment;

public class TabbedAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm);
        Picasso.with(this).load(R.mipmap.ic_import_contacts_black_36dp).into((ImageButton) findViewById(R.id.savedPoemsButton));
        Picasso.with(this).load(R.mipmap.ic_access_alarms_black_36dp).into((ImageButton) findViewById(R.id.alarmButton));
        Picasso.with(this).load(R.mipmap.ic_settings_black_36dp).into((ImageButton) findViewById(R.id.settingsButton));

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, AlarmFragment.newInstance()); // newInstance() is a static factory method.
        transaction.commit();


        final ImageButton alarmButton = (ImageButton) findViewById(R.id.alarmButton);
        final ImageButton settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        final ImageButton savedPoemsButton = (ImageButton) findViewById(R.id.savedPoemsButton);

        alarmButton.setBackgroundColor(0xbababa);
        settingsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
        savedPoemsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));

        savedPoemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to SavedPoemsFragment

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, SavedPoemsFragment.newInstance()); // newInstance() is a static factory method.
                transaction.commit();

                savedPoemsButton.setBackgroundColor(0xbababa);
                alarmButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
                settingsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
            }
        });
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to AlarmFragment

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, AlarmFragment.newInstance()); // newInstance() is a static factory method.
                transaction.commit();

                alarmButton.setBackgroundColor(0xbababa);
                settingsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
                savedPoemsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
            }
        });


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to SettingsFragment
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, SettingsFragment.newInstance()); // newInstance() is a static factory method.
                transaction.commit();
                settingsButton.setBackgroundColor(0xbababa);
                alarmButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
                savedPoemsButton.setBackgroundColor(ContextCompat.getColor(TabbedAlarm.this, android.R.color.transparent));
            }
        });

    }




}
