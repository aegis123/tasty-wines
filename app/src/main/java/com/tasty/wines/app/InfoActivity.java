package com.tasty.wines.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tasty.wines.app.models.Review;
import com.tasty.wines.app.models.Wine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;

public class InfoActivity extends AppCompatActivity {

    private static final String DB_KEY = "db_key";
    private static final String WINE_NAME = "wine_names";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_winery)
    TextView winery;
    @BindView(R.id.tv_wine_name)
    TextView name;
    @BindView(R.id.tv_date_added)
    TextView date;
    @BindView(R.id.tv_year)
    TextView year;
    @BindView(R.id.rt_rating)
    RatingBar ratingBar;
    @BindView(R.id.rv_reviews)
    RecyclerView recyclerView;

    private final DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    public static void startInfoActivity(Context context, @NonNull final String key, @NonNull final String name) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(DB_KEY, key);
        intent.putExtra(WINE_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        new InfoActivity_ViewBinding<>(this, getWindow().getDecorView());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra(DB_KEY)) {
            String key = getIntent().getStringExtra(DB_KEY);
            String wineName = getIntent().getStringExtra(WINE_NAME);
            DatabaseReference wineDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tasty-wine.firebaseio.com/wines").child(key);
            final Query reviewRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tasty-wine.firebaseio.com/reviews").orderByChild("wine").equalTo(wineName).getRef();

            wineDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Wine wine = dataSnapshot.getValue(Wine.class);
                    setTitle(wine.getName());

                    winery.setText(wine.getWinery());
                    name.setText(wine.getName());
                    if (wine.getDateAdded() != null) {

                        date.setText(dateFormat.format(wine.getDateAdded().getTime()));
                    }
                    year.setText(String.format("%s", wine.getYear()));
                    ratingBar.setRating(wine.getRating());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final FirebaseRecyclerAdapter<Review, ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class, R.layout.li_review, ReviewViewHolder.class, reviewRef) {
                @Override
                protected void populateViewHolder(ReviewViewHolder viewHolder, Review model, int position) {
                    viewHolder.setReview(model);
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);


        } else {
            throw new RuntimeException("activity not started with a " + DB_KEY + " in the intent");
        }
    }
}
