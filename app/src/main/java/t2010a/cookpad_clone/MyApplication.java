package t2010a.cookpad_clone;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

import t2010a.cookpad_clone.local_data.LocalDataManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalDataManager.init(getApplicationContext());

        Map config = new HashMap();
        config.put("cloud_name", "dab9sgwgk");
        config.put("api_key", "167715828929422");
        config.put("api_secret", "Myvc7WBz-mToLmAJNBjyr9DS7as");
//        config.put("secure", true);
        MediaManager.init(getApplicationContext(), config);

    }
}
