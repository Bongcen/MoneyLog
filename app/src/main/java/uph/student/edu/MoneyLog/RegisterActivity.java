package uph.student.edu.MoneyLog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity  {

    DatabaseHelper db;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private TextView goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView2 = (EditText) findViewById(R.id.password1);

        goLogin = (TextView) findViewById(R.id.loginText);

        goLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.regisBtn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkRegisterInput()) {

                    if(db.userRegister(mEmailView.getText().toString(),  mPasswordView.getText().toString())) {

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(),"Email has been registered. Please use a new email.", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }

    private boolean checkRegisterInput() {

        String emailWarn    = "Please Insert a Valid Email";
        String passWarn     = "Please Insert a Password";

        if(mEmailView.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),emailWarn, Toast.LENGTH_LONG).show();
            mEmailView.requestFocus();
            return false;
        }

        if(mEmailView.getText().toString().indexOf("@") == -1 || mEmailView.getText().toString().indexOf(".") == -1) {
            Toast.makeText(getApplicationContext(),emailWarn, Toast.LENGTH_LONG).show();
            mEmailView.requestFocus();
            return false;
        }

        if(mPasswordView.getText().toString().matches("") ) {
            Toast.makeText(getApplicationContext(),passWarn, Toast.LENGTH_LONG).show();
            mPasswordView.requestFocus();
            return false;
        }

        if(mPasswordView2.getText().toString().matches("") ) {
            Toast.makeText(getApplicationContext(),"Please Re-enter Password", Toast.LENGTH_LONG).show();
            mPasswordView2.requestFocus();
            return false;
        }

        if(!mPasswordView.getText().toString().matches(mPasswordView2.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Passwords do not match.", Toast.LENGTH_LONG).show();
            mPasswordView2.requestFocus();
            return false;
        }


        return true;
    }
}

