package com.paynerg.host.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.paynerg.host.R;

public class EnterPinDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText editTextPin;

    public interface EnterPinDialogListener {
        void onFinishEnterPinDialog(String inputText);
    }

    public EnterPinDialogFragment() {
        // Required empty public constructor
    }

    public static EnterPinDialogFragment newInstance(String title) {
        EnterPinDialogFragment fragment = new EnterPinDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_pin_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextPin = view.findViewById(R.id.dialog_enter_pin);
        editTextPin.setOnEditorActionListener(this);
        String title = getArguments().getString("title", "Enter Pin");
        getDialog().setTitle(title);

        editTextPin.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.i("dialog fragment", "editor action");
        if(EditorInfo.IME_ACTION_DONE == actionId){
            Log.i("dialog fragment", "action done");
            EnterPinDialogListener listener = (EnterPinDialogListener) getActivity();
            assert listener != null;
            listener.onFinishEnterPinDialog(editTextPin.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }


}