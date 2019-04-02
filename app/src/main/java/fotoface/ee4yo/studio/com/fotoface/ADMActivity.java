package fotoface.ee4yo.studio.com.fotoface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fotoface.ee4yo.studio.com.fotoface.Adapter.CadastradosAdapter;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.model.Usuario;

public class ADMActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Usuario> usuarios;
    private Usuario usuario;
    private String email;

    private DatabaseReference databaseFireB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setTitle("Usuários Cadastrados");

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);

        usuarios = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_cadastrados);

        adapter = new CadastradosAdapter(ADMActivity.this, usuarios);
        listView.setAdapter(adapter);

        //recuperando os dados do firebase
        databaseFireB = ConfiguracaoFirebase.getDatabaseFireB()
                .child("usuarios");

        //listenes
        databaseFireB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //limpar a lista
                usuarios.clear();

                //listar as fotos
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    usuario = dados.getValue(Usuario.class);
                    usuarios.add(usuario);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);

                usuario = (Usuario) adapter.getItem(i);
                email = usuario.getEmail();

                Intent intent = new Intent(ADMActivity.this, FotosActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

    }
}
