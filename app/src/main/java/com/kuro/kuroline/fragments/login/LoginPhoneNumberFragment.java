package com.kuro.kuroline.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
import com.kuro.kuroline.Home;
import com.kuro.kuroline.R;
import com.kuro.kuroline.fragments.utils.FragmentManagerUtils;

public class LoginPhoneNumberFragment extends Fragment {


    FirebaseAuth mAuth;

    TextInputEditText phoneNumberEditText;

    CountryCodePicker ccp;

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

            phoneNumberEditText = loginView.findViewById(R.id.login_phone_number);
            ccp = loginView.findViewById(R.id.ccp);

            // Attach CarrierNumber editText to CCP
            ccp.registerCarrierNumberEditText(phoneNumberEditText);

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
                    String phoneNumber;
                    phoneNumber = String.valueOf(phoneNumberEditText.getText());

                    if (TextUtils.isEmpty(phoneNumber)) {
                        Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (ccp.isValidFullNumber()) {
                        Toast.makeText(getContext(), "Valid phone number", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getContext(), "Invalid phone ", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }
        return loginView;
    }


}
