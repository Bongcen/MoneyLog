package uph.student.edu.MoneyLog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddRecord extends AppCompatActivity {

    DatabaseHelper db;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private Button mDisplayCategory;

    private EditText amount;
    private ImageButton cancel;
    private ImageButton accept;
    private Button minus;
    private Button plus;
    private EditText desc;
    private ConstraintLayout cons;

    private boolean datePicked;
    private boolean catePicked;

    private static final String TAG = "AddRecord";

    private String      username;
    private boolean     input_positive = true;
    private int         input_amount;
    private String      input_category;
    private String      input_date;
    private String      input_desc;

    Records newRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecords);

        db = new DatabaseHelper(this);

        final Intent intent = getIntent();
        username = intent.getStringExtra("USER");

//        newRecord = new Records();

        cons                = (ConstraintLayout) findViewById(R.id.bgColor);
        mDisplayCategory    = (Button)      findViewById(R.id.cat);
        mDisplayDate        = (TextView)    findViewById(R.id.datePicker);
        amount              = (EditText)    findViewById(R.id.editText);
        cancel              = (ImageButton) findViewById(R.id.imageCancel);
        accept              = (ImageButton) findViewById(R.id.imageAccept);
        minus               = (Button)      findViewById(R.id.button);
        plus                = (Button)      findViewById(R.id.button2);
        desc                = (EditText)    findViewById(R.id.editText2);



        /************
         * ACCEPT
         ***********/
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
                if(checkInput()) {

                    input_amount = Integer.parseInt(amount.getText().toString());
                    input_desc = desc.getText().toString();

                    newRecord = new Records(username,input_category,input_positive,input_amount,input_desc,input_date);

                    if(db.writeRecordData(newRecord)) {
                        Toast.makeText(getApplicationContext(),"Record saved!", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),username+"\n Balance: "+ db.getUserBalance(username), Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Error connecting to Database", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });



        /************
         * CANCEL
         ***********/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /************
         * POSITIVE
         ***********/
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_positive = true;
                plus.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.btnIncome, null));
                cons.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.themeGreen, null));
                minus.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.btnDisabled, null));
            }
        });

        /************
         * NEGATIVE
         ***********/
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_positive = false;
                minus.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.btnExpense, null));
                cons.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.minusBG, null));
                plus.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.btnDisabled, null));
            }
        });

        /************
         * CATEGORY
         ***********/
        mDisplayCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddRecord.this, mDisplayCategory);
                popup.getMenuInflater().inflate(R.menu.popup_category, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mDisplayCategory.setText(item.getTitle());
                        input_category = item.getTitle()+"";
                        catePicked = true;
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


        /************
         * DATE
         ***********/
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddRecord.this,
                        android.R.style.Theme_Material_Dialog_NoActionBar,
                        mDataSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDataSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
                input_date = date;
                datePicked = true;

            }
        };
    }

    public boolean checkInput() {
        if(isEmpty(amount)) {
            Toast.makeText(getApplicationContext(),"Please Insert Amount", Toast.LENGTH_LONG).show();
            return false;
        } else if(!catePicked) {
            Toast.makeText(getApplicationContext(),"Please Pick a Category", Toast.LENGTH_LONG).show();
            return false;
        } else if(!datePicked) {
            Toast.makeText(getApplicationContext(),"Please Pick a Date", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
