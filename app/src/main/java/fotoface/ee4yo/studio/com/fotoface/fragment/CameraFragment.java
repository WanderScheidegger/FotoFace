package fotoface.ee4yo.studio.com.fotoface.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import fotoface.ee4yo.studio.com.fotoface.Adapter.FotosAdapter;
import fotoface.ee4yo.studio.com.fotoface.CameraActivity;
import fotoface.ee4yo.studio.com.fotoface.LoginActivity;
import fotoface.ee4yo.studio.com.fotoface.R;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private ImageButton bt_camera_flu;
    private ImageView imageView;
    private int cont = 0;
    private Button proximaDica;
    private TextView textoDicas;
    private Animation zoomAnimation;

    private ArrayAdapter adapter;
    private ArrayList<String> fotos;
    private ListView listView;

    private DatabaseReference databaseFireB;
    private Preferencias preferencias;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);

        textoDicas = (TextView) view.findViewById(R.id.textoDicasID);

        proximaDica = (Button) view.findViewById(R.id.bt_proxima_dica);

        fotos = new ArrayList<>();

        imageView = (ImageView) view.findViewById(R.id.imagem_do_zoom);
        listView = (ListView) view.findViewById(R.id.lv_fotos);

        adapter = new FotosAdapter(getContext(), fotos);
        listView.setAdapter(adapter);

        //Recuperar a ID do usuario no arquivo preferencias
        preferencias = new Preferencias(getContext());
        String identificadorUsuarioLogado = preferencias.getIdentificador();


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

                if (!fotos.isEmpty()){
                    textoDicas.setVisibility(view.INVISIBLE);
                    proximaDica.setVisibility(view.INVISIBLE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bt_camera_flu = (ImageButton) view.findViewById(R.id.botaoCameraID);

        bt_camera_flu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
            }
        });

        proximaDica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_camera_flu.setVisibility(view.INVISIBLE);
                criarDicas();
            }
        });

        return view;
    }

    public void abrirCamera(){
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
    }

    private void criarDicas(){

        if (cont==0) {
            proximaDica.setText("PRÓXIMA DICA");
        }
        switch (cont){

            case 0:
                textoDicas.setText("Após ver as dicas, você será redirecionado para " +
                        "a tela de edição de fotos.");
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.img1);
                textoDicas.setText("Ao carregar a tela de edição, clique no botão do meio para " +
                        "acessar a camera ou a galeria de fotos do dispositivo.");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom);
                zoomAnimation.setDuration(3000);
                imageView.startAnimation(zoomAnimation);

                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.img2);
                zoomAnimation.reset();
                textoDicas.setText("Você pode utilizar uma fotografia existente de sua galeria " +
                        "de fotos ou tirar uma nova fotografia  utilizando a câmera. Procure tirar " +
                        "fotos em ambientes com boa iluminação diminuindo as sombras na face.");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom2);
                imageView.startAnimation(zoomAnimation);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.img3);
                zoomAnimation.reset();
                textoDicas.setText("A foto de sua escolha será carregada na janela de edição. " +
                        "É possível centralizar a parte do rosto utilizando o quadrado de edição. ");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom3);
                imageView.startAnimation(zoomAnimation);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.img4);
                zoomAnimation.reset();
                textoDicas.setText("Tente centralizar a regiao da cabeça. " +
                        "A foto deve ocupar toda a regiao da cabeça, conforme modelo abaixo. ");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom3);
                imageView.startAnimation(zoomAnimation);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.img4);
                zoomAnimation.reset();
                textoDicas.setText("Após centralizar a foto na janela de edição, clique no " +
                        "botao cortar, à esquerda, para terminar a  edição da imagem.");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom2);
                imageView.startAnimation(zoomAnimation);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.img4);
                zoomAnimation.reset();
                textoDicas.setText("Quase pronto. Agora basta clicar no botão salvar, à direita, " +
                        "e aguardar o envio da foto. Se possível, envie no mínimo 3 fotos " +
                        "para contribuir com a qualidade do banco de faces.");
                zoomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom4);
                imageView.startAnimation(zoomAnimation);
                proximaDica.setText("FINALIZAR");
                break;
        }
        cont+=1;
        if (cont>7){
            cont=0;
            abrirCamera();
            getActivity().finish();
        }
    }

}
