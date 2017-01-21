package com.tasty.wines.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tasty.wines.app.models.Review;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ReviewViewHolder(View itemView) {
        super(itemView);
        new ReviewViewHolder_ViewBinding<>(this, itemView);
    }

    private void updateView() {
        String bodyText = review.getBody();
        if (review.getUser() != null) {
            bodyText = bodyText + "\nBy: " + review.getUser().getNickname();
        }
        body.setText(bodyText);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(review.getDate());

        date.setText(format.format(c.getTime()));
        ratingBar.setRating(review.getScore());
    }

    public void setReview(Review review) {
        this.review = review;
        updateView();
    }
}
