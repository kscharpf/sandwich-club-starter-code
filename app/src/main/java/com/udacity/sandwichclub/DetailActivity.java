package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView imageIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        final TextView loadingTv = findViewById(R.id.image_loading_tv);
        populateUI(sandwich);
        Log.v(TAG, "Sandwich image URL: " + sandwich.getImage());
        Picasso.get()
                .load(sandwich.getImage())
                .into(imageIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageIv.setVisibility(View.VISIBLE);
                        loadingTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        TextView errorTv = findViewById(R.id.image_not_found_tv);
                        errorTv.setVisibility(View.VISIBLE);
                        loadingTv.setVisibility(View.GONE);
                    }

                });


        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView tv = findViewById(R.id.origin_tv);
        if (sandwich.getPlaceOfOrigin() == null || sandwich.getPlaceOfOrigin().equals("")) {
            tv.setText(R.string.unknown_label);
        } else {
            tv.setText(sandwich.getPlaceOfOrigin());
        }
        tv = findViewById(R.id.also_known_tv);
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            tv.setText("N/A");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : sandwich.getAlsoKnownAs()) {
                sb.append(s).append(", ");
            }
            tv.setText(sb.deleteCharAt(sb.length() - 2).toString());
        }

        tv = findViewById(R.id.description_tv);
        if (sandwich.getDescription() == null || sandwich.getDescription().equals("")) {
            tv.setText(R.string.unavailable_label);
        } else {
            tv.setText(sandwich.getDescription());
        }

        tv = findViewById(R.id.ingredients_tv);
        Log.v(TAG, "Ingredients len: " + sandwich.getIngredients().size());
        if (sandwich.getIngredients().isEmpty()) {
            tv.setText(R.string.not_listed_label);
        } else {
            StringBuilder sb = new StringBuilder();

            for (String s : sandwich.getIngredients()) {
                sb.append(s).append(", ");
            }

            tv.setText(sb.deleteCharAt(sb.length() - 2).toString());
        }
    }
}
