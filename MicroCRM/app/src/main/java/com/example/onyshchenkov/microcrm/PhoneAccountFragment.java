package com.example.onyshchenkov.microcrm;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneAccountFragment extends DialogFragment {

    private static final String EXTRA_PHONEACCOUNTS = "com.example.onyshchenkov.simpledialer.PHONEACCOUNTS";
    private static final String EXTRA_PHONENUMBER = "com.example.onyshchenkov.simpledialer.PHONENUMBER";


    //private TextView mTextViewFirstName, mTextViewLastName, mTextViewAge;
    private ListView mlistView;
    private TextView mtextView;
    private ArrayList<SelectPA> mPhoneAccounts;
    private String mPhoneNumber;

    public static PhoneAccountFragment newInstance(ArrayList<SelectPA> data, String phonenumber) {
        PhoneAccountFragment fragment = new PhoneAccountFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_PHONEACCOUNTS, data);
        args.putString(EXTRA_PHONENUMBER, phonenumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("MicroCRM", "Exception", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoneAccounts = getArguments().getParcelableArrayList(EXTRA_PHONEACCOUNTS);
        mPhoneNumber = getArguments().getString(EXTRA_PHONENUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("MicroCRM", "PhoneAccountFragment. onCreateView");


        View view = inflater.inflate(R.layout.fragment_phone_account, container, false);

        mlistView = view.findViewById(R.id.listView);
        mtextView = view.findViewById(R.id.textView);
        mlistView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mtextView.setText("Виберіть картку для дзвінка на номер: " + mPhoneNumber);

        //getDialog().setCanceledOnTouchOutside(true);
        //getDialog().setOnCancelListener(cancelListener);

        init();

        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mListener != null) {
            mListener.cancel();
        }
        super.onCancel(dialog);
    }


    private AdapterView.OnItemClickListener saveListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Log.d("ddd", String.valueOf(position));
            if (mListener != null) {
                mListener.save(position);
            }

        }
    };

    private void init() {

        String[] str = new String [mPhoneAccounts.size()];
        for(int i=0; i<mPhoneAccounts.size(); i++){
            str[i] = mPhoneAccounts.get(i).name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                str
        );

        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener(saveListener);

    }

    public interface ActionListener {
        void save(int position);
        void cancel();
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

}
