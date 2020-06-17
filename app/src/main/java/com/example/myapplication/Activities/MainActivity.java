package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Fragments.*;
import com.example.myapplication.Fragments.ProfileFragment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_home_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_account_balance_wallet_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_monetization_on_black_24dp));
        navigationView.addSpaceItem(new SpaceItem("",R.drawable.ic_person_black_24dp));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        final Map<String, Object> data = new HashMap<>();
        data.put("id", user.getUid());
        data.put("balance", 0);
        data.put("debt", 0);

        final DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (!snapshot.exists()) {
                        db.collection("users").document(user.getUid()).set(data);
                    }

//                    final Map<String, Object> dummy = new HashMap<>();
//                    dummy.put("name", "TR1");
//                    dummy.put("type", "Income");
//                    dummy.put("amount", 10000);
//                    dummy.put("timestamp", FieldValue.serverTimestamp());
//
//                    ref.collection("transactions").add(dummy);
//                    ref.update("balance", FieldValue.increment((int)dummy.get("amount")));

//                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navbar);
//                    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                }
            }
        });

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            Fragment fragment = null;
            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                //navigationView.setCentreButtonSelectable(true);
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex+ " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex) {
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new WalletFragment();
                        break;
                    case 2:
                        fragment = new TransactionFragment();
                        break;
                    case 3:
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex+ " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment fragment = null;
//
//                    switch (item.getItemId()) {
//                        case R.id.nav_home:
//                            fragment = new HomeFragment();
//                            break;
//                        case R.id.nav_wallet:
//                            fragment = new WalletFragment();
//                            break;
//                        case R.id.nav_transactions:
//                            fragment = new TransactionFragment();
//                            break;
//                        case R.id.nav_profile:
//                            fragment = new ProfileFragment();
//                            break;
//                    }
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//
//                    return true;
//                }
//            };
}
