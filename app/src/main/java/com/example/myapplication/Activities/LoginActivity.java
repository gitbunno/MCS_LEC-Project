package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 243;

    FirebaseAuth firebaseAuth;

    Button btnLogin;
    TextView btnRegister;
    EditText txtEmail, txtPassword;
    TextInputLayout tilEmail, tilPassword;
    ProgressDialog progressDialog;

    GoogleSignInClient mgGoogleSignInClient;
    SignInButton sgnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        sgnGoogle = findViewById(R.id.login_sgn_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sgnGoogle.setOnClickListener(googleListener);

        tilEmail = findViewById(R.id.login_til_email);
        tilPassword = findViewById(R.id.login_til_password);

        txtEmail = findViewById(R.id.login_et_email);
        txtPassword = findViewById(R.id.login_et_password);

        btnLogin = findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(loginListener);

        btnRegister = findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(registerListener);

    }

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
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            boolean valid = true;

            //Nyalain disini (Progress bar)
            progressDialog = new ProgressDialog(LoginActivity.this);
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
            } else {
                tilPassword.setError(null);
            }

            if(valid){
                firebaseAuthNormal(email, password);
            }else{
                progressDialog.dismiss();
            }

        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener googleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn();
        }
    };

    void signIn() {
        Intent intent = mgGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthGoogle(account.getIdToken());
            } catch (ApiException e) {
                tilPassword.setError("Login failed");
            }

        }

    }

    private void firebaseAuthGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            tilPassword.setError("Login failed");
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void firebaseAuthNormal(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                        }
                        progressDialog.dismiss();
                    }
                });
    }
}