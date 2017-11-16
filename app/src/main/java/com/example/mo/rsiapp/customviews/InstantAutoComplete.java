package com.example.mo.rsiapp.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mo.rsiapp.NavActivity;
import com.example.mo.rsiapp.R;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.example.mo.rsiapp.datamanaging.JSONFetcher;

import java.util.ArrayList;

import static android.R.id.list;
import static com.example.mo.rsiapp.R.id.textView;

/**
 * Created by mo on 07/09/17.
 */

public class InstantAutoComplete extends android.support.v7.widget.AppCompatAutoCompleteTextView implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {
    public String TAG = "InstantAuto";

    public InstantAutoComplete(Context context) {
        super(context);
        setupView();
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        setupView();
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        setupView();
    }

    public void setupView(){
        //this.setDropDownBackgroundResource(R.color.colorWhite);
        Log.d(TAG, "InstantAutoComplete: ");
        this.setThreshold(0);
       // this.clearFocus();

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d(TAG, "onFocusChange2: focus: " + b);

            }
        });


        setOnEditorActionListener(this); // listen for enter button
        setOnItemClickListener(this); // listen for enter button
    }

    public void updateList(ArrayList<String> list){
        String[] areas = new String[list.size()];
        list.toArray(areas);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                //new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, areas);
                new ArrayAdapter<String>(getContext(), R.layout.autocomplete_list_item, areas);
        this.setAdapter(adapter);


    }


    @Override
    // when enter button is pressed on keyboard
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        removeFocusAndKeyboard();
        useSelectedValue();
        Toast.makeText(getContext(),getText(),Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        // if back button is pressed while keyboard is open
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            removeFocusAndKeyboard();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void removeFocusAndKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();

    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        //Log.d(TAG, "onFocusChanged: focused" + focused);

        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            //performFiltering(getText(), 0);
            showDropDown();
            //Log.d(TAG, "onFocusChanged: filtering"  );
        }
    }

    public void useSelectedValue(){
        Toast.makeText(getContext(), getText(), Toast.LENGTH_SHORT).show();
        String areaName = getText().toString();
        int index = FetchingManager.areasName.indexOf(areaName);
        String areaID = FetchingManager.areasID.get(index);
        NavActivity.openLoadingScreen();
        FetchingManager.fetchForecast(areaID, FetchingManager.latestForecastTime, JSONFetcher.FETCH_FORECAST);
    }

    @Override
    // When an item in the list is selected
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        removeFocusAndKeyboard();
        useSelectedValue();
        Log.d(TAG, "onItemClick: ");
    }
}