package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with the viewholder
        holder.bind(tweet);
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear(); //SHOULD THIS BE TWEETS?
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list); //SHOULD THIS BE TWEETS?
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImage;
        TextView tvTimestamp;
        ProgressBar pbLoading;
        // Button btnReply;
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            pbLoading = itemView.findViewById(R.id.pbLoading);
            // btnReply = itemView.findViewById(R.id.btnReply);

        }
        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(rawJsonDate).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + "m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + "h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + "d";
                }
            } catch (ParseException e) {
                //Log.i(TAG, "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }

        public void bind(Tweet tweet) {
            // on some click or some loading we need to wait for...
            pbLoading.setVisibility(ProgressBar.VISIBLE);

            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            String timestamp = getRelativeTimeAgo(tweet.createdAt);
            tvTimestamp.setText(timestamp);
            Glide.with(context).load(tweet.user.profileImageUrl).circleCrop().into(ivProfileImage);
            if (tweet.tweetImageUrl != null) {
                ivTweetImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.tweetImageUrl).into(ivTweetImage);
            }
            else {
                ivTweetImage.setVisibility(View.GONE);
            }
            // run a background job and once complete
            pbLoading.setVisibility(ProgressBar.INVISIBLE);

            // Set click listener on button
//            btnReply.setOnClickListener(new View.OnClickListener() {
//                // --THIS--
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ComposeActivity.class);
//
//                }
//            });
        }
    }
}
