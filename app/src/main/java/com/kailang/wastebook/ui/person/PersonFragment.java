package com.kailang.wastebook.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.login.LoginActivity;
import com.kailang.wastebook.utils.DateToLongUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PersonFragment extends Fragment {

    private final static String YEAR_BUDGET="year_budget_total",MONTH_BUDGET="month_budget_total";
    private PersonViewModel personViewModel;
    private SharedPreferences shp;
    private TextView tv_total_wastebook,tv_year_budget_total,tv_month_budget_total,tv_year_budget_left,tv_month_budget_left,tv_logout;
    private TextView et_year_budget_total,et_month_budget_total;
    private String thisYear,thisMonth;
    private double yearTotal=0.0,monthTotal=0.0;
    private int y_b_shp,m_b_shp;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                ViewModelProviders.of(this).get(PersonViewModel.class);

        thisYear=DateToLongUtils.getSysYear();
        thisMonth=DateToLongUtils.getSysMonth();

        View root = inflater.inflate(R.layout.fragment_person, container, false);
        tv_total_wastebook=root.findViewById(R.id.tv_total_wastebook);

        tv_year_budget_total=root.findViewById(R.id.textView_year_budget);
        tv_year_budget_total.setText("设置"+thisYear+"年预算");
        tv_month_budget_total=root.findViewById(R.id.textView_month_budget);
        tv_month_budget_total.setText("设置"+thisMonth+"月预算");
        et_year_budget_total=root.findViewById(R.id.et_year_budget_total);
        et_month_budget_total=root.findViewById(R.id.et_month_budget_total);
        tv_year_budget_left=root.findViewById(R.id.tv_year_budget_left);
        tv_month_budget_left=root.findViewById(R.id.tv_month_budget_left);

        tv_logout=root.findViewById(R.id.textView_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shp = requireActivity().getSharedPreferences("budget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        if(shp.getAll()!=null){
            y_b_shp= shp.getInt(YEAR_BUDGET,0);
            if(y_b_shp!=0){
                et_year_budget_total.setText(y_b_shp+"元");
            }
             m_b_shp=shp.getInt(MONTH_BUDGET,0);
            if(m_b_shp!=0){
                et_month_budget_total.setText(m_b_shp+"元");
            }
        }

        personViewModel.getAllWasteBookLive().observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                tv_total_wastebook.setText(wasteBooks.size()+"条记录");
                if(wasteBooks!=null) {
                    yearTotal=0.0;monthTotal=0.0;
                    for(WasteBook w:wasteBooks) {
                        String yearStart = thisYear + "-01-01 00:00:00";
                        String monthStart = thisYear + "-" + thisMonth + "-1 00:00:00";
                        if (w.getTime()>=DateToLongUtils.dateToLong(yearStart)){
                            if(w.isType())
                            yearTotal+=w.getAmount();
                            if(w.getTime()>=DateToLongUtils.dateToLong(monthStart)){
                                if(w.isType())
                                    monthTotal+=w.getAmount();
                            }
                        }
                    }
                    if(y_b_shp!=0&&m_b_shp!=0){
                        tv_year_budget_left.setText((y_b_shp-yearTotal)+"元");
                        tv_month_budget_left.setText((m_b_shp-monthTotal)+"元");
                    }
                }
            }
        });


        et_year_budget_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = new EditText(getActivity());
                input.setText(et_year_budget_total.getText());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加年预算").setView(input)
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String tmp;
                        tmp =input.getText().toString().trim();
                        try{

                            if(!tmp.isEmpty()){
                                et_year_budget_total.setText(Integer.parseInt(input.getText().toString())+"元");
                                editor.putInt(YEAR_BUDGET,Integer.parseInt(input.getText().toString()));
                                editor.commit();
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(),"请输入合法信息",Toast.LENGTH_SHORT);
                        }

                    }
                }).show();
            }
        });

        et_month_budget_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = new EditText(getActivity());
                input.setText(et_month_budget_total.getText());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加月预算").setView(input)
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String tmp;
                        tmp =input.getText().toString().trim();
                        try{

                            if(!tmp.isEmpty()){
                                et_month_budget_total.setText(Integer.parseInt(input.getText().toString())+"元");
                                editor.putInt(MONTH_BUDGET,Integer.parseInt(input.getText().toString()));
                                editor.commit();
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(),"请输入合法信息",Toast.LENGTH_SHORT);
                        }
                    }
                }).show();


            }
        });
    }

}