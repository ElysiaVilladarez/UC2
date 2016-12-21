package utot.utot.triggeralarm;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utot.utot.R;
import utot.utot.alarm.CreatingAlarmActivity;
import utot.utot.helpers.Computations;
import utot.utot.poem.ShowPoems;

public class TriggeredActivity extends AppCompatActivity {
    private MediaPlayer ringtone;
    private int pk;
    private String  alarmDays;
    private Date alarmDate;
    private Vibrator vibrate;
    private boolean isVibrate;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window wind = getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_triggered);

        isVibrate = getIntent().getBooleanExtra(CreatingAlarmActivity.ALARM_VIBRATE, false);
        if(isVibrate){
            vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 0, 250, 0 };
            vibrate.vibrate(pattern, 0);
        } else{
            vibrate = null;
        }

        System.out.println("CHECK: " + getIntent().getStringExtra(CreatingAlarmActivity.ALARM_RINGTONE));
        uri = Uri.parse(getIntent().getStringExtra(CreatingAlarmActivity.ALARM_RINGTONE));
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

        String alarmTime = getIntent().getStringExtra(CreatingAlarmActivity.ALARM_TIME_SET);
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat fmt2 = new SimpleDateFormat("hh:mm\na");
        alarmDate = null;
        try {
            alarmDate = fmt.parse(alarmTime);
            alarmTime = fmt2.format(alarmDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        alarmDays = getIntent().getStringExtra(CreatingAlarmActivity.ALARM_DATE_SET);
        pk = getIntent().getIntExtra(CreatingAlarmActivity.ALARM_PRIMARY_KEY, 0);
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
                if (getIntent().getBooleanExtra(CreatingAlarmActivity.ALARM_IS_REPEATING, false)){
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
}
