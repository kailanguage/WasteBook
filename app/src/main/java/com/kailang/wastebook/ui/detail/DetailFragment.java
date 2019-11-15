package com.kailang.wastebook.ui.detail;

import android.content.Intent;
import android.os.Bundle;
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
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.add.AddActivity;
import com.kailang.wastebook.ui.category.CategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private WasteBookAdapter wasteBookAdapter;
    private LiveData<List<WasteBook>> wasteBooks;
    private List<WasteBook> allWasteBooks;
    private DetailViewModel detailViewModel;

    //选择器
    private OptionsPickerView pvNoLinkOptions;
    private ArrayList<String> options1Items_type = new ArrayList<>();
    private ArrayList<String> options1Items_year = new ArrayList<>();
    private ArrayList<String> options1Items_moonth = new ArrayList<>();

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

        //按月查询
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
        wasteBookAdapter = new WasteBookAdapter(requireContext(),detailViewModel);
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
        options1Items_type.add("支出");
        options1Items_type.add("收入");
        options1Items_type.add("全部");
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
}