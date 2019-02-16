package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject reader = new JSONObject(json);
            JSONObject nameJson = reader.getJSONObject("name");
            String mainName = nameJson.getString("mainName");
            JSONArray arrayObj = nameJson.getJSONArray("alsoKnownAs");
            List<String> alsoKnownAs = new ArrayList<>();
            for (int i = 0; i < arrayObj.length(); i++) alsoKnownAs.add(arrayObj.optString(i));

            String placeOfOrigin = reader.getString("placeOfOrigin");
            String description = reader.getString("description");
            String image = reader.getString("image");
            arrayObj = reader.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (int i = 0; i < arrayObj.length(); i++) ingredients.add(arrayObj.optString(i));

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
