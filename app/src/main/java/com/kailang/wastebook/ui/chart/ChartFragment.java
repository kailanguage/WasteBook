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

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {

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


//        //条形图
//        mBarChart = (BarChart) root.findViewById(R.id.mBarChart);
//        //设置表格上的点，被点击的时候，的回调函数
//        //mBarChart.setOnChartValueSelectedListener(this);
//        ArrayList<Integer> list = new ArrayList<>();
//        for(int i=100;i<10000;i=i+10){
//            list.add(i);
//        }
//        setData3(list);
//
//        mBarChart.setDrawBarShadow(false);
//        mBarChart.setDrawValueAboveBar(true);
//        mBarChart.getDescription().setEnabled(false);
//        // 如果60多个条目显示在图表,drawn没有值
//        mBarChart.setMaxVisibleValueCount(60);
//        // 扩展现在只能分别在x轴和y轴
//        mBarChart.setPinchZoom(false);
//        //是否显示表格颜色
//        mBarChart.setDrawGridBackground(false);
//        XAxis xAxis = mBarChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        // 只有1天的时间间隔
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(7);
//        xAxis.setAxisMaximum(50f);
//        xAxis.setAxisMinimum(0f);
//        //xAxis.setValueFormatter(xAxisFormatter);
//
//        YAxis leftAxis = mBarChart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
//        //这个替换setStartAtZero(true)
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(50f);
//        YAxis rightAxis = mBarChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMaximum(50f);

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

}