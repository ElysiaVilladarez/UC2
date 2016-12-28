package utot.utot.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import utot.utot.R;
import utot.utot.alarm.RingtoneDialog;
import utot.utot.alarm.TabbedAlarm;
import utot.utot.poem.PoemAdapter;

/**
 * Created by elysi on 12/22/2016.
 */

public class CommonMethods {

    private ToggleButton everydayButton, weekendsButton, weekdaysButton;
    private ToggleButton[] daysToggle;
    private CheckBox repeatingSwitch;
    private Activity act;

    public Date alarmTime;
    public String ringtoneText;
    public int ringtonePos;
    public boolean[] days;


    public CommonMethods(Activity act, ToggleButton everydayButton, ToggleButton weekendsButton, ToggleButton weekdaysButton,
                         ToggleButton[] daysToggle, CheckBox repeatingSwitch) {
        this.everydayButton = everydayButton;
        this.weekendsButton = weekendsButton;
        this.weekdaysButton = weekdaysButton;
        this.daysToggle = daysToggle;
        this.repeatingSwitch = repeatingSwitch;
        this.act = act;
    }

    public int getHour(Calendar mcurrentTime, TextView timeSet) {
        String time = timeSet.getText().toString();
        Date date = null;
        try {
            date = FinalVariables.timeAMPM.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mcurrentTime.setTime(date);
        return mcurrentTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(Calendar mcurrentTime, TextView timeSet) {
        String time = timeSet.getText().toString();
        Date date = null;
        try {
            date = FinalVariables.timeAMPM.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mcurrentTime.setTime(date);
        return mcurrentTime.get(Calendar.MINUTE);
    }

    public void checkOtherToggles() {
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

    public View.OnClickListener makeTimePickerDialog(final Calendar mcurrentTime, final TextView timeSet, Date aT) {
        alarmTime = aT;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog timePicker, int selectedHour, int selectedMinute, int second) {
                                String time = selectedHour + ":" + selectedMinute;
                                try {
                                    alarmTime = FinalVariables.hourOfDayMin.parse(time);
                                } catch (ParseException e) {

                                    e.printStackTrace();
                                }

                                timeSet.setText(FinalVariables.timeAMPM.format(alarmTime));
                            }
                        },
                        getHour(mcurrentTime, timeSet),
                        getMinute(mcurrentTime, timeSet),
                        false);
                mTimePicker.show(act.getFragmentManager(), "Timepickerdialog");
                mTimePicker.setTitle("Set Time");

            }
        };

    }

    public View.OnClickListener createRingtoneDialog(String rT, int rP){
        ringtoneText = rT;
        ringtonePos = rP;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RingtoneDialog dialog = new RingtoneDialog(act, ringtoneText, ringtonePos);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ringtoneText = RingtoneDialog.ringtoneName;
                        ringtonePos = RingtoneDialog.ringtonePos;
                    }
                });
                DialogSize.setSize(act, dialog);

            }
        };

    }

    public CompoundButton.OnCheckedChangeListener repeatingSwitchToggle(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setEnabledRepeatButtons(b);
                checkOtherToggles();
            }
        };
    }

    public View.OnClickListener everydayButtonOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < daysToggle.length; i++) {
                    daysToggle[i].setChecked(everydayButton.isChecked());
                }
                weekdaysButton.setChecked(everydayButton.isChecked());
                weekendsButton.setChecked(everydayButton.isChecked());

            }
        };
    }

    public View.OnClickListener weekdaysButtonOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i <= 4; i++) {
                    daysToggle[i].setChecked(weekdaysButton.isChecked());
                }
                checkOtherToggles();

            }
        };
    }

    public View.OnClickListener weekendsButtonOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysToggle[5].setChecked(weekendsButton.isChecked());
                daysToggle[6].setChecked(weekendsButton.isChecked());
                checkOtherToggles();

            }
        };
    }

    public View.OnClickListener cancellButtonOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, TabbedAlarm.class));
                act.finish();

            }
        };
    }


    public void setToggleButtonTypeface(){
        Typeface customFont = Typeface.createFromAsset(act.getAssets(), act.getResources().getString(R.string.toggle_butons_font));
        everydayButton.setTypeface(customFont);
        weekendsButton.setTypeface(customFont);
        weekdaysButton.setTypeface(customFont);
        for (int i = 0; i < daysToggle.length; i++) {
            daysToggle[i].setTypeface(customFont);
        }

    }

    public boolean hasSelectedDate(){
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

        this.days = days;
        return selectedDate;
    }

    public void weekNamesClick(View view){
        if(repeatingSwitch.isChecked()){
            checkOtherToggles();
        }
        else{
            for(ToggleButton otherdays: daysToggle){
                if(otherdays != view){
                    otherdays.setChecked(false);
                }
            }
        }
    }

    public void setEnabledRepeatButtons(boolean b){
        everydayButton.setEnabled(b);
        weekdaysButton.setEnabled(b);
        weekendsButton.setEnabled(b);
    }

    public int setDayOfWeek(Calendar alarm){
        int i;
        Calendar now = Calendar.getInstance();
        int nowDay = now.get(Calendar.DAY_OF_WEEK);
        if(alarm.getTimeInMillis() < now.getTimeInMillis()) {
            now.add(Calendar.DAY_OF_YEAR, 7);
            i = nowDay-1;
        } else{
            if (nowDay == Calendar.SUNDAY) i = 6;
            else if(nowDay == Calendar.MONDAY)
                i = 0;
            else{
                i = nowDay - 2;
            }
        }

        return i;
    }

}
