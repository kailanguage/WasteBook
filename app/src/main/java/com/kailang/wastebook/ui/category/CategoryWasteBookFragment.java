package com.kailang.wastebook.ui.category;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kailang.wastebook.R;
import com.kailang.wastebook.adapters.WasteBookAdapter;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.detail.DetailViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryWasteBookFragment#} factory method to
 * create an instance of this fragment.
 * **************BUG Fragment ************
 * I don't how to transact and it can't findViewById ,occur NullPointerException all the time .
 * so I make it become a activity
 */


public class CategoryWasteBookFragment extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "CategoryWasteBookFragment";

    // TODO: Rename and change types of parameters
    private String categoryWasteBook;
    private TextView tv_categoryWasteBook;
    private TextView tv_category_mount_wb;
    private ImageView imageViewBack;

    private RecyclerView recyclerView;
    private WasteBookAdapter wasteBookAdapter;
    private List<WasteBook> selectedWasteBooks;
    private DetailViewModel detailViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category_wastebook);
        if (getIntent() != null) {
            categoryWasteBook = getIntent().getStringExtra(ARG_PARAM1);
        }
        tv_categoryWasteBook = findViewById(R.id.textView_category_wb_total);
        tv_category_mount_wb = findViewById(R.id.textView_category_mount_wb);
        imageViewBack = findViewById(R.id.imageView_wb_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        recyclerView = findViewById(R.id.recyclerView_categoryWb);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wasteBookAdapter = new WasteBookAdapter(this, false);
        recyclerView.setAdapter(wasteBookAdapter);

        detailViewModel.getAllWasteBookLive().observe(this, new Observer<List<WasteBook>>() {
            @Override
            public void onChanged(List<WasteBook> wasteBooks) {
                if (wasteBooks != null) {
                    DecimalFormat mAmountFormat = new DecimalFormat("0.00");
                    int mount = 0;
                    double total = 0.0;
                    selectedWasteBooks = new ArrayList<>();
                    for (WasteBook w : wasteBooks) {
                        if (w.getCategory().equals(categoryWasteBook)) {
                            selectedWasteBooks.add(w);
                            total += w.getAmount();
                            mount++;
                        }
                    }
                    tv_category_mount_wb.setText("总计：" + mount + "条账单");
                    tv_categoryWasteBook.setText("共 " + mAmountFormat.format(total) + "元");
                    wasteBookAdapter.setAllWasteBook(selectedWasteBooks);
                    wasteBookAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
