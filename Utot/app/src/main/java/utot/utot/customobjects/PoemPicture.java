package utot.utot.customobjects;

import io.realm.RealmObject;

/**
 * Created by elysi on 12/24/2016.
 */

public class PoemPicture extends RealmObject {
    private int primaryKey;
    private String filename;
    private int status;

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename() {
        this.filename = Integer.toString(primaryKey) + ".jpg";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
