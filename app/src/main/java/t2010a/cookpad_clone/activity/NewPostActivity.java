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
import t2010a.cookpad_clone.adapter.NewPostIngredientAdapter;
import t2010a.cookpad_clone.adapter.NewPostStepAdapter;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Category;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.Ingredient;
import t2010a.cookpad_clone.model.client_model.Making;
import t2010a.cookpad_clone.model.client_model.Origin;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.repository.Repository;


public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {
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
    private NewPostIngredientAdapter ingredientAdapter;
    private List<Making> makingList = new ArrayList<>();
    private NewPostStepAdapter stepAdapter;
    private User user;

    private Faker faker = new Faker();
    private Repository repository = Repository.getInstance();

    private static final String TAG = "Upload ###";
    private static int IMAGE_REQ = 1;
    private Uri imagePath;
    Map config = new HashMap();
    private String[] countries;
    private String[] categories;
    private String strCate = "Khác";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
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
        etDescription = findViewById(R.id.etDescription);
        etCategory = findViewById(R.id.etAutoComplete_1);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        imageView = findViewById(R.id.imageView);
        layoutUploadImg = findViewById(R.id.layoutUploadImg);
        layoutGone = findViewById(R.id.layoutGone);

        countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        etOrigin.setAdapter(arrayAdapter);

        user = LocalDataManager.getUserDetail();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        ingredientList = populateGradientsList();
        makingList = populateStepList();

        ingredientAdapter = new NewPostIngredientAdapter(this, ingredientList);
        stepAdapter = new NewPostStepAdapter(this, makingList);

        rvNewRecipeGradient.setLayoutManager(layoutManager);
        rvNewRecipeGradient.setAdapter(ingredientAdapter);

        rvNewRecipeStep.setLayoutManager(layoutManager1);
        rvNewRecipeStep.setAdapter(stepAdapter);

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

    private void setSavePost() {
        Content content = new Content();

        String name = etName.getText().toString().toLowerCase(Locale.ROOT).trim();
        String strOrigin = etOrigin.getText().toString().toLowerCase(Locale.ROOT).trim();
        String eaterNumber = etEaterNumber.getText().toString().toLowerCase(Locale.ROOT).trim();
        String cookingTime = etCookingTime.getText().toString().toLowerCase(Locale.ROOT).trim();
        String description = etDescription.getText().toString().toLowerCase(Locale.ROOT).trim();

        if (name.isEmpty() || strOrigin.isEmpty() || eaterNumber.isEmpty() || cookingTime.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
        } else {
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
                        content.setStatus("INACTIVE");

                        repository.getService().createPost(content).enqueue(new Callback<Content>() {
                            @Override
                            public void onResponse(Call<Content> call, Response<Content> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(NewPostActivity.this, "Lưu thành công", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(NewPostActivity.this, "Lưu thất bại", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Content> call, Throwable t) {

                            }
                        });
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
            } else {
                Toast.makeText(this, "Thumbnail is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUploadPost() {
        Content content = new Content();
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
                    content.setStatus("PENDING");

                    repository.getService().createPost(content).enqueue(new Callback<Content>() {
                        @Override
                        public void onResponse(Call<Content> call, Response<Content> response) {
                            if (response.code() == 200) {
                                Toast.makeText(NewPostActivity.this, "Success to upload new content", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(NewPostActivity.this, "Fail to upload new content", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Content> call, Throwable t) {
                            Toast.makeText(NewPostActivity.this, "Fail to upload new content", Toast.LENGTH_LONG).show();
                        }
                    });
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
        }
    }


    private List<Ingredient> populateGradientsList() {
        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(String.valueOf(i));
            list.add(ingredient);
        }
        return list;
    }

    private List<Making> populateStepList() {
        List<Making> list = new ArrayList<>();
        for (int i = 0; i < ingredientList.size(); i++) {
            Making making = new Making();
            making.setName(String.valueOf(i));
            list.add(making);
        }
        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miUpload:
                setUploadPost();
                break;
            case R.id.miSave:
                setSavePost();
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
