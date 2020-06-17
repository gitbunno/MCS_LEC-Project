package com.example.myapplication.Fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.EditActivity;
import com.example.myapplication.Activities.LoginActivity;
import com.example.myapplication.Activities.SplashActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAuth auth;
    FirebaseUser user;

    Button btnLogin, btnEdit;
    ImageView imageView;
    TextView txtGreetings, txtEmail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnLogin = v.findViewById(R.id.profile_btn_login);
        btnLogin.setOnClickListener(loginListener);

        btnEdit = v.findViewById(R.id.profile_btn_edit);
        btnEdit.setOnClickListener(editListener);

        imageView = v.findViewById(R.id.profile_img);
        imageView.setClipToOutline(true);

        txtGreetings = v.findViewById(R.id.profile_tv_greeting);
        txtEmail = v.findViewById(R.id.profile_tv_email);

        txtGreetings.setText("Hello, " + user.getDisplayName());
        txtEmail.setText(user.getEmail());

        RelativeLayout relativeLayout = v.findViewById(R.id.profile_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        Glide.with(v)
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.profile_icon)
                .centerCrop()
                .into(imageView);

        return v;
    }

    private View.OnClickListener loginListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            };

    private View.OnClickListener editListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditActivity.class);
                    startActivity(intent);
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        txtGreetings.setText("Hello, " + user.getDisplayName());
    }
}
