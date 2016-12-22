package utot.utot.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Alarm;
import utot.utot.helpers.Computations;
import utot.utot.triggeralarm.AlarmReceiver;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private RealmResults<Alarm> alarms;
    private static Activity act;
    private Realm realm;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView alarmTime, alarmFrequency;
        public SwitchButton alarmStatus;
        public PercentRelativeLayout mainwindow, timeStuff;
        public ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            alarmTime = (TextView) view.findViewById(R.id.alarmTime);
            alarmFrequency = (TextView) view.findViewById(R.id.alarmFrequency);
            alarmStatus = (SwitchButton) view.findViewById(R.id.alarmStatus);
            mainwindow = (PercentRelativeLayout) view.findViewById(R.id.mainwindow);
            timeStuff = (PercentRelativeLayout) view.findViewById(R.id.timeStuff);
            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);

            timeStuff.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent edits = new Intent(act, EditingAlarmActivity.class);
            edits.putExtra("POS", getAdapterPosition());
            act.startActivity(edits);
            act.finish();
        }
    }


    public AlarmAdapter(RealmResults<Alarm> alarms, Activity act) {
        this.alarms = alarms;
        this.act = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holderT, int position) {
        final ViewHolder holder = holderT;
        final Alarm alarm = alarms.get(position);
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
        realm = Realm.getDefaultInstance();
        holder.alarmTime.setText(fmt.format(alarm.getAlarmTime()));
        holder.alarmStatus.setChecked(alarm.isOn());
        if (holder.alarmStatus.isChecked()) {
            holder.mainwindow.setBackgroundColor(0x8C00ace6);
        } else {
            holder.mainwindow.setBackgroundColor(0x3300ace6);
        }
        holder.alarmStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    Computations.makeAlarm(act, alarm, Calendar.getInstance());
                    holder.mainwindow.setBackgroundColor(0x8C00ace6);
                } else {
                    Intent myIntent = new Intent(act.getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(act.getApplicationContext(), (int) alarm.getPrimaryKey(),
                            myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    pendingIntent.cancel();
                    holder.mainwindow.setBackgroundColor(0x3300ace6);
                }
                realm.beginTransaction();
                alarm.setIsOn(b);
                realm.commitTransaction();
            }
        });

        String freq = "";
        if (alarm.getAlarmFrequency().trim().isEmpty()) {
            freq = "Once";
        } else {
            freq = Computations.translationToReadableText(Computations.transformToBooleanArray(alarm.getAlarmFrequency().trim()));
        }

        holder.alarmFrequency.setText(freq);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(act)
                        .setTitle("Deleting Alarm")
                        .setMessage("Do you really want to delete " + holder.alarmTime.getText().toString() + " alarm?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent myIntent = new Intent(act.getApplicationContext(), AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(act.getApplicationContext(), (int) alarm.getPrimaryKey(),
                                        myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                pendingIntent.cancel();

                                realm.beginTransaction();
                                alarm.deleteFromRealm();
                                realm.commitTransaction();

                                AlarmAdapter.this.notifyDataSetChanged();


                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}