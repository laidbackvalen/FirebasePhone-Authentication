package com.example.firebasephone;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasephone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    String otp;
    private ActivityMainBinding binding;

//EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        editText = findViewById(R.id.editTextNumber);

        binding.buttonGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextPhone.getText().toString().isEmpty()) {
                    binding.editTextPhone.setError("It cannot be Empty");
                } else {
                    verifyPhoneNumber(binding.editTextPhone.getText().toString());
                }
            }
        });



        binding.buttonVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextNumber.getText().toString().isEmpty()) {
                    binding.editTextNumber.setError("It cannot be Empty");
                } else {
                    verifyCode(binding.editTextNumber.getText().toString());
                }
            }
        });

    }






    private void verifyPhoneNumber(String phon) {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phon)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);  //SENDING OTP
                        otp = s;

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String sms = phoneAuthCredential.getSmsCode();   //VERIFYING PHONE NUMBER

                        if (sms != null) {
                            verifyCode(sms);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(MainActivity.this, "NUMBER INCORRECT  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).build());


    }





    private void verifyCode(String sms) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, sms);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "invalid" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}