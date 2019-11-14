package com.kailang.wastebook.ui.category;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.snackbar.Snackbar;
import com.kailang.wastebook.R;
import com.kailang.wastebook.adapters.CategoryDragTouchAdapter;
import com.kailang.wastebook.data.Entity.Category;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";

    private CategoryViewModel categoryViewModel;

    private LiveData<List<Category>> allCategoriesLive;
    private List<Category> allCategories, INCategory, OUTCategory;
    private SwipeRecyclerView mRecyclerView,mRecyclerView2;
    private CategoryDragTouchAdapter adapter,adapter2;
    private List<String> list,list2;

    public static CategoryFragment newInstance(int index) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = ViewModelProviders.of(requireActivity()).get(CategoryViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        categoryViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        allCategoriesLive=categoryViewModel.getAllCategoriesLive();
        allCategoriesLive.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                allCategories=categories;
            }
        });

        list = new ArrayList<>();
        list2=new ArrayList<>();

        categoryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("CategoryFragment",s);
                if (s.contains("1")) {
                    mRecyclerView = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
                    adapter = new CategoryDragTouchAdapter(requireContext(), mRecyclerView);
                    mRecyclerView.setAdapter(adapter);
                    //读取数据库数据
                    for (int i = 0; i < 20; i++) {
                        list.add(i + " 支出");
                    }
                    adapter.notifyDataSetChanged(list);

                    mRecyclerView.setOnItemMoveListener(new OnItemMoveListener() {
                        @Override
                        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                            if (srcHolder.getItemViewType() != targetHolder.getItemViewType())
                                return false;

                            int fromPosition = srcHolder.getAdapterPosition();
                            int toPosition = targetHolder.getAdapterPosition();

                            Collections.swap(list, fromPosition, toPosition);
                            adapter.notifyItemMoved(fromPosition, toPosition);
                            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
                        }

                        @Override
                        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                            int position = srcHolder.getAdapterPosition();
                            String toDelete = list.get(position);
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                            //Toast.makeText(CategoryActivity.this, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
                            Snackbar.make(root.findViewById(R.id.fragment_category), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list.add(toDelete);
                                }
                            }).show();
                        }
                    });
                }
                if (s.contains("2")) {
                    mRecyclerView2 = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView2.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView2.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
                    adapter2 = new CategoryDragTouchAdapter(requireContext(), mRecyclerView2);
                    mRecyclerView2.setAdapter(adapter2);
                    //读取数据库数据
                    for (int i = 0; i < 20; i++) {
                        list2.add(i + "收入");
                    }
//                    if(allCategories!=null)
//                    for(Category c:allCategories){
//                        if(!c.isType()){
//                            list.add(c.getName());
//                        }
//                    }

                    adapter2.notifyDataSetChanged(list2);

                    mRecyclerView2.setOnItemMoveListener(new OnItemMoveListener() {
                        @Override
                        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                            if (srcHolder.getItemViewType() != targetHolder.getItemViewType())
                                return false;

                            int fromPosition = srcHolder.getAdapterPosition();
                            int toPosition = targetHolder.getAdapterPosition();

                            Collections.swap(list2, fromPosition, toPosition);
                            adapter2.notifyItemMoved(fromPosition, toPosition);
                            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
                        }

                        @Override
                        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                            int position = srcHolder.getAdapterPosition();
                            String toDelete = list2.get(position);
                            list2.remove(position);
                            adapter2.notifyItemRemoved(position);
                            //Toast.makeText(CategoryActivity.this, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();

                            Snackbar.make(root.findViewById(R.id.fragment_category), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list2.add(toDelete);
                                }
                            }).show();
                        }
                    });

                }
            }
        });
        return root;
    }

    //初始化category数据库
    private void initCategory() {
        Category category1 = new Category();
        category1.setIcon("category_out_1.png");
        category1.setType(true);
        category1.setName("健身");
        category1.setOrder(1);
        Category category2 = new Category();
        category2.setIcon("2.png");
        category2.setType(true);
        category2.setName("购物");
        category2.setOrder(2);
        Category category3 = new Category();
        category3.setIcon("3.png");
        category3.setType(true);
        category3.setName("通信");
        category3.setOrder(3);
        Category category4 = new Category();
        category4.setIcon("20.png");
        category4.setType(false);
        category4.setName("工资");
        category4.setOrder(1);
        categoryViewModel.insertCategory(category1);
        categoryViewModel.insertCategory(category2);
        categoryViewModel.insertCategory(category3);
        categoryViewModel.insertCategory(category4);
    }

}
