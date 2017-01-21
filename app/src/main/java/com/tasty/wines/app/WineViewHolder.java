package com.tasty.wines.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tasty.wines.app.models.Wine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;


public class WineViewHolder extends RecyclerView.ViewHolder {
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

    private final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
    private Wine wine;

    public WineViewHolder(View itemView) {
        super(itemView);
        new WineViewHolder_ViewBinding<>(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.startInfoActivity(v.getContext(), wine.getKey());
            }
        });

    }

    void setWine(Wine wine) {
        this.wine = wine;
        updateView();
    }

    private void updateView() {
        winery.setText(wine.getWinery());
        name.setText(wine.getName());
        if (wine.getDateAdded() != null) {

            date.setText(dateFormat.format(wine.getDateAdded().getTime()));
        }
        year.setText(String.format("%s", wine.getYear()));
        ratingBar.setRating(wine.getRating());
    }
}
