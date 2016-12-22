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
}
