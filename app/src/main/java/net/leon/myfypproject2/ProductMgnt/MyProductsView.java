package net.leon.myfypproject2.ProductMgnt;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.ProductsClass;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProductsView extends AppCompatActivity {
    private RecyclerView myProducts_list;
    private FirebaseAuth mAuth;
    private DatabaseReference ProductsRef;
    private CircleImageView backbtn;
    String current_users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products_view);

        myProducts_list = (RecyclerView)findViewById(R.id.MyProductView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myProducts_list.setLayoutManager(linearLayoutManager);
        myProducts_list.setLayoutManager(new GridLayoutManager(this,3));

        mAuth = FirebaseAuth.getInstance();
        current_users = mAuth.getCurrentUser().getUid();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        DisplayAllMyProducts();

        backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void DisplayAllMyProducts() {
        Query MyProduct = ProductsRef.orderByChild("UserID").startAt(current_users).endAt(current_users + "\uf8ff");
        FirebaseRecyclerAdapter<ProductsClass,MyProductListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductsClass, MyProductListViewHolder>(
                ProductsClass.class,
                R.layout.my_product_list,
                MyProductListViewHolder.class,
                MyProduct
        ) {
            @Override
            protected void populateViewHolder(MyProductListViewHolder viewHolder, ProductsClass model, int position) {
                viewHolder.setProductsImage(getApplication(),model.getProductsImage());
                viewHolder.setProductsName(model.getProductsName());

            }
        };
        myProducts_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MyProductListViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MyProductListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }
        public void setProductsImage(Context ctx,String productsImage) {
            ImageView myproductpic = (ImageView) mView.findViewById(R.id.MyProduct_Picture);
            Glide.with(ctx).load(productsImage).into(myproductpic);
        }

        public void setProductsName(String productsName) {
            TextView myproductname = (TextView) mView.findViewById(R.id.MyProduct_Name);
            myproductname.setText(productsName);
        }
    }

}
