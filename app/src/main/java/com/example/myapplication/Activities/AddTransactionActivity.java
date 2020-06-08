package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AddTransactionActivity extends AppCompatActivity {

    TextInputLayout tilName, tilDate, tilPrice;
    EditText etName, etDate, etPrice;
    Button btnConfirm, btnCancel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        tilName = findViewById(R.id.add_transaction_til_name);
        tilDate = findViewById(R.id.add_transaction_til_date);
        tilPrice = findViewById(R.id.add_transaction_til_price);

        etName = findViewById(R.id.add_transaction_et_name);
        etDate = findViewById(R.id.add_transaction_et_date);
        etPrice = findViewById(R.id.add_transaction_et_price);

        btnConfirm = findViewById(R.id.add_btn_confirm);
        btnCancel = findViewById(R.id.add_btn_cancel);

        btnConfirm.setOnClickListener(confirmListener);
        btnCancel.setOnClickListener(cancelListener);
    }


    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString();
            String date = etDate.getText().toString();
            String price = etPrice.getText().toString();

            boolean valid = true;

            //Nyalain disini (Progress bar)
            progressDialog = new ProgressDialog(AddTransactionActivity.this);
            //Show Dialog
            progressDialog.show();
            //Set Content View
            progressDialog.setContentView(R.layout.progress_dialog);
            //Set Transparent Background
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            if(name.isEmpty()){
                tilName.setError("Name must be filled");
                valid = false;
            } else {
                tilName.setError(null);
            }

            if(date.isEmpty()){
                tilDate.setError("Date must be filled");
                valid = false;
            } else {
                tilDate.setError(null);
            }

            if(price.isEmpty()){
                tilPrice.setError("Price must be filled");
                valid = false;
            } else {
                tilPrice.setError(null);
            }

            //Tambah akun ke database, tampilin di home -> accounts

            //Matiin progress bar
            progressDialog.dismiss();
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
