package uph.student.edu.MoneyLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {

    DatabaseHelper db;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView goRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        goRegis = (TextView) findViewById(R.id.regisText);

        goRegis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.signInBtn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLoginInput()) {
                    if(db.userAuth(mEmailView.getText().toString(), mPasswordView.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("USER",mEmailView.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed to Login, Wrong Username or Password.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean checkLoginInput() {

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
            mEmailView.requestFocus();
            return false;
        }


        return true;
    }


}

