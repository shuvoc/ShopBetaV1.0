package binaryblue.shopbetav10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context arg0, Intent arg1)
    {
        arg0.stopService(new Intent(arg0, CallRecordingService.class));
        Intent newIntent=new Intent(arg0, CallRecordingService.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        arg0.startService(newIntent);
    }
}
