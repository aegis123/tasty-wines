package com.tasty.wines.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tasty.wines.app.R;
import com.tasty.wines.app.models.Grape;
import com.tasty.wines.app.models.Review;
import com.tasty.wines.app.models.User;
import com.tasty.wines.app.models.Wine;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    public String EXTRA_WINE = "extra-wine";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String wineIdToReview = "";

    private boolean loggedIn = false;
    private String userName = "";
    private DatabaseReference mWineReference;

    private RatingBar ratingBar;
    private EditText reviewBody;
    private AutoCompleteTextView wineGrape;
    private AutoCompleteTextView wineName;
    private AutoCompleteTextView wineColor;

    private String uid;
    private ValueEventListener valueListener;

    ArrayAdapter<String> autoCompleteWineNames;
    ArrayAdapter<String> autoCompleteWineGrapes;
    ArrayAdapter<String> autoCompleteWineColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ratingBar = (RatingBar) findViewById(R.id.review_stars);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1.0f);

        reviewBody = (EditText) findViewById(R.id.review_body);

        wineGrape = (AutoCompleteTextView) findViewById(R.id.review_grape);
        wineName = (AutoCompleteTextView) findViewById(R.id.review_wine_name);
        wineColor = (AutoCompleteTextView) findViewById(R.id.review_color);

        Button addButton = (Button) findViewById(R.id.review_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userName = user.getDisplayName();
                    uid = user.getUid();
                    loggedIn = true;
                } else {
                    // User is signed out
                    loggedIn = false;
                    userName = "";
                    uid = "";

                    LoginHelper.openLoginDialog(ReviewActivity.this);
                }
            }
        };


        loadWineInfo();
        loadGrapes();
        loadColors();
        loadWines();
    }

    private void loadGrapes() {
        autoCompleteWineGrapes = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, new ArrayList<String>());

        DatabaseReference grapeReference = mFirebaseDatabase.getReference().child("grapes");

        grapeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> grapesInDb = dataSnapshot.getChildren();
                for (DataSnapshot g : grapesInDb) {
                    Grape grape = g.getValue(Grape.class);
                    if (grape != null) {
                        autoCompleteWineGrapes.add(grape.getGrape());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        wineGrape.setAdapter(autoCompleteWineGrapes);
    }

    private void loadColors() {
        autoCompleteWineColors = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, new ArrayList<String>());

        autoCompleteWineColors.add(Wine.RED);
        autoCompleteWineColors.add(Wine.WHITE);
        autoCompleteWineColors.add(Wine.ROSE);

        wineColor.setAdapter(autoCompleteWineColors);
    }

    private void loadWines() {
        autoCompleteWineNames = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, new ArrayList<String>());

        DatabaseReference grapeReference = mFirebaseDatabase.getReference().child("wines");

        grapeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> winesInDb = dataSnapshot.getChildren();
                for (DataSnapshot g : winesInDb) {
                    Wine wine = g.getValue(Wine.class);
                    if (wine != null) {
                        autoCompleteWineNames.add(wine.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        wineName.setAdapter(autoCompleteWineGrapes);
    }

    private void loadWineInfo() {
        Intent intent = getIntent();

        wineIdToReview = (intent != null) ? intent.getStringExtra(EXTRA_WINE) : null;

        if (wineIdToReview == null) {
            Toast.makeText(this, "No wine defined", Toast.LENGTH_LONG);
            wineIdToReview = "-Kb0-WZ0C7Zp6VNhdv7a"; //TODO remove
            //finish();
            //return;
        }
        mWineReference = mFirebaseDatabase.getReference().child("wines").child(wineIdToReview);

        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Wine wine = dataSnapshot.getValue(Wine.class);
                if (wine != null) {
                    setTitle("Rate wine: " + wine.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mWineReference.addListenerForSingleValueEvent(valueListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (valueListener != null) {
            mWineReference.removeEventListener(valueListener);
            valueListener = null;
        }
    }

    private void add() {

        User user = new User();
        user.setNickname(userName);
        user.setUid(uid);

        Review reviewToSave = new Review();
        reviewToSave.setBody(reviewBody.getText().toString());
        reviewToSave.setDate(System.currentTimeMillis());
        reviewToSave.setScore(Math.round(ratingBar.getRating()));
        reviewToSave.setUser(user);

        mWineReference.child("reviews").push().setValue(reviewToSave);

        reviewBody.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoginHelper.RC_SIGN_IN) {
            if (!LoginHelper.verifyLogin(requestCode, resultCode, data, this)) {
                finish();
            }
        }
    }
}
