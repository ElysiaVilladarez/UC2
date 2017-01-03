package utot.utot.alarm;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import utot.utot.R;
import utot.utot.customobjects.Alarm;
import utot.utot.helpers.ActionBarMaker;
import utot.utot.helpers.CommonMethods;
import utot.utot.helpers.Computations;
import utot.utot.helpers.CreateObjects;
import utot.utot.helpers.FinalVariables;

public class CreatingAlarmActivity extends AppCompatActivity {

    private CheckBox repeatingSwitch, vibrateSwitch;
    private TextView timeSet;
    private Date alarmTime;
    private String ringtoneText;
    private int ringtonePos;
    private ToggleButton[] daysToggle;
    private ToggleButton everydayButton, weekendsButton, weekdaysButton;
    private ImageView timeAMP;
    private Calendar mcurrentTime;

    private CommonMethods cM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_alarm);


        final ImageButton ringtoneButton = (ImageButton) findViewById(R.id.ringtoneButton);
        vibrateSwitch = (CheckBox) findViewById(R.id.vibrateButton);
        repeatingSwitch = (CheckBox) findViewById(R.id.isRepeating);
        everydayButton = (ToggleButton) findViewById(R.id.everydayButton);
        weekendsButton = (ToggleButton) findViewById(R.id.weekendsButton);
        weekdaysButton = (ToggleButton) findViewById(R.id.weekdaysButton);

        timeSet = (TextView) findViewById(R.id.time);
        timeAMP = (ImageView) findViewById(R.id.time_am_pm);

        daysToggle = new ToggleButton[7];
        daysToggle[0] = (ToggleButton) findViewById(R.id.mondButton);
        daysToggle[1] = (ToggleButton) findViewById(R.id.tuesButton);
        daysToggle[2] = (ToggleButton) findViewById(R.id.wedButton);
        daysToggle[3] = (ToggleButton) findViewById(R.id.thursButton);
        daysToggle[4] = (ToggleButton) findViewById(R.id.friButton);
        daysToggle[5] = (ToggleButton) findViewById(R.id.satButton);
        daysToggle[6] = (ToggleButton) findViewById(R.id.sunButton);

        cM = new CommonMethods(CreatingAlarmActivity.this,everydayButton, weekendsButton, weekdaysButton, daysToggle,
                repeatingSwitch, timeAMP);

        mcurrentTime = Calendar.getInstance();

        daysToggle[cM.setDayOfWeek(mcurrentTime)].setChecked(true);

        cM.setTimeAMP(mcurrentTime);

        String time = FinalVariables.timeAMPM.format(mcurrentTime.getTime());
        timeSet.setText(time.substring(0, 5));

        try {
            alarmTime = FinalVariables.timeAMPM.parse(time);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        findViewById(R.id.timePicker).setOnClickListener(cM.makeTimePickerDialog(mcurrentTime, timeSet, alarmTime));

        cM.setToggleButtonTypeface();

        ringtonePos = 0;

        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneManager.getCursor();
        if (alarmsCursor.getCount() != 0 && alarmsCursor.moveToFirst()) {
            ringtoneText = ringtoneManager.getRingtoneUri(alarmsCursor.getPosition()).toString();
        }

        ringtoneButton.setOnClickListener(cM.createRingtoneDialog(ringtoneText, ringtonePos));

        repeatingSwitch.setOnCheckedChangeListener(cM.repeatingSwitchToggle());

        everydayButton.setOnClickListener(cM.everydayButtonOnClick());
        weekdaysButton.setOnClickListener(cM.weekdaysButtonOnClick());

        weekendsButton.setOnClickListener(cM.weekendsButtonOnClick());

//        findViewById(R.id.cancelButton).setOnClickListener(cM.cancellButtonOnClick());
        ActionBarMaker.makeActionBar(this, "Create Alarm", cM.cancellButtonOnClick());

        ImageButton saveAlarm = (ImageButton)findViewById(R.id.saveAlarmButton);
        saveAlarm.setImageResource(R.mipmap.ic_alarm_on_white_36dp);
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cM.hasSelectedDate()){
                    String alarmDays = (new JSONArray(Arrays.asList(cM.days))).toString();

                    Alarm alarm = CreateObjects.createAlarm(CreatingAlarmActivity.this,alarmDays, cM.alarmTime, cM.ringtoneText,
                            cM.ringtonePos, vibrateSwitch.isChecked(), repeatingSwitch.isChecked(), true);

                    Computations.makeAlarm(CreatingAlarmActivity.this, alarm, Calendar.getInstance());
                    CreatingAlarmActivity.this.startActivity(new Intent(CreatingAlarmActivity.this, TabbedAlarm.class));
                    CreatingAlarmActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    CreatingAlarmActivity.this.finish();

                } else{
                    Toast.makeText(CreatingAlarmActivity.this, "Please select a day to set the alarm", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    public void weekNamesClick(View view) {
        cM.weekNamesClick(view);
    }


}
