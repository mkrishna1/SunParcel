package com.example.sunparcel.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sunparcel.R;
import com.example.sunparcel.model.DirectionsApiResponseDTO;
import com.example.sunparcel.retrofit.ApiClient;
import com.example.sunparcel.retrofit.ApiInterface;
import com.example.sunparcel.utils.AppConstant;
import com.example.sunparcel.utils.LoaderUtil;
import com.example.sunparcel.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPriceFragment extends Fragment {

    TextView price;
    String km_str;
    Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Toast.makeText(getActivity(), "I am Called", Toast.LENGTH_LONG).show();

        View view = inflater.inflate(R.layout.fragment_showprice, container, false);

        price = (TextView) view.findViewById(R.id.price);

        getRouteDistance();


        return view;
    }


    private void getRouteDistance() {

         dialog = LoaderUtil.showProgressBar(getActivity());

        String originLat = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_A_LAT);
        String originLong = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_A_LONG);

        System.out.println("FromPoint" + originLat + " " + originLong);

        String destiLat = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_B_LAT);
        String destiLong = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_B_LONG);

        System.out.println("ToPoint" + destiLat + " " + destiLong);


        ApiInterface api = ApiClient.getAPIClient().create(ApiInterface.class);

        //Call<DirectionsApiResponseDTO> directionsApiResponseDTOCall = api.getRouteDistance("13.0223,80.1749", "13.0735,80.2214", AppConstant.API_KEY);

        Call<DirectionsApiResponseDTO> directionsApiResponseDTOCall = api.getRouteDistance(originLat + "," + originLong, destiLat + "," + destiLong, AppConstant.API_KEY);

        directionsApiResponseDTOCall.enqueue(new Callback<DirectionsApiResponseDTO>() {
            @Override
            public void onResponse(Call<DirectionsApiResponseDTO> call, Response<DirectionsApiResponseDTO> response) {

                DirectionsApiResponseDTO directions = response.body();

                System.out.println("Success Method Called");

                System.out.println("Routes" + directions.getRoutes());

                System.out.println("Kilometer" + directions.getRoutes().get(0).getLegs().get(0).getDistance().getText());

                String km_km = directions.getRoutes().get(0).getLegs().get(0).getDistance().getText();

                //String[] words=s1.split("\\s");

                String[] s = km_km.split("\\s");

                System.out.println("SSSSSS" + s[0]);
                km_str = s[0];

                if (km_str != null) {
                    calculatePriceForDistance(km_str);
                }

                LoaderUtil.dismisProgressBar(getActivity(),dialog);


                //Toast.makeText(getActivity(),directions.getRoutes().get(0).getLegs().get(0).getDistance().getText(),Toast.LENGTH_LONG).show();

                //directions.getRoutes().get(0).getLegs().get(0).getDistance().getText();


            }

            @Override
            public void onFailure(Call<DirectionsApiResponseDTO> call, Throwable t) {

                //Toast.makeText(getActivity(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

                System.out.println("MessageFrom" + t.getMessage().toString());
                System.out.println("Failure Method Called");

            }
        });


    }


    private void calculatePriceForDistance(String km_) {
        float f = Float.parseFloat(km_);
        int km = Math.round(f);
        //int _km = Integer.parseInt(km_);


        if (km != 0 && km > 0) {

            if (km <= 10) {
                price.setText("RM5.0");
            } else if (km > 10) {
                int i = km - 10;
                int finalPrice = i + 5;
                price.setText("" + finalPrice);
            }


        } else {
            System.out.println("You are selected less than 1km");
        }

    }

}
