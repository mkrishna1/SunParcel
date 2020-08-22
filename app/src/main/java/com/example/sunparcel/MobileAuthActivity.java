package com.example.sunparcel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MobileAuthActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
//    private Button resendButton;

    private RelativeLayout generateOtpLayout, receiveOtpLayout;

    String phoneNumber;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_auth);

        FirebaseApp.initializeApp(MobileAuthActivity.this);

        phoneText = (EditText) findViewById(R.id.input_phoneTxt);
        codeText = (EditText) findViewById(R.id.pinView);
        verifyButton = (Button) findViewById(R.id.verifyOtpBtn);
        sendButton = (Button) findViewById(R.id.generateOtpBtn);
//        resendButton = (Button) findViewById(R.id.resendOtpBtn);
        generateOtpLayout = (RelativeLayout) findViewById(R.id.phoneAuth);
        receiveOtpLayout = (RelativeLayout) findViewById(R.id.receiveOTP);


        ccp = (CountryCodePicker) findViewById(R.id.ccp);

//        verifyButton.setEnabled(false);
//        resendButton.setEnabled(false);


        fbAuth = FirebaseAuth.getInstance();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phoneText.getText().toString();

                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(MobileAuthActivity.this, "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    ccp.registerCarrierNumberEditText(phoneText);
                    phoneNumber = ccp.getFullNumberWithPlus();

//                    setUpVerificatonCallbacks();

                    try {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                MobileAuthActivity.this,               // Activity (for callback binding)
                                verificationCallbacks);
                    }
                    catch (Exception e) {
                        Toast.makeText(MobileAuthActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                generateOtpLayout.setVisibility(View.INVISIBLE);

                receiveOtpLayout.setVisibility(View.VISIBLE);
            }
        });


        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

//                        resendButton.setEnabled(false);
//                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(MobileAuthActivity.this,"Invalid number, please enter correct number with your country code...", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(MobileAuthActivity.this, e.getMessage() + "Something went wrong, try again later...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
//                        resendButton.setEnabled(true);
                        Toast.makeText(MobileAuthActivity.this,"Code has been sent, please check and verify...", Toast.LENGTH_SHORT).show();
                    }
                };



        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeText.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(MobileAuthActivity.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential =
                            PhoneAuthProvider.getCredential(phoneVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }





    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            codeText.setText("");
//                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();

                            Intent intent = new Intent(MobileAuthActivity.this, MainActivity.class);
                            intent.putExtra("phone", phoneNumber);
                            startActivity(intent);
                            finish();

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(MobileAuthActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}