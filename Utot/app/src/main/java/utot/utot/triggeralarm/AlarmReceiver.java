package utot.utot.triggeralarm;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import utot.utot.alarm.CreatingAlarmActivity;

/**
 * Created by elysi on 12/15/2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            // Set the alarm here.
//        }


        Intent trigger = new Intent(context, TriggeredActivity.class);
        trigger.putExtra(CreatingAlarmActivity.ALARM_TIME_SET, intent.getStringExtra(CreatingAlarmActivity.ALARM_TIME_SET));
        trigger.putExtra(CreatingAlarmActivity.ALARM_DATE_SET, intent.getStringExtra(CreatingAlarmActivity.ALARM_DATE_SET));
        trigger.putExtra(CreatingAlarmActivity.ALARM_PRIMARY_KEY, intent.getIntExtra(CreatingAlarmActivity.ALARM_PRIMARY_KEY,0));
        trigger.putExtra(CreatingAlarmActivity.ALARM_IS_REPEATING, intent.getBooleanExtra(CreatingAlarmActivity.ALARM_IS_REPEATING, false));
        trigger.putExtra(CreatingAlarmActivity.ALARM_VIBRATE, intent.getBooleanExtra(CreatingAlarmActivity.ALARM_VIBRATE, false));
        trigger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(trigger);
    }
}
