package com.example.summar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.summar.service.RetrofitClient;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText username,password,email;
    TextView birth;
    RadioButton male,female;
    Calendar cal;
    int year,month,day;
    RadioGroup gender;
    CheckBox checkBox;

    //存變數的地方
    String str_username =null;
    String str_password;
    String str_email = null;
    String str_gender =null;
    String str_birth =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.et_username);
        password= findViewById(R.id.et_password);
        email=findViewById(R.id.et_email);
        male=findViewById(R.id.radioButton_male);
        female=findViewById(R.id.radioButton_female);
        gender = findViewById(R.id.radio_group);

        birth = findViewById(R.id.et_birth);
        birth.setOnClickListener(this);

        checkBox=findViewById(R.id.checkBox);

        getDate();
    }
    //今天日期
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }

    //註冊鈕
    public void Onreg(View view){
        str_username = username.getText().toString().trim();
        str_password = password.getText().toString().trim();
        str_email = email.getText().toString().trim();
        str_gender ="";
        str_birth =birth.getText().toString().trim();
        switch (gender.getCheckedRadioButtonId()){
                    case R.id.radioButton_male:
                        str_gender="male";
                        break;
                    case R.id.radioButton_female:
                        str_gender="female";
                        break;
                }

        //email驗證
        String validemail="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(validemail).matcher(str_email);
        if(str_username.isEmpty()||str_username.equals("")) {
            username.setError("用戶名不可為空");
        }
        else if(!matcher.matches()) {
            email.setError("請輸入有效的電子郵件");
        }
        else if(str_password.isEmpty()||str_password.equals("")||str_password.length()<6) {
            password.setError("請輸入密碼且至少六碼");
        }
        else if(!checkBox.isChecked()){
                        Toast.makeText(this,"請詳細閱讀並同意",Toast.LENGTH_SHORT).show();
                    }
        else{
            //User user = new User(str_email, str_password, str_username, str_gender, str_birth);
            Log.d("USER",str_email+" --- "+str_password);
            RetrofitClient.getInstance()
                    .getAPI()
                    .register(str_email, str_password, str_username, str_gender, str_birth)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                Log.d("SEND",response.body().string());
                                //String res = response.body().string();
                                JsonObject res = new JsonObject().get(response.body().string()).getAsJsonObject();
                                if(res.get("message").getAsString().contains("sucessfully")){//註冊成功
                                    Toast.makeText(Register.this,"sucessfully",Toast.LENGTH_LONG).show();
                                    Log.d("RESPONSE","sucessfully");
                                    startActivity(new Intent(Register.this,MainActivity.class));
                                }
                                else{
                                    Toast.makeText(Register.this,"aleardy existed",Toast.LENGTH_LONG).show();
                                    Log.d("RESPONSE","aleardy existed");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("ERROR",t.getMessage());
                        }
                    });
         }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.et_birth){
            DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker arg0, int y, int m, int d) {
                    year=y; month=m; day=d;
                    birth.setText(y+"-"+(++m)+"-"+d);

                    //将选择的日期显示到birth中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,listener,year,month,day);
            //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
            //設定最大日期
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.show();
        }
    }
}