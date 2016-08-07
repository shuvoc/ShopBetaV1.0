package binaryblue.shopbetav10;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallRecordingService extends Service
{
    final MediaRecorder recorder=new MediaRecorder();
    boolean recording=false;
    int i=0;
    String fname;
    String filepath;

    BroadcastReceiver CallRecorder=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context arg0, Intent intent)
        {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            i++;
            if(TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state))
            {
                //Toast.makeText(getApplicationContext(), state, Toast.LENGTH_LONG).show();
                //Toast.makeText(arg0, "Start CaLLED "+recording+fname, Toast.LENGTH_LONG).show();
                startRecording();
            }
            if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)&&recording==true)
            {
                //Toast.makeText(getApplicationContext(), state, Toast.LENGTH_LONG).show();
                //Toast.makeText(arg0, "STOP CaLLED :"+recording, Toast.LENGTH_LONG).show();
                stopRecording();
            }
            if(TelephonyManager.EXTRA_STATE_RINGING.equals(state))
            {
                fname=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                //Toast.makeText(getApplicationContext(), state+" : "+fname, Toast.LENGTH_LONG).show();
            }
        }
    };
    BroadcastReceiver OutGoingNumDetector=new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            // TODO Auto-generated method stub
            fname=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
    };
    @Override
    public void onCreate()
    {
        super.onCreate();
        //Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_LONG).show();

        IntentFilter RecFilter = new IntentFilter();
        RecFilter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(CallRecorder, RecFilter);
        IntentFilter OutGoingNumFilter=new IntentFilter();
        OutGoingNumFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(OutGoingNumDetector, OutGoingNumFilter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(CallRecorder);
        unregisterReceiver(OutGoingNumDetector);
        //Toast.makeText(getApplicationContext(), "Destroyed", Toast.LENGTH_SHORT).show();
    }
    public void startRecording()
    {
        if(recording==false)
        {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String file= Environment.getExternalStorageDirectory().toString();
            filepath= file+"/Shop_call_record";
            File dir= new File(filepath);
            dir.mkdirs();


            Date d = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String curTimeAndDate = df.format(d);

            filepath += "/"+fname+"_"+curTimeAndDate+".3gp";
            recorder.setOutputFile(filepath);

            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            recorder.start();
            recording=true;
        }
    }
    public void stopRecording()
    {
        if(recording==true)
        {
            recorder.stop();
            /*Push Notification on Call Record*/
            String title = "You have new call record",msg = "the path of new call record is"+filepath;
            Intent intentNotification = new Intent();
            try {
                ComponentName comp = new ComponentName("com.android.music", "com.android.music.MediaPlaybackActivity");
                intentNotification.setComponent(comp);
                intentNotification.setAction(android.content.Intent.ACTION_VIEW);
                File filename = new File(filepath);
                intentNotification.setDataAndType(Uri.fromFile(filename), "audio/*");
            } catch (Exception e) {
                e.printStackTrace();
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intentNotification,0);
            //Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(msg))
                    .setAutoCancel(true)
                    .setContentText(msg)
                    .setContentIntent(contentIntent)
                    .build();
            notificationManager.notify(0, notification);
            /* *****************
             * Notification end
             * *****************
             */
            recorder.reset();
            recorder.release();
            recording=false;
            broadcastIntent();
        }
    }
    public void broadcastIntent()
    {
        Intent intent = new Intent();
        intent.setAction("com.exampled.beta.CUSTOM_INTENT");
        sendBroadcast(intent);
        //Toast.makeText(getApplicationContext(), "BroadCaste", Toast.LENGTH_LONG).show();

    }
}
