package com.kailang.wastebook.ui.person;

import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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

    private PersonViewModel personViewModel;
    private SharedPreferences shp;
    private TextView tv_total_wastebook,tv_year_budget_total,tv_month_budget_total,tv_year_budget_left,tv_month_budget_left,tv_logout;
    private EditText et_year_budget_total,et_month_budget_total;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                ViewModelProviders.of(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person, container, false);
        tv_total_wastebook=root.findViewById(R.id.tv_total_wastebook);

        tv_year_budget_total=root.findViewById(R.id.textView_year_budget);
        tv_year_budget_total.setText("设置"+DateToLongUtils.getSysYear()+"年预算");
        tv_month_budget_total=root.findViewById(R.id.textView_month_budget);
        tv_month_budget_total.setText("设置"+DateToLongUtils.getSysMonth()+"月预算");
        et_year_budget_total=root.findViewById(R.id.et_year_budget_total);
        et_year_budget_total.addTextChangedListener(yearWatcher);
        et_month_budget_total=root.findViewById(R.id.et_month_budget_total);
        et_month_budget_total.addTextChangedListener(monthWatcher);
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
        personViewModel.getAllWasteBookLive().observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                tv_total_wastebook.setText(wasteBooks.size()+"条记录");
            }
        });



        shp = requireActivity().getSharedPreferences("budget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        String tmpYear=et_year_budget_total.getText().toString();

        if(tmpYear!=null&&!tmpYear.isEmpty()){
            editor.putInt("year_budget_total",Integer.parseInt(tmpYear));
            editor.commit();
        }
    }

    private TextWatcher yearWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("xxxx",s.toString());
        }
    };
    private TextWatcher monthWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("xxxx",s.toString());
        }
    };
}