package utot.utot.alarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import utot.utot.poem.SavedPoemsFragment;
import utot.utot.settings.SettingsFragment;

/**
 * Created by Visitor on 3/18/2017.
 */

public class TabbedAlarmAdapter extends FragmentPagerAdapter {

    public TabbedAlarmAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return SavedPoemsFragment.newInstance();
        } else if(position==1){
            return AlarmFragment.newInstance();
        } else{
            return SettingsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}