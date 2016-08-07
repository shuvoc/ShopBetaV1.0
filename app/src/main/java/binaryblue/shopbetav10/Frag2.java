package binaryblue.shopbetav10;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class Frag2 extends Fragment
{
    public Frag2()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.frag2, container, false);
        /////////////////////////////////////////////////////////////////////////////////////
        Toast.makeText(getContext(),"abcd",Toast.LENGTH_LONG).show();
        //////////////////////////////////////////////////////////////////////////////////////
        return view;
    }
}


