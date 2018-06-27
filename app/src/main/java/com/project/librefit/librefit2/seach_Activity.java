package com.project.librefit.librefit2;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class seach_Activity extends AppCompatActivity {
private EditText search_field;
private RecyclerView recyclerView;
private DatabaseReference reference;
private List<account_info> mList;
    search_adapter search_adapter;
private FirebaseRecyclerAdapter<account_info,viewholder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_);
        intial();
       search_field.addTextChangedListener(new TextWatcher() {
           int previousLength;
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 previousLength=s.length();
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {




           }

           @Override
           public void afterTextChanged(Editable s) {


            boolean   backSpace = previousLength > s.length();

               if (backSpace) {
                   Handler handler=new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           int size = mList.size();
                           mList.clear();
                           recyclerView.setAdapter(search_adapter);
                           search_adapter.notifyItemRangeRemoved(0, size);
                           recyclerView.removeAllViewsInLayout();
                       }
                   },300);



               }
               if (!s.toString().equals("")) {
                   search2(s.toString());
               }
           }
       });

    }

    private void search(String text) {

        Query queryRef = reference.orderByChild("name_lowercase").startAt(text.toLowerCase()).endAt(text.toLowerCase()+"\uf8ff");
        FirebaseRecyclerOptions <account_info> options=new FirebaseRecyclerOptions.Builder<account_info>().setQuery(queryRef,account_info.class).setLifecycleOwner(this)
               .build();
      adapter =new FirebaseRecyclerAdapter<account_info, viewholder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull account_info model) {
               if(model.image_url.equals("empty")){
                   holder.image.setImageResource(R.drawable.unknown);
               }
               else {
                   Picasso.get().load(model.image_url).into(holder.image);
               }
               holder.username.setText(model.name);
           }

           @NonNull
           @Override
           public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_item, parent, false);
               return new viewholder(view);
           }

       };
        recyclerView.setLayoutManager(new LinearLayoutManager(seach_Activity.this));
        recyclerView.setAdapter(adapter);
         }
    private void search2(final String text){

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                recyclerView.removeAllViews();
                 int counter=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    account_info info=(account_info)snapshot.getValue(account_info.class);
                    if(info.name.toLowerCase().startsWith(text.toLowerCase())){
                        mList.add(info);
                        counter++;
                    }
                    if(counter==15) {
                        break;
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(seach_Activity.this));
                recyclerView.setAdapter(search_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void intial(){
        search_field=(EditText)findViewById(R.id.search_field);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        reference=FirebaseDatabase.getInstance().getReference("user");
        mList=new ArrayList<>();
        search_adapter=new search_adapter(seach_Activity.this,mList);
    }
    public class viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView username;
        public viewholder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.profile_image);
            username=(TextView)itemView.findViewById(R.id.name);

        }


    }
}
