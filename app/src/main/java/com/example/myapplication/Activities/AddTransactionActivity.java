package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTransactionActivity extends AppCompatActivity {

    TextInputLayout tilName, tilDate, tilPrice;
    EditText etName, etDate, etPrice;
    Button btnConfirm, btnCancel;
    ProgressDialog progressDialog;
    Spinner spinner;

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
//        tilDate = findViewById(R.id.add_transaction_til_date);
        tilPrice = findViewById(R.id.add_transaction_til_price);

        etName = findViewById(R.id.add_transaction_et_name);
//        etDate = findViewById(R.id.add_transaction_et_date);
        etPrice = findViewById(R.id.add_transaction_et_price);

        btnConfirm = findViewById(R.id.add_btn_confirm);
        btnCancel = findViewById(R.id.add_btn_cancel);

        btnConfirm.setOnClickListener(confirmListener);
        btnCancel.setOnClickListener(cancelListener);

        spinner = (Spinner) findViewById(R.id.add_transaction_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }


    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString();
//            String date = etDate.getText().toString();
            String category = spinner.getSelectedItem().toString();
            String price = etPrice.getText().toString();

            boolean valid = true;


            if(name.isEmpty()){
                tilName.setError("Name must be filled");
                valid = false;
            } else {
                tilName.setError(null);
            }

//            if(date.isEmpty()){
//                tilDate.setError("Date must be filled");
//                valid = false;
//            } else {
//                tilDate.setError(null);
//            }

            if(price.isEmpty()){
                tilPrice.setError("Price must be filled");
                valid = false;
            } else {
                tilPrice.setError(null);
            }

            if(valid){
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

                String image = "";
                if (category.equals("Income")){
                    image = "https://i.postimg.cc/90hfvv3s/incomeA.png";
                } else if (category.equals("Expense")){
                    image = "https://i.postimg.cc/6p3QJrS6/expenseA.png";
                } else if (category.equals("Debt")){
                    image = "https://i.postimg.cc/xdXqHvyp/debtA.png";
                } else if (category.equals("Paid Debt")) {
                    image = "https://i.postimg.cc/FshRG5VL/payDebtA.png";
                }

                Map<String, Object> dummy = new HashMap<>();
                dummy.put("name", name);
                //date pake timestamp
//                dummy.put("date", etDate.getText().toString());
                dummy.put("amount", Integer.parseInt(price));
                dummy.put("timestamp", Calendar.getInstance().getTime());
                dummy.put("category", category);
                dummy.put("image", image);

                db.collection("users").document(user.getUid()).collection("transactions").add(dummy).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                });
                if (category.equals("Income")){
                    db.collection("users").document(user.getUid()).update("balance", FieldValue.increment((int)dummy.get("amount")));
                } else if (category.equals("Expense")){
                    db.collection("users").document(user.getUid()).update("balance", FieldValue.increment(-(int)dummy.get("amount")));
                } else if (category.equals("Debt")){
                    db.collection("users").document(user.getUid()).update("debt", FieldValue.increment((int)dummy.get("amount")));
                    db.collection("users").document(user.getUid()).update("balance", FieldValue.increment((int)dummy.get("amount")));
                } else if (category.equals("Paid Debt")) {
                    db.collection("users").document(user.getUid()).update("debt", FieldValue.increment(-(int) dummy.get("amount")));
                    db.collection("users").document(user.getUid()).update("balance", FieldValue.increment(-(int)dummy.get("amount")));
                }
//                finish();
            }
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
