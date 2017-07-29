package com.pythonanywhere.ticatwolves.freeeth;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pythonanywhere.ticatwolves.freeeth.user.Complaints;

import java.util.Locale;

import static android.R.attr.typeface;

public class Contect extends Fragment {
    View myview;

    EditText name,email,msg;

    Button send;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.contect_contact,container,false);
        return myview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        send = (Button)getActivity().findViewById(R.id.msgs);
        name = (EditText)getActivity().findViewById(R.id.name);
        email = (EditText)getActivity().findViewById(R.id.email);
        msg = (EditText)getActivity().findViewById(R.id.msg);

        getActivity().setTitle("Contact us");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Complaints c= new Complaints(name.getText().toString(),email.getText().toString(),msg.getText().toString());
                FirebaseDatabase.getInstance().getReference("Complaints").push().setValue(c);
                Toast.makeText(getActivity(),"Send Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        //TextView myTextView = (TextView) getActivity().findViewById(R.id.contact_form_title);
        //Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arial.ttf");
        //myTextView.setTypeface(typeface);
    }
}
