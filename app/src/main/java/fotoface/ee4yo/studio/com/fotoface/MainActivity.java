package fotoface.ee4yo.studio.com.fotoface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import fotoface.ee4yo.studio.com.fotoface.Adapter.TabAdapter;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.config.PermissionUtils;
import fotoface.ee4yo.studio.com.fotoface.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticao;

    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicita as permissÃµes
        String[] permissoes = new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        PermissionUtils.validate(this, 0, permissoes);

        autenticao = ConfiguracaoFirebase.getAutenticacaoFireB();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Fotos");

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar o Sliding Tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.fundoAzul));

        //Configurar o tabAdapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //pega o menu criado e coloca no menu que o android
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //switch case caso surjam mais itens
        switch (item.getItemId()){
            case R.id.item_sair:
                delogarUsuario();
                return true;
            case R.id.item_meus_dados:
                abrirMeusDados();
                return true;
            case R.id.item_administrador:
                abrirADM();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void delogarUsuario(){
        autenticao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirMeusDados(){
        Intent intent = new Intent(MainActivity.this, MeusDadosActivity.class);
        startActivity(intent);
    }

    public void abrirADM(){
        Intent intent = new Intent(MainActivity.this, LoginADMActivity.class);
        startActivity(intent);
    }
}
