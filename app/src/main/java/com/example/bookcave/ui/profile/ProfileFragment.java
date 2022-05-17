package com.example.bookcave.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bookcave.CustomerLogin;
import com.example.bookcave.R;
import com.example.bookcave.profile.ContactDeveloper;
import com.example.bookcave.profile.MyFavorites;
import com.example.bookcave.profile.OrderHistoryC;
import com.example.bookcave.profile.PaymentHistoryC;
import com.example.bookcave.profile.ProfileCustomer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView profileName;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = root.findViewById(R.id.profileName);
        LinearLayout linearl0 = root.findViewById(R.id.linearl0);
        LinearLayout linearl2 = root.findViewById(R.id.linearl2);
        LinearLayout linearl3 = root.findViewById(R.id.linearl3);
        LinearLayout linearl4 = root.findViewById(R.id.linearl4);
        LinearLayout linearl5 = root.findViewById(R.id.linearl5);
        LinearLayout linearl7 = root.findViewById(R.id.linearl7);

        //Checked signed
        //Firebase
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        DocumentReference typeref = db.collection("Users").document(userid);
        typeref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname");
                    profileName.setText(fullName);
                }
            }
        });


        linearl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My profile
                Intent i = new Intent(getActivity(), ProfileCustomer.class);
                startActivity(i);
            }
        });

        linearl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My profile
                Intent i = new Intent(getActivity(), MyFavorites.class);
                startActivity(i);
            }
        });

        linearl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Order history
                Intent i = new Intent(getActivity(), OrderHistoryC.class);
                startActivity(i);
            }
        });

        linearl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Payment History
                Intent i = new Intent(getActivity(), PaymentHistoryC.class);
                startActivity(i);
            }
        });

        linearl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Contact Developer
                Intent i = new Intent(getActivity(), ContactDeveloper.class);
                startActivity(i);
            }
        });

        linearl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), CustomerLogin.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return root;
    }

}
