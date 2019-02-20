package uph.student.edu.MoneyLog;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;

    private TextView balanceText;
    private TextView mTextMessage;
    private ImageButton goAddRecord;
    private ImageButton goLogOut;
    private String username;
    private BottomNavigationView botnav;

    private Button recs;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {



        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_icon1:
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("USER",username);
                    startActivity(intent);return true;
                case R.id.navigation_icon2:
                    Toast.makeText(getApplicationContext(), "Hehehe", Toast.LENGTH_LONG).show();
                return true;
                case R.id.navigation_icon3:
                    Toast.makeText(getApplicationContext(), "Hehehe", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_icon4:
                    Intent intent1 = new Intent(getApplicationContext(), RecordActivity.class);
                    intent1.putExtra("USER",username);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_icon5:
                    Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    Toast.makeText(getApplicationContext(), "Error has occurred", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);


        final Intent intent = getIntent();
        username = intent.getStringExtra("USER");

        botnav = (BottomNavigationView) findViewById(R.id.navigation);
        botnav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        balanceText = (TextView) findViewById(R.id.textView9);
        goAddRecord = (ImageButton) findViewById(R.id.addRecordBtn);

        goLogOut    = (ImageButton) findViewById(R.id.imageButton3);
        goLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                username = "";
                startActivity(intent);
                finish();
            }
        });


        recs = (Button) findViewById(R.id.button3);
        recs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("USER",username);
                startActivity(intent);
            }
        });

        goAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecord.class);
                intent.putExtra("USER",username);
                startActivity(intent);
            }
        });

        balanceText.setText(convertToRP(db.getUserBalance(username)));

//        balanceText.setText(convertToRP(number));



//        Toast.makeText(getApplicationContext(),username+"\n Balance: " + db.getUserBalance(username), Toast.LENGTH_LONG).show();




    }

    @Override
    protected void onResume() {
        super.onResume();
        balanceText.setText(convertToRP(db.getUserBalance(username)));
    }


    public static String convertToRP(int money) {

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);

        String bal = format.format(money);

        bal = bal.substring(1,bal.length());
        bal = "Rp " + bal;

        return bal;
    }

}

