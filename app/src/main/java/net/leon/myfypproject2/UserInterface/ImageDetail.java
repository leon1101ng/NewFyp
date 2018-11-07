package net.leon.myfypproject2.UserInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetail extends AppCompatActivity {
    private String postKey;
    private DatabaseReference UserPostedImgRef;
    private FirebaseAuth mAuth;
    private String current_UserID;
    private ImageView ImageDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_image_detail);
        postKey = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        UserPostedImgRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts");
        ImageDetail = (ImageView)findViewById(R.id.ImageDetail);

        UserPostedImgRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image = dataSnapshot.child("ImageUrl").getValue().toString();
                    Glide.with(ImageDetail.this).load(image).into(ImageDetail);
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(ImageDetail);
                    photoViewAttacher.update();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
