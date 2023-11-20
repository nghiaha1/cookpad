package t2010a.cookpad_clone.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.bloco.faker.Faker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.adapter.EditPostIngredientAdapter;
import t2010a.cookpad_clone.adapter.EditPostStepAdapter;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Category;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.Ingredient;
import t2010a.cookpad_clone.model.client_model.Making;
import t2010a.cookpad_clone.model.client_model.Origin;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.repository.Repository;


public class EditActivePostActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout addGradient, addStep, layoutGone;
    private RelativeLayout layoutUploadImg;
    private RecyclerView rvNewRecipeGradient, rvNewRecipeStep;
    private TextInputEditText etName, etEaterNumber, etCookingTime, etDescription;
    private AutoCompleteTextView etOrigin;
    private Spinner etCategory;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private ImageView imageView;

    private List<Ingredient> ingredientList = new ArrayList<>();
    private EditPostIngredientAdapter ingredientAdapter;
    private List<Making> makingList = new ArrayList<>();
    private EditPostStepAdapter stepAdapter;
    private User user;

    private Faker faker = new Faker();
    private Repository repository = Repository.getInstance();

    private static final String TAG = "Upload ###";
    private static int IMAGE_REQ = 1;
    private Uri imagePath;
    Map config = new HashMap();
    private String[] countries;
    private Content content;
    private long id;
    private String[] categories;
    private String strCate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_active_post);
        initView();
        setSpinner();
        addGradient.setOnClickListener(this);
        addStep.setOnClickListener(this);
        layoutUploadImg.setOnClickListener(this);
        setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initView() {
        addGradient = findViewById(R.id.addGradient);
        addStep = findViewById(R.id.addStep);
        rvNewRecipeGradient = findViewById(R.id.rvNewRecipeGradient);
        rvNewRecipeStep = findViewById(R.id.rvNewRecipeStep);
        etName = findViewById(R.id.etName);
        etOrigin = findViewById(R.id.etAutoComplete);
        etEaterNumber = findViewById(R.id.etEaterNumber);
        etCookingTime = findViewById(R.id.etCookingTime);
        etCategory = findViewById(R.id.etAutoComplete_1);
        etDescription = findViewById(R.id.etDescription);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        imageView = findViewById(R.id.imageView);
        layoutUploadImg = findViewById(R.id.layoutUploadImg);
        layoutGone = findViewById(R.id.layoutGone);

        content = (Content) getIntent().getSerializableExtra("EDIT ACTIVE POST");
        id = content.getId();
        strCate = content.getCategory().getName();
        if (content.getThumbnails() != null) layoutGone.setVisibility(View.GONE);

        etName.setText(content.getName());
        etCookingTime.setText(content.getCookingTime().toString());
        etOrigin.setText(content.getOrigin().getCode());
        etEaterNumber.setText(content.getEatNumber().toString());
        etDescription.setText(content.getDetail());
        Glide.with(this).load(content.getThumbnails()).into(imageView);

        countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        etOrigin.setAdapter(arrayAdapter);

        user = LocalDataManager.getUserDetail();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        ingredientList = content.getIngredient();
        makingList = content.getMaking();

        ingredientAdapter = new EditPostIngredientAdapter(this, ingredientList);
        stepAdapter = new EditPostStepAdapter(this, makingList);

        rvNewRecipeGradient.setLayoutManager(layoutManager);
        rvNewRecipeGradient.setAdapter(ingredientAdapter);

        rvNewRecipeStep.setLayoutManager(layoutManager1);
        rvNewRecipeStep.setAdapter(stepAdapter);
    }

    private void setSpinner() {
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCategory.setAdapter(arrayAdapter1);
        etCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCate = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAddGradient(Ingredient postGradient) {
        ingredientList.add(postGradient);
        ingredientAdapter.notifyItemInserted(ingredientList.size() + 1);
        for (int i = 0; i < ingredientList.size(); i++) {
            Log.d("TAG", "Id " + i + " " + ingredientList.get(i).getId());
        }
    }

    private void setAddStep(Making making) {
        makingList.add(making);
        stepAdapter.notifyItemInserted(makingList.size() + 1);
        for (int i = 0; i < makingList.size(); i++) {
            Log.d("TAG", "Id " + i + " " + makingList.get(i).getId());
        }
    }

    private void setUploadPost() {
        String name = etName.getText().toString().toLowerCase(Locale.ROOT).trim();
        String strOrigin = etOrigin.getText().toString().toLowerCase(Locale.ROOT).trim();
        String eaterNumber = etEaterNumber.getText().toString().toLowerCase(Locale.ROOT).trim();
        String cookingTime = etCookingTime.getText().toString().toLowerCase(Locale.ROOT).trim();
        String description = etDescription.getText().toString().toLowerCase(Locale.ROOT).trim();

        if (imagePath != null) {
            MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String strImage = (String) resultData.get("url");
                    Log.d(TAG, "onStart: " + "usuccess");

                    uploadPost(name, strCate, strOrigin, ingredientList,
                            makingList, cookingTime, user, eaterNumber,
                            description, strImage, "ACTIVE");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }
            }).dispatch();
        } else Toast.makeText(this, "Chưa có thumbnail", Toast.LENGTH_SHORT).show();
    }

    private void uploadPost(String name, String strCate, String strOrigin, List<Ingredient> ingredientList,
                            List<Making> makingList, String cookingTime, User user, String eaterNumber,
                            String description, String strImage, String status) {
        content.setId(null);
        content.setName(name);
        content.setCategory(new Category(strCate));
        content.setOrigin(new Origin(strOrigin));
        content.setIngredient(ingredientList);
        content.setMaking(makingList);
        content.setCookingTime(Integer.valueOf(cookingTime));
        content.setUser(user);
        content.setEatNumber(Integer.valueOf(eaterNumber));
        content.setDetail(description);
        content.setThumbnails(strImage);
        content.setStatus(status);
        content.setLikes(content.getLikes());

        repository.getService().createPost(content).enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                if (response.code() == 200) {
                    Toast.makeText(EditActivePostActivity.this, "Sửa bài thành công", Toast.LENGTH_SHORT).show();
                    deletePost(id);
                    finish();
                } else {
                    Toast.makeText(EditActivePostActivity.this, "Sửa bài thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
                Toast.makeText(EditActivePostActivity.this, "Sửa bài thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deletePost(long postId) {
        repository.getService().deletePost(postId, "Bearer " + LocalDataManager.getAccessToken()).enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {

            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_active, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miUpload:
                setUploadPost();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addGradient:
                setAddGradient(new Ingredient());
                ingredientAdapter.reloadData(ingredientList);
                break;
            case R.id.addStep:
                setAddStep(new Making());
                stepAdapter.reloadData(makingList);
                break;
            case R.id.layoutUploadImg:
                requestPermission();
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMAGE_REQ);
        }
    }

    /*
     * sele the image from the gallery
     * */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath = data.getData();
                        Picasso.get().load(imagePath).into(imageView);
                        layoutGone.setVisibility(View.GONE);
                    }
                }
            });
}
