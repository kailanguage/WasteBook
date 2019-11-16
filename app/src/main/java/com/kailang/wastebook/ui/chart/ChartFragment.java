package com.kailang.wastebook.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kailang.wastebook.R;
import com.kailang.wastebook.adapters.WasteBookAdapter;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.detail.DetailViewModel;
import com.kailang.wastebook.utils.DateToLongUtils;
import com.kailang.wastebook.utils.PieChartUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChartFragment extends Fragment {

    private boolean isAll=false,isOUT=false;
    private Double IN=0.0,OUT=0.0,TOTAL=0.0;
    private long start,end;
    private List<WasteBook> allWasteBooks;
    private MutableLiveData<List<WasteBook>> selectedWasteBooks=new MutableLiveData<>();

    private RecyclerView recyclerView;
    private WasteBookAdapter wasteBookAdapter;
    private LiveData<List<WasteBook>> wasteBooksLive;
    private ChartViewModel chartViewModel;
    private LineChart lineChart;
    private HashMap dataMap;
    private PieChart mPieChart;
    //选择器
    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> options1Items_type = new ArrayList<>(),options1Items_INOUT=new ArrayList<>();
    private ArrayList<ArrayList<String>> options1Items_date = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        initCustomOptionPicker();
        final TextView select = root.findViewById(R.id.tv_select_chart);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvNoLinkOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        String temp_date = options1Items_date.get(options1).get(options2);
//                        select.setText(temp_date+"▼");
                    }
                }).setSubmitText("确定")
                        .setCancelText("取消")
                        .setTitleText("查询")
                        .setOutSideCancelable(false)
                        .build();
                pvNoLinkOptions.setPicker(options1Items_type, options1Items_date);
                pvNoLinkOptions.show();
            }
        });


        //饼状图
        mPieChart = root.findViewById(R.id.mPieChart);

        dataMap=new HashMap();
        dataMap.put("Ⅰ类水","8");
        dataMap.put("Ⅱ类水","12");
        dataMap.put("Ⅲ类水","31");
        dataMap.put("Ⅳ类水","24");
        dataMap.put("Ⅴ类水","10");
        dataMap.put("劣Ⅴ类水","15");
        PieChartUtils.getPitChart().setPieChart(mPieChart,dataMap,"水质",true);
        //点击事件
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pieEntry=(PieEntry)e;
                mPieChart.setCenterText(pieEntry.getLabel());
            }
            @Override
            public void onNothingSelected() {

            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化适配器
        chartViewModel = ViewModelProviders.of(getActivity()).get(ChartViewModel.class);
        recyclerView = requireActivity().findViewById(R.id.recyclerView_wasteBook_chart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wasteBookAdapter = new WasteBookAdapter(requireContext());
        recyclerView.setAdapter(wasteBookAdapter);

        chartViewModel.getAllWasteBookLive().observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                allWasteBooks=wasteBooks;
                int tmp=wasteBookAdapter.getItemCount();
                wasteBookAdapter.setAllWasteBook(wasteBooks);
                if(tmp!=wasteBooks.size()) {
                    wasteBookAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    //二级联动
    private void initCustomOptionPicker() {
        //选项0
        options1Items_INOUT.add("支出");
        options1Items_INOUT.add("收入");
        //选项1
        options1Items_type.add("按周");
        options1Items_type.add("按月");
        options1Items_type.add("按年");
        //选项2
        ArrayList<String> item_0=new ArrayList<>();
        item_0.add("当前周");
        item_0.add("上周");
        item_0.add("上上周");
        ArrayList<String> item_1=new ArrayList<>();
        item_1.add("近1个月");
        item_1.add("近3个月");
        item_1.add("近6个月");
        ArrayList<String> item_2=new ArrayList<>();
        for(int i=2019;i>=2010;i--){
            item_2.add(i+"年");
        }
        options1Items_date.add(item_0);
        options1Items_date.add(item_1);
        options1Items_date.add(item_2);
    }



    public void setSelectedWasteBook(){
        List<WasteBook> wasteBookList=new ArrayList<>();
        IN=0.0;OUT=0.0;TOTAL=0.0;
        if(allWasteBooks!=null){
            if(isAll){
                for (WasteBook w:allWasteBooks){
                    if(w.getTime()<=start&&w.getTime()>=end){
                        wasteBookList.add(w);
                        if(w.isType())OUT+=w.getAmount();
                        else IN+=w.getAmount();
                    }
                }
            }else if(isOUT){
                for (WasteBook w:allWasteBooks){
                    if(w.isType()&&w.getTime()<=start&&w.getTime()>=end){
                        wasteBookList.add(w);
                        OUT+=w.getAmount();
                    }
                }
            }else {
                for (WasteBook w:allWasteBooks){
                    if(!w.isType()&&w.getTime()<=start&&w.getTime()>=end){
                        wasteBookList.add(w);
                        IN+=w.getAmount();
                    }
                }
            }
        }
        selectedWasteBooks.setValue(wasteBookList);
    }


    private void selector(String type, String year, String month) {
        switch (type){
            case "全部":
                isAll=true;
                dealStartEnd(type,year,month);
                break;
            case "支出":
                isOUT=true;
                isAll=false;
                dealStartEnd(type,year,month);
                break;
            case "收入":
                isOUT=false;
                isAll=false;
                dealStartEnd(type,year,month);
                break;
        }
        //开始筛选
        setSelectedWasteBook();
    }

    private void dealStartEnd(String type, String year, String month){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(year.contains("近")){
            start= DateToLongUtils.dateToLong(sdf.format(date));
            if(year.contains("1")){
                cal.add(Calendar.MONTH, -1);
                end=DateToLongUtils.dateToLong(sdf.format(cal.getTime()));
                Log.e("xxxxxxxxxxx",start+" "+end+" "+year);
            }else if(year.contains("3")){
                cal.add(Calendar.MONTH, -3);
                end=DateToLongUtils.dateToLong(sdf.format(cal.getTime()));
                Log.e("xxxxxxxxxxx",start+" "+end+" "+year);
            }else if(year.contains("6")){
                cal.add(Calendar.MONTH, -6);
                end=DateToLongUtils.dateToLong(sdf.format(cal.getTime()));
                Log.e("xxxxxxxxxxx",start+" "+end+" "+year);
            }
        }else {
            //全年,否则月
            int startYear =Integer.parseInt(year.substring(0,4));
            if(month.isEmpty()){
                end=DateToLongUtils.dateToLong(startYear+"-1-1 00:00:00");
                start=DateToLongUtils.dateToLong(startYear+"-12-31 23:59:59");
                Log.e("xxxxxxxxxxx",start+" "+end+" "+startYear);
            }else{
                int startMonth=Integer.parseInt(month.substring(0,month.length()-1));
                date.setTime(DateToLongUtils.dateToLong(startYear+"-"+startMonth+"-1 00:00:00"));
                cal.setTime(date);
                Log.e("xxxxxxxxxx",sdf.format(cal.getTime()));
                end=cal.getTimeInMillis();
                cal.add(Calendar.MONTH, 1);
                start=cal.getTimeInMillis();
                Log.e("xxxxxxxxxxx",sdf.format(cal.getTime()));
            }
        }
    }

}