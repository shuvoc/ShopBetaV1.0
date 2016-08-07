package binaryblue.shopbetav10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jobayed Ullah on 7/23/2016.
 *
 * This class is used to keep the loaded data into variables
 *
 */
public class ProjectDB {
    private static ProjectDB ourInstance = new ProjectDB();
    private HashMap<String,String> contactList;

    public HashMap<String, String> getContactList() {
        return contactList;
    }

    public void setContactList(HashMap<String, String> contactList) {
        this.contactList = contactList;
    }

    public static ProjectDB getInstance() {
        return ourInstance;
    }

    private ProjectDB() {

    }

    public HashMap<String,String> getContactListUpdated(Context context)
    {
        String id;
        String name;
        String contactNumber;
        //Uri contactImage;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        HashMap<String,String> hMap = new HashMap<String,String>();

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {

                id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    contactNumber = getPhoneNumber(id,context);
                    //contactImage  = getPhotoURIFromId(id);

                    hMap.put(contactNumber,name);
                }
                //else l.add(new Contact(name,id));

            }
        }
        return  hMap;
    }

    private String getPhoneNumber(String id,Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();
        String contactNumber = "";

        Cursor cursorPhone = null;
        try {
            cursorPhone = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{id}, null
            );
            while (cursorPhone.moveToNext())
            {
                contactNumber  = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursorPhone.close();
        }
        if(contactNumber.length()>11)return contactNumber.substring(3,contactNumber.length());
        else return contactNumber;

    }


    public ArrayList<Contact> getCallDetails(Context context)
    {

        ArrayList<Contact> list = new ArrayList<Contact>();
        StringBuffer sb = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return list;
        }
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext())
        {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            int callDuration = Integer.parseInt(managedCursor.getString(duration));
            String dir = null; int dircode = Integer.parseInt(callType);
            switch (dircode)
            {
                case CallLog.Calls.OUTGOING_TYPE: dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE: dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE: dir = "MISSED";
                    break;
                default:
                    break;
            }


            if(phNumber.length()>11)phNumber = phNumber.substring(3,phNumber.length());
            if(contactList.get(phNumber)==null  )
                list.add(new Contact("Unknown",phNumber,new Calls(dir,callDuration/60+":"+callDuration%60,callDayTime)));
            else list.add(new Contact(contactList.get(phNumber),phNumber,new Calls(dir,callDuration/60+":"+callDuration%60,callDayTime)));

        }
        managedCursor.close();
        return list;
    }




    public ArrayList<Contact> iniList()
    {
        ArrayList<Contact> list = new ArrayList<Contact>();

        for(int i=0;i<=10;i++)
        {
            //list.add(new Contact(""+i+'A',i+1+""));
        }
        return list;
    }


}

class Calls
{
    private String callType;
    private String callDuration;
    private Date callDate;



    public Calls(String callType, String callDuration, Date callDate)
    {
        this.callType = callType;
        this.callDate = callDate;
        this.callDuration = callDuration;
    }




    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}

class Contact implements Comparable<Contact>
{
    private String contactName;
    private String contactNumber;
    private Calls  contactCalls;



    public Contact(String contactName,String contactNumber,Calls contactCalls)
    {
        super();
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactCalls = contactCalls;
    }

    /*
     *Sort on contactName
     */
    public int compareTo(Contact c)
    {
        return c.contactCalls.getCallDate().compareTo(contactCalls.getCallDate());
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Calls getContactCalls() {
        return contactCalls;
    }

    public void setContactCalls(Calls contactCalls) {
        this.contactCalls = contactCalls;
    }
}
