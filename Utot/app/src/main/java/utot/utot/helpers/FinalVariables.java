package utot.utot.helpers;

import java.text.SimpleDateFormat;

/**
 * Created by elysi on 12/22/2016.
 */

public class FinalVariables {
    public static final String ALARM_TIME_SET = "ATS";
    public static final String ALARM_DATE_SET = "ADS";
    public static final String ALARM_PRIMARY_KEY = "PK";
    public static final String ALARM_IS_REPEATING = "AIS";
    public static final String ALARM_VIBRATE = "AV";
    public static final String ALARM_RINGTONE = "AR";

    public final static String FOLDER_NAME = "/UtotCatalog";
    public final static int POEM_SHARE = 2;
    public final static int POEM_DISCARD = 1;
    public final static int POEM_SAVE = 0;
    public final static int POEM_NOT_SHOWN = 3;
    public final static int POEM_BRODCAST = 4;

    public final static String ACTION_DONE = "AD";


    public final static SimpleDateFormat timeAMPM = new SimpleDateFormat("hh:mm a");
    public final static SimpleDateFormat hourOfDayMin = new SimpleDateFormat("HH:mm");
    public final static SimpleDateFormat triggeredFormat = new SimpleDateFormat("hh:mm\na");

    public final static SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    public final static SimpleDateFormat serverDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final static SimpleDateFormat brodcastDateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm");


    public static final String PREFS_NAME = "CheckingVersion";
    public final static String PREF_VERSION_CODE_KEY = "version_code";
    public final static int DOESNT_EXIST = -1;
    public final static String SLEEP_COUNT = "SC";

    public final static int pictureCount = 21;

    public final static String LOGGED_IN = "LI";

    public final static int REQUEST_WRITE_STORAGE = 1;

    public final static String FB_ID = "idFacebook";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String EMAIL = "email";


}
