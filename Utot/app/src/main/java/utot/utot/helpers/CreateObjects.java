package utot.utot.helpers;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;

import io.realm.Realm;
import utot.utot.customobjects.Alarm;

/**
 * Created by elysi on 12/22/2016.
 */

public class CreateObjects {
    private static Alarm alarm;

    public static Alarm createAlarm(final Context c, final String alarmDays, final Date alarmTime, final String ringtoneText,
                                    final boolean isVibrating, final boolean isRepeating){
        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                alarm = bgRealm.createObject(Alarm.class);
//                alarm.setPrimaryKey((int) System.currentTimeMillis());
//                alarm.setAlarmFrequency(alarmDays);
//                alarm.setAlarmTime(alarmTime);
//                alarm.setAlarmAudio(ringtoneText);
//                alarm.setOn(true);
//                alarm.setVibrate(isVibrating);
//                alarm.setRepeating(isRepeating);
//
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(c, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        realm.beginTransaction();
        alarm = realm.createObject(Alarm.class); // Create a new object
        alarm.setPrimaryKey((int) System.currentTimeMillis());
        alarm.setAlarmFrequency(alarmDays);
        alarm.setAlarmTime(alarmTime);
        alarm.setAlarmAudio(ringtoneText);
        alarm.setOn(true);
        alarm.setVibrate(isVibrating);
        alarm.setRepeating(isRepeating);
        realm.commitTransaction();
        Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();

        return alarm;

    }

    public static Alarm editAlarm(Alarm editAlarm,final Context c, final String alarmDays, final Date alarmTime, final String ringtoneText,
                                  final boolean isVibrating, final boolean isRepeating){
        alarm = editAlarm;
        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                alarm.setPrimaryKey((int)System.currentTimeMillis());
//                alarm.setAlarmFrequency(alarmDays);
//                alarm.setAlarmTime(alarmTime);
//                alarm.setIsOn(true);
//                alarm.setIsVibrate(isVibrating);
//                alarm.setRepeating(isRepeating);
//                alarm.setAlarmAudio(ringtoneText);
//
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(c, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        realm.beginTransaction();
        alarm.setAlarmFrequency(alarmDays);
        alarm.setAlarmTime(alarmTime);
        alarm.setIsOn(true);
        alarm.setIsVibrate(isVibrating);
        alarm.setRepeating(isRepeating);
        alarm.setAlarmAudio(ringtoneText);
        realm.commitTransaction();
        Toast.makeText(c, "Alarm edited successfully!", Toast.LENGTH_SHORT).show();

        return alarm;
    }
}
