package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTransactionActivity extends AppCompatActivity {

    TextInputLayout tilName, tilDate, tilPrice;
    EditText etName, etDate, etPrice;
    Button btnConfirm, btnCancel;
    ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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

            if(valid){
                Map<String, Object> dummy = new HashMap<>();
                dummy.put("name", etName.getText().toString());
                //date pake timestamp
                dummy.put("date", etDate.getText().toString());
                dummy.put("price", Integer.parseInt(etPrice.getText().toString()));
                dummy.put("timestamp", FieldValue.serverTimestamp());

                db.collection("users").document(user.getUid()).collection("transactions").add(dummy).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        }
                    }
                });
            }

            //Matiin progress bar
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
