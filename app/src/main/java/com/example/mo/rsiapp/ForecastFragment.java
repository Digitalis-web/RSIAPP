package com.example.mo.rsiapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mo.rsiapp.customviews.WatchAreaButton;
import com.example.mo.rsiapp.datamanaging.FetchingManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

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
    //private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Forecast";

    PieChart chart1;
    PieChart chart2;
    PieChart chart3;

    // TODO: Rename and change types of parameters
    private String areaID;

    private ViewGroup rootViewGroup;

    private OnFragmentInteractionListener mListener;
    ArrayList<HashMap<String, String>> roadConditionInfo = initRoadConditionInfoArray();


    public ForecastFragment() {
        // Required empty public constructor
    }

    public ArrayList<HashMap<String, String>> initRoadConditionInfoArray(){
        ArrayList<HashMap<String, String>> roadConditionInfo = new ArrayList<>();

        HashMap<String, String> map = new HashMap<>();

        map.put("name", "Dry");
        map.put("color", "#99cc66");
        map.put("label", "Torrt");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Moist");
        map.put("color", "#8eb1e6");
        map.put("label", "Fuktigt");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Wet");
        map.put("color", "#2a70d9");
        map.put("label", "Vått");
        roadConditionInfo.add(map);

        //[{ name: 'Dry', color: '#99cc66', stroke: 'rgba(0,0,0,0.2)', label: 'Torrt' },
        // { name: 'Moist', color: '#8eb1e6', stroke: 'rgba(0,0,0,0.2)', label: 'Fuktigt' },
        // { name: 'Wet', color: '#2a70d9', stroke: 'rgba(0,0,0,0.2)', label: 'VÃ¥tt' },
        // { name: 'LightSnow', color: '#66ffff', stroke: 'rgba(0,0,0,0.2)', label: 'LÃ¤tt snÃ¶' },
        // { name: 'Snow', color: '#00c0c0', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶' },
        // { name: 'DriftingSnow', color: '#156262', stroke: 'rgba(0,0,0,0.2)', label: 'SnÃ¶drev' },
        // { name: 'Slipperiness', color: '#cc66cc', stroke: 'rgba(0,0,0,0.2)', label: 'Halka' },
        // { name: 'Hazardous', color: '#e96605', stroke: 'rgba(0,0,0,0.2)', label: 'SvÃ¥r halka' }];

        map = new HashMap<>();
        map.put("name", "LightSnow");
        map.put("color", "#66ffff");
        map.put("label", "Lätt snö");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Snow");
        map.put("color", "#00c0c0");
        map.put("label", "Snö");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "DriftingSnow");
        map.put("color", "#156262");
        map.put("label", "Snö drev");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Slipperiness");
        map.put("color", "#cc66cc");
        map.put("label", "Halka");
        roadConditionInfo.add(map);

        map = new HashMap<>();
        map.put("name", "Hazardous");
        map.put("color", "#e96605");
        map.put("label", "Svår halka");
        roadConditionInfo.add(map);

        return roadConditionInfo;
    }

    public String getRoadConditionInfoByName(String name, String type){
        String value = "";

        for(int i = 0; i < roadConditionInfo.size(); i++) {
            HashMap<String, String> map = roadConditionInfo.get(i);
            if (map.get("name").equals(name)) {
                return map.get(type);
            }
        }

       return value;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setText("right");
        View inflatedView = inflater.inflate(R.layout.fragment_forecast, container,false);
        rootViewGroup = container;

        initComponents(inflatedView);

        // Inflate the layout for this fragment
        return inflatedView;
    }

    public void initComponents(View inflatedView){


        final WatchAreaButton watchButton = (WatchAreaButton) inflatedView.findViewById(R.id.watchAreaBtn);
        watchButton.init(areaID);

        String areaName = FetchingManager.getAreaNameFromID(areaID);
        TextView headerView = (TextView) inflatedView.findViewById(R.id.forecast_header);
        headerView.setText(areaName);

        String category = FetchingManager.categories.get(0);
        HashMap<String, Long> chart1Values = FetchingManager.getDataPoint(category, FetchingManager.chartOneTime);
        HashMap<String, Long> chart2Values = FetchingManager.getDataPoint(category, FetchingManager.chartTwoTime);
        HashMap<String, Long> chart3Values = FetchingManager.getDataPoint(category, FetchingManager.chartThreeTime);


        Log.d(TAG, "onCreateView: starting create chart");
        chart1 = (PieChart) inflatedView.findViewById(R.id.piChartOne);
        LinearLayout chart1InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoOne);
        initPieChart(chart1);
        addDataSet(category, chart1, chart1Values, chart1InfoLayout);

        chart2 = (PieChart) inflatedView.findViewById(R.id.piChartTwo);
        LinearLayout chart2InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoTwo);
        initPieChart(chart2);
        addDataSet(category, chart2, chart2Values, chart2InfoLayout);

        chart3 = (PieChart) inflatedView.findViewById(R.id.piChartThree);
        LinearLayout chart3InfoLayout = (LinearLayout) inflatedView.findViewById(R.id.chartInfoThree);
        initPieChart(chart3);
        addDataSet(category, chart3, chart3Values, chart3InfoLayout);



        chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Log.d(TAG, "onValueSelected: value selected" );
                Log.d(TAG, "onValueSelected: e"  + e.toString() );
                Log.d(TAG, "onValueSelected: h"  + h.toString() );
                float data = e.getY();
                Log.d(TAG, "onValueSelected: y"  + data);
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

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

    // Adds a value to the list next to a chart that explains what each color on the chart represents
    private void addInfoListItem(String label, int color, LinearLayout infoLayout){
        TextView labelView = (TextView) View.inflate(getContext(), R.layout.chart_list_item, null);
        labelView.setText(" " + label);
        infoLayout.addView(labelView);



        Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_circle).mutate();
        icon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        labelView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);


    }
    private void addDataSet(String category, PieChart chart, HashMap<String, Long> values, LinearLayout infoLayout) {
        Log.d(TAG, "addDataSet: RUNNING ADD DATA");
        Log.d(TAG, "addDataSet: started");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        ArrayList<Integer> colors = new ArrayList<>();



        int i = 0;
        for(String key : values.keySet()){
            long value = values.get(key);

            if (value > 0) {
                yEntries.add(new PieEntry(value, i));
                xEntries.add(key);
                if(category.equals("roadcondition")){
                    String hexColor = getRoadConditionInfoByName(key, "color");
                    String label = getRoadConditionInfoByName(key, "label");
                    Log.d(TAG, "addDataSet: label: " + label);
                    int color = Color.parseColor(hexColor);
                    colors.add(color);
                    addInfoListItem(label, color, infoLayout);
                }
            }
            i++;
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
