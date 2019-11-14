package com.kailang.wastebook.ui.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.kailang.wastebook.R;


import com.kailang.wastebook.databinding.FragmentAddBinding;
import com.wihaohao.PageGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private AddViewModel addViewModel;
    private FragmentAddBinding binding;

    List<MyIconModel> mList,mList2;
    private PageGridView<MyIconModel> mPageGridView;


    public static AddFragment newInstance(int index) {
        AddFragment fragment = new AddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addViewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        addViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        binding.setVm(addViewModel);
        binding.setActivity(getActivity());
        binding.setLifecycleOwner(getViewLifecycleOwner());


        addViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.e("AddFragment",s);
                if(s.contains("1")) {
                    initData("支出");
                    mPageGridView =binding.getRoot().findViewById(R.id.vp_grid_view);
                    mPageGridView.setData(mList);
                    mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(requireContext(),position+"", Toast.LENGTH_SHORT).show();
                            addViewModel.setType(mList.get(position).getName());
                        }
                    });
                }
                if(s.contains("2")){
                    initData2("收入");
                    mPageGridView =binding.getRoot().findViewById(R.id.vp_grid_view);
                    mPageGridView.setData(mList2);
                    mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(requireContext(),position+"", Toast.LENGTH_SHORT).show();
                            addViewModel.setType(mList2.get(position).getName());
                        }
                    });
                }
            }
        });

        return binding.getRoot();
    }

    private void initData(String str) {
        mList=new ArrayList<>();
        for(int i=0;i<20;i++){
            mList.add(new MyIconModel(str+i,R.mipmap.ic_launcher));
        }
    }
    private void initData2(String str) {
        mList2=new ArrayList<>();
        for(int i=0;i<10;i++){
            mList2.add(new MyIconModel(str+i,R.mipmap.ic_launcher));
        }
    }
}