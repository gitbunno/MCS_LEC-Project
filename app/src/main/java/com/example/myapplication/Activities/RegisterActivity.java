package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLogin = findViewById(R.id.register_btn_login);
        btnLogin.setOnClickListener(loginListener);

        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(registerListener);
    }


    private View.OnClickListener loginListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            };

    private View.OnClickListener registerListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validate()){
                        finish();
                    }
                }
            };

    private boolean validate(){
        boolean valid = true;

        return valid;
    }
}
