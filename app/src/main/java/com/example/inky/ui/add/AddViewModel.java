package com.example.inky.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class AddViewModel extends ViewModel {

    private MutableLiveData<String> mText;
//    private MutableLiveData<List> listdata;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

//    public ListData() {
//        listdata = new  MutableLiveData<>();
//        listdata.setValue();
//    }

    public LiveData<String> getText() {
        return mText;
    }
}