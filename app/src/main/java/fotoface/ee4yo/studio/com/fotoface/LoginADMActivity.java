package fotoface.ee4yo.studio.com.fotoface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginADMActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;
    private Button bt_logar;
    private String loginADM;
    private String senhaADM;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_adm);

        login = (EditText) findViewById(R.id.LoginID);
        senha = (EditText) findViewById(R.id.SenhaID);
        bt_logar = (Button) findViewById(R.id.bt_LogarID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setTitle("Administração");

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);


        bt_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginADM = login.getText().toString();
                senhaADM = senha.getText().toString();

                if (loginADM.equals("breslau") && senhaADM.equals("12568945")){
                    admLogar();
                }else{
                    Toast.makeText(LoginADMActivity.this, "Vaza mané!!!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void admLogar(){
        Intent intent = new Intent(LoginADMActivity.this,ADMActivity.class);
        startActivity(intent);
        finish();
    }
}
