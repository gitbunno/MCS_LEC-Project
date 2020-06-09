package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Activities.AddAccountActivity;
import com.example.myapplication.Activities.AddTransactionActivity;
import com.example.myapplication.Adapters.TransactionAdapter;
import com.example.myapplication.Objects.Transaction;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TransactionAdapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    ArrayList<Transaction> transactions = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    FloatingActionButton add;
    boolean allowRefresh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
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
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

//        Transaction transaction = new Transaction("Box", "01/01/2000", "2.000.000", null);
////        Transaction transactiona = new Transaction("Boxs", "01/01/2000", "3.000.000", null);
////        transactions.add(transaction);
////        transactions.add(transactiona);

        mAdapter = new TransactionAdapter(v.getContext(), transactions);
        mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView = v.findViewById(R.id.transactions_rv_transactions);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        add = v.findViewById(R.id.transaction_fab_add);
        add.setOnClickListener(addListener);




        return v;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), AddTransactionActivity.class);
            startActivity(intent);
            allowRefresh = true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        if (allowRefresh)
//        {
//            allowRefresh = false;
//
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
        transactions.clear();
        CollectionReference cRef = db.collection("users").document(user.getUid()).collection("transactions");
        cRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        Toast.makeText(v.getContext(), "Name: " + doc.getString("name") + " ID: " + doc.getId(), Toast.LENGTH_SHORT).show();
                        Date date = doc.getDate("timestamp");
//                        Date date = new Date(doc.getDate("timestamp"));
                        String d = (date.getDay()+7) + "/" + (date.getMonth()+1) + "/" + (date.getYear()+1900);
//                        Toast.makeText(v.getContext(), "date: " + d, Toast.LENGTH_SHORT).show();
                        Transaction transaction = new Transaction(doc.getString("name"), d, "IDR " + doc.getLong("amount"), doc.getString("image"));
                        transactions.add(transaction);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}
