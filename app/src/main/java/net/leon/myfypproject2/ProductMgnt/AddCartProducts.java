package net.leon.myfypproject2.ProductMgnt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCartProducts extends AppCompatActivity {

    private DatabaseReference ProductsViewRef,AddCartRef,ProductsRatingRef ;
    private static final String TAG = "AddCartProducts";
    private FirebaseAuth mAuth;
    private String current_UserID,postKey,CurrentDate,CurrentTime,Postrandomname;
    private TextView productname,productprice,productcategory,productquantity,username,productdesc,mTitle;
    private ImageView productimage;
    private CircleImageView productuser,back,addcart,chooseseat;
    private int mCountAddcart=0,totalcart=0;
    private float ratingStar;
    private RatingBar mRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart_products);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEB);
        mTitle = (TextView) toolbar.findViewById(R.id.ProductsNameOC);
        productname = (TextView)findViewById(R.id.ProductNameDetail);
        productprice = (TextView)findViewById(R.id.ProductPriceDetail);
        productcategory = (TextView)findViewById(R.id.ProductCategoryDetail);
        productquantity = (TextView)findViewById(R.id.ProductQuantityDetail);
        username = (TextView)findViewById(R.id.ProductUser);
        productdesc = (TextView)findViewById(R.id.ProductDescDetail);
        productimage = (ImageView)findViewById(R.id.ProductImageDetail);
        productuser = (CircleImageView)findViewById(R.id.ProductUserImage);
        chooseseat = (CircleImageView)findViewById(R.id.Choose_Seats);
        chooseseat.setVisibility(View.INVISIBLE);
        mRating = (RatingBar)findViewById(R.id.ProductRatingView);





        back = (CircleImageView)findViewById(R.id.BackToMenu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        postKey = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();

        chooseseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String ProductsUserID = dataSnapshot.child("ProductsUserID").getValue().toString();
                            if(ProductsUserID.equals(current_UserID)){
                                FancyToast.makeText(AddCartProducts.this,"You Cant Add Cart Your Own Item ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                            }else {

                                Intent i = new Intent(AddCartProducts.this, ConcertTicket.class);
                                i.putExtra("PostKey", postKey);
                                startActivity(i);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        current_UserID = mAuth.getCurrentUser().getUid();
        ProductsViewRef = FirebaseDatabase.getInstance().getReference().child("Products").child(postKey);
        AddCartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(current_UserID);
        ProductsRatingRef = FirebaseDatabase.getInstance().getReference().child("ProductRating").child(postKey);

        ProductsViewRef.child("Rating").child(current_UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mRating.setIsIndicator(true);

                }else {
                    mRating.setIsIndicator(false);
                    mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            ratingStar = mRating.getRating();
                            ProductsViewRef.child("Rating").child(current_UserID).child("Rating").setValue(ratingStar).toString();
                            FancyToast.makeText(AddCartProducts.this,"You Have Rate This Products " + ratingStar,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ProductsViewRef.child("Rating").child(current_UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int userrating = dataSnapshot.child("Rating").getValue(Integer.class);
                    FancyToast.makeText(AddCartProducts.this,"You Have Rate This Products " + userrating +" Star",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        ProductsViewRef.child("Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total = 0,
                count = 0;
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    int rating = dataSnapshot1.child("Rating").getValue(Integer.class);
                    count = count + 1;
                    total = (total + rating )/count;

                }
                mRating.setRating(total);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });



        addcart = (CircleImageView)findViewById(R.id.AddToCart);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String ProductsUserID = dataSnapshot.child("ProductsUserID").getValue().toString();
                            if(ProductsUserID.equals(current_UserID)){
                                FancyToast.makeText(AddCartProducts.this,"You Cant Add Cart Your Own Item ",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                            }else {

                                StoreCart();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        


    }
    private void StoreCart() {

        AddCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mCountAddcart = (int) dataSnapshot.getChildrenCount();

                }else {
                    mCountAddcart=0;
                }

                if(mCountAddcart < 1){
                    ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String productquantt = dataSnapshot.child("ProductsQuantity").getValue().toString();
                                int prdqtt = Integer.parseInt(productquantt);

                                if (prdqtt != 0) {
                                    final int cartno = 1;
                                    Calendar caldate = Calendar.getInstance();
                                    SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                                    CurrentDate = currentdate.format(caldate.getTime());

                                    Calendar caltime = Calendar.getInstance();
                                    SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                                    CurrentTime = currenttime.format(caltime.getTime());
                                    Random rand = new Random();

                                    int n = rand.nextInt(50) + 1;
                                    Postrandomname = CurrentDate + CurrentTime + n;

                                    ProductsViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String prdid = dataSnapshot.child("ProductsID").getValue().toString();
                                                String prdprice = dataSnapshot.child("ProductsPrice").getValue().toString();
                                                int prdprice1 = Integer.parseInt(prdprice);
                                                String prdimage = dataSnapshot.child("ProductsImage").getValue().toString();
                                                String prdname = dataSnapshot.child("ProductsName").getValue().toString();
                                                String prdqt = dataSnapshot.child("ProductsQuantity").getValue().toString();
                                                String prduserid = dataSnapshot.child("ProductsUserID").getValue().toString();
                                                int prdqt1 = Integer.parseInt(prdqt);
                                                int newquantity = prdqt1 - 1;
                                                HashMap cart = new HashMap();
                                                cart.put("ProductID", prdid);
                                                cart.put("ProductName", prdname);
                                                cart.put("ProductImage", prdimage);
                                                cart.put("ProductQuantity", cartno);
                                                cart.put("ProductPrice", prdprice1);
                                                cart.put("ProductsUserID", prduserid);
                                                AddCartRef.child(Postrandomname).updateChildren(cart)
                                                        .addOnCompleteListener(new OnCompleteListener() {

                                                            @Override
                                                            public void onComplete(@NonNull Task task) {

                                                                if (task.isSuccessful()) {
                                                                    FancyToast.makeText(AddCartProducts.this,"Products Has Been Add To Cart", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                                                } else {
                                                                    String message = task.getException().getMessage();
                                                                    FancyToast.makeText(AddCartProducts.this,"Failed To Add Cart Products " + message, FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();


                                                                }
                                                            }
                                                        });
                                                ProductsViewRef.child("ProductsQuantity").setValue(newquantity);



                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    FancyToast.makeText(AddCartProducts.this,"The Products Is Out Of Stocks",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    FancyToast.makeText(AddCartProducts.this,"This Item Already Add to Your Cart",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        ProductsViewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String prdcate = dataSnapshot.child("ProductCategorys").getValue().toString();
                    String prddesc = dataSnapshot.child("ProductsDescription").getValue().toString();
                    final String prdimage = dataSnapshot.child("ProductsImage").getValue().toString();
                    final String prdimage1 = dataSnapshot.child("ProductsImage2").getValue().toString();
                    final String prdimage2 = dataSnapshot.child("ProductsImage3").getValue().toString();
                    String prdname = dataSnapshot.child("ProductsName").getValue().toString();
                    String prdprice = dataSnapshot.child("ProductsPrice").getValue().toString();
                    String prdquantity = dataSnapshot.child("ProductsQuantity").getValue().toString();
                    String prduser = dataSnapshot.child("ProductsUser").getValue().toString();
                    String prduserimg = dataSnapshot.child("UserImage").getValue().toString();

                    String ticketcategory = "Ticket";
                    if(prdcate.equals(ticketcategory)){
                        chooseseat.setVisibility(View.VISIBLE);
                    }


                    mTitle.setText(prdname);
                    Glide.with(getApplicationContext()).load(prdimage).into(productimage);
                    productcategory.setText(prdcate);
                    productdesc.setText(prddesc);
                    productprice.setText(prdprice);
                    productquantity.setText(prdquantity);
                    username.setText(prduser);
                    productname.setText(prdcate);
                    Glide.with(getApplicationContext()).load(prduserimg).into(productuser);

                    productimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String i = prdimage2;
                            String ii = prdimage;
                            Glide.with(AddCartProducts.this).load(prdimage1).into(productimage);
                            thirdimage(i,ii);

                        }
                    });




                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void thirdimage(final String i, final String ii) {
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(AddCartProducts.this).load(i).into(productimage);
                firstimage(ii);


            }
        });
    }

    private void firstimage(final String ii) {
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(AddCartProducts.this).load(ii).into(productimage);
                onStart();


            }
        });
    }


}
