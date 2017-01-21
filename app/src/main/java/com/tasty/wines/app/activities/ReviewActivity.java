package com.tasty.wines.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    public String EXTRA_WINE = "extra-wine";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String wineIdToReview = "";

    private boolean loggedIn = false;
    private String userName = "";


    private RatingBar ratingBar;
    private EditText reviewBody;
    private AutoCompleteTextView wineGrape;
    private AutoCompleteTextView wineName;
    private AutoCompleteTextView wineColor;
    private EditText wineYear;

    private String uid;

    ArrayAdapter<String> autoCompleteWineNames;
    ArrayAdapter<String> autoCompleteWineGrapes;
    ArrayAdapter<String> autoCompleteWineColors;

    private List<Wine> wines = new ArrayList<>();
    private EditText wineCountry;
    private EditText wineWinery;


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
        wineYear = (EditText) findViewById(R.id.review_year);

        wineCountry = (EditText) findViewById(R.id.review_country);
        wineWinery = (EditText) findViewById(R.id.review_winery);

        wineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                if (value == null || value.length() <= 1) {
                    return;
                }
                for (Wine wine : wines) {
                    if (wine.getName().equals(value)) {
                        wineColor.setText(wine.getColor());
                        wineGrape.setText(wine.getGrape());
                        wineCountry.setText(wine.getCountry());
                        wineWinery.setText(wine.getWinery());
                        wineYear.setText("" + wine.getYear());
                    }
                }
            }
        });

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
                        //Yes, this is O^2
                        autoCompleteWineGrapes.remove(grape.getGrape());
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
                        wine.setKey(g.getKey());
                        wines.add(wine);

                        autoCompleteWineNames.add(wine.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        wineName.setAdapter(autoCompleteWineNames);
    }

    private void loadWineInfo() {
        Intent intent = getIntent();

        wineIdToReview = (intent != null) ? intent.getStringExtra(EXTRA_WINE) : "";

        wineName.setText(wineIdToReview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void saveWine(final DatabaseReference wineReference, Wine wine, final Review review) {
        wine.setName(wineName.getText().toString());
        wine.setColor(wineColor.getText().toString());
        wine.setCountry(wineCountry.getText().toString());
        wine.setGrape(wineGrape.getText().toString());
        wine.setWinery(wineWinery.getText().toString());
        wine.setYear(Integer.parseInt(wineYear.getText().toString()));

        wineReference.setValue(wine).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference reviewReference = mFirebaseDatabase.getReference("reviews").push();
                reviewReference.setValue(review).addOnCompleteListener(ReviewActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });

            }
        });
    }

    private void saveWine(final Review review) {
        String wine = wineName.getText().toString();
        if (wine.length() < 3) {
            Toast.makeText(this, "Wine name too short", Toast.LENGTH_LONG).show();
            return;
        }

        Wine existingWine = null;
        for (Wine w : wines) {
            if (w.getName().equals(wine)) {
                existingWine = w;
            }
        }

        DatabaseReference winesReference = mFirebaseDatabase.getReference("wines");
        final DatabaseReference wineReference;

        if (existingWine == null) {
            wineReference = winesReference.push();
            Wine wineToSave = new Wine();
            saveWine(wineReference, wineToSave, review);
        } else {
            wineReference = winesReference.child(existingWine.getKey());
            wineReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    saveWine(wineReference, dataSnapshot.getValue(Wine.class), review);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
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
        reviewToSave.setWine(wineName.getText().toString());

        saveWine(reviewToSave);
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
