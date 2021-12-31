/*
12/29
Kobayashi
マイページ画面の処理のプログラム
 */

package com.example.otegoloss.ui.mypage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MypageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MypageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Mypage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
