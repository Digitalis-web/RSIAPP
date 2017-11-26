package com.example.mo.rsiapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mo.rsiapp.customviews.WatchAreaButton;
import com.example.mo.rsiapp.datamanaging.DisplayInfoManager;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.example.mo.rsiapp.datamanaging.Forecast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    //private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Forecast";

    public static Forecast viewedForecast;
    private View inflatedView;


    PieChart chartOne;
    PieChart chartTwo;
    PieChart chartThree;

    LinearLayout chartOneContainer;
    LinearLayout chartTwoContainer;
    LinearLayout chartThreeContainer;

    // TODO: Rename and change types of parameters
    private String areaID;
    private int routeLength;

    private ViewGroup rootViewGroup;

    private OnFragmentInteractionListener mListener;
    ArrayList<String> availableCategories = new ArrayList<>();
    ArrayList<String> availableCategoriesLabels = new ArrayList<>();

    //
    public ForecastFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String areaID, int routeLength) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, areaID);
        args.putInt(ARG_PARAM2, routeLength);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            areaID = getArguments().getString(ARG_PARAM1);
            routeLength = getArguments().getInt(ARG_PARAM2);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //
    public void findAvailableCategories() {
        // viewedForecast.categories contains the "categories" that the chosen area has data about.
        // The viewCategories are the categories that should always be viewable when they are available
        // availableCategories are the categories that the user will be able to choose from
        for (int i = 0; i < viewedForecast.categories.size(); i++) {
            String category = viewedForecast.categories.get(i);
            Log.d(TAG, "initCategoryButtons: " + viewedForecast.categories.get(i));


            // if the category is to be viewable
            if (DisplayInfoManager.viewCategories.contains(category)) {
                availableCategories.add(category);
                availableCategoriesLabels.add(DisplayInfoManager.getCategoryLabel(category));
            }

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setText("right");
        View inflatedView = inflater.inflate(R.layout.fragment_forecast, container,false);
        rootViewGroup = container;

        this.inflatedView = inflatedView;
        initComponents(inflatedView);
        findAvailableCategories();

        // Inflate the layout for this fragment
        return inflatedView;
    }

    /*public void initCategoryButtons(View infaltedView) {
        RadioGroup group = (RadioGroup) infaltedView.findViewById(R.id.category_radio_group);
        // FetchingManager.categories contains the "categories" that the chosen area has data about.
        // The viewCategories are the categories that should always be viewable when they are available
        for (int i = 0; i < FetchingManager.categories.size(); i++) {
            String category = FetchingManager.categories.get(i);
            Log.d(TAG, "initCategoryButtons: " + FetchingManager.categories.get(i));

            // if the category is to be viewable
            if (viewCategories.contains(category)) {
            }

        }
    }*/

    //
    public void openSelectCategoryMenu(){
        CharSequence categories[] = availableCategoriesLabels.toArray(new String[availableCategoriesLabels.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Välj kategori");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                Log.d(TAG, "onClick: category: " + availableCategories.get(index));
                updateCharts(availableCategories.get(index), getView());
            }
        });
        builder.show();

    }

    //
    public  HashMap<String, Long> getDataPoint(String category, long time){

        HashMap<String, Long> values = new HashMap<>();
        try {
            JSONObject data = viewedForecast.getDataForCategory(category);
            JSONArray seriesArray = data.getJSONArray("series");

            for(int i = 0; i < seriesArray.length(); i++){
                JSONObject seriesItem = seriesArray.getJSONObject(i);
                String name = seriesItem.getString("name");
                JSONArray seriesData = seriesItem.getJSONArray("data");

                for(int n = 0; n < seriesData.length(); n++){
                    JSONObject timePointObj = seriesData.getJSONObject(n);
                    long pointTime = timePointObj.getLong("x");
                    Long value = timePointObj.getLong("y");

                    if(pointTime == time){
                        values.put(name, value);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    //
    public void updateCharts(String category, View inflatedView){
        //String category = categories.get(0);
        HashMap<String, Long> chart1Values = viewedForecast.getDataPoint(category, FetchingManager.chartOneTime);
        HashMap<String, Long> chart2Values = viewedForecast.getDataPoint(category, FetchingManager.chartTwoTime);
        HashMap<String, Long> chart3Values = viewedForecast.getDataPoint(category, FetchingManager.chartThreeTime);

        Log.d(TAG, "onCreateView: starting create chart");
        LinearLayout chart1InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoOne);
        addDataSet(category, chartOne, chart1Values, chart1InfoLayout);

        LinearLayout chart2InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoTwo);
        addDataSet(category, chartTwo, chart2Values, chart2InfoLayout);

        LinearLayout chart3InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoThree);
        addDataSet(category, chartThree, chart3Values, chart3InfoLayout);

        /*chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //Log.d(TAG, "onValueSelected: value selected" );
                //Log.d(TAG, "onValueSelected: e"  + e.toString() );
                //Log.d(TAG, "onValueSelected: h"  + h.toString() );
                //float data = e.getY();
                //Log.d(TAG, "onValueSelected: y"  + data);
            }

            @Override
            public void onNothingSelected() {

            }
        });*/


    }

    //
    public void initComponents(View inflatedView){

        chartOne   = inflatedView.findViewById(R.id.piChartOne);
        chartTwo   = inflatedView.findViewById(R.id.piChartTwo);
        chartThree = inflatedView.findViewById(R.id.piChartThree);


        initPieChart(chartOne);
        initPieChart(chartTwo);
        initPieChart(chartThree);

        TextView chartOneHeader = inflatedView.findViewById(R.id.chartOneHeader);
        TextView chartTwoHeader = inflatedView.findViewById(R.id.chartTwoHeader);
        TextView chartThreeHeader = inflatedView.findViewById(R.id.chartThreeHeader);
        chartOneHeader.setText(FetchingManager.chartOneTimeLabel);
        chartTwoHeader.setText(FetchingManager.chartTwoTimeLabel);
        chartThreeHeader.setText(FetchingManager.chartThreeTimeLabel);

        updateCharts(viewedForecast.categories.get(0), inflatedView);

        final WatchAreaButton watchButton = (WatchAreaButton) inflatedView.findViewById(R.id.watch_area_button);
        watchButton.init(areaID);

        final Button categoriesButton = (Button) inflatedView.findViewById(R.id.select_category_button);
        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectCategoryMenu();
            }
        });
        //initCategoryButtons(inflatedView);


        String areaName = FetchingManager.getAreaNameFromID(areaID);
        TextView headerView = (TextView) inflatedView.findViewById(R.id.forecast_header);
        headerView.setText(areaName);


    }

    //
    private void initPieChart(PieChart chart){

        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(0f);
        chart.setTransparentCircleAlpha(0);
        //chart.setCenterText("Nu");
        //chart.setCenterTextSize(15);
        chart.setDrawEntryLabels(false);

    }

    // Removes the info items that explain the charts. Removes everything but the first element which is the title
    private void removeAllInfoListItems(LinearLayout infoLayout){
        //infoLayout.getView();
        int elementCount = infoLayout.getChildCount();
        Log.d(TAG, "removeAllInfoListItems: elementCount: " + elementCount);
        Log.d(TAG, "removeAllInfoListItems: layout: " + infoLayout);
        for(int i = elementCount-1; i > 0; i--){
            infoLayout.removeViewAt(i);
        }
    }

    // Adds a value to the list next to a chart that explains what each color on the chart represents
    private void addInfoListItem(String label, int color, LinearLayout infoLayout){
        TextView labelView = (TextView) View.inflate(getContext(), R.layout.chart_list_item, null);
        labelView.setText(" " + label);
        infoLayout.addView(labelView);



        Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_circle).mutate();
        icon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        labelView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);


    }

    private void setCategoryHeader(String category){
        String categoryLabel = DisplayInfoManager.getCategoryLabel(category);
        TextView categoryHeader = inflatedView.findViewById(R.id.forecast_category_header);
        categoryHeader.setText(categoryLabel);
    }

    //
    private void addDataSet(String category, PieChart chart, HashMap<String, Long> values, LinearLayout infoLayout) {
        Log.d(TAG, "addDataSet: RUNNING ADD DATA");
        Log.d(TAG, "addDataSet: started");
        setCategoryHeader(category);
        ArrayList<PieEntry> yEntries = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<>();

        removeAllInfoListItems(infoLayout);

        int totalLength = this.routeLength;

        int i = 0;
        for(String key : values.keySet()){
            long value = values.get(key);

            Log.d(TAG, "addDataSet: value : " + value);
            Log.d(TAG, "addDataSet: key : " + key);
            if(category.equals("roadcondition")){
                if (value > 0) {
                    yEntries.add(new PieEntry(value, i));
                //xEntries.add(key);
                    int color = Integer.parseInt(DisplayInfoManager.getRoadConditionInfoByName(key, "color"));
                    String label = DisplayInfoManager.getRoadConditionInfoByName(key, "label");
                    //int color = Color.parseColor(hexColor);
                    colors.add(color);
                    addInfoListItem(label, color, infoLayout);
                }
            }
            else if(category.equals("roadtreatment")) {
                // Juding from current application, the "Plough" layer is always 0 and is hidden
                if(!key.equals("Plough") && value > 0) {
                    yEntries.add(new PieEntry(value, i));
                    totalLength -= value; // Calculates the remaining length for the route that have not been salted
                    String hexColor = DisplayInfoManager.getSaltColor(key);
                    Log.d(TAG, "addDataSet: hexColor: " + hexColor);
                    int color = Color.parseColor(hexColor);
                    colors.add(color);
                    addInfoListItem(key, color, infoLayout);
                }
            }
            else if(category.equals("roadtemperature")) {
                //Log.d(TAG, "addDataSet: key: " + key);
                if(!key.equals("StdDev")){
                    addInfoListItem(key + ": " + value, Color.BLACK, infoLayout);
                }

            }
            i++;
        }

        if(category.equals("roadtreatment")) {
            yEntries.add(new PieEntry(totalLength, i));
            int color = Color.YELLOW;
            colors.add(color);
            addInfoListItem("Ej saltat", color, infoLayout);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        pieDataSet.setSliceSpace(0);
        pieDataSet.setValueTextSize(0);
        //pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        chart.setData(pieData);

       // chart.setData(generateCenterText());

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        //legend.setFormSize(10f);

        //ArrayList<LegendEntry> lEntries = new ArrayList<>();
        //LegendEntry e = new LegendEntry("1", legend.getForm(), legend.getFormSize(), legend.getFormLineWidth(), legend.getFormLineDashEffect(), chart.getData().getColors()[0]);
        //lEntries.add(e);
        //legend.setEntries(lEntries);
        //legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        //legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //legend.setTypeface(new Typeface());
        //legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setDrawInside(false);
        //legend.setDrawInside(false);
        //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);*/

        chart.invalidate();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //
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

    //
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
