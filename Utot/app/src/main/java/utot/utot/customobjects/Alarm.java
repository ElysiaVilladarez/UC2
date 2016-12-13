package utot.utot.customobjects;



import java.util.Date;
import io.realm.RealmObject;

/**
 * Created by elysi on 12/9/2016.
 */
public class Alarm extends RealmObject {



    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmFrequency() {
        return alarmFrequency;
    }

    public void setAlarmFrequency(String alarmFrequency) {
        this.alarmFrequency = alarmFrequency;
    }

    public String getAlarmAudio() {
        return alarmAudio;
    }

    public void setAlarmAudio(String alarmAudio) {
        this.alarmAudio = alarmAudio;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean on) {
        this.isOn = on;
    }


    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    private long primaryKey;
    private Date alarmTime;
    private String alarmFrequency;
    private String alarmAudio;
    private boolean isOn;
    private boolean isVibrate;

}