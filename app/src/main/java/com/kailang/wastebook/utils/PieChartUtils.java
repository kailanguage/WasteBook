package com.kailang.wastebook.utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PieChartUtils {
    //设置各区域颜色
    public  final int[]  PIE_COLORS={
            Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243), Color.rgb(181, 194, 202)
    };
    private static PieChartUtils pieChartUtil;
    private List<PieEntry> entries;

    public static  PieChartUtils getPitChart(){
        if( pieChartUtil==null){
            pieChartUtil=new PieChartUtils();
        }
        return  pieChartUtil;
    }


    public void setPieChart(PieChart pieChart, Map<String, Float> pieValues, String title, boolean showLegend) {
        pieChart.setUsePercentValues(true);//设置使用百分比（后续有详细介绍）
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setRotationEnabled(true);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(true);//点击是否放大
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setDrawEntryLabels(true);
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(true);//环形

        pieChart.setExtraOffsets(0, 0, 0, 0); //设置边距
        // 0表示摩擦最大，基本上一滑就停
        // 1表示没有摩擦，会自动转化为0.9999,及其顺滑
        pieChart.setDragDecelerationFrictionCoef(0.35f);//设置滑动时的摩擦系数（值越小摩擦系数越大）
        pieChart.setCenterText(title);//设置环中的文字
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.setCenterTextSize(15f);//设置环中文字的大小
        pieChart.setCenterTextColor(PIE_COLORS[2]);
        pieChart.setRotationAngle(120f);//设置旋转角度

        pieChart.setTransparentCircleRadius(61f);//设置半透明圆环的半径,看着就有一种立体的感觉
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.TRANSPARENT);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);

        //图例设置
        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//方向
        } else {
            legend.setEnabled(false);
        }

        //设置饼图数据
        setPieChartData(pieChart, pieValues);

        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画

    }
    //设置饼图数据
    private void setPieChartData(PieChart pieChart, Map<String, Float> pieValues) {
        //遍历HashMap
        Set set = pieValues.entrySet();
        Iterator it = set.iterator();//得到适配器
        entries=new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(6f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        dataSet.setValueTextSize(5f);
        //设置数据显示方式有见图
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor( PIE_COLORS[3]);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//y轴数据显示在饼图内/外
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//x轴数据显示在饼图内/外

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }


}
