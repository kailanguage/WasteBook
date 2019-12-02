package com.kailang.wastebook.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.kailang.wastebook.R;
import com.kailang.wastebook.adapters.WasteBookAdapter;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.ui.add.AddActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditFragment extends Fragment {
    public static final String WASTEBOOK_EDIT = "wasteBook_edit";
    private DetailViewModel detailViewModel;
    private WasteBook wasteBook;
    private TextView type, amount, info, date, category;
    private ImageView icon;
    private Button bt_edit, bt_delete;
    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm E");

    public EditFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        type = root.findViewById(R.id.textView_edit_type);
        amount = root.findViewById(R.id.textView_edit_amount);
        date = root.findViewById(R.id.textView_edit_date);
        info = root.findViewById(R.id.textView_edit_info);
        icon = root.findViewById(R.id.imageView_edit_icon);
        category = root.findViewById(R.id.textView_edit_category);
        bt_edit = root.findViewById(R.id.button_edit_edit);
        bt_delete = root.findViewById(R.id.button_edit_delete);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        Gson gson = new Gson();
        String wasteBookJson = getArguments().getString(WASTEBOOK_EDIT);
        if (wasteBookJson != null) {
            wasteBook = gson.fromJson(wasteBookJson, WasteBook.class);
            if (wasteBook.isType()) {
                type.setText("支出");
                amount.setText("-" + mAmountFormat.format(wasteBook.getAmount()));
            } else {
                type.setText("收入");
                amount.setText("+" + mAmountFormat.format(wasteBook.getAmount()));
            }
            date.setText(sdf.format(new Date(wasteBook.getTime())));
            category.setText(wasteBook.getCategory());
            info.setText(wasteBook.getNote());
            icon.setImageDrawable(getContext().getDrawable(WasteBookAdapter.getDrawableId(wasteBook.getIcon())));
        }

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasteBook != null) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(WASTEBOOK_EDIT, wasteBookJson);
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(WASTEBOOK_EDIT,wasteBookJson);
//                    Navigation.findNavController(v).navigate(R.id.action_editFragment_to_navigation_add,bundle);
                }
            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasteBook != null)
                    detailViewModel.deleteWasteBook(wasteBook);
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
