package utot.utot.alarm;

import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import utot.utot.R;
import utot.utot.customobjects.Alarm;

public class CreatingAlarmActivity extends AppCompatActivity {
    private TextView timeSet;
    private Date alarmTime;
    private SimpleDateFormat fmt;
    private ToggleButton[] daysToggle;
    private ToggleButton everydayButton, weekendsButton, weekdaysButton;

    Calendar mcurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_alarm);
        fmt = new SimpleDateFormat("hh:mm a");

        mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        String amp;
        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM) {
            amp = "AM";
        } else {
            amp = "PM";
        }

        timeSet = (TextView) findViewById(R.id.time);
        String time = hour + ":" + minute + " " + amp;
        timeSet.setText(time);

        try {
            alarmTime = fmt.parse(time);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        findViewById(R.id.timePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog mTimePicker = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog timePicker, int selectedHour, int selectedMinute, int second) {
                                // eReminderTime.setText( ""selectedHour + ":" + selectedMinute);
                                String time = selectedHour + ":" + selectedMinute;
                                fmt = new SimpleDateFormat("HH:mm");
                                try {
                                    alarmTime = fmt.parse(time);
                                } catch (ParseException e) {

                                    e.printStackTrace();
                                }
                                SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm a");

                                timeSet.setText(fmt2.format(alarmTime));
                            }
                        },
                        hour,
                        minute,
                        false);
                mTimePicker.show(getFragmentManager(), "Timepickerdialog");
                mTimePicker.setTitle("Set Time");
            }
        });

        ImageButton ringtoneButton = (ImageButton) findViewById(R.id.ringtoneButton);
        final CheckBox vibrateSwitch = (CheckBox) findViewById(R.id.vibrateButton);
        final CheckBox repeatingSwitch = (CheckBox) findViewById(R.id.isRepeating);
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

        ringtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        repeatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                everydayButton.setEnabled(b);
                weekdaysButton.setEnabled(b);
                weekendsButton.setEnabled(b);
                for (int i = 0; i < daysToggle.length; i++) {
                    daysToggle[i].setEnabled(b);
                }
            }
        });

        everydayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < daysToggle.length; i++) {
                    daysToggle[i].setChecked(everydayButton.isChecked());
                }
                weekdaysButton.setChecked(everydayButton.isChecked());
                weekendsButton.setChecked(everydayButton.isChecked());

            }
        });
        weekdaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i <= 4; i++) {
                    daysToggle[i].setChecked(weekdaysButton.isChecked());
                }
                checkOtherToggles();

            }
        });

        weekendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysToggle[5].setChecked(weekendsButton.isChecked());
                daysToggle[6].setChecked(weekendsButton.isChecked());
                checkOtherToggles();

            }
        });

        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatingAlarmActivity.this.startActivity(new Intent(CreatingAlarmActivity.this, TabbedAlarm.class));
                CreatingAlarmActivity.this.finish();
            }
        });
        findViewById(R.id.confirmAlarmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<String> days = new ArrayList<>();
//                if(everydayButton.isChecked()){
//                    days.add("Everyday");
//                } else{
//                    if(weekdaysButton.isChecked()){
//                        days.add("Weekdays");
//                        for(int i = 5; i <= 6; i++){
//                            if(daysToggle[i].isChecked()) days.add(daysToggle[i].getTextOn().toString());
//                        }
//                    } else if(weekendsButton.isChecked()){
//                        days.add("Weekends");
//                        for(int i = 0; i <= 4; i++){
//                            if(daysToggle[i].isChecked()) days.add(daysToggle[i].getTextOn().toString());
//                        }
//                    } else{
//
//                    }
//
//
//                }
                String alarmDays ="";
                if(repeatingSwitch.isChecked()) {
                    boolean[] days = new boolean[7];
                    for (int i = 0; i < daysToggle.length; i++) {
                        days[i] = daysToggle[i].isChecked();
                    }

                    alarmDays = (new JSONArray(Arrays.asList(days))).toString();
                } else{
                    alarmDays = "";
                }

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Alarm alarm = realm.createObject(Alarm.class); // Create a new object
                alarm.setPrimaryKey(System.currentTimeMillis());
                alarm.setAlarmFrequency(alarmDays);
                alarm.setAlarmTime(alarmTime);
                alarm.setIsOn(true);
                alarm.setIsVibrate(vibrateSwitch.isChecked());
                alarm.setAlarmAudio("Normal Ringtone");
                realm.commitTransaction();

                CreatingAlarmActivity.this.startActivity(new Intent(CreatingAlarmActivity.this, TabbedAlarm.class));
                CreatingAlarmActivity.this.finish();

            }
        });

    }

    private void checkOtherToggles() {
        boolean everdayCheck = true;

        for (int i = 0; i < daysToggle.length; i++) {
            if (!daysToggle[i].isChecked()) {
                everdayCheck = false;
                break;
            }
        }
        if (everdayCheck) {
            everydayButton.setChecked(true);
            weekdaysButton.setChecked(true);
            weekendsButton.setChecked(true);
        } else {
            everydayButton.setChecked(false);
            boolean weekdayCheck = true;
            for (int i = 0; i <= 4; i++) {
                if (!daysToggle[i].isChecked()) {
                    weekdayCheck = false;
                    break;
                }
            }
            weekdaysButton.setChecked(weekdayCheck);

            if (daysToggle[5].isChecked() && daysToggle[6].isChecked())
                weekendsButton.setChecked(true);
            else
                weekendsButton.setChecked(false);

        }

    }

    public void weekNamesClick(View view){
        checkOtherToggles();
    }

}
