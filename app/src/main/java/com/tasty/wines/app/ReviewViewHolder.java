package com.tasty.wines.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tasty.wines.app.models.Review;

import butterknife.BindView;


public class ReviewViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_nickname)
    TextView nickname;
    @BindView(R.id.tv_review_body)
    TextView body;
    @BindView(R.id.tv_review_date)
    TextView date;
    @BindView(R.id.review_stars)
    RatingBar ratingBar;
    private Review review;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        new ReviewViewHolder_ViewBinding<>(this, itemView);
    }

    private void updateView() {
        body.setText(review.getBody());
        date.setText(String.format("%s", review.getDate()));
        ratingBar.setRating(review.getScore());
    }

    public void setReview(Review review) {
        this.review = review;
        updateView();
    }
}
