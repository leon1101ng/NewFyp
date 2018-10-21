package net.leon.myfypproject2.Function;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.leon.myfypproject2.LiveStream.PlayStreamVideo;
import net.leon.myfypproject2.Model.ProductsClass;
import net.leon.myfypproject2.ProductMgnt.AddCartProducts;
import net.leon.myfypproject2.ProductMgnt.MyProductsView;
import net.leon.myfypproject2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProduct extends Fragment {

    private RecyclerView productslist;
    private DatabaseReference ProductsViewRef,RatingRef ;
    private FirebaseAuth mAuth;
    private String current_UserID;


    public ViewProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        ProductsViewRef = FirebaseDatabase.getInstance().getReference().child("Products");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);

        productslist = (RecyclerView)view.findViewById(R.id.all_products_list2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        productslist.setLayoutManager(linearLayoutManager);
        productslist.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("The Starry");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ProductsClass,ProductListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductsClass, ProductListViewHolder>(
                ProductsClass.class,
                R.layout.viewporductslist,
                ProductListViewHolder.class,
                ProductsViewRef
        ) {
            @Override
            protected void populateViewHolder(ProductListViewHolder viewHolder, ProductsClass model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setProductsImage(getContext(), model.getProductsImage());
                viewHolder.setProductsName(model.getProductsName());
                viewHolder.setProductsPrice(String.valueOf(model.getProductsPrice()));
                viewHolder.ProductUserImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), AddCartProducts.class);
                        i.putExtra("PostKey", postkey);
                        startActivity(i);
                    }
                });

            }
        };
        productslist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private ImageView ProductUserImage;
        public ProductListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ProductUserImage =(ImageView) mView.findViewById(R.id.ProductImageView);


        }
        public void setProductsImage(Context ctx,String productsImage) {
            ImageView myproductpic = (ImageView) mView.findViewById(R.id.ProductImageView);
            Glide.with(ctx).load(productsImage).into(myproductpic);
        }

        public void setProductsName(String productsName) {
            TextView myproductname = (TextView) mView.findViewById(R.id.ProductsNameView);
            myproductname.setText(productsName);
        }

        public void setProductsPrice(String productsPrice) {
            TextView myproductprice = (TextView) mView.findViewById(R.id.ProductPriceView);
            myproductprice.setText(productsPrice);
        }

    }


}
