package utot.utot.alarm;

import android.app.Activity;
import android.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.alarm.EditingAlarmActivity;
import utot.utot.customobjects.Alarm;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
 
    private RealmResults<Alarm> alarms;
	private static Activity act;
 	private Realm realm;

	public String translation(boolean[] days){
		String[] names = new String[7];
		names[0] = "M";
		names[1] = "T";
		names[2] = "W";
		names[3] = "Th";
		names[4] = "F";
		names[5] = "Sa";
		names[6] = "Su";
		String display="";
		
		if(days[0] && days[1] && days[2] && days[3] && days[4] && days[5] && days[6]){
			display+= "Everyday ";
		} else if(days[0] && days[1] && days[2] && days[3] && days[4]){
			display += "Weekdays ";
			if(days[5])display += names[5] + " ";
			if(days[6])display += names[6] + " ";
		} else if(days[5] && days[6]){
			if(days[0])display += names[0] + " ";
			if(days[1])display += names[1] + " ";
			if(days[2])display += names[2] + " ";
			if(days[3])display += names[3] + " ";
			if(days[4])display += names[4] + " ";
			display += "Weekends";
		} else{
			for(int i =0; i <days.length;i++){
				if(days[i]) display += names[i] + " ";
			}
		}
		
		
		return display;
	}
	
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView alarmTime, alarmFrequency;
		public SwitchButton alarmStatus;
		public PercentRelativeLayout mainwindow;
		public ImageButton deleteButton;
 
        public ViewHolder(View view) {
            super(view);
            alarmTime = (TextView) view.findViewById(R.id.alarmTime);
            alarmFrequency = (TextView) view.findViewById(R.id.alarmFrequency);
            alarmStatus = (SwitchButton) view.findViewById(R.id.alarmStatus);
			mainwindow = (PercentRelativeLayout) view.findViewById(R.id.mainwindow);
			deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);

			view.setOnClickListener(this);
        }
		
		public void onClick(View view){
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
		if(holder.alarmStatus.isChecked()){
			holder.mainwindow.setBackgroundColor(0x8C00ace6);
		} else{
			holder.mainwindow.setBackgroundColor(0x3300ace6);
		}
		holder.alarmStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean b) {
				if(b){
					holder.mainwindow.setBackgroundColor(0x8C00ace6);
				} else{
					holder.mainwindow.setBackgroundColor(0x3300ace6);
				}
				realm.beginTransaction();
				alarm.setIsOn(b);
				realm.commitTransaction();
			}
		});

		String freq = "";
		if(alarm.getAlarmFrequency().trim().isEmpty()){
			freq = "Once";
		} else{
			boolean[] days = new boolean[7];
            JSONArray arrayBool = null;
            try {
                arrayBool = new JSONArray(alarm.getAlarmFrequency());
                if (arrayBool != null) {
                    JSONArray lol = arrayBool.getJSONArray(0);
                    for (int i = 0; i < lol.length(); i++) {
                        days[i] = lol.getBoolean(i);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			freq = translation(days);
		}
		
		holder.alarmFrequency.setText(freq);

		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                new AlertDialog.Builder(act)
                        .setTitle("Deleting Alarm")
                        .setMessage("Do you really want to delete "+ holder.alarmTime.getText().toString() + " alarm?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                realm.beginTransaction();
                                alarm.deleteFromRealm();
                                realm.commitTransaction();

                                AlarmAdapter.this.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

			}
		});
    }
 
    @Override
    public int getItemCount() {
        return alarms.size();
    }
}