package com.example.sunparcel.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sunparcel.R;
import com.example.sunparcel.model.BaseResponse;
import com.example.sunparcel.model.EmailUpdateRequest;
import com.example.sunparcel.model.LoginResponse;
import com.example.sunparcel.model.MobileNumUpdateRequest;
import com.example.sunparcel.model.PasswordUpdateRequest;
import com.example.sunparcel.model.UserNameUpdateRequest;
import com.example.sunparcel.retrofit.ApiClient;
import com.example.sunparcel.retrofit.ApiInterface;
import com.example.sunparcel.utils.LoaderUtil;
import com.example.sunparcel.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment {

    RelativeLayout userNameBlock, mobileNumberBlock, emailBlock, passwordBlock;

    TextView userNameEditText, mobileNumberEditText, emailEditText;

    String getMobileNumberFromServer;

    Dialog dialog;

    String userName, userEmail, userMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userNameBlock = (RelativeLayout) view.findViewById(R.id.userNameBlock);
        mobileNumberBlock = (RelativeLayout) view.findViewById(R.id.mobileNumberBlock);
        emailBlock = (RelativeLayout) view.findViewById(R.id.emailBlock);
        passwordBlock = (RelativeLayout) view.findViewById(R.id.passwordBlock);

        userNameEditText = (TextView) view.findViewById(R.id.userName);
        mobileNumberEditText = (TextView) view.findViewById(R.id.mobileNumber);
        emailEditText = (TextView) view.findViewById(R.id.email);


        dogetUserDetails();

        userNameBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Enter Your Name", 1);
            }
        });
        mobileNumberBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Enter Mobile Number", 2);
            }
        });

        emailBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Email", 3);
            }
        });

        passwordBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Enter Password", 4);
            }
        });

        return view;
    }

    private void dogetUserDetails() {
        dialog = LoaderUtil.showProgressBar(getActivity());
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.doGetUserDetails(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //System.out.println("ReceivedUserId" + loginResponse.getUserId());
                LoginResponse loginResponse = response.body();
                userName = loginResponse.getUserName();
                userEmail = loginResponse.getUserEmail();
                userMobile = loginResponse.getUserMobileNumber();

                if (userName != null) {
                    userNameEditText.setText(userName);

                }
                if (userEmail != null) {
                    emailEditText.setText(userEmail);
                } else {
                    emailEditText.setText("null");
                }

                if (userMobile != null) {
                    mobileNumberEditText.setText(userMobile);
                } else {
                    mobileNumberEditText.setText("null");
                }

                LoaderUtil.dismisProgressBar(getActivity(), dialog);


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }


    private void openDialog(final String hintData, final int i) {

        // dialog=LoaderUtil.showProgressBar(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog_userprofile, null);

        builder.setView(view);

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText getUserUpdateData = view.findViewById(R.id.updateName);
                updateUserDetails(getUserUpdateData.getText().toString(), i);



            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void updateUserDetails(String getUserUpdateData, int i) {

        dialog = LoaderUtil.showProgressBar(getActivity());
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
        Call<BaseResponse> call = null;
        if (i == 1) {
//name
            UserNameUpdateRequest userNameUpdateRequest = new UserNameUpdateRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), getUserUpdateData);
            call = apiInterface.updateUserName(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN), userNameUpdateRequest);

        } else if (i == 2) {
//mobile
            MobileNumUpdateRequest mobileNumUpdateRequest = new MobileNumUpdateRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), getUserUpdateData);
            call = apiInterface.updateMobileNumber(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN), mobileNumUpdateRequest);
        } else if (i == 3) {
//email
            EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), getUserUpdateData);
            call = apiInterface.updateEmail(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN), emailUpdateRequest);
        } else if (i == 4) {
            //password
            PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), getUserUpdateData);
            call = apiInterface.updatePassword(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN), passwordUpdateRequest);
        }

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {

                    BaseResponse baseResponse = response.body();


                    LoaderUtil.dismisProgressBar(getActivity(), dialog);

                    dogetUserDetails();



                    Toast.makeText(getActivity(), "updated ", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(getActivity(), dialog);
            }
        });


    }

}