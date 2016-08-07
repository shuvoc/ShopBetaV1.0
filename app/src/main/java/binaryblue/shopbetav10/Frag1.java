package binaryblue.shopbetav10;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Frag1 extends Fragment
{
    private long mLastClickTime = 0;

    public Frag1()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.frag1, container, false);
        /////////////////////////////////////////////////////////////////////////////////////
            final GridView gridView = (GridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new ImageAdapter(getContext()));
            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.gv_animation), 0.25f); //0.25f == time between appearance of listview items.
            gridView.setLayoutAnimation(lac);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(position == gridView.getFirstVisiblePosition())
                {
                    Intent intent = new Intent(getContext(), OrderReceiveListActivity.class);
                    intent.putExtra("title","Order Received");
                    startActivity(intent);
                }
                else if(position == gridView.getFirstVisiblePosition()+1)
                {
                    Intent intent = new Intent(getContext(), OrderReceiveListActivity.class);
                    intent.putExtra("title","Call List");
                    startActivity(intent);
                }
                else if(position == gridView.getFirstVisiblePosition()+2)
                {
                    Intent intent = new Intent(getContext(), OrderReceiveListActivity.class);
                    intent.putExtra("title","SMS List");
                    startActivity(intent);
                }
                else if(position == gridView.getFirstVisiblePosition()+3)
                {
                    Intent intent = new Intent(getContext(), OrderReceiveListActivity.class);
                    intent.putExtra("title","Total Revenue");
                    startActivity(intent);
                }
                else Snackbar.make(parent, "***", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////
        return view;
    }

    ///Adapter
    public class ImageAdapter extends BaseAdapter
    {
        private Context mContext;

        private Integer[] mThumbIds = {
                R.drawable.order_receive,
                R.drawable.call_list,
                R.drawable.sms_list,
                R.drawable.revenue

        };

        private String[] title = {
                "Received Order",
                "Call List",
                "SMS List",
                "Total Revenue"

        };

        private  Integer[] counts = {1,2,3,4};



        //Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.gv_animation);

        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return mThumbIds[position];
        }
        public long getItemId(int position) {
            return 0;
        }
        public ImageAdapter(Context c)
        {
            mContext = c;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;

            if(convertView == null)
            {
                gridView = inflater.inflate(R.layout.gv_layout, null);

                // set value into textview
                TextView textCount = (TextView) gridView.findViewById(R.id.txt_count);
                textCount.setText(counts[position]+"");

                TextView textTitle = (TextView) gridView.findViewById(R.id.text_title);
                textTitle.setText(title[position]);

                // set image based on selected text
                ImageView imageView = (ImageView) gridView.findViewById(R.id.img_icon);
                imageView.setImageResource(mThumbIds[position]);
            }
            else gridView = (View) convertView;

            //gridView.startAnimation(animation);
            return gridView;
        }



    }///Adapter ends here




}


