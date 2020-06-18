package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;

    ProgressDialog progressDialog;

    TextView txtGreetings, tvBalance, tvDebt;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        tvBalance = v.findViewById(R.id.wallet_balance);
        tvDebt = v.findViewById(R.id.wallet_debt);

        progressDialog = new ProgressDialog(v.getContext());
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

        txtGreetings = v.findViewById(R.id.wallet_tv_greeting);

//        txtGreetings.setText("Hello, " + user.getDisplayName());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        txtGreetings.setText("Hello, " + user.getDisplayName());
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    long balance = doc.getLong("balance");
                    long debt = doc.getLong("debt");

                    tvBalance.setText("IDR " + balance);
                    tvDebt.setText("IDR " + debt);
                    progressDialog.dismiss();

                }
            }
        });
    }
}
