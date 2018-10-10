package net.leon.myfypproject2.ProductMgnt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.Imagepost;
import net.leon.myfypproject2.UserInterface.UserInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadProducts extends AppCompatActivity {
    private Spinner productscategorylist;
    private CircleImageView Backbtn;
    final static int Gallery_Pick = 1;

    private ImageButton productimg1, productimg2;
    private Button uploadproduct;
    private Spinner prodctcate;
    private Uri uriImage;
    private EditText productsname,productdesc,productsprice,productsquantity;
    private FirebaseAuth Fauth;
    private DatabaseReference UserRef, ProductsDbRef;
    private StorageReference ProductsImg;
    private String Current_users,CurrentDate,CurrentTime,Postrandomname,Downloadlink;
    String productname ,prodctdesc, prodctprice ,prodctquantity ,productcategory;

    private ProgressDialog loadingupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_products);

        productscategorylist = (Spinner) findViewById(R.id.ProductsCategoryItem);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UploadProducts.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.name));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productscategorylist.setAdapter(myAdapter);
        Fauth = FirebaseAuth.getInstance();
        Current_users = Fauth.getCurrentUser().getUid();
        ProductsImg = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ProductsDbRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingupload = new ProgressDialog(this);


        Backbtn = (CircleImageView) findViewById(R.id.BackToMenu);
        productimg1 = (ImageButton) findViewById(R.id.ProductsImage1);
        productimg2 = (ImageButton) findViewById(R.id.ProductsImage2);
        uploadproduct = (Button) findViewById(R.id.uploadproducts);
        productsname = (EditText)findViewById(R.id.productname);
        productdesc = (EditText)findViewById(R.id.productdesc);
        productsprice = (EditText)findViewById(R.id.ProductsPrice);
        productsquantity = (EditText) findViewById(R.id.ProductsQuantity);
        prodctcate = (Spinner) findViewById(R.id.ProductsCategoryItem) ;

        
        uploadproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidationOfInput();
            }
        });

        productimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoGallery();
            }
        });
        productimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoGallery();
            }
        });
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void ValidationOfInput() {
        productname = productsname.getText().toString();
        prodctdesc = productdesc.getText().toString();
        prodctprice = ("Rm "+productsprice.getText().toString());
        prodctquantity = productsquantity.getText().toString();
        productcategory = prodctcate.getSelectedItem().toString();


        if(uriImage == null){
            Toast.makeText(this,"Please Select Your Image." ,Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(productname)){
            Toast.makeText(this,"Please Enter Product Name",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(prodctdesc)){
            Toast.makeText(this,"Please Enter Product Description",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(prodctprice)){
            Toast.makeText(this,"Please Enter Product Price",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(prodctquantity)){
            Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
        }else {
            loadingupload.setTitle("Uploading Product");
            loadingupload.setMessage("Please Wait!");
            loadingupload.show();
            loadingupload.setCanceledOnTouchOutside(true);


            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            StorageReference filepath = ProductsImg.child("ProductsImage").child(uriImage.getLastPathSegment() + Postrandomname + ".jpg");

            filepath.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Downloadlink = task.getResult().getDownloadUrl().toString();
                        Toast.makeText(UploadProducts.this,"Products Has Been Uploaded...", Toast.LENGTH_SHORT).show();
                        StoreNewProducts();
                        loadingupload.dismiss();
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(UploadProducts.this,"Failed To Upload Products"+ message, Toast.LENGTH_SHORT).show();
                        loadingupload.dismiss();
                    }
                }
            });


        }

    }

    private void StoreNewProducts() {
                    HashMap productsdb = new HashMap();
                    productsdb.put("ProductsImage", Downloadlink);
                    productsdb.put("ProductsName", productname);
                    productsdb.put("ProductsDescription", prodctdesc);
                    productsdb.put("ProductCategorys", productcategory);
                    productsdb.put("ProductsPrice", prodctprice);
                    productsdb.put("ProductsQuantity", prodctquantity);
                    ProductsDbRef.child(Current_users + Postrandomname).updateChildren(productsdb)
                            .addOnCompleteListener(new OnCompleteListener() {

                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()) {


                                        Toast.makeText(UploadProducts.this, "Products Has Been Uploaded ", Toast.LENGTH_SHORT).show();
                                        BackToHome();

                                    } else {

                                        String message = task.getException().getMessage();
                                        Toast.makeText(UploadProducts.this, "Failed To Upload Products " + message, Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
    }

    private void BackToHome() {
        Intent backtohome = new Intent(UploadProducts.this, MainActivity.class);
        backtohome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backtohome);
        finish();
    }

    private void PhotoGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, Gallery_Pick);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            uriImage = data.getData();
            productimg1.setImageURI(uriImage);
        }


    }





}
