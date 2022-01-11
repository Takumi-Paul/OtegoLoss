package com.example.otegoloss;

import android.widget.LinearLayout;

class ChangeBackgraund {
    private static int weight = 5;


    public static void changeBackGround(LinearLayout view){
        if (weight < 10) {
            view.setBackgroundResource(R.drawable.screen1);
        } else if (weight < 50) {
            view.setBackgroundResource(R.drawable.screen2);
        } else if (weight < 100) {
            view.setBackgroundResource(R.drawable.background);
        }
    }

}
