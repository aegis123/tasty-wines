package com.tasty.wines.app;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tasty.wines.app.models.Wine;

import java.util.Calendar;

import butterknife.BindView;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_winelist)
    RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Wine, WineViewHolder> adapter;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new SearchActivity_ViewBinding<>(this, getWindow().getDecorView());

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tasty-wine.firebaseio.com/wines");
        adapter = new FirebaseRecyclerAdapter<Wine, WineViewHolder>(Wine.class, R.layout.li_wine, WineViewHolder.class, databaseReference) {

            @Override
            protected Wine parseSnapshot(DataSnapshot snapshot) {
                Wine wine = super.parseSnapshot(snapshot);
                if (wine != null) {
                    wine.setKey(snapshot.getKey());

                    final DataSnapshot dateAdded = snapshot.child("dateAdded");

                    if (dateAdded.exists()) {
                        Long timeStamp = (Long) dateAdded.getValue();
                        final Calendar instance = Calendar.getInstance();
                        instance.setTimeInMillis(timeStamp);
                        wine.setDateAdded(instance);
                    }
                }

                return wine;
            }

            @Override
            protected void populateViewHolder(WineViewHolder viewHolder, Wine model, int position) {
                viewHolder.setWine(model);
            }
        };
        recyclerView.setAdapter(adapter);// Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
    }
}
