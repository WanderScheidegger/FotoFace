package fotoface.ee4yo.studio.com.fotoface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fotoface.ee4yo.studio.com.fotoface.Adapter.FotosAdapter;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Base64Custom;

public class FotosActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private ArrayList<String> fotos;
    private ListView listView;
    private Toolbar toolbar;

    private DatabaseReference databaseFireB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setTitle("Fotos Cadastradas");

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);

        fotos = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_fotos_cadastradas);

        adapter = new FotosAdapter(FotosActivity.this, fotos);
        listView.setAdapter(adapter);

        //recuperando o email
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        String email = dados.getString("email");
        String identificadorUsuarioLogado = Base64Custom.codificarBase64(email);


        //recuperando os dados do firebase
        databaseFireB = ConfiguracaoFirebase.getDatabaseFireB()
                .child("imagens")
                .child(identificadorUsuarioLogado);
        //listenes
        databaseFireB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //limpar a lista
                fotos.clear();

                //listar as fotos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    fotos.add(dados.getValue().toString());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
