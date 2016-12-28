package utot.utot.helpers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.customobjects.Alarm;
import utot.utot.triggeralarm.AlarmReceiver;

/**
 * Created by elysi on 12/15/2016.
 */

public class Computations {
    public static boolean isRepeating;

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

    public static String translationToReadableTextServer(String s) {
        boolean[] days = transformToBooleanArray(s);
        String[] names = new String[7];
        names[0] = "M";
        names[1] = "T";
        names[2] = "W";
        names[3] = "R";
        names[4] = "F";
        names[5] = "Sa";
        names[6] = "Su";
        String display = "";

        if (days[0] && days[1] && days[2] && days[3] && days[4] && days[5] && days[6]) {
            display += "Everyday ";
        } else if (days[0] && days[1] && days[2] && days[3] && days[4]) {
            if (days[5] || days[6]) {
                for (int i = 0; i < days.length; i++) {
                    if (days[i]) display += names[i] + " ";
                }
            } else{
                display += "Weekdays ";
            }
        } else if (days[5] && days[6]) {
            if (days[0] || days[1] || days[2] || days[3] || days[4]){
                for (int i = 0; i < days.length; i++) {
                    if (days[i]) display += names[i] + " ";
                }
            } else{
                display += "Weekends";
            }
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

    public static String transformToBooleanJSONArray(String sDays) {
        boolean[] days = new boolean[7];
        String[] splitDays = sDays.split(" ");
        if(splitDays.length>1) isRepeating = true;

        String[] names = new String[10];
        names[0] = "M";
        names[1] = "T";
        names[2] = "W";
        names[3] = "R";
        names[4] = "F";
        names[5] = "Sa";
        names[6] = "Su";
        names[7] = "Everyday";
        names[8] = "Weekends";
        names[9] = "Weekdays";

        boolean[] booleanDays = new boolean[7];

        for(int i=0; i < splitDays.length; i++){
            String d = splitDays[i].trim();

            for(int j =i; j < names.length; j++){
                if(d.equalsIgnoreCase(names[j])){
                    if(j==7){
                        isRepeating = true;
                        for(int c=0; c < booleanDays.length; c++){
                            booleanDays[c] = true;
                        }
                    }
                    if(j==8){
                        isRepeating = true;
                        booleanDays[5] = true;
                        booleanDays[6] = true;
                    }
                    if(j==9){
                        isRepeating = true;
                        booleanDays[0] = true;
                        booleanDays[1] = true;
                        booleanDays[2] = true;
                        booleanDays[3] = true;
                        booleanDays[4] = true;
                    }
                    if(j < 7){
                        booleanDays[j] = true;
                    }
                    break;
                }
            }
        }

        JSONArray alarmDays = new JSONArray(Arrays.asList(booleanDays));

        return alarmDays.toString();
    }

    public static void getDayOfWeek(boolean[] days, Calendar now, Calendar alarm) {
//        int dayOfWeek;
        int i;
        int nowDay = now.get(Calendar.DAY_OF_WEEK);
        if(alarm.getTimeInMillis() < now.getTimeInMillis()) {
//            if(nowDay == Calendar.SATURDAY) i =6;
//            else
//            else if (nowDay == Calendar.FRIDAY) i =5;
            now.add(Calendar.DAY_OF_YEAR, 1);
            i = nowDay-1;
        } else{
            if (nowDay == Calendar.SUNDAY) i = 6;
            else{
                i = nowDay - 2;
            }
        }

        int addDays = 0;
        while (true) {
            if (days[i]) {
//                if (i == 6) {
//                    dayOfWeek = Calendar.SUNDAY;
//                } else if (i == 5) {
//                    dayOfWeek = Calendar.SATURDAY;
//                } else {
//                    dayOfWeek = i + 2;
//                }
                break;
            } else{
                addDays++;
            }

            if (i == 6) {
                i = 0;
            }
            else i++;

        }
        now.add(Calendar.DAY_OF_MONTH, addDays);
    }

    public static boolean makeAlarm(Context context, Alarm alarm, Calendar now) {
        //boolean[] days = Computations.transformToBooleanArray(alarmDays);
        Calendar timeA = Calendar.getInstance();
        timeA.setTime(alarm.getAlarmTime());
        timeA.set(Calendar.YEAR, now.get(Calendar.YEAR));
        timeA.set(Calendar.MONTH, now.get(Calendar.MONTH));
        timeA.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

        getDayOfWeek(transformToBooleanArray(alarm.getAlarmFrequency()), now, timeA);
//        now.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        now.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
        now.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));
        now.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(FinalVariables.ALARM_TIME_SET, FinalVariables.timeAMPM.format(alarm.getAlarmTime()));
        myIntent.putExtra(FinalVariables.ALARM_DATE_SET, alarm.getAlarmFrequency());
        myIntent.putExtra(FinalVariables.ALARM_PRIMARY_KEY, alarm.getPrimaryKey());
        myIntent.putExtra(FinalVariables.ALARM_IS_REPEATING, alarm.isRepeating());
        myIntent.putExtra(FinalVariables.ALARM_VIBRATE, alarm.isVibrate());
        myIntent.putExtra(FinalVariables.ALARM_RINGTONE, alarm.getAlarmAudio());


        System.out.println("CHECK: ALARM DATE=" + now.getTime().toString() + " ALARM TIME=" + timeA.getTime());

        long alarmMilli = now.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getPrimaryKey(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmMilli, pendingIntent);

        return true;
    }
//    public static boolean makeAlarm(Context context, Alarm alarm, Calendar now, boolean fromServer) {
//        //boolean[] days = Computations.transformToBooleanArray(alarmDays);
//
//        Calendar timeA = Calendar.getInstance();
//        int nowDay = timeA.get(Calendar.DAY_OF_WEEK);
//        timeA.setTime(alarm.getAlarmTime());
//        timeA.set(Calendar.YEAR, now.get(Calendar.YEAR));
//        timeA.set(Calendar.MONTH, now.get(Calendar.MONTH));
//        timeA.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
//        now.set(Calendar.DAY_OF_WEEK, nowDay);
//        int dayOfWeek = getDayOfWeek(transformToBooleanArray(alarm.getAlarmFrequency()), now, timeA);
//        now.set(Calendar.DAY_OF_WEEK, dayOfWeek);
//        now.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
//        now.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));
//        now.set(Calendar.SECOND, 0);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        Intent myIntent = new Intent(context, AlarmReceiver.class);
//        myIntent.putExtra(FinalVariables.ALARM_TIME_SET, FinalVariables.timeAMPM.format(alarm.getAlarmTime()));
//        myIntent.putExtra(FinalVariables.ALARM_DATE_SET, alarm.getAlarmFrequency());
//        myIntent.putExtra(FinalVariables.ALARM_PRIMARY_KEY, alarm.getPrimaryKey());
//        myIntent.putExtra(FinalVariables.ALARM_IS_REPEATING, alarm.isRepeating());
//        myIntent.putExtra(FinalVariables.ALARM_VIBRATE, alarm.isVibrate());
//        myIntent.putExtra(FinalVariables.ALARM_RINGTONE, alarm.getAlarmAudio());
//
//
//        System.out.println("CHECK: ALARM DATE=" + now.getTime().toString() + " ALARM TIME=" + timeA.getTime());
//
//        long alarmMilli = timeA.getTimeInMillis();
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                alarm.getPrimaryKey(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmMilli, pendingIntent);
//
//        return true;
//    }
    public static boolean makeAlarm(Context context, String alarmDays, Calendar now, Date alarmTime, int pk,
                                    boolean isRepeating, boolean isVibrating, String ringtone){
        //boolean[] days = Computations.transformToBooleanArray(alarmDays);

        Calendar timeA = Calendar.getInstance();
        timeA.setTime(alarmTime);
        timeA.set(Calendar.YEAR, now.get(Calendar.YEAR));
        timeA.set(Calendar.MONTH, now.get(Calendar.MONTH));
        timeA.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

        getDayOfWeek(transformToBooleanArray(alarmDays),now,timeA);
//        now.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        now.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
        now.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));
        now.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(FinalVariables.ALARM_TIME_SET, FinalVariables.timeAMPM.format(alarmTime));
        myIntent.putExtra(FinalVariables.ALARM_DATE_SET, alarmDays);
        myIntent.putExtra(FinalVariables.ALARM_PRIMARY_KEY, pk);
        myIntent.putExtra(FinalVariables.ALARM_IS_REPEATING, isRepeating);
        myIntent.putExtra(FinalVariables.ALARM_VIBRATE, isVibrating);
        myIntent.putExtra(FinalVariables.ALARM_RINGTONE, ringtone);


        System.out.println("CHECK: ALARM DATE" + now.getTime().toString() + " ALARM TIME: "+ timeA.getTime());

        long alarmMilli = now.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                pk, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmMilli, pendingIntent);

//        long days = TimeUnit.MILLISECONDS.toDays(alarmMilli);
//        alarmMilli -= TimeUnit.DAYS.toMillis(days);
//        long hours = TimeUnit.MILLISECONDS.toHours(alarmMilli);
//        alarmMilli -= TimeUnit.HOURS.toMillis(hours);
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(alarmMilli);
//        alarmMilli -= TimeUnit.MINUTES.toMillis(minutes);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(alarmMilli);
//
//        StringBuilder sb = new StringBuilder(200);
//        sb.append("Alarm will ring in ");
//        if(days>0){
//            sb.append(days);
//            sb.append(" days, " );
//        }
//        if(hours>0){
//            sb.append(hours);
//            sb.append(" hours, " );
//        }
//        if(hours>0){
//            sb.append(minutes);
//            sb.append(" minutes, " );
//        }
//        if(seconds>0){
//            sb.append(seconds);
//            sb.append(" seconds" );
//        }
//        Toast.makeText(context, sb, Toast.LENGTH_LONG).show();
        return true;
    }

    public static void cancelAllAlarms(Activity act){
        RealmResults<Alarm> alarms = Realm.getDefaultInstance().where(Alarm.class).findAll();
        for(Alarm a: alarms){
            Intent myIntent = new Intent(act.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(act.getApplicationContext(), a.getPrimaryKey(),
                    myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent.cancel();
        }
    }

}
