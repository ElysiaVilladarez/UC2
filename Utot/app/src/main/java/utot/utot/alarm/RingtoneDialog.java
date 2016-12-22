package utot.utot.alarm;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import utot.utot.R;

/**
 * Created by elysi on 12/13/2016.
 */

public class RingtoneDialog extends Dialog {

    private Context c;
    private RingtoneManager ringtoneManager;
    private Uri[] alarms;
    private Ringtone r;
    public static RadioGroup group;
    public static Button doneButton;
    public static String ringtoneName;
    public RingtoneDialog(Context context, String rName) {
        super(context);

        this.c = context;
        setContentView(R.layout.dialog_ringtone);
        setTitle("Ringtones");

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if( mDialogResult != null ){
//                    RadioButton rad = (RadioButton)findViewById(group.getCheckedRadioButtonId());
//                    mDialogResult.finish(rad.getText().toString());
//                }

                if(r!=null && r.isPlaying()){
                    r.stop();
                    r = null;
                }
                ringtoneManager.stopPreviousRingtone();
                dismiss();
            }
        });


        ringtoneManager = new RingtoneManager(context);
        group = (RadioGroup)findViewById(R.id.radioRingtone);
        alarms = getDefaultAlarms();


        final int alarmCount = alarms.length;
        for(int i=0; i <alarmCount; i++){
            RadioButton ringtone = new RadioButton(context);
            ringtone.setText(RingtoneManager.getRingtone(context, alarms[i]).getTitle(context));
            ringtone.setPadding(10,10,10,10);
            group.addView(ringtone);
        }

        this.ringtoneName = rName;
        if(ringtoneName == null || ringtoneName.trim().isEmpty()) {
            ringtoneName = alarms[0].toString();
            ((RadioButton)group.getChildAt(0)).setChecked(true);
        } else{
            for(int i =0; i <alarmCount; i++){
                if(alarms[i].toString().equals(ringtoneName)){
                    group.check(group.getChildAt(i).getId());
                    break;
                }
            }
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(r!=null && r.isPlaying()){
                    r.stop();
                    r = null;
                }
                View radioButton = group.findViewById(i);
                int index = group.indexOfChild(radioButton);
                r = RingtoneManager.getRingtone(c, alarms[index]);
                ringtoneName = alarms[index].toString();
                r.play();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(r!=null && r.isPlaying()){
            r.stop();
            r = null;
        }
        ringtoneManager.stopPreviousRingtone();

    }


    public Uri[] getDefaultAlarms(){
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneManager.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            return null;
        }
        Uri[] alarms = new Uri[alarmsCount];
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms[currentPosition] = ringtoneManager.getRingtoneUri(currentPosition);

        }
        alarmsCursor.close();
        return alarms;
    }
}
