package utot.utot.customobjects;

import io.realm.RealmObject;

/**
 * Created by elysi on 12/28/2016.
 */

public class BrodcastDelete extends RealmObject {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
