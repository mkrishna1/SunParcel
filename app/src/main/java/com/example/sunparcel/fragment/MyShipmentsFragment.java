package com.example.sunparcel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunparcel.R;
import com.example.sunparcel.adapter.MyShipmentAdapter;
import com.example.sunparcel.model.MyShipmentDTO;
import com.example.sunparcel.utils.PdfGenerationUtil;

import java.util.ArrayList;
import java.util.List;

public class MyShipmentsFragment extends Fragment implements MyShipmentAdapter.MyshipmentClickListener {

    private RecyclerView myShipmentsRecyclerView;
    private MyShipmentAdapter myShipmentAdapter;

    List<MyShipmentDTO> myShipmentDTOList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myshipment, null);

        myShipmentsRecyclerView=(RecyclerView)view.findViewById(R.id.myShipmentsRecyclerView);
        myShipmentsRecyclerView.setHasFixedSize(true);
        myShipmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myShipmentDTOList = new ArrayList<>();

        myShipmentAdapter=new MyShipmentAdapter(getActivity(),getActivity(),myShipmentDTOList,MyShipmentsFragment.this);

        myShipmentsRecyclerView.setAdapter(myShipmentAdapter);

        getMyShipmentData();

        return view;
    }

    private void getMyShipmentData() {
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));
        myShipmentDTOList.add(new MyShipmentDTO(123456,"25-6-2020"));

        myShipmentAdapter.setDate(myShipmentDTOList);



        //ApiInterface apiInterface=

    }

    @Override
    public void onPdfClicked(MyShipmentDTO myShipmentDTO) {

        PdfGenerationUtil pdfGenerationUtil=new PdfGenerationUtil(getActivity());
        pdfGenerationUtil.createPDF();

    }
}
