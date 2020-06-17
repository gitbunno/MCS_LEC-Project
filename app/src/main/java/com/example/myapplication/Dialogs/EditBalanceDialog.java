package com.example.myapplication.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Fragments.HomeFragment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EditBalanceDialog extends DialogFragment {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference ref;
    Context context;
    EditText newBal;

    private long balance;
    private TextView bal;
    public EditBalanceDialog(long balance) {
        this.balance = balance;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        context = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_balance, null);
        newBal = view.findViewById(R.id.edit_balance_balance);
        newBal.setText(String.valueOf(balance));

        ref = firestore.collection("users").document(user.getUid());


        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("balance", Long.parseLong(newBal.getText().toString()));
                        ref.update(map);
                        Toast.makeText(getActivity(), "Save", Toast.LENGTH_SHORT).show();
                        HomeFragment.tvBalance.setText("IDR " + newBal.getText().toString());
                        HomeFragment.balance = Long.parseLong(newBal.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
