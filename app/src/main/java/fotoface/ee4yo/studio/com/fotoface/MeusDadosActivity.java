package fotoface.ee4yo.studio.com.fotoface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;
import fotoface.ee4yo.studio.com.fotoface.model.ModUsuario;

public class MeusDadosActivity extends AppCompatActivity {

    private EditText nome;
    private EditText sobrenome;
    private EditText matricula;
    private EditText curso;
    private EditText instituicao;
    private Button bt_alterar_dados;
    private Toolbar toolbar;
    private ModUsuario modUsuario;

    private DatabaseReference firebaseData;

    private String identificadorUsuarioLogado;
    private String emailUsuarioLogado;
    private String urlDaImagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_dados);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        sobrenome = (EditText) findViewById(R.id.edit_cadastro_sobrenome);
        matricula = (EditText) findViewById(R.id.edit_cadastro_matricula);
        curso = (EditText) findViewById(R.id.edit_cadastro_curso);
        instituicao = (EditText) findViewById(R.id.edit_cadastro_instituicao);
        bt_alterar_dados = (Button) findViewById(R.id.bt_alterar_dados);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setTitle("Meus Dados");

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);

        //Recuperar a ID do usuario no arquivo preferencias
        Preferencias preferencias = new Preferencias(MeusDadosActivity.this);
        identificadorUsuarioLogado = preferencias.getIdentificador();
        emailUsuarioLogado = preferencias.getEmailUsuarioLogado();

        firebaseData = ConfiguracaoFirebase.getDatabaseFireB();
        firebaseData.child("usuarios").child(identificadorUsuarioLogado).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nome.setText(dataSnapshot.child("nome").getValue().toString());
                sobrenome.setText(dataSnapshot.child("sobrenome").getValue().toString());
                matricula.setText(dataSnapshot.child("matricula").getValue().toString());
                curso.setText(dataSnapshot.child("curso").getValue().toString());
                instituicao.setText(dataSnapshot.child("instituicao").getValue().toString());
                urlDaImagem = (dataSnapshot.child("foto").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bt_alterar_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //condição para não clicar com caixa de texto vazia
                if (nome.getText().toString().isEmpty() ||
                        sobrenome.getText().toString().isEmpty() ||
                        matricula.getText().toString().isEmpty() ||
                        curso.getText().toString().isEmpty() ||
                        instituicao.getText().toString().isEmpty()){

                    Toast.makeText(MeusDadosActivity.this, "Por favor, " +
                            "preencha todos os campos", Toast.LENGTH_SHORT).show();

                }else {

                    modUsuario = new ModUsuario();
                    modUsuario.setNome(nome.getText().toString());
                    modUsuario.setSobrenome(sobrenome.getText().toString());
                    modUsuario.setMatricula(matricula.getText().toString());
                    modUsuario.setCurso(curso.getText().toString());
                    modUsuario.setInstituicao(instituicao.getText().toString());
                    modUsuario.setEmail(emailUsuarioLogado);
                    modUsuario.setFoto(urlDaImagem);

                    firebaseData.child("usuarios").child(identificadorUsuarioLogado).setValue(modUsuario);
                    Toast.makeText(MeusDadosActivity.this, "Dados atualizados com sucesso", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
