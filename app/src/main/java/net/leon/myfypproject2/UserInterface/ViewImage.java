package net.leon.myfypproject2.UserInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.Model.UserPostsImage;
import net.leon.myfypproject2.ProfileFragment.ImagePostFragment;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewImage extends AppCompatActivity {
    private RecyclerView imagepostlist;
    private DatabaseReference UserPostedImgRef;
    private FirebaseAuth mAuth;
    private String current_UserID, postKey;
    private CircleImageView backtomenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        postKey = getIntent().getExtras().get("1").toString();
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        UserPostedImgRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts");
        imagepostlist = (RecyclerView)findViewById(R.id.MyImageView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        imagepostlist.setLayoutManager(linearLayoutManager);
        imagepostlist.setLayoutManager(new GridLayoutManager(this,3));
        backtomenu = (CircleImageView)findViewById(R.id.BackToMenu);
        backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void onStart() {
        super.onStart();


        Query ArtistArt = UserPostedImgRef.orderByChild("UserID").startAt(current_UserID).endAt(current_UserID + "\uf8ff");
        FirebaseRecyclerAdapter<UserPostsImage, ImagePostFragment.PostedImageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPostsImage, ImagePostFragment.PostedImageHolder>(
                UserPostsImage.class,
                R.layout.user_imagepost_layout,
                ImagePostFragment.PostedImageHolder.class,
                ArtistArt

        ) {
            @Override
            protected void populateViewHolder(ImagePostFragment.PostedImageHolder viewHolder, UserPostsImage model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setMpostimage(ViewImage.this, model.getImageUrl());
                if(postKey.equals("1")){
                    viewHolder.deleteimage.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(ViewImage.this, ImageDetail.class);
                            i.putExtra("PostKey", postkey);
                            startActivity(i);

                        }
                    });

                }

                viewHolder.deleteimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(ViewImage.this,android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ViewImage.this);
                        }

                        builder.setTitle("Delete Image Post")
                                .setMessage("Do you want delete this image ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        UserPostedImgRef.child(postkey).removeValue();
                                        FancyToast.makeText(ViewImage.this,"Image Has Been Delete",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                    }
                                })
                                .show();


                    }
                });




            }
        };
        imagepostlist.setAdapter(firebaseRecyclerAdapter);
    }
}
