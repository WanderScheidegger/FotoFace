package fotoface.ee4yo.studio.com.fotoface.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import fotoface.ee4yo.studio.com.fotoface.CadastroUsuarioActivity;
import fotoface.ee4yo.studio.com.fotoface.LoginActivity;
import fotoface.ee4yo.studio.com.fotoface.MainActivity;
import fotoface.ee4yo.studio.com.fotoface.R;

public class ContratoActivity extends AppCompatActivity {

    private WebView minhaWebview;
    private Button btAceitar;
    private Button btDiscordar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato);

        minhaWebview = (WebView) findViewById(R.id.webView1Id);
        btAceitar = (Button) findViewById(R.id.btAceitar);
        btDiscordar = (Button) findViewById(R.id.btDiscordar);

        String text = "<html><body bgcolor=\"#656a64\">"
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p_titulo)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p1_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p2_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p3_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p4_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p5_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p6_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p7_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p8_contrato)
                + "</font>"
                + "</p> "
                + "<p align=\"justify\">"
                + "<font color=\"#FFFFFFFF\">"
                + getString(R.string.p9_contrato)
                + "</font>"
                + "</p> "
                + "</body></html>";

        minhaWebview.loadData(text,"text/html;charset=UTF-8",null);

        btAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContratoActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btDiscordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContratoActivity.this, "Obrigado, nenhum dado pessoal foi salvo.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ContratoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
