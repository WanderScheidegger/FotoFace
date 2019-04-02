package fotoface.ee4yo.studio.com.fotoface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Base64Custom;
import fotoface.ee4yo.studio.com.fotoface.helper.EsqueceuSenhaActivity;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;
import fotoface.ee4yo.studio.com.fotoface.model.ContratoActivity;
import fotoface.ee4yo.studio.com.fotoface.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button btLogar;
    private Usuario usuario;
    private TextView btContrato;
    private TextView esqueceuSenha;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.ADM_loginID);
        senha = (EditText) findViewById(R.id.ADM_senhaID);
        btLogar = (Button) findViewById(R.id.bt_ADM_logarID);
        btContrato = (TextView) findViewById(R.id.textBtCadastrarID);
        esqueceuSenha = (TextView) findViewById(R.id.esqueceuSenhaID);

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //condição para não clicar com caixa de texto vazia
                if (email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());

                    validarLogin();
                }
            }
        });

        btContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ContratoActivity.class);
                startActivity(intent);
            }
        });

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,EsqueceuSenhaActivity.class);
                startActivity(intent);
            }
        });

    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFireB();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void validarLogin(){
        //Mostrando o progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Conectando");
        progressDialog.show();

        autenticacao = ConfiguracaoFirebase.getAutenticacaoFireB();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    progressDialog.dismiss();

                    //Salvar os dados nas preferencias
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    String emailUsuarioLogado = usuario.getEmail();
                    preferencias.salvarDadosPref(identificadorUsuarioLogado, emailUsuarioLogado);

                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer o Login.", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.dismiss();

                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "Este e-mail não está cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha inválida.";
                    } catch (Exception e) {
                        erroExcecao = "Login não efetuado.";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
