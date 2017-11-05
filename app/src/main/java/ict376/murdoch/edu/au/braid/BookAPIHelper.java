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

import org.json.JSONArray;
import org.json.JSONException;
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


    public void getBookFromISBN(final String isbn, final VolleyCallback callback) {

        JsonObjectRequest arrReq = new JsonObjectRequest (Request.Method.GET, baseURL + isbn,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //is a json object
                        try {
                            JSONArray resBookArray = response.getJSONArray("items");
                            Book tmp = new Book(-1);
                            String thumbnail = "";
                            if(resBookArray.length()>0){

                                JSONObject bookInfo = resBookArray.getJSONObject(0).getJSONObject("volumeInfo");
                                //Toast.makeText(getActivity(), "ISBN data found", Toast.LENGTH_SHORT).show();

                                //set the data
                                if(bookInfo.has("title")){

                                    tmp.setTitle(bookInfo.getString("title"));
                                }
                                tmp.setISBN(isbn);
                                if(bookInfo.has("authors")){
                                    tmp.setAuthors(bookInfo.getJSONArray("authors").join("; ").replace("\"",""));
                                }
                                if(bookInfo.has("pageCount")){
                                    tmp.setTotalPages(Integer.parseInt(bookInfo.getString("pageCount")));
                                }
                                if(bookInfo.has("publisher")){
                                    tmp.setPublisher(bookInfo.getString("publisher"));
                                }
                                if(bookInfo.has("publishedDate")){
                                    tmp.setPublishedDate(bookInfo.getString("publishedDate"));
                                }
                                if(bookInfo.has("imageLinks")) {
                                    thumbnail = bookInfo.getJSONObject("imageLinks").getString("thumbnail");
                                }
                                callback.VolleyBookResponse(tmp, thumbnail);
                            }

                        } catch (JSONException e) {
                            callback.VolleyBookResponse(null, null);
                            e.printStackTrace();
                            //Toast.makeText(getActivity(), "ISBN data could not be read", Toast.LENGTH_SHORT).show();
                        }
                        //should probably do the JSON processing here and just pass the book to the
                        //callback
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
        void VolleyBookResponse(Book book, String cover_url);
    }
}
