/*
12/29
Kobayashi
出品者画面の処理プログラム
 */

package com.example.otegoloss.ui.shipping;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShippingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ShippingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Shipping fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
