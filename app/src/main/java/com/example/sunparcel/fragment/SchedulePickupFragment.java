package com.example.sunparcel.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sunparcel.R;
import com.example.sunparcel.activity.MapActivity;
import com.example.sunparcel.model.SchedulePickupRequest;
import com.example.sunparcel.model.SchedulePickupResponse;
import com.example.sunparcel.model.ShipperDetailsRequest;
import com.example.sunparcel.retrofit.ApiClient;
import com.example.sunparcel.retrofit.ApiInterface;
import com.example.sunparcel.utils.GpsUtils;
import com.example.sunparcel.utils.PermissionUtils;
import com.example.sunparcel.utils.PreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sunparcel.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;
import static com.example.sunparcel.utils.MathUtil.validateAddress;
import static com.example.sunparcel.utils.MathUtil.validateEmail;
import static com.example.sunparcel.utils.MathUtil.validateMobile;
import static com.example.sunparcel.utils.MathUtil.validateName;


public class SchedulePickupFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner servSpinPickUp, itemSpinPickUp, weightSpinPickUp;

    EditText companyNameShipper, addressShipper, contactNameShipper, contactNumberShipper, emailShipper;

    EditText companyNameReceiver, addressReceiver, contactNameReceiver, contactNumberReceiver, emailReceiver;

    EditText instruction;

    ImageView fromAddrShedulePickup, toAddrShedulePickup;

    Button btn_schePick;

    String serviceSelected, itemSelected, weightSelected;

    TextView schedulePickUpDate;
    RelativeLayout schedulePickupCalender;

    RadioButton radioBtnAccount,radioBtnCash;

    String cashType,schedulePickUpDateInString;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedulepickup, null);


        schedulePickUpDate = (TextView) view.findViewById(R.id.schedulePickUpDate);
        schedulePickupCalender = (RelativeLayout) view.findViewById(R.id.schedulePickupCalender);

        radioBtnAccount = (RadioButton) view.findViewById(R.id.account);
        radioBtnCash=(RadioButton)view.findViewById(R.id.cash);

        companyNameShipper = (EditText) view.findViewById(R.id.shipper_edit_companyName);
        addressShipper = (EditText) view.findViewById(R.id.shipper_edit_companyAddr);
        fromAddrShedulePickup = (ImageView) view.findViewById(R.id.fromAddrShedulePickup);
        contactNameShipper = (EditText) view.findViewById(R.id.shipper_edit_contactName);
        contactNumberShipper = (EditText) view.findViewById(R.id.shipper_edit_contactNumber);
        emailShipper = (EditText) view.findViewById(R.id.shipper_edit_Email);

        companyNameReceiver = (EditText) view.findViewById(R.id.receiver_edit_companyName);
        addressReceiver = (EditText) view.findViewById(R.id.receiver_edit_companyAddr);
        toAddrShedulePickup = (ImageView) view.findViewById(R.id.toAddrShedulePickup);
        contactNameReceiver = (EditText) view.findViewById(R.id.receiver_edit_contactName);
        contactNumberReceiver = (EditText) view.findViewById(R.id.receiver_edit_contactNumber);
        emailReceiver = (EditText) view.findViewById(R.id.receiver_edit_Email);

        instruction = (EditText) view.findViewById(R.id.instr_edit_rider);

        servSpinPickUp = (Spinner) view.findViewById(R.id.selectServ_schePick);
        itemSpinPickUp = (Spinner) view.findViewById(R.id.itemDetails_schePick);
        weightSpinPickUp = (Spinner) view.findViewById(R.id.itemWeight_schePick);

        btn_schePick = (Button) view.findViewById(R.id.btn_schePick);


        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.SelectService, android.R.layout.simple_spinner_item);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servSpinPickUp.setAdapter(serviceAdapter);


        ArrayAdapter<CharSequence> itemAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.SelectItem, android.R.layout.simple_spinner_item);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpinPickUp.setAdapter(itemAdapter);


        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.SelectWeight, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinPickUp.setAdapter(weightAdapter);


        servSpinPickUp.setOnItemSelectedListener(this);
        itemSpinPickUp.setOnItemSelectedListener(this);
        weightSpinPickUp.setOnItemSelectedListener(this);


        Calendar calendar = Calendar.getInstance();
        final int dayC = calendar.get(Calendar.DAY_OF_MONTH);
        final int monthC = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        final int yearC = calendar.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        System.out.println("Today: " + dayOfTheWeek);
        schedulePickUpDateInString=dayOfTheWeek;
        schedulePickUpDate.setText("" + dayOfTheWeek);


        schedulePickupCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeekDay(dayC, monthC, yearC);
            }
        });

        radioBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioBtnAccount.setChecked(true);
                cashType="account";
                radioBtnCash.setChecked(false);


            }
        });

        radioBtnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioBtnCash.setChecked(true);
                cashType="cash";
                radioBtnAccount.setChecked(false);
            }
        });


        fromAddrShedulePickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromAddress = "A";
                checkLocationPermission(fromAddress);
            }
        });

        toAddrShedulePickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromAddress = "B";
                checkLocationPermission(fromAddress);
            }
        });


        companyNameShipper.addTextChangedListener(new MyTextWatcher(companyNameShipper));
        addressShipper.addTextChangedListener(new MyTextWatcher(addressShipper));
        contactNameShipper.addTextChangedListener(new MyTextWatcher(contactNameShipper));
        contactNumberShipper.addTextChangedListener(new MyTextWatcher(contactNumberShipper));
        emailShipper.addTextChangedListener(new MyTextWatcher(emailShipper));

        companyNameReceiver.addTextChangedListener(new MyTextWatcher(companyNameReceiver));
        addressReceiver.addTextChangedListener(new MyTextWatcher(addressReceiver));
        contactNameReceiver.addTextChangedListener(new MyTextWatcher(contactNameReceiver));
        contactNumberReceiver.addTextChangedListener(new MyTextWatcher(contactNumberReceiver));
        emailReceiver.addTextChangedListener(new MyTextWatcher(emailReceiver));

        instruction.addTextChangedListener(new MyTextWatcher(instruction));


        return view;
    }

    /*public void checkRadioButton(@NotNull View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);

    }*/

    private void getWeekDay(int dayC, int monthC, int yearC) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int monthOfYear = month + 1;
                String date = dayOfMonth + "/" + monthOfYear + "/" + year;
                schedulePickUpDate.setText("" + date);


            }
        }, yearC, monthC, dayC);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        System.out.println("Today: " + dayOfTheWeek);
        //pickUpDate.setText("" + dayOfTheWeek);

        int daysOfWeekInMonth = 0;

        if (dayOfTheWeek.equals("Monday")) {
            daysOfWeekInMonth = 5;
        } else if (dayOfTheWeek.equals("Tuesday")) {
            daysOfWeekInMonth = 4;
        } else if (dayOfTheWeek.equals("Wednesday")) {
            daysOfWeekInMonth = 3;
        } else if (dayOfTheWeek.equals("Thursday")) {
            daysOfWeekInMonth = 2;
        } else if (dayOfTheWeek.equals("Friday")) {
            daysOfWeekInMonth = 1;
        } else if (dayOfTheWeek.equals("Saturday")) {
            daysOfWeekInMonth = 0;
        }


        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        //datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*2));
        datePickerDialog.getDatePicker().setMaxDate(now + (1000 * 60 * 60 * 24 * daysOfWeekInMonth));


        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-5000);
        //datePickerDialog.getDatePicker().get

        System.out.println("FirstDayOfWeek" + datePickerDialog.getDatePicker().getFirstDayOfWeek());
        datePickerDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        String originLat = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_A_LAT);
        String originLong = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_A_LONG);

        System.out.println("FromPoint" + originLat + " " + originLong);

        String destiLat = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_B_LAT);
        String destiLong = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.POINT_B_LONG);

        System.out.println("ToPoint" + destiLat + " " + destiLong);

        if (originLat.equals("null") && originLong.equals("null") && destiLat.equals("null") && destiLong.equals("null")) {
            System.out.println("No Coordinates");

        }
        if (!originLat.equals("null") && !originLong.equals("null")) {

            getFromAddressReverseGeoCoding(originLat, originLong, 1);


        }
        if (!destiLat.equals("null") && !destiLong.equals("null")) {
            getFromAddressReverseGeoCoding(destiLat, destiLong, 2);
        }


    }

    private void getFromAddressReverseGeoCoding(String originLat, String originLong, int fromOrTo) {

        double originA = Double.parseDouble(originLat);
        double originB = Double.parseDouble(originLong);
        List<Address> geoAddresses = GpsUtils.getAddressFromMap(getActivity(), originA, originB);
        if (geoAddresses.size() != 0) {

            String address = geoAddresses.get(0).getAddressLine(0);
            String area = geoAddresses.get(0).getLocality();
            String city = geoAddresses.get(0).getAdminArea();
            String country = geoAddresses.get(0).getCountryName();
            String postalCode = geoAddresses.get(0).getPostalCode();
            String subAdminArea = geoAddresses.get(0).getSubAdminArea();
            String subLocality = geoAddresses.get(0).getSubLocality();
            String premises = geoAddresses.get(0).getPremises();
            String addressLine = geoAddresses.get(0).getAddressLine(0);

            System.out.println("MyAddress" + address + " " + area + " " + city + " " + postalCode);

            if (fromOrTo == 1) {
                addressShipper.setText(address + " " + area + " " + city + " " + postalCode);

            } else if (fromOrTo == 2) {
                addressReceiver.setText(address + " " + area + " " + city + " " + postalCode);
            }


        }

    }


    private void checkLocationPermission(String addrPoint) {

        if (!PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                && !PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            PermissionUtils.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


        } else {

            if (PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    && PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {


                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("ADDRESSPOINT", addrPoint);
                startActivity(intent);
                //getActivity().finish();


            }

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.selectServ_schePick:
                serviceSelected = parent.getItemAtPosition(position).toString();
                System.out.println("ItemSelected" + serviceSelected);
                break;

            case R.id.itemDetails_schePick:
                itemSelected = parent.getItemAtPosition(position).toString();
                System.out.println("ItemSelected1" + itemSelected);
                break;

            case R.id.itemWeight_schePick:
                weightSelected = parent.getItemAtPosition(position).toString();
                System.out.println("ItemSelected1" + weightSelected);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            final String shipperCompanyName = companyNameShipper.getText().toString();
            final String shipperAddress = addressShipper.getText().toString();
            final String shipperName = addressShipper.getText().toString();
            final String shipperNumber = addressShipper.getText().toString();
            final String shipperEmail = addressShipper.getText().toString();

            final String receiverCompanyName = companyNameReceiver.getText().toString();
            final String receiverAddress = addressReceiver.getText().toString();
            final String receiverName = contactNameReceiver.getText().toString();
            final String receiverNumber = contactNumberReceiver.getText().toString();
            final String receiverEmail = emailReceiver.getText().toString();

            final String getInstruction = instruction.getText().toString();

            boolean nameStatus = validateName(shipperCompanyName);
            boolean AddrV = validateAddress(shipperAddress);
            boolean nameV = validateName(shipperName);
            boolean numberV = validateMobile(shipperNumber);
            boolean emailV = validateEmail(shipperEmail);

            boolean receiverStatusS = validateName(receiverCompanyName);
            boolean receiverAddresS = validateAddress(receiverAddress);
            boolean receiverNameS = validateName(receiverName);
            boolean receiverSNumS = validateMobile(receiverNumber);
            boolean receiverEmailS = validateEmail(receiverEmail);

            /*boolean spinnerStatus = !serviceSelected.equals("SelectService");
            boolean spinnerStatus2 = !itemSelected.equals("SelectItem");
            boolean spinnerStaus3 = !weightSelected.equals("SelectWeight");
*/
            //  System.out.println("SpinnerStatus"+spinnerStatus+" "+spinnerStatus2+" "+spinnerStaus3);

            System.out.println("ReturnedStatus" + nameStatus + " " + AddrV + " " + nameV + " " + numberV + " " + emailV);
            System.out.println("ReceiverStatusValue" + receiverStatusS + " " + receiverAddresS + " " + receiverNameS + " " + receiverSNumS + " " + receiverEmailS);

            btn_schePick.setEnabled(validateName(shipperCompanyName) && validateAddress(shipperAddress) && validateName(shipperName) &&
                    validateMobile(shipperNumber) && validateEmail(shipperEmail) && validateName(receiverCompanyName) && validateAddress(receiverAddress) && validateName(receiverName) && validateMobile(receiverNumber) && validateEmail(receiverEmail)
                    && validateName(getInstruction) && schedulePickUpDateInString!=null && cashType!=null);
            //&& validateName(getInstruction) && !servSpinPickUp.getSelectedItem().toString().equals("SelectService") && !itemSpinPickUp.getSelectedItem().toString().equals("SelectItem") && !weightSpinPickUp.getSelectedItem().toString().equals("SelectWeight")

            if (btn_schePick.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_schePick.setBackground(getActivity().getDrawable(R.drawable.rectangle_shpae));
                }
            } else if (!btn_schePick.isEnabled()) {
                btn_schePick.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_schePick.setBackground(getActivity().getDrawable(R.color.btn_disable));
                }
            }

            btn_schePick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<ShipperDetailsRequest> shipperDetailsRequestList = new ArrayList<>();
                    List<ShipperDetailsRequest> receiverDetailsRequestList = new ArrayList<>();
                    List<ShipperDetailsRequest> serviceDetailsRequestList = new ArrayList<>();


                    ShipperDetailsRequest shipperDetailsRequest = new ShipperDetailsRequest(shipperCompanyName, shipperAddress, shipperName, shipperNumber, shipperEmail);
                    shipperDetailsRequestList.add(shipperDetailsRequest);
                    ShipperDetailsRequest receiverDetailsRequest = new ShipperDetailsRequest(receiverCompanyName, receiverAddress, receiverName, receiverNumber, receiverEmail);
                    receiverDetailsRequestList.add(receiverDetailsRequest);

                    ShipperDetailsRequest serviceDetailsRequest = new ShipperDetailsRequest(serviceSelected, itemSelected, weightSelected);
                    serviceDetailsRequestList.add(serviceDetailsRequest);


                    final SchedulePickupRequest schedulePickupRequest = new SchedulePickupRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), shipperDetailsRequestList, receiverDetailsRequestList, serviceDetailsRequestList, getInstruction);

                    ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

                    String token=PreferenceUtil.getValueString(getActivity(),PreferenceUtil.AUTH_TOKEN);
                    String auth=PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER);
                    System.out.println("PreferenceToken"+token+" "+auth);

                    Call<SchedulePickupResponse> call = apiInterface.doPostSchedulePickup(PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN), schedulePickupRequest);
                    call.enqueue(new Callback<SchedulePickupResponse>() {
                        @Override
                        public void onResponse(Call<SchedulePickupResponse> call, Response<SchedulePickupResponse> response) {

                            if(response.isSuccessful()) {
                                SchedulePickupResponse schedulePickupResponse = response.body();
                                callShowPriceFragment(schedulePickupResponse);


                                System.out.println("SchedulePickupResponse" + schedulePickupResponse.getTrackingNo() + " " + schedulePickupResponse.getTransactionStatus() + " " + schedulePickupResponse.getTrackingMsg());

                            }


                        }

                        @Override
                        public void onFailure(Call<SchedulePickupResponse> call, Throwable t) {

                        }
                    });

                }
            });


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void callShowPriceFragment(SchedulePickupResponse schedulePickupResponse) {

        Fragment fragment = new ShowPriceFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screenArea, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
