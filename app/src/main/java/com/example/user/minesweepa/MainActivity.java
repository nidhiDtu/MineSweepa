package com.example.user.minesweepa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int level;
    private boolean win;
    private int covered;
    LinearLayout rootLayout;
    int size=10;
    MyButton grid[][];
    int values[][];
    private int marks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
        Intent intent=getIntent();
        level=intent.getIntExtra(StartActivity.LEVELSELECTED,1);
        setUpGrid();
    }

    private void setUpGrid() {
        rootLayout.removeAllViews();
        for(int i=0;i<(int)(1.5*size);i++){

            LinearLayout rowLayout=new LinearLayout(this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            rowLayout.setLayoutParams(params);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            for(int j=0;j<size;j++){

                final MyButton button=new MyButton(this,i,j);
                LinearLayout.LayoutParams buttonParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(buttonParams);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(values[button.getRow()][button.getCol()]==0){
                            onEmptyTile(button.getRow(),button.getCol());

                        }else if(values[button.getRow()][button.getCol()]==-1){
                            onGameOver();

                        }else{
                            onNumberTile(button.getRow(),button.getCol());
                        }
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(!(button.getText().equals("!"))){
                            button.setTextColor(Color.BLACK);
                            button.setText("!");
                            if(values[button.getRow()][button.getCol()]==-1){
                                marks+=100;
                            }
//                            button.setEnabled(false);
                            button.setBackgroundColor(Color.RED);
                            Toast.makeText(MainActivity.this,"Tile is Flaged!!",Toast.LENGTH_SHORT).show();
                        }else{
//                            button.setEnabled(true);
                            button.setBackgroundResource(R.drawable.my_button);
                            button.setTextColor(Color.WHITE);
                            button.setText(null);
                            if(values[button.getRow()][button.getCol()]==-1){
                                marks-=100;
                            }
                            Toast.makeText(MainActivity.this,"Flag is removed!!",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                values[i][j]=0;
                rowLayout.addView(button);
                grid[i][j]=button;
            }
            rootLayout.addView(rowLayout);
        }

        setBombs();
    }

    private void onNumberTile(int row, int col) {
        if(row>=(int)(1.5*size) || col>=size){
            return;
        }
        if(grid[row][col].isEnabled()){
            marks+=10*values[row][col];
            grid[row][col].setText(values[row][col]+"");
            grid[row][col].setEnabled(false);
            checkStatus();
        }
    }

    private void checkStatus() {
        covered++;
        if(covered==((size*(int)(size*1.5))-level*size)){
            onWinningGame();
        }
    }

    private void onWinningGame() {
//        Toast.makeText(MainActivity.this,"YOU WON THIS GAME!!",Toast.LENGTH_LONG).show();
        win=true;
        onGameOver();
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
        alertBuilder.setMessage("Your Score is :\n"+marks)
                .setCancelable(true).setPositiveButton("BACK TO MENU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent();
                finish();
            }
        });
        AlertDialog alert=alertBuilder.create();
        alert.setTitle("YOU WON THIS GAME !");
        alert.show();
    }

    private void onEmptyTile(int row, int col) {
        if (row<0 || col<0 || row >=(int)(1.5*size) || col>=size){
            return;
        }
        if(grid[row][col].isEnabled()){
            marks+=5;
            grid[row][col].setText("0");
            grid[row][col].setEnabled(false);
            checkStatus();
        }

        for(int i=row-1; i<=row+1;i++){
            for (int j=col-1;j<=col+1;j++){
                if(i<(int)(1.5*size) && i>=0 && j<size && j>=0){
                    if(i==row && j==col){
                        continue;
                    }else if(values[i][j]==0 && grid[i][j].isEnabled()){
                        onEmptyTile(i,j);
                    }else if(values[i][j]>0 && grid[i][j].isEnabled()){
                        onNumberTile(i,j);
                    }
                }
            }
        }

    }

    private void onGameOver() {
        for(int i=0;i<(int)(1.5*size);i++){
            for(int j=0;j<size;j++){
                if(values[i][j]==-1){
                    grid[i][j].setBackgroundResource(R.drawable.mine);
                }else {
                    grid[i][j].setText(values[i][j]+"");
                }
                grid[i][j].setEnabled(false);
            }
        }
        if(!win) {
//        Toast.makeText(MainActivity.this,"GAME OVER !",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Your Score is :\n" + marks)
                    .setCancelable(true).setPositiveButton("BACK TO MENU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    finish();
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("GAME OVER !");
            alert.show();

            SharedPreferences sharedPreferences=getSharedPreferences("MAX",MODE_PRIVATE);
            int max=sharedPreferences.getInt("MAX",0);

            if(marks>max){
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putInt("MAX",marks);
                editor.commit();
            }
        }
    }

    private void setBombs() {
        Random r=new Random();
        int mines=0;
        for(;mines<level*size;){
            int i=r.nextInt(20);
            int j=r.nextInt(10);

            if( i<(int)(1.5*size) && j<size && i>=0 && j >=0 && values[i][j]!=-1 ){
                values[i][j]=-1;
                mines++;
               setNeighboursValues(i,j);
            }
        }
    }

    private void setNeighboursValues(int row, int col) {

        for(int i=row-1;i<=row+1;i++){
            for(int j=col-1;j<=col+1;j++){
                if( i<(int)(1.5*size) && j<size && i>=0 && j>=0){
                    if(values[i][j]!=-1)
                       values[i][j]++;
                }
            }
        }
    }
    private void initialise() {
        rootLayout=findViewById(R.id.rootLayout);
        grid= new MyButton[(int)(1.5*size)][size];
        values=new int[(int)(1.5*size)][size];
        win=false;
        covered=0;
        marks=0;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.reset){
            initialise();
            setUpGrid();
        }else if((item.getItemId() == R.id.menu)){
            AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
            alertBuilder.setMessage("")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent();
                    finish();
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertBuilder.create();
            alert.setTitle("Do you want to end this game ?");
            alert.show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
