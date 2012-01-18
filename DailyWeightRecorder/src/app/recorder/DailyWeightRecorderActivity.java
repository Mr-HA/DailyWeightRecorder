package app.recorder;

import android.app.Activity;
import android.os.Bundle;

public class DailyWeightRecorderActivity extends Activity {
    /** Called when the activity is first created. */
	public interface UserSchema {
		String TABLE_NAME = "Users"; 			// Table Name
		String ID = "_id";						// ID
		String USER_NAME = "user_name";			// User Name
	}
	public interface WeightSchema {
		String TABLE_NAME = "Weight";		// Table Name
		String WEIGHT_ID = "_wid";			// Weight ID
		String UID = "_uid";				// User ID
		String DATE = "date";				// Date
		String WEIGHT = "weight";			// Weight 
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}