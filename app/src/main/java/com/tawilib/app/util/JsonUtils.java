package com.tawilib.app.util;

import android.util.Log;

import com.google.gson.Gson;
import com.tawilib.app.data.model.Book;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static List<Book> fromMap(HashMap<String, List<Book>> map) {

        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(map);
            Iterator<String> keys = jsonObject.keys();

            if(keys.hasNext()) {

                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    JSONObject subObject = (JSONObject) jsonObject.get(key);
                    Iterator<String> ks = subObject.keys();;

                    while (ks.hasNext()) {
                        String k = ks.next();
                        Object o = subObject.get(k);
                        Book book = new Gson()
                                .fromJson(o.toString(), Book.class);
                        book.setId(k);
                        books.add(book);
                    }

                }
            }
        } catch (Exception e) {
            Log.d(TAG, "convertToBookList: " + e.getMessage());
        }


        return books;
    }
}
