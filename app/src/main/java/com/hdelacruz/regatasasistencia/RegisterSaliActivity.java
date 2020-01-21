package com.hdelacruz.regatasasistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.hdelacruz.regatasasistencia.models.Salida;
import com.hdelacruz.regatasasistencia.services.ApiServiceGeneratorS;
import com.hdelacruz.regatasasistencia.services.ApiServiceSalida;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterSaliActivity extends AppCompatActivity {

    private static String TAG = RegisterEntraActivity.class.getSimpleName();

    private ImageView imagenPreview;
    private TextView fechaTexts;
    private TextView horatexts;
    private EditText dniTexts;
    private Spinner spinner1;
    private TextView codigoTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sali);

        imagenPreview = findViewById(R.id.imagen_preview);
        fechaTexts = findViewById(R.id.fecha_texts);
        horatexts = findViewById(R.id.hora_texts);
        dniTexts = findViewById(R.id.DNI);
        codigoTexts = findViewById(R.id.codigo_texts);
        spinner1 = (Spinner) findViewById(R.id.spinner);


        String []opciones={"-- Selecione --","Salida-Normal","Comision","Permiso","Compensacion","Almuerzo"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_razones,opciones);
        spinner1.setAdapter(adapter);


        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.fecha_texts);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                }catch (InterruptedException e){

                }
            }
        };
        t.start();

        Thread h = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.hora_texts);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                }catch (InterruptedException e){

                }
            }
        };
        h.start();
    }
    private static final int REQUEST_CAMERA = 100;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }
    private Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = scaleBitmapDown(bitmap, 800);  // Redimensionar
                imagenPreview.setImageBitmap(bitmap);
            }
        }
    }

    public void callRegisterS(View view){

        String codigo_s = codigoTexts.getText().toString();
        String dni_s = dniTexts.getText().toString();
        String fecha_s = fechaTexts.getText().toString();
        String hora_s = horatexts.getText().toString();
        String razon_s = spinner1.getSelectedItem().toString();



        if (dni_s.isEmpty()) {
            Toast.makeText(this, "DNI ES OBLIGATORIO", Toast.LENGTH_SHORT).show();
            return;
        }


        ApiServiceSalida service = ApiServiceGeneratorS.createService(ApiServiceSalida.class);
        Call<Salida> call;
        if(bitmap == null ){

            if(spinner1.getSelectedItemPosition()==0){
                TextView errorText=(TextView)spinner1.getSelectedView();
                errorText.setTextColor(Color.RED);
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                    Toast.makeText(this, "Selecione una razon", Toast.LENGTH_SHORT).show();
                    return;

            }
            if (imagenPreview.getAdjustViewBounds()) {
                Toast.makeText(this, "imaagen es Obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            call = service.createSalida(codigo_s, dni_s, fecha_s, hora_s,razon_s);
        }else {

            // De bitmap a ByteArray
            // De bitmap a ByteArray
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // ByteArray a MultiPart
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "photo.jpg", requestFile);
            //Toast.makeText(this, "Imagen necesaria", Toast.LENGTH_SHORT).show();

            // Paramestros a Part
            RequestBody codigo_sPart = RequestBody.create(MultipartBody.FORM, codigo_s);
            RequestBody dni_sPart = RequestBody.create(MultipartBody.FORM, dni_s);
            RequestBody fecha_sPart = RequestBody.create(MultipartBody.FORM, fecha_s);
            RequestBody hora_sPart = RequestBody.create(MultipartBody.FORM, hora_s);
            RequestBody razon_sPart = RequestBody.create(MultipartBody.FORM, razon_s);

            call = service.createSalida(codigo_sPart, dni_sPart,fecha_sPart, hora_sPart, razon_sPart, imagenPart);
        }

        call.enqueue(new Callback<Salida>() {
            @Override
            public void onResponse(@NonNull Call<Salida> call, @NonNull Response<Salida> response) {
                try {
                    if(response.isSuccessful()) {

                        Salida salida = response.body();

                        Log.d(TAG, "salida: " + salida);

                        Toast.makeText(RegisterSaliActivity.this, "Salida registrada", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);

                        finish();

                    }else {
                        throw new Exception(ApiServiceGeneratorS.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(RegisterSaliActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Salida> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(RegisterSaliActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
