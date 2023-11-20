package t2010a.cookpad_clone.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import t2010a.cookpad_clone.model.LoginResponse;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.model.shop.Cart;
import t2010a.cookpad_clone.model.shop.CartItem;
import t2010a.cookpad_clone.model.shop.Favourite;
import t2010a.cookpad_clone.model.shop.FavouriteModel;
import t2010a.cookpad_clone.model.shop.Product;
import t2010a.cookpad_clone.model.shop.ShopModel;


public interface ApiService {
    String SERVER = "http://10.0.2.2:8066";

    @POST("/api/v1/register")
    Call<User> registerUser(@Body User user);

    @POST("/api/v1/login")
    Call<LoginResponse> loginUser(@Body User user);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/v1/user/profile")
    Call<User> getUser(@Header("Authorization") String token);

    @POST("/api/v1/post")
    Call<Content> createPost(@Body Content content);

    @DELETE("api/v1/post/{id}")
    Call<Content> deletePost(@Path("id") int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("/api/v1/user/{id}")
    Call<User> updateUser(@Body User user,
                          @Path(value = "id") Long id,
                          @Header("Authorization") String token);

    @GET("/api/v1/post")
    Call<HomeModel> getPostList(@Query("page") int page,
                                @Query("limit") int limit,
                                @Query("status") int status,
                                @Query("userPostId") long userId,
                                @Query("sort") String sortId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("/api/v1/post/{id}")
    Call<Content> updatePost(@Path(value = "id") Long id,
                             @Body Content content,
                             @Header("Authorization") String token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("/api/v1/post/{id}")
    Call<Content> deletePost(@Path(value = "id") long id,
                             @Header("Authorization") String token);

    @DELETE("/api/v1/post/{id}")
    Call<Content> deletePost1(@Path(value = "id") long id);

    @GET("/api/v1/user/products")
    Call<ShopModel> getProduct(@Query("page") int page,
                               @Query("limit") int limit,
                               @Query("status") int status,
                               @Query("userPostId") long userId,
                               @Query("sort") String sortId,
                               @Query("sortPrice") String sortPrice);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/v1/user/cart/add")
    Call<String> addToCart(@Header("Authorization") String token,
                           @Query(value = "productId") long productId,
                           @Query(value = "quantity") int quantity);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("/api/v1/user/cart/add")
    Call<String> deleteCart(@Header("Authorization") String token,
                            @Query(value = "cartId") long cartId,
                            @Query(value = "productId") long productId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("/api/v1/user/cart/update")
    Call<String> updateCart(@Header("Authorization") String token,
                           @Query(value = "cartId") long cartId,
                           @Query(value = "productId") long productId,
                           @Query(value = "quantity") int quantity);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/v1/user/cart")
    Call<List<CartItem>> findAllCart(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/v1/user/favourite")
    Call<FavouriteModel> getFavourite(@Header("Authorization") String token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/v1/user/favourite/{productId}")
    Call<FavouriteModel> postFavourite(@Header("Authorization") String token,
                                       @Path(value = "productId") Long productId);
}
