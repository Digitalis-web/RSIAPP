package com.example.mo.rsiapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static android.support.v7.widget.AppCompatDrawableManager.get;


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

    private static final String TAG = "Forecast";

    private float[] yData = {25,3f, 10.7f, 62.2f,102f,50.2f, 10f};
    private String[] xData = {"Part1", "Part2", "part3", "part4", "part5", "part6"};

    PieChart chart1;
    PieChart chart2;
    PieChart chart3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setText("right");
        View inflatedView = inflater.inflate(R.layout.fragment_forecast, container,false);

        initComponents(inflatedView);

        // Inflate the layout for this fragment
        return inflatedView;
    }
    public void initComponents(View inflatedView){

        // Set the Text to try this out
        TextView t = (TextView) inflatedView.findViewById(R.id.textHeader);
        t.setText("Text to Display");

        Log.d(TAG, "onCreateView: starting create chart");
        chart1 = (PieChart) inflatedView.findViewById(R.id.piChartOne);
        initPieChart(chart1);
        addDataSet(chart1);
        //chart1.setCenterText("NOTGING");

        chart2 = (PieChart) inflatedView.findViewById(R.id.piChartTwo);
        initPieChart(chart2);
        addDataSet(chart2);


        chart3 = (PieChart) inflatedView.findViewById(R.id.piChartThree);
        initPieChart(chart3);
        addDataSet(chart3);

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
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setCenterText("Nu");
        chart.setCenterTextSize(15);
        chart.setDrawEntryLabels(false);

    }
    private void addDataSet(PieChart chart) {
        Log.d(TAG, "addDataSet: started");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntries.add(new PieEntry(yData[i], i));
        }

        for(int i = 0; i < xData.length; i++) {
            xEntries.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(pieDataSet);
        chart.setData(pieData);

       // chart.setData(generateCenterText());

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setFormSize(10f);

        ArrayList<LegendEntry> lEntries = new ArrayList<>();
        LegendEntry e = new LegendEntry("1", legend.getForm(), legend.getFormSize(), legend.getFormLineWidth(), legend.getFormLineDashEffect(), chart.getData().getColors()[0]);
        lEntries.add(e);
        legend.setEntries(lEntries);
        //legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //legend.setTypeface(new Typeface());
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDrawInside(false);
        //legend.setDrawInside(false);
        //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        chart.invalidate();


    }
    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
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
