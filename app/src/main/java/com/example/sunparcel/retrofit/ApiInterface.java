package com.example.sunparcel.retrofit;

import com.example.sunparcel.model.BaseResponse;
import com.example.sunparcel.model.DirectionsApiResponseDTO;
import com.example.sunparcel.model.EmailUpdateRequest;
import com.example.sunparcel.model.GmailLoginResponse;
import com.example.sunparcel.model.GmailRegisterRequest;
import com.example.sunparcel.model.LoginAuthResponse;
import com.example.sunparcel.model.LoginRequest;
import com.example.sunparcel.model.LoginResponse;
import com.example.sunparcel.model.MobileNumUpdateRequest;
import com.example.sunparcel.model.PasswordUpdateRequest;
import com.example.sunparcel.model.PaymentRequest;
import com.example.sunparcel.model.RegisterRequest;
import com.example.sunparcel.model.RegisterResponse;
import com.example.sunparcel.model.SchedulePickupRequest;
import com.example.sunparcel.model.SchedulePickupResponse;
import com.example.sunparcel.model.UserNameUpdateRequest;
import com.example.sunparcel.utils.BaseURL;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //Login
   /* @Headers({"Content-Type:application/json"})
    @POST("get_single_user.php")
    Call<SignedInJSONResponse> getLoginUser(@Body SignInDTO signInDTO);
*/

   /*@GET(BuildConfig.NAJDI_END_POINTS + "products")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<List<ProductListResponse>> getProducts();
*/

    @GET(BaseURL.GOOGLE_MAP_URL + "/maps/api/directions/json")
    Call<DirectionsApiResponseDTO> getRouteDistance(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);

    /*Call<DirectionsApiResponseDTO> getRouteDistance(String s, double v, double v1, String apiKey);*/

    @POST(BaseURL.DOMAIN_NAME + "register")
    Call<RegisterResponse> doRegister(@Body RegisterRequest registerRequest);

    @POST(BaseURL.DOMAIN_NAME + BaseURL.SUB_PATH + "login")
    @Headers({"Content-Type:application/json"})
    Call<LoginAuthResponse> doCheckLogin(@Body LoginRequest loginRequest);

    //@Headers({"Content-Type:application/json", "Authorization" + ": " + AppConstant.AUTHTOKEN})
    @POST(BaseURL.DOMAIN_NAME + BaseURL.SUB_PATH + "User")
    @Headers({"Content-Type:application/json"})
    Call<LoginResponse> doGetUserDetails(@Header("Authorization") String token);

    @Headers({"Content-Type:application/json"})
    // @GET(BaseURL.DOMAIN_NAME+BaseURL.SUB_CHECK)
    @GET(BaseURL.DOMAIN_NAME + "check/{mobileNumber}")
    Call<BaseResponse> checkMobileNumberInServer(@Path("mobileNumber") String mobile);

    @Headers({"Content-Type:application/json"})
    @POST(BaseURL.DOMAIN_NAME + "createTrackno")
    Call<SchedulePickupResponse> doPostSchedulePickup(@Header("Authorization") String token, @Body SchedulePickupRequest schedulePickupRequest);


    @POST(BaseURL.DOMAIN_NAME + "register")
    @Headers({"Content-Type:application/json"})
    Call<GmailLoginResponse> doRegisterGmailUser(@Body GmailRegisterRequest gmailLoginRequest);

    @POST(BaseURL.DOMAIN_NAME + "auth/login")
    @Headers({"Content-Type:application/json"})
    Call<LoginAuthResponse> doGetGmailUserDetails(@Body GmailRegisterRequest gmailLoginRequest);


    @PUT(BaseURL.DOMAIN_NAME + "forgotpassword")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateForgetPassword();

    @POST(BaseURL.DOMAIN_NAME + "updateEmail")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateEmail(@Header("Authorization") String token, @Body EmailUpdateRequest emailUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updateMobileno")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateMobileNumber(@Header("Authorization") String token, @Body MobileNumUpdateRequest mobileNumUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updateuserName")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateUserName(@Header("Authorization") String token, @Body UserNameUpdateRequest userNameUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updatePassword")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updatePassword(@Header("Authorization") String token, @Body PasswordUpdateRequest passwordUpdateRequest);

    @POST(BaseURL.PAYMENT_URL + "492159427221256")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> paymentRequest(@Body PaymentRequest paymentRequest);


    @POST(BaseURL.PAYMENT_URL + "492159427221256")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> paymentGatewayURLCall();


}
