package com.example.summar;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText UsernemailEt,PasswordEt;
    TextView openReg;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsernemailEt = findViewById(R.id.etuseremail);
        PasswordEt = findViewById(R.id.etpassword);
        btnlogin = findViewById(R.id.btnlogin);
        openReg = findViewById(R.id.openReg);
    }

    public void OnLogin(View view) {
        String useremail = UsernemailEt.getText().toString(); //這是email
        String password = PasswordEt.getText().toString().trim();

        //email驗證
        String validemail="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(validemail).matcher(useremail);
        if(!matcher.matches()){
            UsernemailEt.setError("請輸入有效的電子郵件");
            if(password.isEmpty()||password.equals("")||password.length()<6){
                PasswordEt.setError("請輸入密碼且至少六碼");
            }
        }
        else {
            //送資料
        }
    }
    public void openReg(View view){
        startActivity(new Intent(this, Register.class));
    }

}
