package utot.utot.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.realm.Realm;
import io.realm.RealmResults;
import utot.utot.customobjects.Alarm;
import utot.utot.customobjects.BrodcastDelete;
import utot.utot.customobjects.Picture;
import utot.utot.customobjects.Poem;
import utot.utot.settings.Brodcast;

/**
 * Created by elysi on 12/22/2016.
 */

public class CreateObjects {

    public static Alarm createAlarm(final Context c, final String alarmDays, final Date alarmTime, final String ringtoneText, int ringtonePos,
                                    final boolean isVibrating, final boolean isRepeating, final boolean isOn){
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                alarm = bgRealm.createObject(Alarm.class);
//                alarm.setPrimaryKey((int) System.currentTimeMillis());
//                alarm.setAlarmFrequency(alarmDays);
//                alarm.setAlarmTime(alarmTime);
//                alarm.setAlarmAudio(ringtoneText);
//                alarm.setOn(true);
//                alarm.setVibrate(isVibrating);
//                alarm.setRepeating(isRepeating);
//
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(c, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Realm realm = Realm.getDefaultInstance();
        Alarm alarm;
        realm.beginTransaction();
        alarm = realm.createObject(Alarm.class);
        int count = ((int) realm.where(Alarm.class).count());
        int nextID;
        if(count>0)
            nextID = realm.where(Alarm.class).max("primaryKey").intValue() + 1;
        else nextID = 1;

        alarm.setPrimaryKey(nextID);
        alarm.setAlarmFrequency(alarmDays);
        alarm.setAlarmTime(alarmTime);
        alarm.setAlarmAudio(ringtoneText);
        alarm.setAlarmAudioPos(ringtonePos);
        alarm.setOn(true);
        alarm.setVibrate(isVibrating);
        alarm.setRepeating(isRepeating);
        alarm.setIsOn(isOn);
        realm.commitTransaction();
        Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();
        System.out.println("ChECK: Alarm Created=" + realm.where(Alarm.class).count());
        return alarm;

    }
    public static Alarm createAlarm(final Context c, final int pk, final String alarmDays, final Date alarmTime, final String ringtoneText, int ringtonePos,
                                    final boolean isVibrating, final boolean isRepeating, final boolean isOn){
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                alarm = bgRealm.createObject(Alarm.class);
//                alarm.setPrimaryKey((int) System.currentTimeMillis());
//                alarm.setAlarmFrequency(alarmDays);
//                alarm.setAlarmTime(alarmTime);
//                alarm.setAlarmAudio(ringtoneText);
//                alarm.setOn(true);
//                alarm.setVibrate(isVibrating);
//                alarm.setRepeating(isRepeating);
//
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(c, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Realm realm = Realm.getDefaultInstance();
        Alarm alarm;
        realm.beginTransaction();
        alarm = realm.createObject(Alarm.class);
        alarm.setPrimaryKey(pk);
        alarm.setAlarmFrequency(alarmDays);
        alarm.setAlarmTime(alarmTime);
        alarm.setAlarmAudio(ringtoneText);
        alarm.setAlarmAudioPos(ringtonePos);
        alarm.setOn(true);
        alarm.setVibrate(isVibrating);
        alarm.setRepeating(isRepeating);
        alarm.setIsOn(isOn);
        realm.commitTransaction();
//        Toast.makeText(c, "Alarm created successfully!", Toast.LENGTH_SHORT).show();

        if(isOn) Computations.makeAlarm(c,alarm, Calendar.getInstance());
        System.out.println("ChECK: Alarm Created=" + realm.where(Alarm.class).count());
        realm.close();
        return alarm;

    }
    public static Alarm editAlarm(Alarm editAlarm,final Context c, final String alarmDays, final Date alarmTime, final String ringtoneText,
                                  int ringtonePos, final boolean isVibrating, final boolean isRepeating, final boolean isOn){
        Alarm alarm = editAlarm;

//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                alarm.setPrimaryKey((int)System.currentTimeMillis());
//                alarm.setAlarmFrequency(alarmDays);
//                alarm.setAlarmTime(alarmTime);
//                alarm.setIsOn(true);
//                alarm.setIsVibrate(isVibrating);
//                alarm.setRepeating(isRepeating);
//                alarm.setAlarmAudio(ringtoneText);
//
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(c, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        alarm.setAlarmFrequency(alarmDays);
        alarm.setAlarmTime(alarmTime);
        alarm.setIsOn(true);
        alarm.setIsVibrate(isVibrating);
        alarm.setRepeating(isRepeating);
        alarm.setAlarmAudio(ringtoneText);
        alarm.setAlarmAudioPos(ringtonePos);
        alarm.setIsOn(isOn);
        realm.commitTransaction();
        Toast.makeText(c, "Alarm edited successfully!", Toast.LENGTH_SHORT).show();

        Computations.makeAlarm(c,alarm, Calendar.getInstance());
        realm.close();
        return alarm;
    }

    public static BrodcastDelete createBrodDelete(int pk){
        Realm realm = Realm.getDefaultInstance();
        BrodcastDelete delete;
        realm.beginTransaction();
        delete = realm.createObject(BrodcastDelete.class);
        delete.setId(pk);
        realm.commitTransaction();

        return delete;
    }
    public static Poem createPoem(int pk, String poemMessage, String bg, int status){
        Realm realm = Realm.getDefaultInstance();
        Poem poem;
        realm.beginTransaction();
        poem = realm.createObject(Poem.class);
        poem.setPrimaryKey(pk);
        poem.setPoem(poemMessage);
        poem.setStatus(status);
        Picture pic = null;
        if(!bg.trim().isEmpty()) {
            pic = realm.where(Picture.class).equalTo("resourceName", bg).findFirst();
        }
        poem.setPic(pic);
        realm.commitTransaction();

        return poem;
    }
    public static Poem createPoem(int pk, String poemMessage, String bg, Date dateAdded, int status){
        Realm realm = Realm.getDefaultInstance();
        Poem poem;
        realm.beginTransaction();
        poem = realm.createObject(Poem.class);
        poem.setPrimaryKey(pk);
        poem.setPoem(poemMessage);
        poem.setDateAdded(dateAdded);
        poem.setStatus(status);
        Picture pic = null;
        if(!bg.trim().isEmpty()) {
            pic = realm.where(Picture.class).equalTo("resourceName", bg).findFirst();
        }
        poem.setPic(pic);
        realm.commitTransaction();

        return poem;
    }

    public static Poem createBrodcast(int pk, String poemMessage, Date date){
        System.out.println("CHECK: creating brodcast="+ pk + ", "+poemMessage+", "+date);
        Realm realm = Realm.getDefaultInstance();
        Poem poem;
        Picture pic = getRandomPicture(realm);
        realm.beginTransaction();
        poem = realm.createObject(Poem.class);
        poem.setPrimaryKey(pk);
        poem.setPoem(poemMessage);
        poem.setStatus(FinalVariables.POEM_BRODCAST);
        poem.setDateAdded(date);
        poem.setPic(pic);
        pic.setIsUsed(true);
        realm.commitTransaction();

        return poem;
    }
    public static Picture getRandomPicture(Realm realm){
        RealmResults<Picture> picList = realm.where(Picture.class).equalTo("isUsed", false).findAll();
        int picCount = picList.size();
        int randomNumPic;
        if(picCount <=0){
            picList = realm.where(Picture.class).findAll();
            realm.beginTransaction();
            for(Picture p: picList){
                p.setIsUsed(false);
            }
            realm.commitTransaction();
            picCount = picList.size();

        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            randomNumPic = ThreadLocalRandom.current().nextInt(1, picCount)-1;
        } else{
            Random rand = new Random();
            randomNumPic = rand.nextInt(picCount);
        }


        Picture pic = picList.get(randomNumPic);

        return pic;
    }
    public static Poem getRandomPoem(){
        Realm realm = Realm.getDefaultInstance();
        int randomNum;
        RealmResults<Poem> poemList = realm.where(Poem.class).equalTo("status", FinalVariables.POEM_NOT_SHOWN).findAll();
        int count = poemList.size();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            randomNum = ThreadLocalRandom.current().nextInt(1, count)-1;
        } else{
            Random rand = new Random();
            randomNum = rand.nextInt(count);
        }
        Poem poem = poemList.get(randomNum);
        Picture pic = getRandomPicture(realm);

        realm.beginTransaction();
        poem.setPic(pic);
        pic.setIsUsed(true);
        realm.commitTransaction();

        return poem;
    }

    public static Poem getLocalPoems(int pk, String bg, Date dateAdded){
        Realm realm = Realm.getDefaultInstance();
        Poem poem;
        realm.beginTransaction();
        poem = realm.where(Poem.class).equalTo("primaryKey", pk).findFirst();
        poem.setStatus(FinalVariables.POEM_SAVE);
        Picture pic = null;
        if(!bg.trim().isEmpty()) {
            pic = realm.where(Picture.class).equalTo("resourceName", bg).findFirst();
            if(pic!=null) pic.setIsUsed(true);
        }
        poem.setPic(pic);
        poem.setDateAdded(dateAdded);
        realm.commitTransaction();

        return poem;
    }

    public static void setPoemDisplay(Activity act, TextView poemText, ImageView bg, Poem poem){

        String poemMes = "";
        //Spanned poemMes2;
        try {
            poemMes = URLDecoder.decode(poem.getPoem(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        if (Build.VERSION.SDK_INT >= 24) {
//            poemMes2 = Html.fromHtml(poem.getPoem(), Html.FROM_HTML_MODE_LEGACY); // for 24 api and more
//            System.out.println("CHECK: POEM MESS " + poemMes + " " + poem.getPoem());
//        } else {
//            poemMes2 = Html.fromHtml(poem.getPoem()); // or for older api
//        }
        poemText.setText(poemMes);
        int resourceID;
        try {
            resourceID = act.getResources().getIdentifier(poem.getPic().getResourceName(), "mipmap", act.getPackageName());
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
        Glide.with(act).load(resourceID).into(bg);


    }
}
