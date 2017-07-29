package com.pythonanywhere.ticatwolves.freeeth;

import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Settings extends Fragment {
    View myview;
    Button button;
    EditText address;
    ConnectivityManager myconnManager;
    NetworkInfo myinfo;

    private DatabaseReference mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.contect_settings,container,false);
        return myview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (Button)getActivity().findViewById(R.id.addethadd);
        address = (EditText)getActivity().findViewById(R.id.ethaddress);

        getActivity().setTitle("Settings");
        myconnManager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        myinfo = myconnManager.getActiveNetworkInfo();


        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile").child("address");
        // .getKey("dName");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address.setText(String.valueOf(dataSnapshot.getValue()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(address.getText()){
                //    Toast.makeText(getActivity(),"Address cant be empty",Toast.LENGTH_SHORT).show();
                //}
                myconnManager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
                myinfo = myconnManager.getActiveNetworkInfo();

                //else{
                if(myinfo!=null && myinfo.isConnected()) {

                    Toast.makeText(getActivity(), "Your address is saved", Toast.LENGTH_SHORT).show();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Profile").child("address").setValue(String.valueOf(address.getText()));
                    //}
                }
                else {
                    Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
