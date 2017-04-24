package utot.utot.alarm;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.asynctasks.GetHugotListTask;
import utot.utot.customobjects.Poem;
import utot.utot.customviews.TextViewPlus;
import utot.utot.helpers.ActionBarMaker;
import utot.utot.helpers.BitmapMaker;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.SavedPoemsFragment;
import utot.utot.settings.SettingsFragment;

public class TabbedAlarm extends AppCompatActivity {
    public static CallbackManager callbackManager;

//    private ImageButton savedPoemsButton, alarmButton, settingsButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextViewPlus titleText;
    private int cur;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm);
        Realm.init(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        if((int) Realm.getDefaultInstance().where(Poem.class).count() <= 0){
            new GetHugotListTask(this).execute();
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        titleText = (TextViewPlus)toolbar.findViewById(R.id.actTitle);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager = (ViewPager) findViewById(R.id.container);

//        alarmButton = (ImageButton) findViewById(R.id.alarmButton);
//        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
//        savedPoemsButton = (ImageButton) findViewById(R.id.savedPoemsButton);
//
//        Glide.with(this).load(R.mipmap.ic_import_contacts_black_36dp).into((ImageButton) findViewById(R.id.savedPoemsButton));
//        Glide.with(this).load(R.mipmap.ic_access_alarms_black_36dp).into((ImageButton) findViewById(R.id.alarmButton));
//        Glide.with(this).load(R.mipmap.ic_settings_black_36dp).into((ImageButton) findViewById(R.id.settingsButton));

        int action = getIntent().getIntExtra(FinalVariables.ACTION_DONE, -1);
        if(action == FinalVariables.POEM_SAVE){
            Toast.makeText(this, "Poem saved!", Toast.LENGTH_SHORT).show();
        } else if(action == FinalVariables.POEM_DISCARD){
            Toast.makeText(this, "Poem discarded!", Toast.LENGTH_SHORT).show();
        } else if(action == FinalVariables.POEM_SHARE){
            Toast.makeText(this, "Poem shared successfully!", Toast.LENGTH_SHORT).show();
        }

        cur = getIntent().getIntExtra("TABBED", 1);
        TabbedAlarmAdapter taa = new TabbedAlarmAdapter(getSupportFragmentManager());
        viewPager.setAdapter(taa);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_import_contacts_black_36dp);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_access_alarms_black_36dp);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_settings_black_36dp);
        viewPager.setCurrentItem(cur);

        if(cur==2){
            titleText.setText("Settings");
        } else if(cur==1){
            titleText.setText("Alarms");
        } else{
            titleText.setText("Saved Poems");
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    titleText.setText("Settings");
                } else if(position==1){
                    titleText.setText("Alarms");
                } else{
                    titleText.setText("Saved Poems");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//       manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.replace(R.id.container, display);
//        transaction.commit();
//
//        savedPoemsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go to SavedPoemsFragment
//                if(cur!=0) {
//                    transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
////                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    transaction.replace(R.id.container, SavedPoemsFragment.newInstance()); // newInstance() is a static factory method.
//                    transaction.commit();
//
//                    changeButtonBG(savedPoemsButton, settingsButton, alarmButton);
//                    cur = 0;
//
//
//                    titleText.setText("Saved Poems");
//                }
//            }
//        });
//        alarmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go to AlarmFragment
//                if(cur!=1) {
//                    transaction = manager.beginTransaction();
//                    if(cur==2) {
//                        transaction.setCustomAnimations(R.anim.left_to_right_slide, R.anim.right_to_left_slide);
////                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    }
//                    if(cur==0) {
//                        transaction.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left);
////                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    }
//                    transaction.replace(R.id.container, AlarmFragment.newInstance());
//                    transaction.commit();
//
//                    changeButtonBG(alarmButton, settingsButton, savedPoemsButton);
//
//                    titleText.setText("Alarms");
//                    cur = 1;
//                }
//            }
//        });
//
//
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go to SettingsFragment
//                if(cur!=2) {
//                    transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left);
////                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    transaction.replace(R.id.container, SettingsFragment.newInstance());
//                    transaction.commit();
//
//                    changeButtonBG(settingsButton, alarmButton, savedPoemsButton);
//                    cur =2;
//
//
//                    titleText.setText("Settings");
//                }
//            }
//        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FinalVariables.REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You can now share hugots in facebook!")
                            .setTitle("Permission granted");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {

                }
                return;
            }
        }
    }
}
