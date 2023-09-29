package com.kuro.kuroline;

import static com.kuro.kuroline.fragments.utils.FragmentManagerUtils.addFragment;
import static com.kuro.kuroline.fragments.utils.FragmentManagerUtils.replaceFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kuro.kuroline.fragments.LoadingScreenFragment;
import com.kuro.kuroline.fragments.login.LoginPhoneNumberFragment;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            LoadingScreenFragment loadingScreenFragment = new LoadingScreenFragment();
            addFragment(loadingScreenFragment, getSupportFragmentManager());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    replaceFragment(new LoginPhoneNumberFragment(), getSupportFragmentManager(), false);
                }
            }, 1000);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();


    }


}