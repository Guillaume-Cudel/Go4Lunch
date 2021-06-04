package com.example.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;

public class WorkmatesFragment extends Fragment {

    private TextView textView;


    public static WorkmatesFragment newInstance() {
        return (new WorkmatesFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        textView = (TextView) view.findViewById(R.id.workmates_text);

        return view;

    }
}