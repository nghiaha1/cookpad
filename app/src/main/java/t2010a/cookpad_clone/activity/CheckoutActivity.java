package t2010a.cookpad_clone.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.adapter.CartDetailAdapter;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.User;

public class CheckoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private TextInputEditText etFullName, etAddress, etProfilePhone;
    private TextView checkoutTotalPrice;
    private CartDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initView();
        initData();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        checkoutTotalPrice = findViewById(R.id.checkoutTotalPrice);

        setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initData() {
        User user = LocalDataManager.getUserDetail();
        etFullName.setText(user.getFullName());
        etAddress.setText(user.getAddress());
        etProfilePhone.setText(user.getPhone());
        CartDetailActivity activity = new CartDetailActivity();
        checkoutTotalPrice.setText((CharSequence) activity.totalPrice);
    }
}