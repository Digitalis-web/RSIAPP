package com.roadstatusinfo.mo.rsiapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.KeyVerifier;
import com.roadstatusinfo.mo.rsiapp.datamanaging.StorageManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 * */

public class LoginFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    private String mParam1;
    private String mParam2;

    private static final String TAG = "LoginFragment";

    private View inflatedView;
    private TextInputEditText inputField;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     *
     */

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
/*        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
/*            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_login, container, false);
        this.inflatedView = inflatedView;
        initComponents();
        NavActivity.navActivity.hideSearchBar();

        // Inflate the layout for this fragment
        return inflatedView;
    }

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

    public void initComponents(){
        Button loginBtn = inflatedView.findViewById(R.id.login_btn);
        this.inputField = inflatedView.findViewById(R.id.input_key);
        inputField.setText(StorageManager.getRSIKey());

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginClicked();
            }
        });

    }

    public void removeFocusAndKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputField.getWindowToken(), 0);
        inputField.clearFocus();
    }

    public void loginClicked() {
        String key = String.valueOf(inputField.getText());
        //String key = ("ogJu1VCu09HpHD6VbHX34jChdoKz2fR5");
        //Log.d(TAG, "loginClicked: " + key);

        if(!key.isEmpty()) {
            StorageManager.saveRSIKey(key);
            FetchingManager.verifyKey(key, KeyVerifier.ON_LOGIN);
/*            StorageManager.saveRSIKey(key);
            Toast.makeText(getContext(), "Nyckel sparad" ,Toast.LENGTH_SHORT).show();
            NavActivity.navActivity.showSearchBar();
            NavActivity.navActivity.openInitial();*/

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

/*    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     */

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
