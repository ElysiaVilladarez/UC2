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

import java.util.ArrayList;

import utot.utot.R;

/**
 * Created by elysi on 12/13/2016.
 */

public class RingtoneDialog extends Dialog {

    private Context c;
    private RingtoneManager ringtoneManager;
    private ArrayList<Uri> alarms;
    private Ringtone r;
    public static RadioGroup group;
    public static Button doneButton;
    public static String ringtoneName;

    public static int ringtonePos;

    public RingtoneDialog(Context context, String rName, int rPos) {
        super(context);

        this.c = context;
        setContentView(R.layout.dialog_ringtone);

        ringtoneManager = new RingtoneManager(context);

        alarms = new ArrayList<>();

        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_01));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_02));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_03));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_04));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_05));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_06));
        alarms.add(Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.fart_07));

        getDefaultAlarms();

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

                int index = group.indexOfChild(findViewById(group.getCheckedRadioButtonId()));
                ringtoneName = alarms.get(index).toString();
                ringtonePos = index;

                dismiss();
            }
        });


        group = (RadioGroup)findViewById(R.id.radioRingtone);


        final int alarmCount = alarms.size();
        for(int i=0; i <7; i++){
            RadioButton ringtone = new RadioButton(context);
            ringtone.setText("Fart " + Integer.toString(i));
            ringtone.setPadding(10,10,10,10);
            group.addView(ringtone);
        }
        for(int i=7; i <alarmCount; i++){
            RadioButton ringtone = new RadioButton(context);
            ringtone.setText(RingtoneManager.getRingtone(context, alarms.get(i)).getTitle(context));
            ringtone.setPadding(10,10,10,10);
            group.addView(ringtone);
        }

        ringtonePos = rPos;
        ringtoneName = rName;
        group.check(group.getChildAt(ringtonePos).getId());
//        if(ringtoneName == null || ringtoneName.trim().isEmpty()) {
//            ringtoneName = alarms[0].toString();
//            ((RadioButton)group.getChildAt(0)).setChecked(true);
//        } else{
//            for(int i =0; i <alarmCount; i++){
//                if(alarms[i].toString().equals(ringtoneName)){
//                    group.check(group.getChildAt(i).getId());
//                    break;
//                }
//            }
//        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(r!=null && r.isPlaying()){
                    r.stop();
                    r = null;
                }
                View radioButton = group.findViewById(i);
                int index = group.indexOfChild(radioButton);
                Uri alarm = alarms.get(index);
                r = RingtoneManager.getRingtone(c, alarm);
                ringtoneName = alarm.toString();
                ringtonePos = index;
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


    public void getDefaultAlarms(){
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneManager.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {

        }
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms.add(ringtoneManager.getRingtoneUri(currentPosition));

        }
        alarmsCursor.close();
    }
}
