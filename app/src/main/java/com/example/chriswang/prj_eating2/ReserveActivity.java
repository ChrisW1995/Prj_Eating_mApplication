package com.example.chriswang.prj_eating2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReserveActivity extends AppCompatActivity {
    private Button btnReserve;
    private Spinner spinnerNum;
    private CheckBox chk_ReserveSmoke;
    private EditText edtChooseTime, edt_reserve_name, edt_reserve_phone;
    private TextView tv_reserve_r_name;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String r_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        btnReserve = findViewById(R.id.btnReserve);
        spinnerNum = findViewById(R.id.spin_reserve_peoplenum);
        chk_ReserveSmoke = findViewById(R.id.chk_ReserveSmoke);
        edt_reserve_name = findViewById(R.id.edt_reserve_name);
        edt_reserve_phone = findViewById(R.id.edt_reserve_phone);
        edtChooseTime = findViewById(R.id.edtChooseDate);
        tv_reserve_r_name = findViewById(R.id.tv_reserve_r_name);
        r_name = getIntent().getStringExtra("r_name");
        tv_reserve_r_name.setText(r_name);
        initSpinner();

        edtChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ReserveActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = year+"/"+month+"/"+dayOfMonth;
                edtChooseTime.setText(date);
            }
        };

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
                    String strToday = sdf.format(Calendar.getInstance().getTime());
                    Date current_date = sdf.parse(strToday);
                    Date choose_date = sdf.parse(edtChooseTime.getText().toString());
                    if(choose_date.before(current_date)){
                        Toast.makeText(ReserveActivity.this, "請確認日期為明天以後！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String peopleNum = spinnerNum.getSelectedItem().toString();
                String r_id = getIntent().getStringExtra("r_id");
                String str_smoke;

                boolean isSmoke = chk_ReserveSmoke.isChecked();
                if(isSmoke == false){
                    str_smoke = "否";
                }else{
                    str_smoke="是";
                }
                //to-do list -----
                Intent i = new Intent(ReserveActivity.this, ReservationDetailActivity.class);
                i.putExtra("date", edtChooseTime.getText().toString());
                i.putExtra("peopleNum", peopleNum);
                i.putExtra("r_id",r_id);
                i.putExtra("smoke", str_smoke);
                i.putExtra("phone", edt_reserve_phone.getText().toString());
                i.putExtra("name", edt_reserve_name.getText().toString());
                i.putExtra("r_name", r_name);
                ReserveActivity.this.startActivityForResult(i, 100);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_CANCELED){
                finish();
            }
        }
    }

    private void initSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 1; i<=12; i++){
            arrayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinnerNum.setAdapter(adapter);

    }
}
