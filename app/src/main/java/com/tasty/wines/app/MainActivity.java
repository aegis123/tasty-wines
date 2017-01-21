package com.tasty.wines.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tasty.wines.app.activities.ReviewActivity;
import com.tasty.wines.app.models.Wine;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_winelist)
    RecyclerView recyclerView;
    private RecyclerView.Adapter<WineViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainActivity_ViewBinding<>(this, getWindow().getDecorView());

        setSupportActionBar(toolbar);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tasty-wine.firebaseio.com/wines");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
