package com.semicolon.criuse.Fragments;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.GroceryAdapter;
import com.semicolon.criuse.Models.AllGroceries_SubCategory;
import com.semicolon.criuse.Models.GroceryPart1;
import com.semicolon.criuse.Models.GroupModel;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SharedPreferences.Preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Categories extends Fragment{
    private static String TAG="TAG";
    private HomeActivity homeActivity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<GroupModel> groupModelList;
    private GroceryPart1 groceryPart1;
    private ImageView img_back;
    private ProgressBar progBar;
    private List<String> subCategory;
    private Button reg_btn;
    private Preferences preferences;
    private String session="";
    private File image_file;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_categories,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Categories getInstance(GroceryPart1 groceryPart1)
    {
        Fragment_Categories fragment_setting = new Fragment_Categories();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,groceryPart1);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {
        preferences = Preferences.getInstance();
        session = preferences.getSession(getActivity());
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            groceryPart1 = (GroceryPart1) bundle.getSerializable(TAG);
        }
        subCategory = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        img_back = view.findViewById(R.id.img_back);
        reg_btn = view.findViewById(R.id.reg_btn);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.white), PorterDuff.Mode.SRC_IN);
        groupModelList = new ArrayList<>();
        recView = view.findViewById(R.id.recView);
        manager =new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.BackFromGroceryRegister2ToRegister1(groceryPart1);
            }
        });

        getData();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register() {

        image_file = Common.getFileFromUrl(getActivity(), Uri.parse(groceryPart1.getUri()));
        AlertDialog progressDialog = Common.CreateProgressDialog(getActivity(), getString(R.string.reging));
        RequestBody imagePart = RequestBody.create(MediaType.parse("image/*"),image_file);
        RequestBody userNamePart = getRequestBody(groceryPart1.getUsername());
        RequestBody userPassPart = getRequestBody(groceryPart1.getPassword());
        RequestBody userPhonePart = getRequestBody(groceryPart1.getPhone());
        RequestBody userFullNamePart = getRequestBody(groceryPart1.getName());
        RequestBody userTokenPart = getRequestBody("");
        RequestBody userLatPart = getRequestBody(String.valueOf(groceryPart1.getLat()));
        RequestBody userLngPart = getRequestBody(String.valueOf(groceryPart1.getLng()));
        RequestBody userEmailPart = getRequestBody(String.valueOf(""));
        RequestBody userHourWorkPart = getRequestBody(groceryPart1.getHour());

        MultipartBody.Part part =MultipartBody.Part.createFormData("user_photo",image_file.getName(),imagePart);
        List<RequestBody> requestBodyList = new ArrayList<>();
        for (String pro_id:subCategory)
        {
            requestBodyList.add(getRequestBody(pro_id));
        }
        progressDialog.show();

        Api.getServices()
                .groceryRegister(userNamePart,userPassPart,userPhonePart,userFullNamePart,userTokenPart,userLatPart,userLngPart,userEmailPart,userHourWorkPart,requestBodyList,part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            subCategory.clear();

                            progressDialog.dismiss();
                            if (response.body().getSuccess()==1)
                            {

                                Log.e("Session",session+"s");

                                if (session!=null&&!TextUtils.isEmpty(session))
                                {
                                    if (session.equals(Tags.session_logout))
                                    {
                                        homeActivity.UpdateData(response.body());
                                        homeActivity.UpdateSettingFragmentUi(response.body());

                                        Log.e("data2",response.body().getUser_id());
                                        Log.e("data2",response.body().getUser_full_name());
                                        Log.e("data2",response.body().getUser_phone());
                                        Log.e("data2_photo",response.body().getUser_photo());

                                        Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();
                                    }else if (session.equals(Tags.session_login))
                                    {
                                        homeActivity.CloseBottomsheet();

                                        Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                    }
                                }else if (session!=null&&TextUtils.isEmpty(session))
                                {
                                    homeActivity.UpdateData(response.body());
                                    homeActivity.UpdateSettingFragmentUi(response.body());
                                    Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                }



                            }else if (response.body().getSuccess()==0)
                            {
                                Toast.makeText(homeActivity, R.string.reg_failed, Toast.LENGTH_SHORT).show();

                            }else if (response.body().getSuccess()==2)
                            {
                                Toast.makeText(homeActivity, R.string.user_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(homeActivity,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getData()
    {

        Api.getServices()
                .getAllGrocery_subcategories()
                .enqueue(new Callback<List<AllGroceries_SubCategory>>() {
                    @Override
                    public void onResponse(Call<List<AllGroceries_SubCategory>> call, Response<List<AllGroceries_SubCategory>> response) {
                        /*for (AllGroceries_SubCategory allGroceries_subCategory:response.body())
                        {
                            images_group.add(allGroceries_subCategory.getImage_categories());
                        }*/

                        progBar.setVisibility(View.GONE);
                        UpdateAdapterList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<AllGroceries_SubCategory>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);

                        Toast.makeText(homeActivity, getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                    }
                });
        /*adapter = new GroceryAdapter(groupModelList,getActivity());
        recView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();*/
    }
    private void UpdateAdapterList(List<AllGroceries_SubCategory> allGroceries_subCategoryList) {
        if (allGroceries_subCategoryList.size()>0)
        {
            for (AllGroceries_SubCategory subCategory :allGroceries_subCategoryList)
            {
                GroupModel groupModel = new GroupModel(subCategory.getName_categories(),subCategory.getSubProducts(),subCategory.getImage_categories());
                groupModelList.add(groupModel);
            }

            adapter = new GroceryAdapter(groupModelList,getActivity(),this);
            recView.setAdapter(adapter);

        }

    }
    public void add_remove_subcategory(View view ,String id)
    {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked())
        {
            subCategory.add(id);
            if (subCategory.size()>0)
            {
                reg_btn.setVisibility(View.VISIBLE);
            }
        }else
            {
                subCategory.remove(id);
                if (subCategory.size()==0)
                {
                    reg_btn.setVisibility(View.INVISIBLE);
                }
            }
    }

    private RequestBody getRequestBody(String part)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"),part);
        return requestBody;
    }
}
