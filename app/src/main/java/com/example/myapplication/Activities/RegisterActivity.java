package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView btnLogin;
    FirebaseAuth auth;
    EditText txtUsername, txtEmail, txtPassword, txtConfirm;
    Button btnRegister;
    TextInputLayout tilUsername, tilEmail, tilPassword, tilConfirm;
    ProgressDialog progressDialog;

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

        tilUsername = findViewById(R.id.register_til_username);
        tilConfirm = findViewById(R.id.register_til_cpassword);
        tilPassword = findViewById(R.id.register_til_password);
        tilEmail = findViewById(R.id.register_til_email);

    }

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = txtUsername.getText().toString();
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            String confirm = txtConfirm.getText().toString();

            boolean valid = true;

            //Nyalain disini (Progress bar)
            progressDialog = new ProgressDialog(RegisterActivity.this);
            //Show Dialog
            progressDialog.show();
            //Set Content View
            progressDialog.setContentView(R.layout.progress_dialog);
            //Set Tapping Out False
            progressDialog.setCancelable(false);
            //Set Transparent Background
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            if(username.isEmpty()){
                valid = false;
                tilUsername.setError("Username must be filled");
            } else {
                tilUsername.setError(null);
            }

            if (email.isEmpty()){
                valid = false;
                tilEmail.setError("Email must be filled");
            } else if (!validEmail(email)) {
                valid = false;
                tilEmail.setError("Email is invalid, the accepted format is something like name@email.com");
            } else {
                tilEmail.setError(null);
            }

            if (password.isEmpty()){
                valid = false;
                tilPassword.setError("Password must be filled");
            } else if(password.length()<8){
                valid = false;
                tilPassword.setError("Password must be 8 digits or longer");
            } else {
                tilPassword.setError(null);
            }

            if(!confirm.equals(password)){
                valid = false;
                tilConfirm.setError("Confirmation Password and Password must be identical");
            } else {
                tilConfirm.setError(null);
            }

            if(!valid){
                progressDialog.dismiss();
                return;
            }

            firebaseCreate(email, password, username);

        }
    };

    private boolean validEmail(String email){
        if(!email.contains("@") || email.startsWith("@")) return false;

        int index = email.indexOf("@");
        email = email.substring(index+1);

        if(email.isEmpty() || email.contains("@") || !email.contains(".") || email.endsWith("."))
            return false;

        return true;
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private void firebaseCreate(String email, String password, final String username) {

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
                            tilEmail.setError("This email is connected to another account");
                            txtEmail.setText("");
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
