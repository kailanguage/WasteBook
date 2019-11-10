package com.kailang.wastebook.ui.category;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private SwipeRecyclerView mRecyclerView;
    private CategoryDragTouchAdapter adapter;
    private List<String> list;

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
        int index = 3;
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
        categoryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.contains("3")) {
                    mRecyclerView = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
                    adapter = new CategoryDragTouchAdapter(requireContext(), mRecyclerView);
                    mRecyclerView.setAdapter(adapter);

                    //读取数据库数据
                    list = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        list.add(i + "xxx");
                    }
                    adapter.notifyDataSetChanged(list);

                    // 设置菜单创建器。
//        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//        mRecyclerView.setSwipeItemMenuEnabled(true);
//        mRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
//            @Override
//            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(CategoryActivity.this)
//                        .setBackgroundColor(Color.RED)
//                        .setImage(R.drawable.ic_delete_forever_black_24dp) // 图标。
//                        .setText("删除") // 文字。
//                        .setTextColor(Color.WHITE) // 文字颜色。
//                        .setTextSize(16) // 文字大小。
//                        .setWidth(200)
//                        .setHeight(height);
//                rightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.
//            }
//        });
                    // 设置菜单Item点击监听。
//        mRecyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
//            @Override
//            public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
//                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
//                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
//                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
//                    Toast.makeText(CategoryActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
//                            .show();
//                } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
//                    Toast.makeText(CategoryActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
//                            .show();
//                }
//
//            }
//        });
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

                            Snackbar.make(root.findViewById(R.id.coordinatorlayout_category), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list.add(toDelete);
                                }
                            }).show();
                        }
                    });


                }
                if (s.contains("4")) {

                    mRecyclerView = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
                    adapter = new CategoryDragTouchAdapter(requireContext(), mRecyclerView);
                    mRecyclerView.setAdapter(adapter);

                    //读取数据库数据
                    list = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        list.add(i + "xxx");
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

                            Snackbar.make(root.findViewById(R.id.coordinatorlayout_category), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list.add(toDelete);
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
        category1.setId(1);
        category1.setIcon("category_out_1.png");
        category1.setType(1);
        category1.setName("健身");
        category1.setOrder(1);
        Category category2 = new Category();
        category2.setId(2);
        category2.setIcon("2.png");
        category2.setType(1);
        category2.setName("购物");
        category2.setOrder(2);
        Category category3 = new Category();
        category3.setId(1);
        category3.setIcon("3.png");
        category3.setType(1);
        category3.setName("通信");
        category3.setOrder(3);
        Category category4 = new Category();
        category4.setId(1);
        category4.setIcon("20.png");
        category4.setType(2);
        category4.setName("工资");
        category4.setOrder(1);
        categoryViewModel.insertCategory(category1);
        categoryViewModel.insertCategory(category2);
        categoryViewModel.insertCategory(category3);
        categoryViewModel.insertCategory(category4);
    }

}
