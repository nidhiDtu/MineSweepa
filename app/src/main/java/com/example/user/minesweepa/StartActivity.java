package com.example.user.minesweepa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartActivity extends AppCompatActivity {

    public static final int EASY=1;
    public static final int MEDIUM=2;
    public static final int HARD=3;
    public static final String LEVELSELECTED="level_key";

    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private Button start;
    private int LEVEL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialise();
    }

    private void initialise() {
        radioGroup=findViewById(R.id.radio);
        start=findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this,MainActivity.class);

                int selectedRadioButton=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(selectedRadioButton);
                LEVEL=getLevelSelected(selectedRadioButton);

                intent.putExtra(LEVELSELECTED,LEVEL);
                startActivity(intent);
            }
        });
    }

    private int getLevelSelected(int id) {
        switch (id){
            case R.id.bt1:
                return 1;
            case R.id.bt2:
                return 2;
            case R.id.bt3:
                return 3;
        }
        return 1;
    }
}
