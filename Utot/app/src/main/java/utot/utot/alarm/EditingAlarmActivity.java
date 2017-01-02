package utot.utot.alarm;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Alarm;
import utot.utot.helpers.ActionBarMaker;
import utot.utot.helpers.CommonMethods;
import utot.utot.helpers.Computations;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;


public class EditingAlarmActivity extends AppCompatActivity {
    private TextView timeSet;
    private Date alarmTime;
    private ToggleButton[] daysToggle;
    private ToggleButton everydayButton, weekendsButton, weekdaysButton;
    private Alarm alarm;
    private CheckBox repeatingSwitch;
    private String ringtoneText;
    private int ringtonePos;
    Calendar mcurrentTime;

    private CommonMethods cM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_alarm);


        ImageButton ringtoneButton = (ImageButton) findViewById(R.id.ringtoneButton);
        final CheckBox vibrateSwitch = (CheckBox) findViewById(R.id.vibrateButton);
        repeatingSwitch = (CheckBox) findViewById(R.id.isRepeating);

        timeSet = (TextView) findViewById(R.id.time);

        everydayButton = (ToggleButton) findViewById(R.id.everydayButton);
        weekendsButton = (ToggleButton) findViewById(R.id.weekendsButton);
        weekdaysButton = (ToggleButton) findViewById(R.id.weekdaysButton);


        daysToggle = new ToggleButton[7];
        daysToggle[0] = (ToggleButton) findViewById(R.id.mondButton);;
        daysToggle[1] = (ToggleButton) findViewById(R.id.tuesButton);
        daysToggle[2] = (ToggleButton) findViewById(R.id.wedButton);
        daysToggle[3] = (ToggleButton) findViewById(R.id.thursButton);
        daysToggle[4] = (ToggleButton) findViewById(R.id.friButton);
        daysToggle[5] = (ToggleButton) findViewById(R.id.satButton);
        daysToggle[6] = (ToggleButton) findViewById(R.id.sunButton);

        cM = new CommonMethods(EditingAlarmActivity.this, everydayButton, weekendsButton, weekdaysButton, daysToggle, repeatingSwitch);

        cM.setToggleButtonTypeface();

        RealmResults<Alarm> alarms = Realm.getDefaultInstance().where(Alarm.class).findAllAsync();
        alarm = alarms.get(getIntent().getIntExtra("POS", 0));

        mcurrentTime = Calendar.getInstance();
        mcurrentTime.setTime(alarm.getAlarmTime());

        timeSet.setText(FinalVariables.timeAMPM.format(alarm.getAlarmTime()));
        alarmTime = alarm.getAlarmTime();

        findViewById(R.id.timePicker).setOnClickListener(cM.makeTimePickerDialog(mcurrentTime, timeSet, alarmTime));


        String alarmFrequency = alarm.getAlarmFrequency();
        if(!alarm.isRepeating()){
            repeatingSwitch.setChecked(false);
            cM.setEnabledRepeatButtons(false);
        } else{
            repeatingSwitch.setChecked(true);
            cM.setEnabledRepeatButtons(true);

        }

        boolean[] days = Computations.transformToBooleanArray(alarmFrequency.trim());
        for (int i = 0; i < days.length; i++) {
            daysToggle[i].setChecked(days[i]);
        }
        cM.checkOtherToggles();

        ringtonePos = alarm.getAlarmAudioPos();
        ringtoneText = alarm.getAlarmAudio();

        ringtoneButton.setOnClickListener(cM.createRingtoneDialog(ringtoneText, ringtonePos));

        vibrateSwitch.setChecked(alarm.isVibrate());

        repeatingSwitch.setOnCheckedChangeListener(cM.repeatingSwitchToggle());

        everydayButton.setOnClickListener(cM.everydayButtonOnClick());
        weekdaysButton.setOnClickListener(cM.weekdaysButtonOnClick());

        weekendsButton.setOnClickListener(cM.weekendsButtonOnClick());

//        findViewById(R.id.cancelButton).setOnClickListener(cM.cancellButtonOnClick());

        ActionBarMaker.makeActionBar(this, "Edit Alarm", cM.cancellButtonOnClick());

        ImageButton saveAlarm = (ImageButton)findViewById(R.id.saveAlarmButton);
        saveAlarm.setImageResource(R.mipmap.ic_done_white_36dp);
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cM.hasSelectedDate()){
                    String alarmDays = (new JSONArray(Arrays.asList(cM.days))).toString();

                    CreateObjects.editAlarm(alarm, EditingAlarmActivity.this, alarmDays, cM.alarmTime, cM.ringtoneText,
                            cM.ringtonePos, vibrateSwitch.isChecked(), repeatingSwitch.isChecked(), true);

                    Computations.makeAlarm(EditingAlarmActivity.this, alarm, Calendar.getInstance());

                    EditingAlarmActivity.this.startActivity(new Intent(EditingAlarmActivity.this, TabbedAlarm.class));
                    EditingAlarmActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    EditingAlarmActivity.this.finish();

                } else{
                    Toast.makeText(EditingAlarmActivity.this, "Please select a day to set the alarm", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    public void weekNamesClick(View view){
        cM.weekNamesClick(view);
    }


}
