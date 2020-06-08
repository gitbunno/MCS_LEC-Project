package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirm;
    Button btnConfirm, btnCancel;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        etUsername = findViewById(R.id.edit_et_name);
        etPassword = findViewById(R.id.edit_et_password);
        etConfirm = findViewById(R.id.edit_et_cpassword);

        etUsername.setText(user.getDisplayName());

        btnConfirm = findViewById(R.id.edit_btn_confirm);
        btnCancel = findViewById(R.id.edit_btn_cancel);

        btnConfirm.setOnClickListener(confirmListener);
        btnCancel.setOnClickListener(cancelListener);

    }

    private View.OnClickListener confirmListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String confirm = etConfirm.getText().toString();

                    if (username.isEmpty()) {
                        //error message username can't be empty <- minta bikin error message
                        return;
                    }

                    if (!password.isEmpty() || !confirm.isEmpty()) {

                        if (password.length() < 8) {
                            //password not long enough error <- minta bikin error message
                            return;
                        }

                        if (!password.equals(confirm)) {
                            //password not same error <- minta bikin error message
                            return;
                        }

                        user.updatePassword(password);
                    }

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) finish();
                        }
                    });
                }
            };

    private View.OnClickListener cancelListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };

}