package binaryblue.shopbetav10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ServiceCaller extends BroadcastReceiver
{
    @Override
    public void onReceive(Context arg0, Intent arg1)
    {
        arg0.stopService(new Intent(arg0,CallRecordingService.class));
        Intent intent=new Intent(arg0, CallRecordingService.class);
        arg0.startService(intent);
    }
}
