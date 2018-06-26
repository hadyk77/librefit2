package com.project.librefit.librefit2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.PendingIntent.getActivity;

public class profiledemo extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener {
    ImageView profile_pic, cover,imgvw,open_nav, follow,aboutme;
    TextView username, email, user_nav, title;
    DrawerLayout nav;
    NavigationView navigationView;
    View hView;
    ListView listView;
    nonsrollable profile_content;
    String[] items = {"settings", "logout", "rate us", "contact us"};
    int[] image_arr = new int[4];
    final int ChooseImage = 101;
    private StorageReference mstorage;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    account_info search_object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledemo);
        intial_variables();

        myAccount();
        open_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.openDrawer(Gravity.LEFT);
            }
        });
        Initialize_array();
        content_adapter ca = new content_adapter();
        profile_content.setAdapter(ca);
        adapter ad = new adapter();
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(profiledemo.this, login_without_anim.class));
                    finish();
                }

            }
        });
        profile_pic.setOnClickListener(this);
        menu();

    }


    public void intial_variables() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView = navigationView.inflateHeaderView(R.layout.customheader);
        imgvw = (ImageView) hView.findViewById(R.id.profile_image);
        profile_pic = (ImageView) findViewById(R.id.profile_image);
        username = (TextView) findViewById(R.id.username);
        nav = (DrawerLayout) findViewById(R.id.drawer_layout);
        open_nav = (ImageView) findViewById(R.id.open_nev);
        user_nav = (TextView) hView.findViewById(R.id.username_navigation);
        email = (TextView) findViewById(R.id.email);
        title = (TextView) findViewById(R.id.Title);
        listView = findViewById(R.id.listview);
        profile_content = (nonsrollable) findViewById(R.id.profile_content);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);
        scrollView.fullScroll(View.FOCUS_UP);
        cover = (ImageView) findViewById(R.id.cover);
        follow = findViewById(R.id.follow);
        search_object=(account_info) getIntent().getSerializableExtra("search");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void close(View view) {
        nav.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //display images in full screen
    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(profiledemo.this, show_fullscreen_img.class);

        if (v == profile_pic || v == imgvw) {
            FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid()).child("image_url")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            intent.putExtra("profile", dataSnapshot.getValue().toString());
                            Log.v("URl", dataSnapshot.getValue().toString());
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }


    }

    public void search_on_click(View view) {
        startActivity(new Intent(this,seach_Activity.class));
    }


    class adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.text_view, null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(items[position]);
            return convertView;
        }
    }

    public void Initialize_array() {
        image_arr[0] = R.drawable.tshirt;
        image_arr[1] = R.drawable.tshirt;
        image_arr[2] = R.drawable.tshirt;
        image_arr[3] = R.drawable.tshirt;

    }

    //slider adapter
    class content_adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return image_arr.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.content_design, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.tshirt);
            imageView.setImageResource(R.drawable.tshirt);
            return convertView;
        }
    }

    //show Gallary
    public void showImage_picker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose profile pic"), ChooseImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Picasso.get().load(data.getData()).into(profile_pic);
            Picasso.get().load(data.getData()).into(imgvw);
            mstorage = FirebaseStorage.getInstance().getReference();
            mstorage.child("profile_images").child(mAuth.getCurrentUser().getUid()).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference ref = mstorage.child("profile_images").child(mAuth.getCurrentUser().getUid());
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                            mAuth.getCurrentUser().updateProfile(request);
                        }
                    });
                }
            });


            // userInfo info=(userInfo)getIntent().getExtras().getSerializable("info");
            // info.url=data.getData();
        }
    }

    //update user info
    private void myAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(search_object!=null&&!search_object.UID.equals(user.getUid()))
        {
            LinearLayout cups=findViewById(R.id.cups);
            LinearLayout contact=findViewById(R.id.contact);
            aboutme=findViewById(R.id.aboutme);
            cups.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            aboutme.setVisibility(View.GONE);
            username.setText(search_object.name);
            Picasso.get().load(search_object.image_url).into(profile_pic);
            if(user.getPhotoUrl().equals("empty")){
                imgvw.setImageResource(R.drawable.unknown);
                }
                else {
                Picasso.get().load(user.getPhotoUrl()).into(imgvw);
            }
        }
        else {
            account_info info;
            if (user != null) {
                for (UserInfo userInfo : user.getProviderData()) {
                    if (userInfo.getProviderId().equals("facebook.com") && user.getPhotoUrl().toString().contains("graph")) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(Profile.getCurrentProfile().getProfilePictureUri(400, 400)).build();
                        user.updateProfile(request);
                    } else if (userInfo.getProviderId().equals("google.com")) {
                        String url = user.getPhotoUrl().toString();
                        String edit = url.replace("s96-c", "s400-c");
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(edit)).build();
                        user.updateProfile(request);
                    }
                }
                if (user.getPhotoUrl() != null) {
                    info = new account_info();
                    info.image_url = user.getPhotoUrl().toString();
                    info.email = user.getEmail();
                    info.name = user.getDisplayName();
                    info.UID = user.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
                    reference.setValue(info);
                }

                FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                account_info info = dataSnapshot.getValue(account_info.class);
                                if (info.image_url.equals("empty")) {
                                    profile_pic.setImageResource(R.drawable.unknown);
                                    imgvw.setImageResource(R.drawable.unknown);
                                } else {
                                    Picasso.get().load(info.image_url).into(profile_pic);
                                    Picasso.get().load(info.image_url).into(imgvw);
                                }
                                username.setText(info.name);
                                user_nav.setText(info.name);
                                email.setText(info.email);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_another_photo:
                showImage_picker();
                return true;
            case R.id.remove:
                profile_pic.setImageResource(R.drawable.unknown);
                imgvw.setImageResource(R.drawable.unknown);
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("empty")).build();
                mAuth.getCurrentUser().updateProfile(request);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void follow(View v) {
        if (follow.getDrawable().equals(R.drawable.follow)) {
            follow.setImageResource(R.drawable.followed);
        } else
            follow.setImageResource(R.drawable.follow);
    }

    //menu
    private void menu() {
        registerForContextMenu(profile_pic);
        registerForContextMenu(imgvw);
        registerForContextMenu(cover);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.profile_pic_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("empty")).build();
                profile_pic.setImageResource(R.drawable.unknown);
                imgvw.setImageResource(R.drawable.unknown);
                mAuth.getCurrentUser().updateProfile(request);
                return true;
            case R.id.choose_another_photo:
                myAccount();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}