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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.kailang.wastebook.R;


import com.kailang.wastebook.data.Entity.Category;
import com.kailang.wastebook.databinding.FragmentAddBinding;
import com.kailang.wastebook.ui.category.CategoryViewModel;
import com.wihaohao.PageGridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "AddFragment_number";
    private AddViewModel addViewModel;
    private FragmentAddBinding binding;
    private List<MyIconModel> mList,mList2;
    private LiveData<List<Category>> allCategoriesLive;
    private List<Category> allCategories;
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
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        addViewModel.setIndex(index);
        allCategoriesLive=addViewModel.getAllCategoriesLive();
        allCategoriesLive.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                allCategories=categories;
                if(categories!=null&&!categories.isEmpty()){
                    Log.e("xxxxAddFragmentsize",allCategories.size()+"");
                    initData();
                    int index = 1;
                    if (getArguments() != null) {
                        index = getArguments().getInt(ARG_SECTION_NUMBER);
                    }
                    addViewModel.setIndex(index);
                for(Category c:allCategories)
                    Log.e("xxxxAddFragment",c.getName()+" "+c.getIcon());
                }
            }
        });

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        binding.setVm(addViewModel);
        binding.setActivity(getActivity());
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initData();
       // initData(addViewModel.getAllCategories());
        addViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.e("AddFragment",s);
                if(s.contains("1")) {
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


    private void initData() {
        mList=new ArrayList<>();
        mList2=new ArrayList<>();
        if (allCategories!=null)
        for(Category c:allCategories) {
            Field field = null;
            int res_ID;
            try {
                field = R.drawable.class.getField(c.getIcon());
                res_ID = field.getInt(field.getName());
            } catch (NoSuchFieldException e) {
                 res_ID = R.drawable.ic_category_out_1;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                 res_ID = R.drawable.ic_category_out_1;
                e.printStackTrace();
            }
            if(c.isType())
                mList.add(new MyIconModel(c.getName(), res_ID));
            if (!c.isType())
                mList2.add(new MyIconModel(c.getName(),res_ID));
        }
    }

}