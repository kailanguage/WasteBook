package com.kailang.wastebook.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartFragment extends Fragment {

    private boolean isOUT=true;
    private Double IN=0.0,OUT=0.0,TOTAL=0.0;
    private long start,end;
    private String selectedStr="近1个月";
    private List<WasteBook> allWasteBooks;
    private MutableLiveData<List<WasteBook>> selectedWasteBooks=new MutableLiveData<>();

    private RecyclerView recyclerView;
    private WasteBookAdapter wasteBookAdapter;
    private LiveData<List<WasteBook>> wasteBooksLive;
    private ChartViewModel chartViewModel;
    private LineChart lineChart;
    private HashMap dataMap;
    private PieChart mPieChart;
    private Button bt_OUT,bt_IN;
    private TextView select;

    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> options1Items_type = new ArrayList<>(),options1Items_INOUT=new ArrayList<>();
    private ArrayList<ArrayList<String>> options1Items_date = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chart, container, false);
        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel.class);
        bt_IN=root.findViewById(R.id.textView_in_chart);
        bt_OUT=root.findViewById(R.id.textView_out_chart);
        bt_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOUT=false;
                selector(selectedStr);
            }
        });
        bt_OUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOUT=true;
                selector(selectedStr);
            }
        });
        chartViewModel.getAllWasteBookLive().observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                allWasteBooks=wasteBooks;
                selector(selectedStr);
            }
        });
        //饼状图数据，更新UI
        selectedWasteBooks.observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                for(WasteBook w:wasteBooks){
//                    Log.e("xxxxxxxxxxx",w.getCategory()+sdf.format(new Date(w.getTime())));
//                }
                dataMap = new HashMap();
                if(wasteBooks.isEmpty()){
                    PieChartUtils.getPitChart().setPieChart(mPieChart, dataMap, isOUT ? "支出" : "收入", true);
                }
                if(wasteBooks!=null&&!wasteBooks.isEmpty()) {

                    String categoriesTmp = "";
                    double[] cWeight = new double[wasteBooks.size()];
                    String[] categories = new String[wasteBooks.size()];
                    int i = 0;
                    for (WasteBook w : wasteBooks) {
                        String cTmp = w.getCategory();
                        if (!categoriesTmp.contains(cTmp)) {
                            categoriesTmp += cTmp + " ";
                            categories[i] = cTmp;
                            cWeight[i] += w.getAmount();
                            i++;
                        } else {
                            for (int j = 0; j < i; j++)
                                if (categories[j].equals(cTmp)) {
                                    cWeight[j] += w.getAmount();
                                    continue;
                                }
                        }
                    }
                    double sum = isOUT ? OUT : IN;
                    for (int j = 0; j < i; j++) {
                        dataMap.put(categories[j], cWeight[j] / sum * 100);
                    }
                    PieChartUtils.getPitChart().setPieChart(mPieChart, dataMap, isOUT ? "支出" : "收入", true);

                    int tmp=wasteBookAdapter.getItemCount();

                    wasteBookAdapter.setAllWasteBook(wasteBooks);
                    if(tmp!=wasteBooks.size()) {
                        wasteBookAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        //选择器
        select = root.findViewById(R.id.tv_select_chart);
        //饼状图
        mPieChart = root.findViewById(R.id.mPieChart);
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

        recyclerView = requireActivity().findViewById(R.id.recyclerView_wasteBook_chart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        wasteBookAdapter = new WasteBookAdapter(requireContext());
        recyclerView.setAdapter(wasteBookAdapter);


        initCustomOptionPicker();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvNoLinkOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String temp_date = options1Items_date.get(options1).get(options2);
                        select.setText(temp_date+"▼");
                        selectedStr=temp_date;
                        selector(temp_date);
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
        item_0.add("近两周");
        item_0.add("近三周");
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


    private void selector(String timeStr) {
        List<WasteBook> showWasteBooks=new ArrayList<>();
        IN=0.0;OUT=0.0;
        //支出
        if(isOUT){
           setStartEnd(timeStr);
           if (allWasteBooks!=null)
           for(WasteBook w:allWasteBooks){
               if(w.isType()&&w.getTime()>=end&&w.getTime()<=start){
                   showWasteBooks.add(w);
                   OUT+=w.getAmount();
               }
           }
        }else if(!isOUT){
            setStartEnd(timeStr);
            for(WasteBook w:allWasteBooks){
                if(!w.isType()&&w.getTime()>=end&&w.getTime()<=start){
                    showWasteBooks.add(w);
                    IN+=w.getAmount();
                }
            }
        }
        selectedWasteBooks.setValue(showWasteBooks);
    }

    private void setStartEnd(String timeStr){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        start= cal.getTimeInMillis();
        if(timeStr.contains("周")){
            if(timeStr.charAt(0)=='当'){
                //一周
                cal.add(Calendar.DAY_OF_MONTH,-7);
                end=cal.getTimeInMillis();
            }else if(timeStr.charAt(1)=='两'){
                //两周
                cal.add(Calendar.DAY_OF_MONTH,-14);
                end=cal.getTimeInMillis();

            }else if(timeStr.charAt(1)=='三'){
                //三周
                cal.add(Calendar.DAY_OF_MONTH,-21);
                end=cal.getTimeInMillis();
            }

        }else if(timeStr.contains("月")){
            if(timeStr.charAt(1)=='1'){
                //一个月
                cal.add(Calendar.MONTH, -1);
                end=cal.getTimeInMillis();
            }else if(timeStr.charAt(1)=='2'){
                //2个月
                cal.add(Calendar.MONTH, -2);
                end=cal.getTimeInMillis();

            }else if(timeStr.charAt(1)=='3'){
                //3个月
                cal.add(Calendar.MONTH, -3);
                end=cal.getTimeInMillis();
            }
        }else if(timeStr.contains("年")){
            //按年
            int yearTemp=Integer.parseInt(timeStr.substring(0,timeStr.length()-1));
            end=DateToLongUtils.dateToLong(yearTemp+"-1-1 00:00:00");
            start=DateToLongUtils.dateToLong(yearTemp+"-12-31 23:59:59");
        }
    }
}