package fotoface.ee4yo.studio.com.fotoface.helper;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import fotoface.ee4yo.studio.com.fotoface.R;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private EditText email;
    private Button botaoEnviarEmail;
    FirebaseAuth autenticação;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        email = (EditText) findViewById(R.id.ADM_loginID);
        botaoEnviarEmail = (Button) findViewById(R.id.bt_ADM_logarID);

        botaoEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){
                    Toast.makeText(EsqueceuSenhaActivity.this, "Por favor, preencha o seu e-mail.", Toast.LENGTH_SHORT).show();
                }else{
                    enviarEmailEsqueceu();
                }

            }
        });
    }

    public void enviarEmailEsqueceu(){

        autenticação = ConfiguracaoFirebase.getAutenticacaoFireB();

        //Mostrando o progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(EsqueceuSenhaActivity.this);
        progressDialog.setTitle("Enviando o e-mail");
        progressDialog.show();

        autenticação.sendPasswordResetEmail(email.getText().toString()).
        addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(EsqueceuSenhaActivity.this, "Email enviado com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    progressDialog.dismiss();

                    String erroExcecao = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "Este e-mail não está cadastrado.";
                    } catch (Exception e) {
                        erroExcecao = "Email não enviado, tente novamente mais tarde.";
                        e.printStackTrace();
                    }

                    Toast.makeText(EsqueceuSenhaActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
