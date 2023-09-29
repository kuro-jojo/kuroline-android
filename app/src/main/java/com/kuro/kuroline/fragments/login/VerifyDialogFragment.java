package com.kuro.kuroline.fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kuro.kuroline.R;

public class VerifyDialogFragment extends DialogFragment {
    private static final String MESSAGE = "arg_text";

    // Method to create a new instance of the dialog with specified text
    public static VerifyDialogFragment newInstance(String text) {
        VerifyDialogFragment fragment = new VerifyDialogFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, text);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_verify_fragment, container, false);

        ImageView closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the close button is clicked
                dismiss();
            }
        });

        return view;
    }@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the text argument
        String text = getArguments().getString(MESSAGE);

        // Set the text of the TextView in the dialog
        TextView messageTextView = view.findViewById(R.id.dialog_message);
        messageTextView.setText(text);

        // Set up click listener for the close button
        View closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the close button is clicked
                dismiss();
            }
        });
    }


}
