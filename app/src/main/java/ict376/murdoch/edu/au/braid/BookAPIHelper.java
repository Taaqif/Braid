package ict376.murdoch.edu.au.braid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Book API to help get books from ISBN by use of Volley library.
 * Adapted from https://developer.android.com/training/volley/index.html
 * Created by Taaqif on 29/10/2017.
 */

public class BookAPIHelper {

    public BookAPIHelper(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    private static String baseURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    RequestQueue requestQueue;  // HTTP requests queue.


    public void getBookFromISBN(String isbn, final VolleyCallback callback) {

        JsonObjectRequest arrReq = new JsonObjectRequest (Request.Method.GET, baseURL + isbn,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //should probably do the JSON processing here and just pass the book to the
                        //callback
                        callback.VolleyResponse(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error with request
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Add the request we just defined to our request queue.
        requestQueue.add(arrReq);
    }
    //the callback interface
    public interface VolleyCallback {
        void VolleyResponse(JSONObject books);
    }
}
