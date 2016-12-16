package utot.utot.triggeralarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utot.utot.alarm.CreatingAlarmActivity;
import utot.utot.helpers.Computations;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "utot.utot.triggeralarm.action.FOO";
    private static final String ACTION_BAZ = "utot.utot.triggeralarm.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "utot.utot.triggeralarm.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "utot.utot.triggeralarm.extra.PARAM2";

    public AlarmService() {
        super("AlarmService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");

        Date alarmTime = null;
        try {
            alarmTime = fmt.parse(intent.getStringExtra("ALARM TIME"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dayOfWeek = -1;
        boolean[] days = Computations.transformToBooleanArray(intent.getStringExtra("ALARM DATE"));
        Calendar now = Calendar.getInstance();
        for(int i = now.get(Calendar.DAY_OF_WEEK); i < days.length; i++){
            if(days[i]){
                if(i==0){
                    dayOfWeek = 0;
                } else{
                    dayOfWeek = i+1;
                }
                break;
            }

            if(i==6) i =0;
        }

        if(dayOfWeek!=-1) now.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Calendar timeA = Calendar.getInstance();
        timeA.setTime(alarmTime);
        now.set(Calendar.MINUTE, timeA.get(Calendar.MINUTE));
        now.set(Calendar.HOUR_OF_DAY, timeA.get(Calendar.HOUR_OF_DAY));

        Toast.makeText(this, now.getTime().toString(), Toast.LENGTH_LONG).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(CreatingAlarmActivity.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                intent.getIntExtra("PK", 0), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);
    }


}
