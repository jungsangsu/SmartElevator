package com.example.ice305.server2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UP_Down_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up__down_);
    }
    public void btn_bottonUp_Next(View v) //올라가기 버튼
    {
        Intent intent = new Intent(this, Elevator_RunningActivity.class);
        //화면 전환하기
        startActivity(intent);
    }
}
