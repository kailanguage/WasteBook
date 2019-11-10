package com.kailang.wastebook.ui.add;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.navigation.Navigation;

import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.WasteBook;
import com.kailang.wastebook.data.UserRepository;
import com.kailang.wastebook.data.WasteBookRepository;
import com.kailang.wastebook.utils.CommonUtils;
import com.kailang.wastebook.utils.DatePickUtils;
import com.kailang.wastebook.utils.ResUtils;
import com.kailang.wastebook.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private SimpleDateFormat mDateFormat;
    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private ObservableField<String> mDateText=new ObservableField<>();
    private ObservableField<String> mAmountText=new ObservableField<>();
    private ObservableField<String> mDesc=new ObservableField<>();
    private ObservableField<String> mType=new ObservableField<>();
    private long mDate;
    private double mAmount;
    private WasteBookRepository wasteBookRepository;

    public AddViewModel(@NonNull Application application) {
        super(application);
        mDateFormat = new SimpleDateFormat(ResUtils.getString(
                application, R.string.date_format_y_m_d) + " HH:mm", Locale.getDefault());
        mDate = System.currentTimeMillis();
        mDateText.set(mDateFormat.format(new Date(mDate)));
        wasteBookRepository=new WasteBookRepository(application);
    }


    /** 消费说明点击 */
    public void onDescClick(Activity activity) {
        EditText editText = activity.findViewById(R.id.tvDesc);
        editText.setCursorVisible(true);
    }

    /** 消费记录时间点击 */
    public void onDateClick(Activity activity) {
        DatePickUtils.showDatePickDialog(activity,
                mDate,
                new DatePickUtils.OnDatePickListener() {
                    @Override
                    public void onDatePick(DialogInterface dialog,
                                           int year,
                                           int month,
                                           int dayOfMonth) {
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog, long timeInMills) {
                        mDate = timeInMills;
                        mDateText.set(mDateFormat.format(new Date(mDate)));
                    }
                });
    }

    /** 键盘数字点击 */
    public void onNumberClick(String number) {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if ("0".equals(amount)) {
            amount = "";
        }
        amount += number;
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /** 键盘删除点击 */
    public void onDeleteClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!TextUtils.isEmpty(amount)) {
            amount = amount.substring(0, amount.length() - 1);
        }
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /** 键盘清除点击 */
    public void onClearClick() {
        mAmountText.set("0");
        mAmount = 0;
    }

    /** 键盘 . 点击 */
    public void onDotClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!amount.contains(".")) {
            amount = amount + ".";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }
    /** 确定点击 */
    public void onEnterClick(Activity activity) {
        if(getType().get().isEmpty()&&getAmountText().get().isEmpty()){
            Toast.makeText(activity,"请输入完整的信息",Toast.LENGTH_SHORT).show();
        }else {
            Boolean wasteBookType=true;
            if (getText().getValue().contains("2"))wasteBookType=false;
            Log.e("xxxxxxxxxxx",mAmountFormat.format(Double.parseDouble(getAmountText().get())));
            WasteBook wasteBook = new WasteBook(wasteBookType, Double.parseDouble(getAmountText().get()),getType().get(),mDate,getDesc().get().trim());
            saveData(wasteBook);
            //activity.finish();
        }
    }

    private void saveData(WasteBook wasteBook) {
        Log.e("xxxxxx","insert");
       wasteBookRepository.insertWasteBook(wasteBook);
    }

    public ObservableField<String> getDateText() {
        return mDateText;
    }

    public ObservableField<String> getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType.set(mType);
    }

    public ObservableField<String> getAmountText() {
        return mAmountText;
    }

    public ObservableField<String> getDesc() {
        if (mDesc.get()==null)
            mDesc.set("");
        return mDesc;
    }

    //pager data
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "index:" + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}