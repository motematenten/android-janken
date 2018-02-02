package com.example.tanakatensho.finalpresentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    final int JANKEN_GU = 0;
    final int JANKEN_CHOKI = 1;
    final int JANKEN_PA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int mySign;
        Intent intent = getIntent ();
        int id = intent.getIntExtra("MY_HAND", 0);

        ImageView mySignImageView = (ImageView) findViewById(R.id.my_hand_image);
        switch(id){
            case R.id.gu:
                mySignImageView.setImageResource(R.drawable.gu);
                mySign = JANKEN_GU;
                break;
            case R.id.choki:
                mySignImageView.setImageResource(R.drawable.choki);
                mySign = JANKEN_CHOKI;
                break;
            case R.id.pa:
                mySignImageView.setImageResource(R.drawable.pa);
                mySign = JANKEN_PA;
                break;
            default:
                mySign = JANKEN_GU;
                break;
        }

        int comHand = getCPU();
        ImageView comHandImageView = (ImageView) findViewById(R.id.com_hand_image);
        switch (comHand){
            case JANKEN_GU:
                comHandImageView.setImageResource(R.drawable.com_gu);
                break;
            case JANKEN_CHOKI:
                comHandImageView.setImageResource(R.drawable.com_choki);
                break;
            case JANKEN_PA:
                comHandImageView.setImageResource(R.drawable.com_pa);
                break;
        }
        TextView resultLabel =(TextView) findViewById(R.id.result_label);
        int gameResult = (comHand - mySign + 3) % 3;
        switch (gameResult){
            case 0:
                resultLabel.setText(R.string.result_draw);
                break;
            case 1:
                resultLabel.setText(R.string.result_win);
                break;
            case 2:
                resultLabel.setText(R.string.result_lose);
                break;
        }
        saveData(mySign, comHand, gameResult);
    }
    public void onBackButtonTapped(View view){
        finish();
    }

    private void saveData(int myHand, int comHand, int gameResult){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        int gameCount = pref.getInt("GAME_COUNT", 0);
        int winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0);
        int lastComSign = pref.getInt("LAST_COM_HAND",0);
        int lastGameResult = pref.getInt("GAME_RESULT", -1);
        editor.putInt("GAME_COUNT", gameCount +1);
        if(lastGameResult == 2 && gameResult==2){
            editor.putInt("WINNING_STREAK_COUNT", winningStreakCount +1);
        } else {
            editor.putInt("WINNING_STREAK_COUNT", 0);
        }
        editor.putInt("LAST_MY_HAND", myHand);
        editor.putInt("LAST_COM_HAND", comHand);
        editor.putInt("BEFORE_LAST_COM_HAND", lastComSign);
        editor.putInt("GAME_RESULT", gameResult);

        editor.commit();
    }

    private int getCPU(){
        int cpu = (int)(Math.random() *3);
        SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
        int gameCount = prf.getInt("GAME_COUNT", 0);
        int winningStreakCount = prf.getInt("WINNING_STREAM_COUNT", 0);
        int lastMySign = prf.getInt("LAST_MY_HAND", 0);
        int lastComSign = prf.getInt("LAST_COM_HAND", 0);
        int beforeLastComSign = prf.getInt("BEFORE_LAST_COM_HAND", 0);
        int gameResult = prf.getInt("GAME_RESULT", -1);

        if (gameCount==1) {
            if (gameResult ==2) {
                while(lastComSign==cpu){
                    cpu = (int) (Math.random()*3);
                }
            } else if (gameResult==1){
                cpu = (lastMySign-1+3)%3;
            }
        } else if (winningStreakCount>0){
            if(beforeLastComSign==lastComSign){
                while(lastComSign==cpu){
                    cpu=(int)(Math.random()*3);
                }
            }
        }
        return cpu;
    }
}
