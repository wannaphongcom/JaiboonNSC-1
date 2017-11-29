package test.jaiboondemand;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * Created by wannaphong on 11/29/2017.
 */

public class EditPost  extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private FirebaseAuth mAuth;
    private TextView singlePostDesc;
    private Button bn_del;
    private CollapsingToolbarLayout mCollapsing = null;
    private String post_key;
    private EditText EditTextEditText,editDesc1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        EditTextEditText = (EditText) findViewById(R.id.EditTextEditText);
        editDesc1 = (EditText) findViewById(R.id.editDesc1);

        //singlePostDesc = (TextView) findViewById(R.id.singleDesc);
        //singlePostImage = (ImageView) findViewById(R.id.Image_single);
        //bn_del = (Button) findViewById(R.id.singleDesc1);

        //mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String user_id = (String) dataSnapshot.child("userid").getValue();
                EditTextEditText.setText(post_title);
                editDesc1.setText(post_desc);
                //Picasso.with(EditPost.this).load(post_image).into(singlePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void submitButtonClicked1(View view) {
        final String titleValue = EditTextEditText.getText().toString().trim();
        final String titleDesc = editDesc1.getText().toString().trim();

        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(titleDesc)){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");
            mDatabase.child(post_key).child("title").setValue(titleValue);
            mDatabase.child(post_key).child("desc").setValue(titleDesc);

                }

            Intent intent = new Intent(EditPost.this,Main2Activity.class);
            startActivity(intent);

        }
    }