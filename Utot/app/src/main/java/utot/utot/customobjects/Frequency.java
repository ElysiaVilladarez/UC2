package utot.utot.customobjects;

import io.realm.RealmObject;

/**
 * Created by elysi on 12/9/2016.
 */
public class Frequency extends RealmObject {
    public int frequency; // days of the week. Calenday.MONDAY, etc.

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
