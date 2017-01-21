package com.tasty.wines.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tasty.wines.app.models.Wine;

import butterknife.BindView;


class WineViewHolder extends RecyclerView.ViewHolder {
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

    private Wine wine;

    public WineViewHolder(View itemView) {
        super(itemView);
        new WineViewHolder_ViewBinding<>(this, itemView);

    }

    void setWine(Wine wine) {
        this.wine = wine;
        updateView();
    }

    private void updateView() {
        winery.setText(wine.getWinery());
        name.setText(wine.getWinery());
        date.setText(wine.getWinery());
        year.setText(String.format("%s", wine.getYear()));
        ratingBar.setRating(wine.getRating());
    }
}
