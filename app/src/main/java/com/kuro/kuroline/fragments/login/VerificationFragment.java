package com.kuro.kuroline.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kuro.kuroline.R;
import com.kuro.kuroline.fragments.utils.FragmentManagerUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificationFragment extends Fragment {

    private static final String VERIFICATION_ID = "verificationID";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String REGISTER_FRAGMENT = "registerFragment";

    private RegisterPhoneNumberFragment mRegisterFragment;

    private FirebaseAuth mAuth;

    private String mVerificationID;
    private String mPhoneNumber;
    private String mOTPCode;

    private TextView mCodeToSentMessage;

    private Button mVerificationContinueBtn;

    private EditText mOTPCodeEditText1;
    private EditText mOTPCodeEditText2;
    private EditText mOTPCodeEditText3;
    private EditText mOTPCodeEditText4;
    private EditText mOTPCodeEditText5;
    private EditText mOTPCodeEditText6;

    public VerificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param verificationID Parameter 1.
     * @return A new instance of fragment VerificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificationFragment newInstance(String verificationID, String phoneNumber) {
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        args.putString(VERIFICATION_ID, verificationID);
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVerificationID = getArguments().getString(VERIFICATION_ID);
            mPhoneNumber = getArguments().getString(PHONE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View verificationView = inflater.inflate(R.layout.fragment_verification, container, false);
        mAuth = FirebaseAuth.getInstance();

        String code_to_sent_message = getString(R.string.code_to_sent_message);

        mCodeToSentMessage = verificationView.findViewById(R.id.code_to_sent_message);
        mCodeToSentMessage.setText(String.format(code_to_sent_message, mPhoneNumber));

        mOTPCodeEditText1 = verificationView.findViewById(R.id.phone_number1);
        mOTPCodeEditText2 = verificationView.findViewById(R.id.phone_number2);
        mOTPCodeEditText3 = verificationView.findViewById(R.id.phone_number3);
        mOTPCodeEditText4 = verificationView.findViewById(R.id.phone_number4);
        mOTPCodeEditText5 = verificationView.findViewById(R.id.phone_number5);
        mOTPCodeEditText6 = verificationView.findViewById(R.id.phone_number6);


        mVerificationContinueBtn = verificationView.findViewById(R.id.verify_continue_btn);
        mVerificationContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, mOTPCode);
                signInWithPhoneAuthCredential(credential);
            }
        });

        // Add TextChangedListener to handle OTP input
        OTPTextWatcher textWatcher1 = new OTPTextWatcher(mOTPCodeEditText1, mOTPCodeEditText2, null);
        OTPTextWatcher textWatcher2 = new OTPTextWatcher(mOTPCodeEditText2, mOTPCodeEditText3, mOTPCodeEditText1);
        OTPTextWatcher textWatcher3 = new OTPTextWatcher(mOTPCodeEditText3, mOTPCodeEditText4, mOTPCodeEditText2);
        OTPTextWatcher textWatcher4 = new OTPTextWatcher(mOTPCodeEditText4, mOTPCodeEditText5, mOTPCodeEditText3);
        OTPTextWatcher textWatcher5 = new OTPTextWatcher(mOTPCodeEditText5, mOTPCodeEditText6, mOTPCodeEditText4);
        OTPTextWatcher textWatcher6 = new OTPTextWatcher(mOTPCodeEditText6, null, mOTPCodeEditText5);

        mOTPCodeEditText1.setOnKeyListener(textWatcher1);
        mOTPCodeEditText2.setOnKeyListener(textWatcher2);
        mOTPCodeEditText3.setOnKeyListener(textWatcher3);
        mOTPCodeEditText4.setOnKeyListener(textWatcher4);
        mOTPCodeEditText5.setOnKeyListener(textWatcher5);
        mOTPCodeEditText6.setOnKeyListener(textWatcher6);

        mOTPCodeEditText1.addTextChangedListener(textWatcher1);
        mOTPCodeEditText2.addTextChangedListener(textWatcher2);
        mOTPCodeEditText3.addTextChangedListener(textWatcher3);
        mOTPCodeEditText4.addTextChangedListener(textWatcher4);
        mOTPCodeEditText5.addTextChangedListener(textWatcher5);

        VerificationFragment verificationFragment = this;
        // Add TextChangedListener to the last field for OTP verification
        mOTPCodeEditText6.addTextChangedListener(new OTPTextWatcher(mOTPCodeEditText6, null, mOTPCodeEditText5) {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // When the last field is filled, you can verify the OTP here
                mOTPCode = mOTPCodeEditText1.getText().toString() +
                        mOTPCodeEditText2.getText().toString() +
                        mOTPCodeEditText3.getText().toString() +
                        mOTPCodeEditText4.getText().toString() +
                        mOTPCodeEditText5.getText().toString() +
                        mOTPCodeEditText6.getText().toString();

                Log.i("OTP", mOTPCode);
            }

        });
        return verificationView;

    }

    // TextWatcher to handle OTP input navigation
    private class OTPTextWatcher implements TextWatcher, View.OnKeyListener {
        private EditText currentField;
        private EditText nextField;
        private EditText previousField;

        public OTPTextWatcher(EditText currentField, EditText nextField, EditText previousField) {
            this.currentField = currentField;
            this.nextField = nextField;
            this.previousField = previousField;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1) {
                // Move to the next field when one digit is entered
                nextField.setEnabled(true);
                nextField.requestFocus();
            }
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (previousField != null) {
                    previousField.requestFocus();
                    currentField.setEnabled(false);
                }
            }
            return false;
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getActivity(), "Authentication succeeded", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            // TODO : UPDATE THIS
                            getActivity().finish();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                VerifyDialogFragment verifyDialogFragment = VerifyDialogFragment.newInstance("The code you entered is incorrect!");
                                verifyDialogFragment.show(getChildFragmentManager(),"DIALOG");
                                Toast.makeText(getActivity(), "Incorrect code", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}