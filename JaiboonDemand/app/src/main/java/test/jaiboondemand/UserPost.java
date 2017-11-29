package test.jaiboondemand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wannaphong on 11/29/2017.
 */

public class UserPost  extends Fragment {
    private RecyclerView mIBstaList;
    private DatabaseReference mDatabase;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View x;
    private ArrayList<Integer> num;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_user_post,container,false);

        mIBstaList = (RecyclerView) x.findViewById(R.id.insta_list_post);
        mIBstaList.setHasFixedSize(true);
        mIBstaList.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatingActionButton = (FloatingActionButton) x.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PostActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }
                else{
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");
        mDatabase.getKey();


        return x;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Insta,DonateSocial.InstaViewHolde> FBRA = new FirebaseRecyclerAdapter<Insta, DonateSocial.InstaViewHolde>(
                Insta.class,
                R.layout.insta_row,
                DonateSocial.InstaViewHolde.class,
                mDatabase.orderByChild("userid").equalTo(mAuth.getUid())
        ) {
            @Override
            protected void populateViewHolder(DonateSocial.InstaViewHolde viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent singleInstaActivity = new Intent(getActivity(), SingleInstaActivity.class);
                            singleInstaActivity.putExtra("PostID", post_key);
                            startActivity(singleInstaActivity);
                        }
                    });
            }
        };
        mIBstaList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);
    }
}

