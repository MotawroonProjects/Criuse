package com.semicolon.criuse.Services;

import com.semicolon.criuse.Models.AllGroceries_SubCategory;
import com.semicolon.criuse.Models.ContactsModel;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.Models.MiniMarketDataModel;
import com.semicolon.criuse.Models.Object;
import com.semicolon.criuse.Models.ResponseModel;
import com.semicolon.criuse.Models.RuleModel;
import com.semicolon.criuse.Models.SuperMarketModel;
import com.semicolon.criuse.Models.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Service {

    @Multipart
    @POST("Api/ClientRegistration")
    Call<UserModel> ClientRegister(@Part MultipartBody.Part imageFile,
                                   @Part("user_name") RequestBody user_name,
                                   @Part("user_pass")RequestBody user_pass,
                                   @Part("user_phone")RequestBody user_phone,
                                   @Part("user_full_name")RequestBody user_full_name,
                                   @Part("user_token_id")RequestBody user_token_id,
                                   @Part("user_google_lat")RequestBody user_google_lat,
                                   @Part("user_google_long")RequestBody user_google_long,
                                   @Part("user_email")RequestBody user_email,
                                   @Part("user_photo_send")RequestBody user_photo_send);
    @Multipart
    @POST("Api/DriverRegistration")
    Call<UserModel> DriverRegister(
                                   @Part("user_name") RequestBody user_name,
                                   @Part("user_pass")RequestBody user_pass,
                                   @Part("user_phone")RequestBody user_phone,
                                   @Part("user_full_name")RequestBody user_full_name,
                                   @Part("user_token_id")RequestBody user_token_id,
                                   @Part("user_google_lat")RequestBody user_google_lat,
                                   @Part("user_google_long")RequestBody user_google_long,
                                   @Part("user_email")RequestBody user_email,
                                   @Part("user_neighborhood")RequestBody user_neighborhood,
                                   @Part("user_city")RequestBody user_city,
                                   @Part MultipartBody.Part user_photo,
                                   @Part MultipartBody.Part  user_license_photo,
                                   @Part MultipartBody.Part  user_car_photo,
                                   @Part MultipartBody.Part  user_residence_photo
                                   );
    @FormUrlEncoded
    @POST("Api/Login")
    Call<UserModel> Login(@Field("user_name") String user_name,
                          @Field("user_pass") String user_pass);

    @GET("Api/Logout/{user_id}")
    Call<ResponseModel> logout(@Path("user_id")String user_id);

    @FormUrlEncoded
    @POST("Api/UpdateLocation/{user_id}")
    Call<ResponseModel> updateLocation(@Path("user_id") String user_id ,
                                       @Field("user_google_lat") String user_google_lat,
                                       @Field("user_google_long") String user_google_long
                                       );
    @Multipart
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part MultipartBody.Part user_photo);

    @Multipart
    @POST("Api/UpdateClientRegistration/{user_id}")
    Call<UserModel> UpdateClientData(@Path("user_id") String user_id,
                                     @Part("user_name") RequestBody user_name,
                                     @Part("user_phone")RequestBody user_phone,
                                     @Part("user_full_name")RequestBody user_full_name,
                                     @Part("user_email")RequestBody user_email);

    @Multipart
    @POST("Api/UpdateDriverRegistration/{user_id}")
    Call<UserModel> UpdateDriverImage(@Path("user_id") String user_id,
                                      @Part("user_name")RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part("user_neighborhood")RequestBody user_neighborhood,
                                      @Part("user_city")RequestBody user_city,
                                      @Part MultipartBody.Part photo);

    @Multipart
    @POST("Api/UpdateDriverRegistration/{user_id}")
    Call<UserModel> UpdateDriverData(@Path("user_id") String user_id,
                                      @Part("user_name") RequestBody user_name,
                                      @Part("user_phone")RequestBody user_phone,
                                      @Part("user_full_name")RequestBody user_full_name,
                                      @Part("user_email")RequestBody user_email,
                                      @Part("user_neighborhood")RequestBody user_neighborhood,
                                      @Part("user_city")RequestBody user_city);

    @FormUrlEncoded
    @POST("Api/UpdatePass/{user_id}")
    Call<UserModel> updatePassword(@Path("user_id") String user_id,@Field("user_old_pass")String user_old_pass,@Field("user_new_pass")String user_new_pass);

    @GET("Api/AllDepartProduct")
    Call<List<AllGroceries_SubCategory>> getAllGrocery_subcategories();

    @Multipart
    @POST("Api/GroceryRegistration")
    Call<UserModel> groceryRegister(@Part("user_name") RequestBody user_name,
                                    @Part("user_pass")RequestBody user_pass,
                                    @Part("user_phone")RequestBody user_phone,
                                    @Part("user_full_name")RequestBody user_full_name,
                                    @Part("user_token_id")RequestBody user_token_id,
                                    @Part("user_google_lat")RequestBody user_google_lat,
                                    @Part("user_google_long")RequestBody user_google_long,
                                    @Part("user_email")RequestBody user_email,
                                    @Part("user_work_hours")RequestBody user_work_hours,
                                    @Part("my_products[]")List<RequestBody> id_product,
                                    @Part MultipartBody.Part user_photo);
    @GET("Api/AppDetails/3")
    Call<RuleModel> getRuleData();

    @POST("Api/mytester")
    Call<Object> test(@Body List<ItemsModel> lists);

    @GET("Api/ContactUs")
    Call<ContactsModel> getContacts();

    @FormUrlEncoded
    @POST("Api/ContactUs")
    Call<ResponseModel> ContactUs(@FieldMap Map<String,String> map);

    @GET("Api/SuperMarket")
    Call<List<SuperMarketModel>> getSuperMarketCategories();

    @FormUrlEncoded
    @POST("Api/MinMarket")
    Call<List<MiniMarketDataModel>> getMiniMarketData(@Field("my_google_lat") double my_google_lat,@Field("my_google_long") double my_google_long);

    @POST("Api/AddMinOrder")
    Call<ResponseModel> sendOrderToMiniMarket(@Body List<ItemsModel> itemsModelList);

    @POST("Api/AddSuperOrder")
    Call<ResponseModel> sendOrderToSuperMarket(@Body List<ItemsModel> itemsModelList);

    @FormUrlEncoded
    @POST("Api/RestMyPass")
    Call<ResponseModel> resetPassword(@Field("user_name") String user_name,@Field("user_email") String user_email);

}
