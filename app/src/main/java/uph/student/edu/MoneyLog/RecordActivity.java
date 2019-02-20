package uph.student.edu.MoneyLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static uph.student.edu.MoneyLog.MainActivity.convertToRP;

public class RecordActivity extends AppCompatActivity {

    DatabaseHelper db;

    private ImageButton goToAddRecord;
    private ImageButton finish;

    //ryan
    ListView list;
    ArrayList<Records> listOfRecords;
    private String username;


    int[] images = {R.drawable.bill, R.drawable.cart, R.drawable.health, R.drawable.investment, R.drawable.paycheck};

    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        db = new DatabaseHelper(this);

        final Intent intent = getIntent();
        username = intent.getStringExtra("USER");

        listOfRecords = db.readRecordData(username);

        goToAddRecord = (ImageButton) findViewById(R.id.addRecordBtn);
        goToAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, AddRecord.class);
                intent.putExtra("USER",username);
                startActivity(intent);
            }
        });

        finish = (ImageButton) findViewById(R.id.returnBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        callCards();



    }

    private void callCards() {

        listOfRecords = db.readRecordData(username);

        String[]    arrayCategory   = new String[listOfRecords.size()];
        String[]    arrayDate       = new String[listOfRecords.size()];
        int[]       arrayInt        = new int[listOfRecords.size()];
        boolean[]   arrayBool       = new boolean[listOfRecords.size()];

        Records temp;

        for(int i=0;i<listOfRecords.size();i++) {
            arrayCategory[i]    = listOfRecords.get(i).category;
            arrayDate[i]        = listOfRecords.get(i).date;
            arrayInt[i]         = listOfRecords.get(i).amount;
            arrayBool[i]        = listOfRecords.get(i).positive;
        }

        //ryan
        list = (ListView) findViewById(R.id.listView);
        myAdapter adapter = new myAdapter(this, arrayCategory, arrayDate, arrayInt, arrayBool, images);
        list.setAdapter(adapter);
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        callCards();
    }
}

class myAdapter extends ArrayAdapter<String> {

    Context context;
    String[] arrayCategory;
    String[] arrayDate;
    int[] arrayInt;
    boolean[] arrayPos;
    int[] images;

    myAdapter(Context c, String[] arrCat, String[]arrDate, int arrInt[], boolean arrBool[], int imgs[]){
        super(c, R.layout.single_row, R.id.textView9, arrCat);
        this.context = c;
        this.arrayCategory = arrCat;
        this.arrayDate = arrDate;
        this.arrayInt = arrInt;
        this.arrayPos = arrBool;
        this.images = imgs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row, parent, false);

        System.out.println("BISA GET VIEW");

        ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
        TextView myCategories = (TextView) row.findViewById(R.id.textView9);
        TextView myAmount = (TextView) row.findViewById(R.id.textView11);
        TextView myDate = (TextView) row.findViewById(R.id.textView12);

        System.out.println(arrayCategory[position] + "INI LOH");
        if(arrayCategory[position].equals("Groceries")){
            myCategories.setText("Groceries");
            myImage.setImageResource(images[1]);

        }

        else if(arrayCategory[position].equals("Paycheck")){
            myCategories.setText("Paycheck");
            myImage.setImageResource(images[4]);

        }

        else if(arrayCategory[position].equals("Bill")){
            myCategories.setText("Bill");
            myImage.setImageResource(images[0]);

        }

        else if(arrayCategory[position].equals("Health")){
            myCategories.setText("Health");
            myImage.setImageResource(images[2]);

        }

        else if(arrayCategory[position].equals("Investment")){
            myCategories.setText("Investment");
            myImage.setImageResource(images[3]);

        } else {
            System.out.println("NPTOHNGGSAS");
        }




        myAmount.setText(convertToRP(arrayInt[position]));

        if(arrayPos[position]) {
            myAmount.setTextColor(Color.parseColor("#33cc00"));
        } else {
            myAmount.setTextColor(Color.parseColor("#ed2939"));
        }



        myDate.setText(arrayDate[position]);




        return row;
    }
}
