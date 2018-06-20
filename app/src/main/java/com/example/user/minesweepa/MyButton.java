package com.example.user.minesweepa;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;

/**
 * Created by user on 2/3/2018.
 */

public class MyButton extends AppCompatButton{
    private int row;
    private int col;
    boolean visited=false;

    public MyButton(Context context,int i,int j){
        super(context);
        this.setTextColor(Color.WHITE);
        this.row=i;
        this.col=j;
        this.setBackgroundResource(R.drawable.my_button);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
