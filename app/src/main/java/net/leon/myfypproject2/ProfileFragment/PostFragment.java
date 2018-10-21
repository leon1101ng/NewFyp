package net.leon.myfypproject2.ProfileFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.leon.myfypproject2.Model.Status;
import net.leon.myfypproject2.Model.Video;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.Imagepost;
import net.leon.myfypproject2.UserInterface.StatusPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private RecyclerView statuspostlist;
    private DatabaseReference StatusPostRef;
    private FirebaseAuth mAuth;
    private String CurrentUser;


    public PostFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        StatusPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Status_Posts");



        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        View view =  inflater.inflate(R.layout.fragment_post, container, false);
        statuspostlist = (RecyclerView)view.findViewById(R.id.Post_post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        statuspostlist.setLayoutManager(linearLayoutManager);

        return view;


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.statuspostfragmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Drawable drawable = menu.findItem(R.id.add_status).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_status:
                Intent addimge = new Intent(getContext(), StatusPost.class);
                startActivity(addimge);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query  userstatuspost = StatusPostRef.orderByChild("UserID").startAt(CurrentUser).endAt(CurrentUser + "\uf8ff");
        FirebaseRecyclerAdapter<Status,StatusPostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Status, StatusPostHolder>(
                Status.class,
                R.layout.statuslayout,
                StatusPostHolder.class,
                userstatuspost) {
            @Override
            protected void populateViewHolder(StatusPostHolder viewHolder, Status model, int position) {
                viewHolder.setStatus(model.getStatus());
            }
        };
        statuspostlist.setAdapter(firebaseRecyclerAdapter);


    }
    public static class StatusPostHolder extends RecyclerView.ViewHolder{
        View mView;
        public StatusPostHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setStatus(String status){
            TextView Status = (TextView)mView.findViewById(R.id.statusText);
            Status.setText(status);

        }
    }
}
