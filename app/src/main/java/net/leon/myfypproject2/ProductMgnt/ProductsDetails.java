package net.leon.myfypproject2.ProductMgnt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import orionpreview.applikeysolutions.animation.BlurAnimation;
import orionpreview.applikeysolutions.animation.orionpreview.ScaleAnimation;
import orionpreview.applikeysolutions.animation.orionpreview.TranslationAnimation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.leon.myfypproject2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsDetails extends AppCompatActivity {

    //Views
    @BindView(R.id.cv_product_details) CardView cvProductDetails;
    @BindView(R.id.ll_product_details) LinearLayout llProductDetails;
    @BindView(R.id.rl_container) RelativeLayout rlContainer;
    @BindView(R.id.tv_product_title) TextView tvProductTitle;
    @BindView(R.id.imgBackground) ImageView imgBackground;
    @BindView(R.id.productdetaildescription) TextView productdesc;
    @BindView(R.id.productdetailcategory) TextView productcate;
    @BindView(R.id.productdetailquantity) TextView productquan;
    @BindView(R.id.tv_price) TextView productsprice;

    //Animations
    private ScaleAnimation increaseAnimationImage;
    private ScaleAnimation decreaseAnimationImage;
    private ScaleAnimation increaseAnimationText;
    private ScaleAnimation decreaseAnimationText;
    private TranslationAnimation upAnimationImageView;
    private TranslationAnimation arcUpAnimationTv;
    private TranslationAnimation downAnimationImageView;
    private TranslationAnimation arcDownAnimationTv;
    private BlurAnimation blurAnimation;

    private float screenHeight;
    private float screenWidth;
    private Bitmap blurredBitmap;
    private FirebaseAuth mAuth;
    private String CurrentUser,postKey,ProductsImage;
    private DatabaseReference ProductsRef;
    private ImageView backtoMyproduct;
    private int resID;
    private Context context;
    private Dialog myDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        backtoMyproduct = (ImageView)findViewById(R.id.iv_back);
        backtoMyproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        myDialog2 = new Dialog(ProductsDetails.this);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(postKey);
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String productsDescription = dataSnapshot.child("ProductsDescription").getValue().toString();
                    String productscategory = dataSnapshot.child("ProductCategorys").getValue().toString();
                    String ProductsName = dataSnapshot.child("ProductsName").getValue().toString();
                    ProductsImage = dataSnapshot.child("ProductsImage").getValue().toString();
                    Picasso.get().load(ProductsImage).placeholder(R.drawable.sample_background).into(imgBackground);

                    resID = getResources().getIdentifier(ProductsImage , "drawable-nodpi", getPackageName());
                    int productsPrice = dataSnapshot.child("ProductsPrice").getValue(Integer.class);
                    int ProductsQuantity = dataSnapshot.child("ProductsQuantity").getValue(Integer.class);
                    tvProductTitle.setText(ProductsName);

                    productdesc.setText(productsDescription);
                    productcate.setText(productscategory);
                    productsprice.setText(String.valueOf(productsPrice) + "Coins");
                    productquan.setText(String.valueOf(ProductsQuantity));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        ButterKnife.bind(this);

        getScreenSize();
        setProductBottomMargin();
    }

    @OnClick(R.id.cv_product_details)
    void onProductDetailsClick() {
        upAnimationImageView.showAnimation();
        arcUpAnimationTv.showAnimation();
        increaseAnimationImage.showAnimation();
        increaseAnimationText.showAnimation();

        cvProductDetails.setClickable(false);
        rlContainer.setClickable(true);
        addBlur();
    }

    @OnClick(R.id.rl_container)
    void onContainerClick() {
        downAnimationImageView.showAnimation();
        arcDownAnimationTv.showAnimation();
        decreaseAnimationImage.showAnimation();
        decreaseAnimationText.showAnimation();

        rlContainer.setClickable(false);
        cvProductDetails.setClickable(true);
        removeBlur();
    }

    private void getScreenSize(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

    private void setProductBottomMargin(){
        llProductDetails.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    llProductDetails.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    llProductDetails.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cvProductDetails.getLayoutParams();
                params.bottomMargin = llProductDetails.getMeasuredHeight() * -1;
                cvProductDetails.setLayoutParams(params);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnimation();
    }

    private void initAnimation() {



        tvProductTitle.post(() -> {
            increaseAnimationImage = new ScaleAnimation.ScaleAnimationBuilder(imgBackground, 1.5f, 1.5f).build();
            increaseAnimationText = new ScaleAnimation.ScaleAnimationBuilder(tvProductTitle, 2.25f, 2.25f).build();
            decreaseAnimationImage = new ScaleAnimation.ScaleAnimationBuilder(imgBackground, 1f, 1f).build();
            decreaseAnimationText = new ScaleAnimation.ScaleAnimationBuilder(tvProductTitle, 1f, 1f).build();
            upAnimationImageView = new TranslationAnimation.TranslationAnimationBuilder(cvProductDetails,
                    TranslationAnimation.TranslationMode.TranslationY, 0, -(screenHeight / 2.2f))
                    .build();
            arcUpAnimationTv = new TranslationAnimation.TranslationAnimationBuilder(tvProductTitle,
                    TranslationAnimation.TranslationMode.TranslationAll, 0, screenHeight / 10)
                    .arcMode(TranslationAnimation.ArcMode.ArcUpward)
                    .additionStartPoint(0)
                    .additionEndPoint(screenWidth / 2 - tvProductTitle.getWidth())
                    .build();
            downAnimationImageView = new TranslationAnimation.TranslationAnimationBuilder(cvProductDetails,
                    TranslationAnimation.TranslationMode.TranslationY, -(screenHeight / 3), 0)
                    .build();
            arcDownAnimationTv = new TranslationAnimation.TranslationAnimationBuilder(tvProductTitle,
                    TranslationAnimation.TranslationMode.TranslationAll, screenHeight / 7, 0)
                    .arcMode(TranslationAnimation.ArcMode.ArcDownard)
                    .additionStartPoint(screenWidth / 2 - tvProductTitle.getWidth())
                    .additionEndPoint(0)
                    .build();

            blurAnimation = new BlurAnimation.BlurAnimationBuilder(0.4f, 7f).build();

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_background );
            blurredBitmap = blurAnimation.blur(ProductsDetails.this, originalBitmap);
        });
    }

    private void addBlur() {
        imgBackground.setImageBitmap(new BitmapDrawable(getResources(), blurredBitmap).getBitmap());
    }

    private void removeBlur() {
        Picasso.get().load(ProductsImage).placeholder(R.drawable.sample_background).into(imgBackground);
    }

    public void Streampopup(View view) {
        Button editprod;
        String n,d,p,q;
        EditText productn,productd,productp,productq;
        myDialog2.setContentView(R.layout.editproduct_dialog);
        editprod = (Button)myDialog2.findViewById(R.id.Edit_Products);
        productn = (EditText)myDialog2.findViewById(R.id.EditProductsName);
        productd = (EditText)myDialog2.findViewById(R.id.EditProductDesc);
        productp = (EditText)myDialog2.findViewById(R.id.EditProductPrice);
        productq = (EditText)myDialog2.findViewById(R.id.EditProductQuantity);

        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String productsDescription = dataSnapshot.child("ProductsDescription").getValue().toString();
                    String ProductsName = dataSnapshot.child("ProductsName").getValue().toString();
                    int productsPrice = dataSnapshot.child("ProductsPrice").getValue(Integer.class);
                    int ProductsQuantity = dataSnapshot.child("ProductsQuantity").getValue(Integer.class);

                    productn.setText(ProductsName);
                    productd.setText(productsDescription);
                    productp.setText(String.valueOf(productsPrice));
                    productq.setText(String.valueOf(ProductsQuantity));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       editprod.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String n = productn.getText().toString();
              String d = productd.getText().toString();
              int p = Integer.parseInt(productp.getText().toString());
              int q = Integer.parseInt(productq.getText().toString());


               ProductsRef.child("ProductsName").setValue(n).toString();
               ProductsRef.child("ProductsDescription").setValue(d).toString();
               ProductsRef.child("ProductsPrice").setValue(p).toString();
               ProductsRef.child("ProductsQuantity").setValue(q).toString();

               com.shashank.sony.fancytoastlib.FancyToast.makeText(ProductsDetails.this,"Products Has Been Updated !", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_LONG, com.shashank.sony.fancytoastlib.FancyToast.SUCCESS,true).show();
               myDialog2.dismiss();

           }
       });

        myDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog2.show();

    }
}

