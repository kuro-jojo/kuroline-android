package com.kuro.kuroline.fragments.login;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kuro.kuroline.R;
import com.kuro.kuroline.data.model.User;
import com.kuro.kuroline.fragments.utils.FragmentManagerUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEmailFragment extends Fragment {
    private final int PASSWORD_MIN_LENGTH = 6;
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@(.+)$";
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText confirmPasswordEditText;

    Button registerBtn;

    TextView alreadyAccount;

    ImageButton logGoogleBtn;
    ImageButton logFacebookBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://kuroline-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        View registerView = inflater.inflate(R.layout.fragment_register, container,false);
        FirebaseUser currentUser = mAuth.getCurrentUser();


        usernameEditText = registerView.findViewById(R.id.register_username);
        emailEditText = registerView.findViewById(R.id.register_email);
        passwordEditText = registerView.findViewById(R.id.register_password);
        confirmPasswordEditText = registerView.findViewById(R.id.register_confirm_password);

        registerBtn = registerView.findViewById(R.id.register_btn);

        alreadyAccount = registerView.findViewById(R.id.already_account);

        logGoogleBtn = registerView.findViewById(R.id.login_google);
        logFacebookBtn = registerView.findViewById(R.id.login_facebook);

        // open Login page for new user
        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManagerUtils.replaceFragment(new LoginPhoneNumberFragment(), getParentFragmentManager(), false);
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, email, password, confirmPassword;
                email = String.valueOf(emailEditText.getText());
                username = String.valueOf(usernameEditText.getText());
                password = String.valueOf(passwordEditText.getText());
                confirmPassword = String.valueOf(confirmPasswordEditText.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getActivity(), "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(email)) {
                    return;
                }
                if (!checkPassword(password)) {
                    return;
                }
                if (!TextUtils.equals(password, confirmPassword)) {
                    Toast.makeText(getContext(), "Password and Confirmation password are different.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(username, email, password);
                                    String  uid = task.getResult().getUser().getUid();
                                    mDatabase.child("users").child(uid).setValue(user);
                                    Toast.makeText(getActivity(), "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    Log.e("FIREBASE_ERROR_REGISTER", task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    return registerView;
    }



    private boolean checkPassword(String password) {

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            Toast.makeText(getActivity(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO: Add more verifications to the password

        return true;
    }

    private boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }


        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(getActivity(), "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
