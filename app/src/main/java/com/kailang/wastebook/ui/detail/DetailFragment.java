package com.kailang.wastebook.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.snackbar.Snackbar;
import com.kailang.wastebook.R;
import com.kailang.wastebook.adapters.WasteBookAdapter;
import com.kailang.wastebook.data.Entity.Category;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.add.AddActivity;
import com.kailang.wastebook.ui.category.CategoryActivity;
import com.kailang.wastebook.utils.DateToLongUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private WasteBookAdapter wasteBookAdapter;
    private LiveData<List<WasteBook>> wasteBooks;
    private MutableLiveData<List<WasteBook>> selectedWasteBooks=new MutableLiveData<>();
    private List<WasteBook> allWasteBooks;
    private DetailViewModel detailViewModel;
    private boolean isAll=false,isOUT=false;
    private Double IN=0.0,OUT=0.0,TOTAL=0.0;
    private long start,end;
    private String TAG="DetailFragment";

    //选择器
    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> options1Items_type = new ArrayList<>();
    private ArrayList<String> options1Items_year = new ArrayList<>();
    private ArrayList<String> options1Items_moonth = new ArrayList<>();

    private TextView tv_IN,tv_OUT,tv_TOTAL;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
                //Navigation.findNavController(getActivity().getCurrentFocus()).navigate(R.id.action_navigation_home_to_addFragment);
                break;
            case R.id.category_item:
                Intent intent2 = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_fragment_menu, menu);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        tv_IN=root.findViewById(R.id.sum_in);
        tv_OUT=root.findViewById(R.id.sum_out);
        tv_TOTAL=root.findViewById(R.id.balance);
//        final Spinner spinner = root.findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.time_items, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        //下拉列表选择事件监听器
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i >= 0)
//                    Toast.makeText(requireContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });

        //查询
        initCustomOptionPicker();
        final TextView select = root.findViewById(R.id.tv_select_detail);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvNoLinkOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        String str = "type:" + options1Items_type.get(options1)
//                                + "\nyear:" + options1Items_year.get(options2)
//                                + "\nmonth:" + options1Items_moonth.get(options3);
//                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                        String year = options1Items_year.get(options2);
                        String month = options1Items_moonth.get(options3);
                        String type = options1Items_type.get(options1);
                        if(month.equals("-"))month="";
                        if(year.contains("近"))month="";
                        selector(type,year,month);
                        select.setText(year+" "+month+" "+type+"▼");
                    }

                }).setSubmitText("确定")
                        .setCancelText("取消")
                        .setTitleText("查询")
                        .build();
                pvNoLinkOptions.setNPicker(options1Items_type, options1Items_year, options1Items_moonth);
                pvNoLinkOptions.show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化适配器
        detailViewModel = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        recyclerView = requireActivity().findViewById(R.id.recyclerView_memo);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        wasteBookAdapter = new WasteBookAdapter(requireContext());
        recyclerView.setAdapter(wasteBookAdapter);

        //Item单击编辑
        wasteBookAdapter.setOnItemClickListener(new WasteBookAdapter.WasteBookClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("memo", position);
                //Navigation.findNavController(v).navigate(R.id.action_memoFragment_to_editFragment,bundle);
            }
        });

        //筛选，更新UI
        selectedWasteBooks.observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
//                for(WasteBook w:wasteBooks)
//                    Log.e(TAG,"selected "+w.getTime());
                int tmp=wasteBookAdapter.getItemCount();
                wasteBookAdapter.setAllWasteBook(wasteBooks);
                if(tmp!=wasteBooks.size()) {
                    wasteBookAdapter.notifyDataSetChanged();
                }
                tv_IN.setText("+ "+IN);
                tv_OUT.setText("- "+OUT);
                TOTAL=IN-OUT;
                if(TOTAL>0)
                tv_TOTAL.setText(""+TOTAL);
                else tv_TOTAL.setText("无");
            }
        });

        //感知数据库更新，并更新UI
        wasteBooks = detailViewModel.getAllWasteBookLive();
        wasteBooks.observe(getViewLifecycleOwner(), new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                allWasteBooks = wasteBooks;
                int tmp = wasteBookAdapter.getItemCount();
                wasteBookAdapter.setAllWasteBook(wasteBooks);
                if (tmp != wasteBooks.size())
                    wasteBookAdapter.notifyDataSetChanged();

                IN=0.0;OUT=0.0;TOTAL=0.0;
                for(WasteBook w:wasteBooks){
                    if(w.isType())OUT+=w.getAmount();
                    else IN+=w.getAmount();
                }
                tv_IN.setText("+ "+IN);
                tv_OUT.setText("- "+OUT);
                TOTAL=IN-OUT;
                if(TOTAL>0)
                tv_TOTAL.setText(""+TOTAL);
                else tv_TOTAL.setText("无");
            }
        });


        //左右滑动启动删除功能
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Item位置移动
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final WasteBook wasteBookToDelete = allWasteBooks.get(viewHolder.getAdapterPosition());
                detailViewModel.deleteWasteBook(wasteBookToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.fragment_detail_CoordinatorLayout), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailViewModel.insertWasteBook(wasteBookToDelete);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void initCustomOptionPicker() {
        //选项1
        options1Items_type.add("全部");
        options1Items_type.add("支出");
        options1Items_type.add("收入");
        //选项2
        options1Items_year.add("近1个月");
        options1Items_year.add("近3个月");
        options1Items_year.add("近6个月");
        for(int i=2019;i>=2000;i--){
            options1Items_year.add(i+"年");
        }
        //选项3
        options1Items_moonth.add("-");
        for(int i=1;i<=12;i++){
            options1Items_moonth.add(i+"月");
        }
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
            start=DateToLongUtils.dateToLong(sdf.format(date));
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