package com.example.myapplication.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activities.AddAccountActivity;
import com.example.myapplication.Activities.LoginActivity;
import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Activities.RegisterActivity;
import com.example.myapplication.Adapters.AccountAdapter;
import com.example.myapplication.Adapters.TransactionAdapter;
import com.example.myapplication.Adapters.TransactionMiniAdapter;
import com.example.myapplication.Dialogs.EditBalanceDialog;
import com.example.myapplication.Objects.Transaction;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TransactionMiniAdapter mAdapter;
//    TransactionAdapter nAdapter;
    RecyclerView mRecyclerView, nRecyclerView;
    LinearLayoutManager mLinearLayoutManager, nLinearLayoutManager;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Button add;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    public static TextView tvBalance, no;
    ProgressDialog progressDialog;
    LinearLayout balanceLayout;
    public static long balance;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        final View v = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

//        add = v.findViewById(R.id.home_account_add);
//        add.setOnClickListener(addListener);

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

        tvBalance = v.findViewById(R.id.home_balance);
        balanceLayout = v.findViewById(R.id.home_balance_layout);

        balanceLayout.setOnClickListener(layoutListener);



        mAdapter = new TransactionMiniAdapter(v.getContext(), transactions);
        mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView = v.findViewById(R.id.home_rv_history);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        no = v.findViewById(R.id.home_history_tv_noitem);

//        add = v.findViewById(R.id.home_account_add);
//        add.setOnClickListener(addListener);


//        Transaction transaction = new Transaction("Box", "01/01/2000", "2.000.000", null);
//        Transaction transactiona = new Transaction("Boxs", "01/01/2000", "3.000.000", null);
//        transactions.add(transaction);
//        transactions.add(transactiona);




//        nAdapter = new TransactionAdapter(v.getContext(), transactions);
//        nLinearLayoutManager = new LinearLayoutManager(v.getContext());
//        nRecyclerView = v.findViewById(R.id.home_rv_history);
//        nRecyclerView.setHasFixedSize(true);
//        nRecyclerView.setLayoutManager(nLinearLayoutManager);
//        nRecyclerView.setAdapter(nAdapter);


        return v;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), AddAccountActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    long bal = doc.getLong("balance");
                    balance = bal;
                    tvBalance.setText("IDR " + balance);
                }
            }
        });

        transactions.clear();
        ref = db.collection("users").document(user.getUid());
        CollectionReference cRef = ref.collection("transactions");
        cRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Timestamp time = doc.getTimestamp("timestamp");
                        Date date = time.toDate();
                        String d = DateFormat.format("dd", date) + "/" + DateFormat.format("MM", date) + "/" + DateFormat.format("yyyy", date);

                        String category = doc.getString("category");
                        String name = doc.getString("name");
                        String method = doc.getString("method");
                        String desc = doc.getString("desc");

                        long price = 0;

                        try {
                            price = doc.getLong("amount");
                        } catch (Exception e) {
                            price = 0;
                        }

                        String amount = "";
                        int id = 0;

                        switch(category) {
                            case "Income":
                                amount = "+IDR " + price;
                                id = R.drawable.img_income;
                                break;
                            case "Paid Debt":
                                amount = "+IDR " + price;
                                id = R.drawable.img_pay;
                                break;
                            case "Expense":
                                amount = "-IDR " + price;
                                id = R.drawable.img_expense;
                                break;
                            case "Debt":
                                amount = "-IDR " + price;
                                id = R.drawable.img_debt;
                                break;
                        }

                        Transaction transaction = new Transaction(name, d, amount, id, category, method, desc);
                        transactions.add(transaction);
                        mAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                    if (transactions.isEmpty()) {
                        no.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private View.OnClickListener layoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment fragment = new EditBalanceDialog(balance);
            fragment.show(getParentFragmentManager(), "Dialog");
        }
    };
}
