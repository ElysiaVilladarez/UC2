package utot.utot.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utot.utot.alarm.CreatingAlarmActivity;
import utot.utot.triggeralarm.AlarmReceiver;

/**
 * Created by elysi on 12/15/2016.
 */

public class Computations {

    public static String translationToReadableText(boolean[] days) {
        String[] names = new String[7];
        names[0] = "M";
        names[1] = "T";
        names[2] = "W";
        names[3] = "Th";
        names[4] = "F";
        names[5] = "Sa";
        names[6] = "Su";
        String display = "";

        if (days[0] && days[1] && days[2] && days[3] && days[4] && days[5] && days[6]) {
            display += "Everyday ";
        } else if (days[0] && days[1] && days[2] && days[3] && days[4]) {
            display += "Weekdays ";
            if (days[5]) display += names[5] + " ";
            if (days[6]) display += names[6] + " ";
        } else if (days[5] && days[6]) {
            if (days[0]) display += names[0] + " ";
            if (days[1]) display += names[1] + " ";
            if (days[2]) display += names[2] + " ";
            if (days[3]) display += names[3] + " ";
            if (days[4]) display += names[4] + " ";
            display += "Weekends";
        } else {
            for (int i = 0; i < days.length; i++) {
                if (days[i]) display += names[i] + " ";
            }
        }


        return display;
    }

    public static boolean[] transformToBooleanArray(String alarmFrequency) {
        boolean[] days = new boolean[7];
        JSONArray arrayBool = null;
        try {
            arrayBool = new JSONArray(alarmFrequency.trim());
            if (arrayBool != null) {
                JSONArray lol = arrayBool.getJSONArray(0);
                for (int i = 0; i < lol.length(); i++) {
                    days[i] = lol.getBoolean(i);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static int getDayOfWeek(boolean[] days, Calendar now) {
        int dayOfWeek;
        int i;
        if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) i = 6;
        else i = 0;
        while (true) {
            if (days[i]) {
                if (i == 6) {
                    dayOfWeek = Calendar.SUNDAY;
                } else if (i == 5) {
                    dayOfWeek = Calendar.SATURDAY;
                } else {
                    dayOfWeek = i + 2;
                }
                break;
            }

            if (i == 6) i = 0;
            else i++;

        }

        return dayOfWeek;
    }

    public static boolean makeAlarm(Context context, String alarmDays, boolean[] days, Date alarmTime, int pk){
        int dayOfWeek;
        //boolean[] days = Computations.transformToBooleanArray(alarmDays);
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");

        Calendar now = Calendar.getInstance();

        if (alarmDays.trim().isEmpty()) {
            dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        } else {
            dayOfWeek = Computations.getDayOfWeek(days, now);
        }

        now.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Calendar timeA = Calendar.getInstance();
        timeA.setTime(alarmTime);
        now.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
        now.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));
        now.set(Calendar.SECOND, 0);

        Toast.makeText(context, now.getTime().toString() + "- " + dayOfWeek, Toast.LENGTH_LONG).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(CreatingAlarmActivity.ALARM_TIME_SET, fmt.format(alarmTime));
        myIntent.putExtra(CreatingAlarmActivity.ALARM_DATE_SET, alarmDays);
        myIntent.putExtra(CreatingAlarmActivity.ALARM_PRIMARY_KEY, pk);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                pk, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);

        return true;
    }
}
