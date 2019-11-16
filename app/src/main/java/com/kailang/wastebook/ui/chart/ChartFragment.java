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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.utils.DateToLongUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartFragment extends Fragment {

    private boolean isAll=false,isOUT=false;
    private Double IN=0.0,OUT=0.0,TOTAL=0.0;
    private long start,end;
    private List<WasteBook> allWasteBooks;
    private MutableLiveData<List<WasteBook>> selectedWasteBooks=new MutableLiveData<>();

    private ChartViewModel chartViewModel;
    private BarChart mBarChart;
    private PieChart mPieChart;
    //选择器
    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> options1Items_type = new ArrayList<>();
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
//                        String str = "type:" + options1Items_type.get(options1)
//                                + "\nyear:" + options1Items_date.get(options1).get(options2);
//                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                        String temp_date = options1Items_date.get(options1).get(options2);
                        select.setText(temp_date+"▼");
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
        LineChart mLineChart = (LineChart) root.findViewById(R.id.lineChart);
        //显示边界
        mLineChart.setDrawBorders(true);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);


        //饼状图
        mPieChart = (PieChart) root.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
       // mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
       // mPieChart.setOnChartValueSelectedListener(this);
        //模拟数据
        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();
        entries1.add(new PieEntry(40, "优秀"));
        entries1.add(new PieEntry(20, "满分"));
        entries1.add(new PieEntry(30, "及格"));
        entries1.add(new PieEntry(10, "不及格"));

        //设置数据
        setData2(entries1);
        //mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
        for (IDataSet<?> set : mPieChart.getData().getDataSets()){
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        mPieChart.invalidate();

        return root;
    }

    //设置数据,饼状图
    private void setData2(ArrayList<PieEntry> entries1){
        PieDataSet dataSet = new PieDataSet(entries1, "三年级一班");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data1 = new PieData(dataSet);
        data1.setValueFormatter(new PercentFormatter());
        data1.setValueTextSize(11f);
        data1.setValueTextColor(Color.WHITE);
        mPieChart.setData(data1);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }
    //设置数据,条形图
    private void setData3(ArrayList yVals1) {
        float start = 1f;
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "2017年工资涨幅");
            //设置有四种颜色
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            //设置数据
            mBarChart.setData(data);
        }
    }
    //二级联动
    private void initCustomOptionPicker() {
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