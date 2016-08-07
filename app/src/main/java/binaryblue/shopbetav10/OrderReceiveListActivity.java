package binaryblue.shopbetav10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class OrderReceiveListActivity extends AppCompatActivity
{
    ProjectDB pdb = ProjectDB.getInstance();
    ListView lvContacts;
    ArrayList<Contact> list;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Set Title from parent Activity
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        setTitle(title);


        setContentView(R.layout.activity_order_receive_list);
        if(title.equals("Call List"))
        {
            //ListView Setup

            lvContacts = (ListView) findViewById(R.id.list);
            list = pdb.getCallDetails(this);
            ArrayList<Contact> testAL=new ArrayList<Contact>();
            Collections.sort(list);
            for (int i=0;i<4;i++)
            {
                testAL.add(i,list.get(i));
            }
            //contactListAdapter = new ContactListAdapter(lvContacts.getContext(), list);
            contactListAdapter = new ContactListAdapter(lvContacts.getContext(), testAL);

            lvContacts.setAdapter(contactListAdapter);
            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.lv_animation), 0.5f);
            //0.5f == time between appearance of listview items.
            lvContacts.setLayoutAnimation(lac);
            //lvContacts.setOnItemClickListener(itemClickListener);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_order_rcv_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId())
        {
            case R.id.call_history:
                contactListAdapter.updateResults(list);
                LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.lv_animation), 0.5f);
                //0.5f == time between appearance of listview items.
                lvContacts.setLayoutAnimation(lac);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public class ContactListAdapter extends ArrayAdapter<Contact>
    {
        private final Context context;
        private ArrayList<Contact> itemsArrayList;
        LayoutInflater inflater;



        public ContactListAdapter(Context context, ArrayList<Contact> itemsArrayList) {

            super(context, -1, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
            // 1. Create inflater
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return this.itemsArrayList.size();
        }

        public Contact getItem(int position) {
            return this.itemsArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.lv_call_list, parent, false);

            // 3. Get the two text view from the rowView
            TextView cName = (TextView) rowView.findViewById(R.id.l_contact_name);
            TextView cNo = (TextView) rowView.findViewById(R.id.l_contact_number);
            TextView cType = (TextView) rowView.findViewById(R.id.l_call_type);
            TextView cDuration = (TextView) rowView.findViewById(R.id.l_call_duration);
            TextView cDate = (TextView) rowView.findViewById(R.id.l_call_date);
            ImageView cImage = (ImageView) rowView.findViewById(R.id.iv_contact);

            // 4. Set the text for textView
            if(itemsArrayList.get(position).getContactName().length()>20)
            cName.setText(itemsArrayList.get(position).getContactName().substring(0,18)+"..");
            else cName.setText(itemsArrayList.get(position).getContactName());
            cNo.setText(itemsArrayList.get(position).getContactNumber());
            cType.setText(itemsArrayList.get(position).getContactCalls().getCallType());
            cDuration.setText(itemsArrayList.get(position).getContactCalls().getCallDuration());

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            cDate.setText(df.format("hh:mm aa dd-MM-yyyy", itemsArrayList.get(position).getContactCalls().getCallDate()));

            cImage.setImageResource(R.drawable.contact_photo);

            // 5. retrn rowView
            //LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getContext(), R.anim.lv_animation), 0.5f);
            //0.5f == time between appearance of listview items.
            //rowView.setAnimation(lac.getAnimation());

            return rowView;
        }

        public void updateResults(ArrayList<Contact> itemsArrayList)
        {
            this.itemsArrayList = itemsArrayList;
            //Triggers the list update
            notifyDataSetChanged();
        }
    }//

}
