package utot.utot.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.R;
import utot.utot.customobjects.Alarm;

public class AlarmFragment extends Fragment {

    private RecyclerView alarmList;
    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlarmFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        Realm.init(getActivity().getApplicationContext());
        rootView.findViewById(R.id.addAlarmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmFragment.this.getActivity().startActivity(new Intent(AlarmFragment.this.getActivity(), CreatingAlarmActivity.class));
                AlarmFragment.this.getActivity().finish();

            }
        });

        alarmList = (RecyclerView)rootView.findViewById(R.id.alarmList);
        TextView noAlarmText = (TextView) rootView.findViewById(R.id.noAlarmText);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Alarm> alarms = realm.where(Alarm.class).findAll();
        if(alarms.size()<=0){
            alarmList.setVisibility(View.GONE);
            noAlarmText.setVisibility(View.VISIBLE);
        } else{
            alarmList.setVisibility(View.VISIBLE);
            noAlarmText.setVisibility(View.GONE);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            alarmList.setLayoutManager(llm);

            AlarmAdapter alarmAdapter = new AlarmAdapter(alarms, getActivity());
            alarmList.setHasFixedSize(true);
            alarmList.setAdapter(alarmAdapter);
            alarmAdapter.notifyDataSetChanged();
        }

        return rootView;
    }

}

