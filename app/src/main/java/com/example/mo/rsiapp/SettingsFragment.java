package com.example.mo.rsiapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.example.mo.rsiapp.customviews.NavAreaItemAdapter;
import com.example.mo.rsiapp.customviews.SettingsItem;
import com.example.mo.rsiapp.customviews.SettingsItemAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Settings";

    private ViewGroup rootViewGroup;
    private View inflatedView;

    private OnFragmentInteractionListener mListener;


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_settings, container,false);
        this.inflatedView = inflatedView;
        rootViewGroup = container;

        initComponents();

        // Inflate the layout for this fragment
        return inflatedView;
    }

    public void initComponents(){
        initCategorySettings();
        Button saveButton = inflatedView.findViewById(R.id.save_settings_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
    }

    public void saveSettings(){
        Log.d(TAG, "saveSettings: saving settings");

    }

    public void initCategorySettings(){
        ArrayList<SettingsItem> settingsItems = new ArrayList<>();
        settingsItems.add(new SettingsItem("Moist"));
        settingsItems.add(new SettingsItem("Wet"));
        settingsItems.add(new SettingsItem("LightSnow"));
        settingsItems.add(new SettingsItem("Snow"));
        settingsItems.add(new SettingsItem("DriftingSnow"));
        settingsItems.add(new SettingsItem("Slipperiness"));
        settingsItems.add(new SettingsItem("Hazardous"));

        ListView categoriesView = inflatedView.findViewById(R.id.categories_settings_view);
        categoriesView.setAdapter(new SettingsItemAdapter(inflatedView.getContext(), settingsItems, categoriesView));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
