package test.jaiboondemand;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleInstaActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private FirebaseAuth mAuth;
    private TextView singlePostDesc;
    private Button bn_del,bn_edit;
    private CollapsingToolbarLayout mCollapsing = null;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        singlePostDesc = (TextView) findViewById(R.id.singleDesc);
        singlePostImage = (ImageView) findViewById(R.id.Image_single);
        bn_del = (Button) findViewById(R.id.singleDesc1);
        bn_edit=(Button) findViewById(R.id.editpost1);

        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String user_id;
                try {
                    user_id = (String) dataSnapshot.child("userid").getValue();
                }
                catch(Exception e){
                    user_id ="";
                }
                try {
                if((user_id.equals(mAuth.getUid()))==false){
                    bn_del.setVisibility(View.INVISIBLE);
                    bn_edit.setVisibility(View.INVISIBLE);
                }
                }
                catch(Exception e){

                }

                mCollapsing.setTitle(post_title);
                singlePostDesc.setText(post_desc);
                Picasso.with(SingleInstaActivity.this).load(post_image).into(singlePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void deleleteButtonClicked(View view) {
        mDatabase.child(post_key).removeValue();
        Intent mainIntent = new Intent(this,Main2Activity.class);
        startActivity(mainIntent);
    }
    public void toeditPost(View view) {
        Intent edit1 = new Intent(getApplicationContext(), EditPost.class);
        edit1.putExtra("PostID", post_key);
        startActivity(edit1);
    }
}
