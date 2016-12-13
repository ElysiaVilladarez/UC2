package utot.utot.alarm;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import utot.utot.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by elysi on 12/13/2016.
 */

public class RingtoneDialog extends Dialog {

    private Context context;
    private RingtoneManager ringtoneManager;
    private Ringtone[] alarms;
    private Ringtone r;
    public static RadioGroup group;
    public static Button doneButton;

    public RingtoneDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_ringtone);
        setTitle("Ringtones");

        doneButton = (Button) findViewById(R.id.doneButton);
        ringtoneManager = new RingtoneManager(context);
        group = (RadioGroup)findViewById(R.id.radioRingtone);
        alarms = getDefaultAlarms();
        final int alarmCount = alarms.length;
        for(int i=0; i <alarmCount; i++){
            RadioButton ringtone = new RadioButton(context);
            ringtone.setText(alarms[i].getTitle(context));
            group.addView(ringtone);
        }
        ((RadioButton)group.getChildAt(0)).setChecked(true);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ringtoneManager.stopPreviousRingtone();
                r = alarms[i];
                r.play();
            }
        });

    }

    public static void setRadioChecked(String title){
        for(int i=0; i <group.getChildCount(); i++){
            RadioButton rad = (RadioButton) group.getChildAt(i);
            if(rad.getText().toString().trim().equals(title)){
                rad.setChecked(true);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        r.stop();
    }

    public Ringtone[] getDefaultAlarms(){
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneManager.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            return null;
        }
        Ringtone[] alarms = new Ringtone[alarmsCount];
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms[currentPosition] = ringtoneManager.getRingtone(getApplicationContext(),
                    ringtoneManager.getRingtoneUri(currentPosition));

        }
        alarmsCursor.close();
        return alarms;
    }
}
