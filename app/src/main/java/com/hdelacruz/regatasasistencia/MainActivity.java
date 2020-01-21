package com.hdelacruz.regatasasistencia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button registerEButton;
    private Button registerSButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerSButton = findViewById(R.id.salida_Butt);
        registerSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSalida();
            }
        });

        registerEButton = findViewById(R.id.ingreso_Butt);
        registerEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
    }





    private static final int REQUEST_REGISTER_FORM = 100;

    private void goSalida(){
        Intent intent = new Intent(this, RegisterSaliActivity.class);
        startActivity(intent);
    }

    private void goRegister(){
        Intent intent = new Intent(this, RegisterEntraActivity.class);
        startActivity(intent);
    }

    public void callRegisterE(View view){
        startActivityForResult(new Intent(this, RegisterEntraActivity.class), REQUEST_REGISTER_FORM);
    }

    public void callRegisterS(View view){
        startActivityForResult(new Intent(this, RegisterSaliActivity.class), REQUEST_REGISTER_FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_REGISTER_FORM) {
            return;
        }
    }



    }


