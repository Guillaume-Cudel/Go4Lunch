package com.example.go4lunch.ui.workmates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;

public class WorkmatesFragment extends Fragment {

    //private static GetLocation location;


    public WorkmatesFragment(){}

    public static WorkmatesFragment newInstance() {
        return (new WorkmatesFragment());
    }

    /*public WorkmatesFragment(GetLocation location){
        this.location = location;
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        TextView textView = (TextView) view.findViewById(R.id.workmates_text);
        TextView showLocation = (TextView) view.findViewById(R.id.show_location);

        NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        double mLongitude = navigationActivity.getDoubleLongitude();
        double mLatitude = navigationActivity.getDoubleLatitude();
        /*double mLongitude = location.getLongitude();
        double mLatitude = location.getLatitude();*/

        String latitudeText = Double.toString(mLatitude);
        String longitudeText = Double.toString(mLongitude);
        String locationText = latitudeText + ", " + longitudeText;
        showLocation.setText(locationText);
        Log.d("localisation", locationText);

        return view;

    }
}