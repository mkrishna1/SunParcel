package com.example.sunparcel.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sunparcel.R;
import com.example.sunparcel.model.MyShipmentDTO;
import com.example.sunparcel.utils.PermissionUtils;

import java.util.List;

import static com.example.sunparcel.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;


public class MyShipmentAdapter extends RecyclerView.Adapter<MyShipmentAdapter.MyShipmentViewHolder> {

    private Context mCtx;
    private Activity activity;
    private List<MyShipmentDTO> myShipmentDTOList;

    MyshipmentClickListener myshipmentClickListener;

    public interface MyshipmentClickListener{

        void onPdfClicked(MyShipmentDTO myShipmentDTO);
    }

    public MyShipmentAdapter(Context mCtx, Activity activity, List<MyShipmentDTO> myShipmentDTOList, MyshipmentClickListener myshipmentClickListener) {
        this.mCtx = mCtx;
        this.activity=activity;
        this.myShipmentDTOList = myShipmentDTOList;
        this.myshipmentClickListener=myshipmentClickListener;
    }

    public void setDate(List<MyShipmentDTO> myShipmentDTOList) {
        this.myShipmentDTOList = myShipmentDTOList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.myshipment_adapter, parent, false);
        return new MyShipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShipmentViewHolder holder, int position) {

        holder.trackingNum.setText("" + myShipmentDTOList.get(position).getTrackingNumber());
        holder.trackingDate.setText(myShipmentDTOList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return myShipmentDTOList == null ? 0 : myShipmentDTOList.size();
    }

    class MyShipmentViewHolder extends RecyclerView.ViewHolder {

        TextView trackingNum, trackingDate, trackingView;

        public MyShipmentViewHolder(@NonNull View itemView) {
            super(itemView);

            trackingNum = (TextView) itemView.findViewById(R.id.trackingNum);
            trackingDate = (TextView) itemView.findViewById(R.id.trackingDate);
            trackingView = (TextView) itemView.findViewById(R.id.trackingView);

            trackingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

checkExternalStoragePermission(getAdapterPosition());
                    /*MyShipmentDTO myShipmentDTO=myShipmentDTOList.get(getAdapterPosition());

                    myshipmentClickListener.onPdfClicked(myShipmentDTO);
*/
                }
            });

        }
    }

    private void checkExternalStoragePermission(int getAdapterPosition) {

        if (!PermissionUtils.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            PermissionUtils.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_PERMISSION_REQUEST_CODE);


        }
        else {

            if (PermissionUtils.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && PermissionUtils.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                MyShipmentDTO myShipmentDTO=myShipmentDTOList.get(getAdapterPosition);

                myshipmentClickListener.onPdfClicked(myShipmentDTO);




            }}

    }

}
