package utot.utot.triggeralarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import utot.utot.helpers.FinalVariables;

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
        trigger.putExtra(FinalVariables.ALARM_TIME_SET, intent.getStringExtra(FinalVariables.ALARM_TIME_SET));
        trigger.putExtra(FinalVariables.ALARM_DATE_SET, intent.getStringExtra(FinalVariables.ALARM_DATE_SET));
        trigger.putExtra(FinalVariables.ALARM_PRIMARY_KEY, intent.getIntExtra(FinalVariables.ALARM_PRIMARY_KEY,0));
        trigger.putExtra(FinalVariables.ALARM_IS_REPEATING, intent.getBooleanExtra(FinalVariables.ALARM_IS_REPEATING, false));
        trigger.putExtra(FinalVariables.ALARM_VIBRATE, intent.getBooleanExtra(FinalVariables.ALARM_VIBRATE, false));
        trigger.putExtra(FinalVariables.ALARM_RINGTONE, intent.getStringExtra(FinalVariables.ALARM_RINGTONE));
        trigger.putExtra(FinalVariables.SLEEP_COUNT, intent.getIntExtra(FinalVariables.SLEEP_COUNT, 0));

        trigger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        trigger.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(trigger);
    }
}
