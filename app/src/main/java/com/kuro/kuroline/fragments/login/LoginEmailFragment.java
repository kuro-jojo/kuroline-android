package com.kuro.kuroline.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kuro.kuroline.Home;
import com.kuro.kuroline.R;
import com.kuro.kuroline.fragments.utils.FragmentManagerUtils;

public class LoginEmailFragment extends Fragment {


    FirebaseAuth mAuth;

    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;

    TextView forgotPassword;
    TextView newAccount;

    Button loginBtn;

    ImageButton logGoogleBtn;
    ImageButton logFacebookBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(getContext(), Home.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        if (loginView != null) {

            emailEditText = loginView.findViewById(R.id.login_email);
            passwordEditText = loginView.findViewById(R.id.login_password);

            forgotPassword = loginView.findViewById(R.id.forgot_password);
            newAccount = loginView.findViewById(R.id.new_account);

            loginBtn = loginView.findViewById(R.id.login_btn);


            logGoogleBtn = loginView.findViewById(R.id.login_google);
            logFacebookBtn = loginView.findViewById(R.id.login_facebook);


            // open Registration page for new user
            newAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManagerUtils.replaceFragment(new RegisterPhoneNumberFragment(), getParentFragmentManager(), true);
                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email, password;
                    email = String.valueOf(emailEditText.getText());
                    password = String.valueOf(passwordEditText.getText());

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getContext(), "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        Log.e("FIREBASE_ERROR_LOGIN", task.getException().getMessage());
                                    }
                                }
                            });
                }
            });
        }
        return loginView;
    }


}
