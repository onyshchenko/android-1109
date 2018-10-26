package com.example.onyshchenkov.android10;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment2 extends Fragment {

    public static final String EXTRA_TEXT = "com.example.onyshchenkov.android10.TEXT";


    public static BlankFragment2 newInstance(String text){

        BlankFragment2 fragment1 = new BlankFragment2();
        Bundle args = new Bundle();

        args.putString(EXTRA_TEXT, text);

        fragment1.setArguments(args);

        return fragment1;

    }

    public BlankFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);

        TextView textView = view.findViewById(R.id.textView);
        String text = getArguments().getString(EXTRA_TEXT);
        textView.setText(text);

        return view;
    }

}
