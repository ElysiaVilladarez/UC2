package utot.utot.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
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
import io.realm.RealmResults;
import io.realm.Sort;
import utot.utot.R;
import utot.utot.customobjects.Alarm;
import utot.utot.helpers.Computations;
import utot.utot.helpers.DialogSize;
import utot.utot.triggeralarm.AlarmReceiver;
import utot.utot.triggeralarm.AlarmService;
import utot.utot.triggeralarm.TriggeredActivity;

public class CreatingAlarmActivity extends AppCompatActivity {
    public static final String ALARM_TIME_SET = "ATS";
    public static final String ALARM_DATE_SET = "ADS";
    public static final String ALARM_PRIMARY_KEY = "PK";
    public static final String ALARM_IS_REPEATING = "AIS";
    public static final String ALARM_VIBRATE = "AV";

    private CheckBox repeatingSwitch, vibrateSwitch;
    private TextView timeSet;
    private Date alarmTime;
    private SimpleDateFormat fmt;
    private String ringtoneText;
    private ToggleButton[] daysToggle;
    private ToggleButton everydayButton, weekendsButton, weekdaysButton;
    private Realm realm;
    Calendar mcurrentTime;
    private int pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_alarm);


        realm = Realm.getDefaultInstance();
        fmt = new SimpleDateFormat("hh:mm a");
        ringtoneText = "";

        mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR);
        final int hour24 = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        String amp;
        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM) {
            amp = "AM";
        } else {
            amp = "PM";
        }

        timeSet = (TextView) findViewById(R.id.time);
        String time = fmt.format(mcurrentTime.getTime());
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
                        hour24,
                        minute,
                        false);
                mTimePicker.show(getFragmentManager(), "Timepickerdialog");
                mTimePicker.setTitle("Set Time");
            }
        });

        Typeface customFont = Typeface.createFromAsset(this.getAssets(), getResources().getString(R.string.toggle_butons_font));


        ImageButton ringtoneButton = (ImageButton) findViewById(R.id.ringtoneButton);
        vibrateSwitch = (CheckBox) findViewById(R.id.vibrateButton);
        repeatingSwitch = (CheckBox) findViewById(R.id.isRepeating);
        everydayButton = (ToggleButton) findViewById(R.id.everydayButton);
        weekendsButton = (ToggleButton) findViewById(R.id.weekendsButton);
        weekdaysButton = (ToggleButton) findViewById(R.id.weekdaysButton);


        daysToggle = new ToggleButton[7];
        daysToggle[0] = (ToggleButton) findViewById(R.id.mondButton);
        daysToggle[1] = (ToggleButton) findViewById(R.id.tuesButton);
        daysToggle[2] = (ToggleButton) findViewById(R.id.wedButton);
        daysToggle[3] = (ToggleButton) findViewById(R.id.thursButton);
        daysToggle[4] = (ToggleButton) findViewById(R.id.friButton);
        daysToggle[5] = (ToggleButton) findViewById(R.id.satButton);
        daysToggle[6] = (ToggleButton) findViewById(R.id.sunButton);

        everydayButton.setTypeface(customFont);
        weekendsButton.setTypeface(customFont);
        weekdaysButton.setTypeface(customFont);
        for (int i = 0; i < daysToggle.length; i++) {
            daysToggle[i].setTypeface(customFont);
        }


        ringtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RingtoneDialog dialog = new RingtoneDialog(CreatingAlarmActivity.this);
                dialog.show();
                dialog.setDialogResult(new RingtoneDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        ringtoneText = result;
                    }
                });

                DialogSize.setSize(CreatingAlarmActivity.this, dialog);
            }
        });

        repeatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                everydayButton.setEnabled(b);
                weekdaysButton.setEnabled(b);
                weekendsButton.setEnabled(b);
                checkOtherToggles();
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
                String alarmDays = "";
                int dayOfWeek = -1;
                boolean[] days = new boolean[7];

                for (int i = 0; i < daysToggle.length; i++) {
                    days[i] = daysToggle[i].isChecked();
                }
                boolean selectedDate = false;
                for (int i = 0; i < days.length; i++) {
                    if (days[i]) {
                        selectedDate = true;
                        break;
                    }

                }

                if(selectedDate){
                    alarmDays = (new JSONArray(Arrays.asList(days))).toString();

                    Alarm alarm;
                    realm.beginTransaction();
                    alarm = realm.createObject(Alarm.class); // Create a new object
                    pk = (int) System.currentTimeMillis();
                    alarm.setPrimaryKey(pk);
                    alarm.setAlarmFrequency(alarmDays);
                    alarm.setAlarmTime(alarmTime);
                    alarm.setAlarmAudio(ringtoneText);
                    alarm.setOn(true);
                    alarm.setVibrate(vibrateSwitch.isChecked());
                    alarm.setRepeating(repeatingSwitch.isChecked());
                    alarm.setAlarmAudio("Normal Ringtone");
                    realm.commitTransaction();

                    //  new CreatingAlarmAsync().execute(alarmDays);
                    Calendar now = Calendar.getInstance();

                    Computations.makeAlarm(CreatingAlarmActivity.this, alarmDays, now, alarmTime, pk,
                            repeatingSwitch.isChecked(), vibrateSwitch.isChecked());
                    CreatingAlarmActivity.this.startActivity(new Intent(CreatingAlarmActivity.this, TabbedAlarm.class));
                    CreatingAlarmActivity.this.finish();
                } else{
                    Toast.makeText(CreatingAlarmActivity.this, "Please select a day to set the alarm", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

////    class CreatingAlarmAsync extends AsyncTask<String, Void, Date>{
////
////
////        @Override
////        protected Date doInBackground(String... a) {
////            int dayOfWeek = -1;
////            boolean[] days = Computations.transformToBooleanArray(a[0]);
////            for(int i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); i < days.length; i++){
////                if(days[i]){
////                    if(i==0){
////                        dayOfWeek = 0;
////                    } else{
////                        dayOfWeek = i+1;
////                    }
////                    break;
////                }
////
////                if(i==6) i =0;
////            }
////
////            mcurrentTime = Calendar.getInstance();
////            if(dayOfWeek!=-1) mcurrentTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
////            Calendar timeA = Calendar.getInstance();
////            timeA.setTime(alarmTime);
////            mcurrentTime.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
////            mcurrentTime.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));
////
////
////            AlarmManager alarmManager = (AlarmManager) CreatingAlarmActivity.this.
////                    getSystemService(CreatingAlarmActivity.ALARM_SERVICE);
////            Intent myIntent = new Intent(CreatingAlarmActivity.this.getApplicationContext(), AlarmReceiver.class);
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(CreatingAlarmActivity.this.getApplicationContext(),
////                    pk, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
////            alarmManager.set(AlarmManager.RTC_WAKEUP, mcurrentTime.getTimeInMillis(), pendingIntent);
////
////            return mcurrentTime.getTime();
////        }
//
//        protected void onPostExecute(Date result){
//            Toast.makeText(CreatingAlarmActivity.this, result .toString(), Toast.LENGTH_LONG).show();
//
//        }
//    }

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

    public void weekNamesClick(View view) {
        if (repeatingSwitch.isChecked()) {
            checkOtherToggles();
        } else {
            for (ToggleButton otherdays : daysToggle) {
                if (otherdays != view) {
                    otherdays.setChecked(false);
                }
            }
        }
    }

}
