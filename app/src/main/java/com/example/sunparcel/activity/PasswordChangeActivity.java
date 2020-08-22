package com.example.sunparcel.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.example.sunparcel.R;
import com.example.sunparcel.model.BaseResponse;
import com.example.sunparcel.model.LoginRequest;
import com.example.sunparcel.retrofit.ApiClient;
import com.example.sunparcel.retrofit.ApiInterface;
import com.example.sunparcel.utils.LoaderUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sunparcel.utils.MathUtil.validatePassword;


public class PasswordChangeActivity extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button btn_password_update;
    String mobileNumber;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        Intent intent=getIntent();
        mobileNumber=intent.getStringExtra("MOBILENUM");

        newPassword = (EditText) findViewById(R.id.edit_new_password);
        confirmPassword = (EditText) findViewById(R.id.edit_confirm_password);

        btn_password_update=(Button)findViewById(R.id.btn_password_update);

        newPassword.addTextChangedListener(new PasswordChangeActivity.MyTextWatcher(newPassword));
        confirmPassword.addTextChangedListener(new PasswordChangeActivity.MyTextWatcher(confirmPassword));

        btn_password_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateForgetPassword();

            }
        });

    }

    private void updateForgetPassword() {
        dialog = LoaderUtil.showProgressBar(this);

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        LoginRequest loginRequest=new LoginRequest(mobileNumber,confirmPassword.getText().toString());

        Call<BaseResponse> call=apiInterface.updateForgetPassword();

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse=response.body();

                if(baseResponse.getSuccess()==true){
                    Intent intent=new Intent(PasswordChangeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }



            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            String password = newPassword.getText().toString().trim();
            String confirm_Password = confirmPassword.getText().toString().trim();

            btn_password_update.setEnabled(validatePassword(confirm_Password) && validatePassword(password) && password.equals(confirm_Password));

            if (btn_password_update.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_password_update.setBackground(getDrawable(R.drawable.rectangle_shpae));
                    btn_password_update.setTextColor(getApplication().getResources().getColor(R.color.white));
                }
            } else if (!btn_password_update.isEnabled()) {
                btn_password_update.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_password_update.setBackground(getDrawable(R.color.btn_disable));
                    btn_password_update.setTextColor(R.color.black);
                }
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}