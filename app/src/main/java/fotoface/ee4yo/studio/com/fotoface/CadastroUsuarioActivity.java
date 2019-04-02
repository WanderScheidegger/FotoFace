package fotoface.ee4yo.studio.com.fotoface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Base64Custom;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;
import fotoface.ee4yo.studio.com.fotoface.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText sobrenome;
    private EditText matricula;
    private EditText curso;
    private EditText instituicao;
    private EditText email;
    private EditText senha;
    private Button btCadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        sobrenome = (EditText) findViewById(R.id.edit_cadastro_sobrenome);
        matricula = (EditText) findViewById(R.id.edit_cadastro_matricula);
        curso = (EditText) findViewById(R.id.edit_cadastro_curso);
        instituicao = (EditText) findViewById(R.id.edit_cadastro_instituicao);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        btCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //condição para não clicar com caixa de texto vazia
                if (nome.getText().toString().isEmpty() ||
                        sobrenome.getText().toString().isEmpty() ||
                        matricula.getText().toString().isEmpty() ||
                        curso.getText().toString().isEmpty() ||
                        instituicao.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() ||
                        senha.getText().toString().isEmpty()){

                    Toast.makeText(CadastroUsuarioActivity.this, "Por favor, " +
                            "preencha todos os campos", Toast.LENGTH_SHORT).show();

                }else{
                    usuario = new Usuario();
                    usuario.setNome(nome.getText().toString());
                    usuario.setSobrenome(sobrenome.getText().toString());
                    usuario.setMatricula(matricula.getText().toString());
                    usuario.setCurso(curso.getText().toString());
                    usuario.setInstituicao(instituicao.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    usuario.setFoto("https://firebasestorage.googleapis.com");

                    cadastrarUsuario();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFireB();

        //Mostrando o progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(CadastroUsuarioActivity.this);
        progressDialog.setTitle("Cadastrando");
        progressDialog.show();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String identificadoUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    usuario.setId(identificadoUsuario);
                    usuario.salvar();

                    //Salvar os dados nas preferencias
                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    String emailUsuarioLogado = usuario.getEmail();
                    preferencias.salvarDadosPref(identificadorUsuarioLogado, emailUsuarioLogado);

                    progressDialog.dismiss();
                    Toast.makeText(CadastroUsuarioActivity.this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                    abrirLoginUsuario();

                }else{
                    progressDialog.dismiss();
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte! Use mais de seis caracteres.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido! Digite um novo e-mail.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está em uso no App!";
                    } catch (Exception e) {
                        erroExcecao = "Cadastro não efetuado.";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show();

                }
            }
        });

    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
