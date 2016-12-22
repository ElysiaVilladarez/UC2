package utot.utot.triggeralarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utot.utot.R;
import utot.utot.helpers.Computations;
import utot.utot.helpers.FinalVariables;
import utot.utot.poem.ShowPoems;

public class TriggeredActivity extends AppCompatActivity {
    private MediaPlayer ringtone;
    private int pk;
    private String  alarmDays, ringtoneText;
    private Date alarmDate;
    private Vibrator vibrate;
    private boolean isVibrate, isRepeating;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window wind = getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_triggered);

        alarmDays = getIntent().getStringExtra(FinalVariables.ALARM_DATE_SET);
        pk = getIntent().getIntExtra(FinalVariables.ALARM_PRIMARY_KEY, 0);
        isRepeating = getIntent().getBooleanExtra(FinalVariables.ALARM_IS_REPEATING, false);
        ringtoneText = getIntent().getStringExtra(FinalVariables.ALARM_RINGTONE);
        isVibrate = getIntent().getBooleanExtra(FinalVariables.ALARM_VIBRATE, false);

        if(isVibrate){
            vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 0, 250, 0 };
            vibrate.vibrate(pattern, 0);
        } else{
            vibrate = null;
        }

        uri = Uri.parse(ringtoneText);
        ringtone = new MediaPlayer();
        try {
            ringtone.setDataSource(this, uri);
            ringtone.setAudioStreamType(AudioManager.STREAM_RING);
            ringtone.setLooping(true);
            ringtone.prepare();
            ringtone.start();
        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        } catch (IllegalStateException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
//        ringtone = RingtoneManager.getRingtone(this, uri);
//        ringtone
//        ringtone.play();

        TextView time = (TextView)findViewById(R.id.time);

        String alarmTime = getIntent().getStringExtra(FinalVariables.ALARM_TIME_SET);

        alarmDate = null;
        try {
            alarmDate = FinalVariables.timeAMPM.parse(alarmTime);
            alarmTime = FinalVariables.triggeredFormat.format(alarmDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time.setText(alarmTime);

        findViewById(R.id.sleepButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibrate!=null){
                    vibrate.cancel();
                    vibrate=null;
                }
                if(ringtone!=null) {
                    ringtone.stop();
                    ringtone = null;
                }

                sleepFunction();
                TriggeredActivity.this.finish();

            }
        });
        findViewById(R.id.dismissButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibrate!=null){
                    vibrate.cancel();
                    vibrate=null;
                }
                if (ringtone != null) {
                    ringtone.stop();
                    ringtone = null;
                }
                if (isRepeating){
                    Calendar now = Calendar.getInstance();
                    Calendar timeA = Calendar.getInstance();
                    timeA.setTime(alarmDate);
                    timeA.set(Calendar.YEAR, now.get(Calendar.YEAR));
                    timeA.set(Calendar.MONTH, now.get(Calendar.MONTH));
                    timeA.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

                    Computations.makeAlarm(TriggeredActivity.this, alarmDays, now, alarmDate, pk, true, isVibrate, uri.toString());

                }

                TriggeredActivity.this.startActivity(new Intent(TriggeredActivity.this, ShowPoems.class));
                TriggeredActivity.this.finish();
            }
        });


    }

    public void sleepFunction(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(FinalVariables.ALARM_TIME_SET, FinalVariables.timeAMPM.format(alarmDate));
        myIntent.putExtra(FinalVariables.ALARM_DATE_SET, alarmDays);
        myIntent.putExtra(FinalVariables.ALARM_PRIMARY_KEY, pk);
        myIntent.putExtra(FinalVariables.ALARM_IS_REPEATING, isRepeating);
        myIntent.putExtra(FinalVariables.ALARM_VIBRATE, isVibrate);
        myIntent.putExtra(FinalVariables.ALARM_RINGTONE, ringtoneText);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                pk, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+5*60*1000, pendingIntent);
        Toast.makeText(this, "Snooze for 5 minutes", Toast.LENGTH_LONG).show();
    }
}
