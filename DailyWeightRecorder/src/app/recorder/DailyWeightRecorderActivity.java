package app.recorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import app.recorder.DBConnection;
import app.recorder.R;
import app.recorder.DailyWeightRecorderActivity.WeightSchema;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
	protected static final int ID_TIMEPICKER = 1;
	private static final int ID_DATEPICKER = 0;
	protected static final int GRIDVIEW_COLUMN = 5;
	protected static final int MENU_ITEM_LIST = Menu.FIRST;
	protected static final int MENU_ITEM_RECORD = Menu.FIRST+1;
	protected static final int MENU_ITEM_ABOUT = Menu.FIRST+2;
	private int intYear, intMonth, intDay;
	private String strDate,strWeight;
	private TextView dateText, textWeight;
	Calendar calCurrent, calNext;
	DBConnection helper;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calCurrent = Calendar.getInstance();
    	calNext = Calendar.getInstance();
    	calNext.add(Calendar.MONTH, 1);
    	intYear = calCurrent.get(Calendar.YEAR);
   	    intMonth = calCurrent.get(Calendar.MONTH);
   	    intDay = calCurrent.get(Calendar.DAY_OF_MONTH);
        jumpToRecord();
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,MENU_ITEM_LIST,0,R.string.strList);
		menu.add(0,MENU_ITEM_RECORD,0,R.string.strRecord);
		menu.add(0,MENU_ITEM_ABOUT,0,R.string.strAbout);
		return super.onCreateOptionsMenu(menu);
		
	}
    
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case MENU_ITEM_ABOUT:
				openOptionsDialog();
				break;
			case MENU_ITEM_LIST:
				// jumpToList();
				jumptoQuery();
				break;
			case MENU_ITEM_RECORD:
				jumpToRecord();
				break;
		}
		return true;
		
	}
    private void openOptionsDialog() {
		// TODO Auto-generated method stub
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.app_name)
    	.setMessage(R.string.app_about_msg)
    	.setPositiveButton(R.string.btnOk, 
    			new DialogInterface.OnClickListener()
    	{
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
    	}
    
	)
	.show();
   }
    public void jumptoQuery() {
    	setContentView(R.layout.grid);
    	calCurrent = Calendar.getInstance();
    	calNext = Calendar.getInstance();
    	calNext.add(Calendar.MONTH, 1);
    	Button btnNext = (Button) findViewById(R.id.btnNext);
    	Button btnLast = (Button) findViewById(R.id.btnLast);
    	btnNext.setOnClickListener(dateButtonOnClickListener);
    	btnLast.setOnClickListener(dateButtonOnClickListener);
    	updateGrid();
   	        	
    }
    public void jumpToRecord() {
    	setContentView(R.layout.main);
    	String strYear = String.format("%04d",intYear);
 	   	String strMonth = String.format("%02d", intMonth+1);
 	   	String strDay = String.format("%02d", intDay);
 	   	strDate = strYear +"/"+ strMonth + "/" + strDay;
 	   	dateText= (TextView) findViewById(R.id.strDate);
 	   	dateText.setText(strDate);   
 	   	textWeight = (TextView) findViewById(R.id.strWeight);
 	   	textWeight.setText(strWeight);
	    Button datePickerButton = (Button)findViewById(R.id.btnDateChange);
	    datePickerButton.setOnClickListener(datePickerButtonOnClickListener);
	    Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
	    btnSubmit.setOnClickListener(submitButtonOnClickListener);
	    Button btn1 = (Button)findViewById(R.id.btnOne);
	    Button btn2 = (Button)findViewById(R.id.btnTwo);
	    Button btn3 = (Button)findViewById(R.id.btnThree);
	    Button btn4 = (Button)findViewById(R.id.btnFour);
	    Button btn5 = (Button)findViewById(R.id.btnFive);
	    Button btn6 = (Button)findViewById(R.id.btnSix);
	    Button btn7 = (Button)findViewById(R.id.btnSeven);
	    Button btn8 = (Button)findViewById(R.id.btnEight);
	    Button btn9 = (Button)findViewById(R.id.btnNine);
	    Button btn0 = (Button)findViewById(R.id.btnZero);
	    Button btnDot = (Button)findViewById(R.id.btnDot);
	    Button btnDel = (Button)findViewById(R.id.btnDel);
	    Button btnClear = (Button)findViewById(R.id.btnClear);
	    btn1.setOnClickListener(weightButtonOnClickListener);
	    btn2.setOnClickListener(weightButtonOnClickListener);
	    btn3.setOnClickListener(weightButtonOnClickListener);
	    btn4.setOnClickListener(weightButtonOnClickListener);
	    btn5.setOnClickListener(weightButtonOnClickListener);
	    btn6.setOnClickListener(weightButtonOnClickListener);
	    btn7.setOnClickListener(weightButtonOnClickListener);
	    btn8.setOnClickListener(weightButtonOnClickListener);
	    btn9.setOnClickListener(weightButtonOnClickListener);
	    btn0.setOnClickListener(weightButtonOnClickListener);
	    btnDot.setOnClickListener(weightButtonOnClickListener);
	    btnDel.setOnClickListener(weightButtonOnClickListener);
	    btnClear.setOnClickListener(weightButtonOnClickListener);
	    helper = new DBConnection(this);
    }
    public void updateGrid() {
    	GridView gridViewWeight = (GridView) findViewById(R.id.gridViewWeight);
    	TextView textViewGridViewDate = (TextView) findViewById(R.id.textViewGridViewDate);
    	String strCurrentDate = generateYearMonth(calCurrent);
   	    String strNextDate = generateYearMonth(calNext);
   	    Log.v(this.toString(),strCurrentDate);
   	    Log.v(this.toString(),strNextDate);
    	textViewGridViewDate.setText(strCurrentDate);
    	SimpleAdapter adapter;
    	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    	gridViewWeight.setNumColumns(GRIDVIEW_COLUMN);
    	final SQLiteDatabase dbR = helper.getReadableDatabase();
    	String tmpDate, tmpWeight;
    	String strSelect ="( " + WeightSchema.DATE + " > '" + strCurrentDate + "' AND " + WeightSchema.DATE + " < '" + strNextDate + "' ) ";
    	Log.v(this.toString(),strSelect);
    	Cursor cursor = dbR.query(WeightSchema.TABLE_NAME, null, 
    			strSelect,
    			null, null, null, WeightSchema.DATE);
    	if (cursor.getCount() > 0) {
    		cursor.moveToLast();
    		while (!cursor.isBeforeFirst()) {
    			tmpDate = cursor.getString(2);
        		tmpWeight = cursor.getString(3);
        		HashMap<String,String> item = new HashMap<String,String>();
        		item.put("date", tmpDate);
        		item.put("weight", tmpWeight);
        		list.add(item);
        		cursor.moveToPrevious();	
    		}
    	}
    	cursor.close();
    	dbR.close();
    	adapter = new SimpleAdapter (
    			this,
    			list,
    			R.layout.item,
    			new String[] {"weight","date"},
    			new int[] {R.id.itemTextViewWeight,R.id.itemTextViewDate}
    			);
    	gridViewWeight.setAdapter(adapter);
	
    }
    public String generateYearMonth(Calendar c) {
    	String strTempDate;
    	int intTempYear = c.get(Calendar.YEAR);
   	    int intTempMonth = c.get(Calendar.MONTH);
   	    String strYear = String.format("%04d",intTempYear);
   	    String strMonth = String.format("%02d", intTempMonth+1);
   	    strTempDate = strYear +"/"+ strMonth;
		return strTempDate;
    	
    }
    private Button.OnClickListener dateButtonOnClickListener = new Button.OnClickListener() {

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btnLast:
				calCurrent.add(Calendar.MONTH, -1);
				calNext.add(Calendar.MONTH, -1);
				break;
			case R.id.btnNext:
				calCurrent.add(Calendar.MONTH, 1);
				calNext.add(Calendar.MONTH, 1);
				break;
			}
				
			updateGrid();
		}
    	
    };
	private Button.OnClickListener weightButtonOnClickListener = new Button.OnClickListener() {

		public void onClick(View v) {
			strWeight = (String) textWeight.getText();
			Button btn = (Button) v;
			int strLength;
			switch (v.getId()) {
			case R.id.btnDel:
				strLength = textWeight.length();
				if (strLength>0) {
					strWeight = strWeight.substring(0,strLength-1);
				}
				break;
				
			case R.id.btnDot:
				int idx = strWeight.indexOf(".");
				strLength = textWeight.length();
				if ((idx < 0) && (strLength>0)) {
					strWeight = strWeight + ".";
				}
				break;
			case R.id.btnClear:
				strWeight="";
				break;
			default:
					String btnText = btn.getText().toString();
					strWeight = strWeight + btnText;				
			}
			textWeight.setText(strWeight);
		}
    	
    };
    private Button.OnClickListener submitButtonOnClickListener = new Button.OnClickListener() {

		private String strDate2;
		

		public void onClick(View v) {
		    final SQLiteDatabase db = helper.getWritableDatabase();
		    strDate2 = (String) dateText.getText();
		    Log.v(this.toString(),"Date: "+strDate2);
		    Cursor cursor = db.query(WeightSchema.TABLE_NAME, null, WeightSchema.DATE + "='"+ strDate2 +"'", null, null, null, null);
		    if (cursor.getCount() >0) {
		    	Log.v(this.toString(),"Update Case");
		    	cursor.moveToFirst();
		    	String strDate3 = cursor.getString(2);
		    	String strWeight = cursor.getString(3);
		    	Log.v(this.toString(),"Befor Update Date: "+strDate3+" Weight: "+strWeight);
		    	ContentValues values = new ContentValues();
				values.put(WeightSchema.UID, 1);
				values.put(WeightSchema.DATE, strDate2);
				values.put(WeightSchema.WEIGHT,Float.parseFloat(textWeight.getText().toString()));
				String where = WeightSchema.DATE + " = '" + strDate2+"'";
				Log.v(this.toString(),"where ="+ where);
				db.update(WeightSchema.TABLE_NAME,values,where,null);
				cursor.close();
				db.close();
		    }
		    else {
		    	Log.v(this.toString(),"Insert Case");
		    	ContentValues values = new ContentValues();
				values.put(WeightSchema.UID, 1);
				values.put(WeightSchema.DATE, strDate2);
				values.put(WeightSchema.WEIGHT,Float.parseFloat(textWeight.getText().toString()));
				db.insert(WeightSchema.TABLE_NAME, null, values);
				cursor.close();
				db.close();
		    }
		}
    	
    };
    
    private Button.OnClickListener datePickerButtonOnClickListener = new Button.OnClickListener(){

		public void onClick(View v) {
			
			showDialog(ID_DATEPICKER);	
		}
    };	
    @Override
    protected Dialog onCreateDialog(int id) {
     
     switch(id){
      case ID_DATEPICKER:
    	 
       return new DatePickerDialog(this,
         myDateSetListener, intYear, intMonth, intDay);
       
      default:
       return null;
       
     }
    }
    private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){
			public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
				String strYear = String.format("%04d",year);
				String strMonth = String.format("%02d", monthOfYear+1);
				String strDay = String.format("%02d", dayOfMonth);
				strDate = strYear +"/"+ strMonth + "/" + strDay;
			 	dateText.setText(strDate);
			 	intYear = year;
			 	intMonth = monthOfYear;
			 	intDay = dayOfMonth;
		 	
				}

   };
}