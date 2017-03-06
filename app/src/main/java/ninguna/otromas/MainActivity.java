package ninguna.otromas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btAceptar;
    EditText txtUsu;
    EditText txtPass;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAceptar=(Button) findViewById(R.id.btAceptar);
        txtUsu=(EditText)findViewById(R.id.txtUsu);
        txtPass=(EditText)findViewById(R.id.txtPass);
        btAceptar.setOnClickListener(this);

    }

    public String enviarDatosGet(String usu,String pass){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;

        try {

            url = new URL("185.28.21.131/obtenerUsuarios.php");
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();

            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in =new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }

        }catch(Exception e){

        }

        return resul.toString();
    }

    public int obtenerDatosJSON (String response){
        int res=0;
        try{
            JSONArray json=new JSONArray(response);
            if(json.length()>0){
                res=1;
            }


        }catch(Exception e){

        }
        return res;

    }

    @Override
    public void onClick(View v) {
        Thread tr=new Thread(){
            @Override
            public void run() {
                final String resultado=enviarDatosGet(txtUsu.getText().toString(),txtUsu.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r=obtenerDatosJSON(resultado);
                        if(r>0){
                            Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                            i.putExtra("Usuario",txtUsu.getText().toString());
                        }else{
                            Toast.makeText(getApplicationContext(),"Usuario o Pass incorrecto",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        };
        tr.start();
    }
}
