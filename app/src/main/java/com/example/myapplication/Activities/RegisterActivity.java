package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView btnLogin;
    FirebaseAuth auth;
    EditText txtUsername, txtEmail, txtPassword, txtConfirm;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        txtUsername = findViewById(R.id.register_et_username);
        txtEmail = findViewById(R.id.register_et_email);
        txtPassword = findViewById(R.id.register_et_password);
        txtConfirm = findViewById(R.id.register_et_cpassword);

        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(registerListener);

        btnLogin = findViewById(R.id.register_btn_login);
        btnLogin.setOnClickListener(loginListener);

    }

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            String username = txtUsername.getText().toString();

            // Tolong validasi password minimal 8 character

            firebaseCreate(email, password, username);
        }
    };

    private View.OnClickListener loginListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

    private void firebaseCreate(String email, String password, final String username) {

//        Toast.makeText(this, email + password, Toast.LENGTH_SHORT).show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            FirebaseUser user = auth.getCurrentUser();
                            user.updateProfile(profileChangeRequest);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
