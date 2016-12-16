package utot.utot.triggeralarm;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utot.utot.R;
import utot.utot.alarm.CreatingAlarmActivity;
import utot.utot.helpers.Computations;

public class TriggeredActivity extends AppCompatActivity {
    private MediaPlayer ringtone;
    private int pk;
    private String  alarmDays;
    private Date alarmDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggered);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
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
                if(ringtone!=null) {
                    ringtone.stop();
                    ringtone = null;
                }

            }
        });
        findViewById(R.id.dismissButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ringtone!=null) {
                    ringtone.stop();
                    ringtone = null;
                }
                Computations.makeAlarm(TriggeredActivity.this, alarmDays, Computations.transformToBooleanArray(alarmDays),
                        alarmDate, pk);
            }
        });


    }
}