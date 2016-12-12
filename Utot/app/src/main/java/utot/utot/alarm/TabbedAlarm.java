package utot.utot.alarm;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;


import com.squareup.picasso.Picasso;

import utot.utot.R;

public class TabbedAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm);
        Picasso.with(this).load(R.mipmap.ic_import_contacts_black_24dp).into((ImageButton) findViewById(R.id.savedPoemsButton));
        Picasso.with(this).load(R.mipmap.ic_alarm_black_24dp).into((ImageButton) findViewById(R.id.alarmButton));
        Picasso.with(this).load(R.mipmap.ic_settings_black_24dp).into((ImageButton) findViewById(R.id.settingsButton));

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, AlarmFragment.newInstance()); // newInstance() is a static factory method.
        transaction.commit();

        final ImageButton alarmButton = (ImageButton) findViewById(R.id.alarmButton);
        final ImageButton settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        final ImageButton savedPoemsButton = (ImageButton) findViewById(R.id.savedPoemsButton);

        savedPoemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to SavedPoemsFragment

                alarmButton.clearColorFilter();
                savedPoemsButton.getBackground().setColorFilter(0xededed, PorterDuff.Mode.MULTIPLY);
                settingsButton.clearColorFilter();
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

                alarmButton.getBackground().setColorFilter(0xededed, PorterDuff.Mode.MULTIPLY);
                settingsButton.clearColorFilter();
                savedPoemsButton.clearColorFilter();
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
                settingsButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);//line changed
                settingsButton.invalidate();
                alarmButton.clearColorFilter();
                savedPoemsButton.clearColorFilter();
            }
        });

    }




}
