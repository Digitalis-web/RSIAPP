package com.roadstatusinfo.mo.rsiapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roadstatusinfo.mo.rsiapp.customviews.WatchAreaButton;
import com.roadstatusinfo.mo.rsiapp.datamanaging.DisplayInfoManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.FetchingManager;
import com.roadstatusinfo.mo.rsiapp.datamanaging.Forecast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.roadstatusinfo.mo.rsiapp.datamanaging.JSONFetcher;

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
    // TODO: Rename parameter arguments, choose names that matcha
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Forecast";

    public static Forecast viewedForecast;
    private View inflatedView;

    public static final String chartOneLabel = "Nu";
    public static final String chartTwoLabel = "Om 4h";
    public static final String chartThreeLabel = "Om 8h";

    LinearLayout chartOneContainer;
    LinearLayout chartTwoContainer;
    LinearLayout chartThreeContainer;


    // TODO: Rename and change types of parameters
    private String areaID;
    private int routeLength;

    private LinearLayout forecastLayout;

    private OnFragmentInteractionListener mListener;
    ArrayList<String> availableCategories = new ArrayList<>();
    ArrayList<String> availableCategoriesLabels = new ArrayList<>();

    HashMap<String, Long> chart1Values;
    HashMap<String, Long> chart2Values;
    HashMap<String, Long> chart3Values;

    AppCompatButton roadConditionButton;
    AppCompatButton temperatureButton;
    //LinearLayout moreButton;

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
    public static ForecastFragment newInstance(String areaID) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, areaID);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            areaID = getArguments().getString(ARG_PARAM1);
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
        View inflatedView = inflater.inflate(R.layout.fragment_forecast, container, false);

        this.inflatedView = inflatedView;
        this.routeLength = viewedForecast.getTotalLengthForRoute(0);
        initComponents();
        findAvailableCategories();

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
/*    public void openSelectCategoryMenu() {
        CharSequence categories[] = availableCategoriesLabels.toArray(new String[availableCategoriesLabels.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Välj kategori");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                Log.d(TAG, "onClick: category: " + availableCategories.get(index));
                updateCharts(availableCategories.get(index));
            }
        });
        builder.show();

    }*/

    //
    public HashMap<String, Long> getDataPoint(String category, long time) {

        HashMap<String, Long> values = new HashMap<>();
        try {
            JSONObject data = viewedForecast.getDataForCategory(category);
            JSONArray seriesArray = data.getJSONArray("series");

            for (int i = 0; i < seriesArray.length(); i++) {
                JSONObject seriesItem = seriesArray.getJSONObject(i);
                String name = seriesItem.getString("name");
                JSONArray seriesData = seriesItem.getJSONArray("data");

                for (int n = 0; n < seriesData.length(); n++) {
                    JSONObject timePointObj = seriesData.getJSONObject(n);
                    long pointTime = timePointObj.getLong("x");
                    Long value = timePointObj.getLong("y");

                    if (pointTime == time) {
                        values.put(name, value);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public void removeAllCharts() {
        forecastLayout.removeView(chartOneContainer);
        forecastLayout.removeView(chartTwoContainer);
        forecastLayout.removeView(chartThreeContainer);
    }

    public void updateRoadConditionCharts(String category) {
        removeAllCharts();
        chartOneContainer = addPieChart(chartOneLabel);
        chartTwoContainer = addPieChart(chartTwoLabel);
        chartThreeContainer = addPieChart(chartThreeLabel);
    }

    public void updateTemperatureCharts(String category) {
        removeAllCharts();
        chartOneContainer = addTemperatureChart(FetchingManager.chartOneTimeLabel);
        chartTwoContainer = addTemperatureChart(FetchingManager.chartTwoTimeLabel);
        chartThreeContainer = addTemperatureChart(FetchingManager.chartThreeTimeLabel);
    }

    public void updateCharts(String category) {
        chart1Values = viewedForecast.getDataPoint(category, FetchingManager.chartOneTime, null);
        chart2Values = viewedForecast.getDataPoint(category, FetchingManager.chartTwoTime, null);
        chart3Values = viewedForecast.getDataPoint(category, FetchingManager.chartThreeTime, null);

        if (category.equals("roadcondition")) {
            updateRoadConditionCharts(category);
        } else {
            updateTemperatureCharts(category);
        }

        addDataSet(category, chartOneContainer, chart1Values);
        addDataSet(category, chartTwoContainer, chart2Values);
        addDataSet(category, chartThreeContainer, chart3Values);

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

    public void initComponents() {
        forecastLayout = inflatedView.findViewById(R.id.forecast_layout);

        if (viewedForecast == null || viewedForecast.categories == null) {
            NavActivity.navActivity.displayConnectError();
            NavActivity.openLoadingScreen();
            return;
        }

        setForecastTimeLabel();

        TextView updateText = inflatedView.findViewById(R.id.forecast_update);
        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavActivity.openLoadingScreen();
                FetchingManager.fetchAreas(JSONFetcher.FETCH_AREAS_AND_REOPEN_FORECAST);
            }
        });
/*        chartOne = inflatedView.findViewById(R.id.piChartOne);
        chartTwo = inflatedView.findViewById(R.id.piChartTwo);
        chartThree = inflatedView.findViewById(R.id.piChartThree);*/


/*        TextView chartOneHeader = inflatedView.findViewById(R.id.chartOneHeader);
        TextView chartTwoHeader = inflatedView.findViewById(R.id.chartTwoHeader);
        TextView chartThreeHeader = inflatedView.findViewById(R.id.chartThreeHeader);
        chartOneHeader.setText(FetchingManager.chartOneTimeLabel);
        chartTwoHeader.setText(FetchingManager.chartTwoTimeLabel);
        chartThreeHeader.setText(FetchingManager.chartThreeTimeLabel);*/


        final WatchAreaButton watchButton = inflatedView.findViewById(R.id.watch_area_button);
        watchButton.init(areaID);

        roadConditionButton = inflatedView.findViewById(R.id.roadBtn);
        roadConditionButton.setOnClickListener(new AppCompatButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRoadContidion();
            }
        });
        temperatureButton = inflatedView.findViewById(R.id.temperatureBtn);
        temperatureButton.setOnClickListener(new AppCompatButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTemperature();
            }
        });

        /*final Button categoriesButton = (Button) inflatedView.findViewById(R.id.select_category_button);
        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectCategoryMenu();
            }
        });*/
        //initCategoryButtons(inflatedView);

        String areaName = FetchingManager.getAreaNameFromID(areaID);
        TextView headerView = (TextView) inflatedView.findViewById(R.id.forecast_header);
        headerView.setText(areaName);

        if (viewedForecast.categories.size() > 0) {
            //updateCharts(viewedForecast.categories.get(0));
            openRoadContidion();
        } else {
            NavActivity.navActivity.displayConnectError();
            return;
        }
    }

    public void openRoadContidion() {
        updateCharts("roadcondition");
        roadConditionButton.setBackgroundColor(Color.GRAY);
        temperatureButton.setBackgroundColor(Color.WHITE);
    }

    public void openTemperature() {
        updateCharts("roadtemperature");
        roadConditionButton.setBackgroundColor(Color.WHITE);
        temperatureButton.setBackgroundColor(Color.GRAY);
    }

    private LinearLayout addPieChart(String header) {

        LayoutInflater inflater = LayoutInflater.from(NavActivity.navActivity);
        LinearLayout chartContainer = (LinearLayout) inflater.inflate(R.layout.forecast_chart_container, forecastLayout, false);
        RelativeLayout innerChartContainer = chartContainer.findViewById(R.id.innerChartContainer);

        TextView headerView = chartContainer.findViewById(R.id.chartHeader);
        headerView.setText(header);

        //PieChart chart = chartContainer.findViewById(R.id.pieChart);
        PieChart chart = (PieChart) inflater.inflate(R.layout.piechart, forecastLayout, false);

        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(0f);
        chart.setTransparentCircleAlpha(0);
        //chart.setCenterText("Nu");
        //chart.setCenterTextSize(15);
        chart.setDrawEntryLabels(false);

        innerChartContainer.addView(chart);
        forecastLayout.addView(chartContainer);

        return chartContainer;
    }

    private void setForecastTimeLabel(){
        String time = viewedForecast.latestForecastImportTimeLabel;
        TextView timeLabel = inflatedView.findViewById(R.id.forecast_time);
        timeLabel.setText("Prognos beräknad " + time);
    }

    private LinearLayout addTemperatureChart(String header) {

        LayoutInflater inflater = LayoutInflater.from(NavActivity.navActivity);
        LinearLayout chartContainer = (LinearLayout) inflater.inflate(R.layout.forecast_chart_container, forecastLayout, false);
        RelativeLayout innerChartContainer = chartContainer.findViewById(R.id.innerChartContainer);

        TextView headerView = chartContainer.findViewById(R.id.chartHeader);
        headerView.setText(header);

        //PieChart chart = chartContainer.findViewById(R.id.pieChart);
        LinearLayout chart = (LinearLayout) inflater.inflate(R.layout.temperature_chart, forecastLayout, false);

        innerChartContainer.addView(chart);
        forecastLayout.addView(chartContainer);

        return chartContainer;
    }

    // Removes the info items that explain the charts. Removes everything but the first element which is the title
    private void removeAllInfoListItems(LinearLayout infoLayout) {
        //infoLayout.getView();
        int elementCount = infoLayout.getChildCount();
        for (int i = elementCount - 1; i > 0; i--) {
            infoLayout.removeViewAt(i);
        }
    }

    // Adds a value to the list next to a chart that explains what each color on the chart represents
    private void addInfoListItem(String label, int color, LinearLayout infoLayout) {
        TextView labelView = (TextView) View.inflate(getContext(), R.layout.chart_list_item, null);
        labelView.setText(" " + label);
        infoLayout.addView(labelView);


        Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_circle).mutate();
        icon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        labelView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);


    }

    private void setCategoryHeader(String category) {
        String categoryLabel = DisplayInfoManager.getCategoryLabel(category);
        TextView categoryHeader = inflatedView.findViewById(R.id.forecast_category_header);
        categoryHeader.setText(categoryLabel);
    }

    private void addDataSet(String category, LinearLayout chartContainer, HashMap<String, Long> values) {
        LinearLayout infoLayout = chartContainer.findViewById(R.id.chartInfo);
        PieChart chart = chartContainer.findViewById(R.id.pieChart);
        Log.d(TAG, "addDataSet: RUNNING ADD DATA");
        Log.d(TAG, "addDataSet: started");
        setCategoryHeader(category);
        ArrayList<PieEntry> yEntries = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<>();

        removeAllInfoListItems(infoLayout);

        int totalLength = routeLength;

        long temperatureMin = -1;
        long temperatureMax = -1;

        int i = 0;
        for (String key : values.keySet()) {
            long value = values.get(key);

/*            Log.d(TAG, "addDataSet: value : " + value);
            Log.d(TAG, "addDataSet: key : " + key);*/
            if (category.equals("roadcondition")) {
                if (value > 0) {
                    if(!key.equals("Error")){
                        double percent = (int)(((value * 1.0) / routeLength)*100*10);
                        if(percent > 0) {
                            Log.d(TAG, "addDataSet: percent + " + percent);
                            percent /= 10;

                            Log.d(TAG, "addDataSet: percent" + percent);
                            yEntries.add(new PieEntry(value, i));
                            //xEntries.add(key);
                            Log.d(TAG, "addDataSet: key" + key);
                            int color = Integer.parseInt(DisplayInfoManager.getRoadConditionInfoByName(key, "color"));
                            String label = DisplayInfoManager.getRoadConditionInfoByName(key, "label");

                            String percentStr = String.valueOf(percent);

                            if (percent >= 1) {
                                percentStr = String.valueOf(Math.round(percent));
                            }

                            label = percentStr + "% " + label;
                            //int color = Color.parseColor(hexColor);
                            colors.add(color);
                            addInfoListItem(label, color, infoLayout);
                        }
                    }
                }
            } else if (category.equals("roadtreatment")) {
                // Juding from current application, the "Plough" layer is always 0 and is hidden
                if (!key.equals("Plough") && value > 0) {
                    yEntries.add(new PieEntry(value, i));
                    totalLength -= value; // Calculates the remaining length for the route that have not been salted
                    String hexColor = DisplayInfoManager.getSaltColor(key);
                    Log.d(TAG, "addDataSet: hexColor: " + hexColor);
                    int color = Color.parseColor(hexColor);
                    colors.add(color);
                    addInfoListItem(key, color, infoLayout);
                }
            } else if (category.equals("roadtemperature")) {
                //Log.d(TAG, "addDataSet: key: " + key);
                if (!key.equals("StdDev")) {
                    if (key.equals("Min")) {
                        temperatureMin = value;
                    } else if (key.equals("Max")) {
                        temperatureMax = value;
                    }
                    else if (key.equals("Avg")){
                        key = "Medel";
                    }
                    addInfoListItem(key + ": " + value + "°", Color.WHITE, infoLayout);
                }

            }
            i++;
        }


        if (category.equals("roadtreatment")) {
            yEntries.add(new PieEntry(totalLength, i));
            int color = Color.RED;
            colors.add(color);
            addInfoListItem("Ej saltat", color, infoLayout);
        }
        if (category.equals("roadtemperature")) {
            setTemperatureChart(chartContainer, (int) temperatureMin, (int) temperatureMax);
        } else {

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


            chart.invalidate();
        }


    }

    public void setTemperatureChart(LinearLayout chartContainer, int min, int max) {
        int spanLength = 10;
        Log.d(TAG, "setTemperatureChart: spanlength: " + spanLength);

        TextView minView = chartContainer.findViewById(R.id.temperatureMin);
        View spanView = chartContainer.findViewById(R.id.temperatureSpan);
        TextView maxView = chartContainer.findViewById(R.id.temperatureMax);

        minView.setText(String.valueOf(min) + "° ");
        maxView.setText(" " + String.valueOf(max) + "°");


        int diff = max - min;


        int minViewSize = 20;
        setViewWeight(minView, minViewSize);

        int spanViewSize = (int) (diff * 1.0 / spanLength * 100);

        if (spanViewSize > 100 - minViewSize * 2) {
            spanViewSize = 100 - minViewSize * 2;
        } else if (spanViewSize == 0) {
            spanViewSize = 10;
        }

        setViewWeight(spanView, spanViewSize);

        int maxViewSize = 100 - minViewSize - spanViewSize;
        setViewWeight(maxView, maxViewSize);
    }
/*    public void setTemperatureChart(LinearLayout chartContainer, long min, long max){
        int lowestDisplayTemp = -8;
        int highestDisplayTemp = 5;
        int spanLength = highestDisplayTemp - lowestDisplayTemp;

        TextView minView = chartContainer.findViewById(R.id.temperature_min);
        View spanView = chartContainer.findViewById(R.id.temperature_span);
        TextView maxView = chartContainer.findViewById(R.id.temperature_max);


        min = -18;
        max = -8;

        minView.setText(String.valueOf(min) + "° ");
        maxView.setText(" " + String.valueOf(max) + "°");

        if(min == max){ // for the purpose of not lettings the span width be 0 only
            min -= 1;
        }

        if(min <= lowestDisplayTemp){
            min = lowestDisplayTemp + 3;
        }

        if(max <= lowestDisplayTemp){
            max = lowestDisplayTemp + 4;
        }

        if(max >= highestDisplayTemp){
            max = highestDisplayTemp - 1;
        }

        Log.d(TAG, "setTempatureChart: max: " + max);
        Log.d(TAG, "setTempatureChart: min: " + min);
        long diff = max - min;
        Log.d(TAG, "setTempatureChart: diff: " + diff);



        int minViewSize = (int)Math.abs((lowestDisplayTemp-min)*1.0 / spanLength * 100);
        Log.d(TAG, "setTempatureChart: minviewsize: " + minViewSize);
        setViewWeight(minView, minViewSize);

        int spanViewSize = (int)(diff*1.0 / spanLength*100);
        Log.d(TAG, "setTempatureChart: spanviewsize: " + spanViewSize);
        setViewWeight(spanView, spanViewSize);

        int maxViewSize = (int)((highestDisplayTemp-max)*1.0 / spanLength * 100);
        Log.d(TAG, "setTempatureChart: minviewsize: " + maxViewSize);
        setViewWeight(maxView, maxViewSize);


    }*/

    public void setViewWeight(View view, int weight) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        p.weight = weight;
        view.setLayoutParams(p);
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
