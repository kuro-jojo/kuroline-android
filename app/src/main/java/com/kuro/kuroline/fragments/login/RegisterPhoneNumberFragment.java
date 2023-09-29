package com.kuro.kuroline.fragments.login;

import static com.kuro.kuroline.fragments.utils.FragmentManagerUtils.*;

import android.os.Bundle;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.kuro.kuroline.R;
import com.kuro.kuroline.fragments.utils.FragmentManagerUtils;

import java.util.concurrent.TimeUnit;

public class RegisterPhoneNumberFragment extends Fragment {
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private String mPhoneNumber;
    TextInputEditText mPhoneNumberTextEdit;

    CountryCodePicker mCcp;

    Button mRegisterContinueBtn;

    TextView mAlreadyAccount;

    ImageButton mLoginGoogleBtn;
    ImageButton mLoginFacebookBtn;
    ImageButton mLoginEmailBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://kuroline-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        View registerView = inflater.inflate(R.layout.fragment_register, container,false);
        FirebaseUser currentUser = mAuth.getCurrentUser();


        mPhoneNumberTextEdit = registerView.findViewById(R.id.register_phone_number);

        mCcp = registerView.findViewById(R.id.register_ccp);
        mCcp.registerCarrierNumberEditText(mPhoneNumberTextEdit);

        mRegisterContinueBtn = registerView.findViewById(R.id.register_continue);

        mAlreadyAccount = registerView.findViewById(R.id.already_account);

        mLoginGoogleBtn = registerView.findViewById(R.id.login_google);
        mLoginFacebookBtn = registerView.findViewById(R.id.login_facebook);

        // open Login page for new user
        mAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new LoginPhoneNumberFragment(), getParentFragmentManager(), false);
            }
        });

        mCcp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // enable the button when the phone number is valid
                mRegisterContinueBtn.setEnabled(mCcp.isValidFullNumber());
            }
        });

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("COMPLETED", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(getActivity(), "onVerificationFailed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FAILED", e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Toast.makeText(getActivity(), "onCodeSent:" + verificationId, Toast.LENGTH_SHORT).show();
                VerificationFragment verificationFragment = VerificationFragment.newInstance(verificationId, mPhoneNumber);
                replaceFragment(verificationFragment, getParentFragmentManager(), false);
            }

        };

        mRegisterContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneNumber = mCcp.getFormattedFullNumber();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(getActivity())                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    return registerView;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getActivity(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getActivity(), "signInWithCredential:failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
