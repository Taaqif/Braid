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
 * Created by Taaqif on 29/10/2017.
 */

public class BookAPIHelper {

    public BookAPIHelper(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    private static String baseURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.


    public void getBookFromISBN(String isbn, final VolleyCallback callback) {

        JsonObjectRequest arrReq = new JsonObjectRequest (Request.Method.GET, baseURL + isbn,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.VolleyResponse(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
//                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }
}
