package fotoface.ee4yo.studio.com.fotoface.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import fotoface.ee4yo.studio.com.fotoface.R;
import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private final int VIDEO_REQUEST_CODE = 101;
    private VideoView videoView;
    private ImageButton bt_play;
    private ImageButton bt_upload;
    private ImageButton bt_gravar_video;
    private String EXTENSAO = ".mp4";
    private String identificadorUsuarioLogado;
    private String lado;
    private Uri foto;
    private int cont = 0;

    private FirebaseStorage armazenagemFire;
    private DatabaseReference databaseFireB;

    private TextView textoDicas;
    private Button bt_dicas;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        textoDicas = (TextView) view.findViewById(R.id.texto_dicas_videoID);
        bt_dicas = (Button) view.findViewById(R.id.bt_dicas_videoID);

        videoView = (VideoView) view.findViewById(R.id.video_play);
        bt_gravar_video = (ImageButton) view.findViewById(R.id.bt_gravar_video);
        bt_play = (ImageButton) view.findViewById(R.id.bt_play);
        bt_upload = (ImageButton) view.findViewById(R.id.bt_upload);

        videoView.setVisibility(View.GONE);

        //Recuperar a ID do usuario no arquivo preferencias
        Preferencias preferencias = new Preferencias(getContext());
        identificadorUsuarioLogado = preferencias.getIdentificador();
        EXTENSAO = identificadorUsuarioLogado+EXTENSAO;

        bt_gravar_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureVideo();
            }
        });

            bt_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoView.setVisibility(View.VISIBLE);
                    playVideo();
                }
            });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibeDialog(view);

            }
        });

        bt_dicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dicas();
            }
        });

        return view;
    }

    public void dicas(){

        if (cont==0){
            bt_dicas.setText("PRÓXIMA DICA");
            videoView.setVisibility(View.INVISIBLE);
        }
        switch (cont){
            case 0:
                textoDicas.setVisibility(videoView.VISIBLE);
                textoDicas.setText("Após ler todas as dicas, clique no botão do meio, abaixo, e utilize a câmera" +
                        " frontal para gravar o vídeo.");
                break;
            case 1:
                textoDicas.setText("Primeiro, mantenha o celular na mão esquerda, com o braço totalmente esticado, " +
                        "visualizando a lateral do rosto.");
                videoEsquerdo();
                break;
            case 2:
                textoDicas.setText("Tente centralizar o rosto verticalmente na tela.");
                break;
            case 3:
                textoDicas.setText("Grave o lado esquerdo rotacionando a câmera ao redor do rosto" +
                        " conforme o vídeo abaixo.");
                videoEsquerdo();
                break;
            case 4:
                textoDicas.setText("Após gravar o vídeo, você pode ver o resultado clicando no botão play da direita.");
                break;
            case 5:
                textoDicas.setText("Após a visualização, clique no botão do lado esquerdo para fazer o upload. Em seguida, " +
                        "selecione a caixa 'Esquerdo' e clique em enviar. Aguarde o envio.");
                break;
            case 6:
                textoDicas.setText("Repita o procedimento de gravação para o lado direito, conforme o vídeo abaixo.");
                videoDireito();
                break;
            case 7:
                textoDicas.setText("Após gravar o vídeo, clique no botão do lado esquerdo para fazer o upload. Em seguida, " +
                        "selecione a caixa 'Direito' e clique em enviar. Aguarde o envio.");
                break;
            case 8:
                textoDicas.setText("Pronto, agora você já pode gravar e enviar os vídeos.");
                bt_dicas.setText("FIM");
                videoView.setVisibility(View.INVISIBLE);
                break;
            case 9:
                bt_dicas.setText("DICAS");
                textoDicas.setVisibility(videoView.INVISIBLE);
                break;
        }

        cont+=1;
        if (cont>9){
            cont=0;
        }
    }

    public void videoEsquerdo(){
        videoView.setVisibility(View.VISIBLE);
        Uri src = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.esquerdo );
        videoView.setVideoURI(src);
        videoView.start();
    }

    public void videoDireito(){
        videoView.setVisibility(View.VISIBLE);
        Uri src = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.direito );
        videoView.setVideoURI(src);
        videoView.start();
    }

    private void exibeDialog(View view){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.caixa_dialogo_video);

        final TextView textoDialogo = (TextView) view.findViewById(R.id.textoSCaixaDialogoID);
        final  RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.RadioGroupID);
        final Button botao_enviar_dialogo = (Button) dialog.findViewById(R.id.botao_enviar_dialogID);
        final Button botao_cancelar_dialogo = (Button) dialog.findViewById(R.id.botao_cancelar_dialogID);

        //define o título do Dialog
        dialog.setTitle("VIDEO");

        botao_cancelar_dialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finaliza o dialog
                dialog.dismiss();
            }
        });

        botao_enviar_dialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int idRadioButtonEscolhido = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButtonEscolhido = (RadioButton) dialog.findViewById(idRadioButtonEscolhido);
                String textoRadio = radioButtonEscolhido.getText().toString();
                uploadVideo(textoRadio);
                //finaliza o dialog
                dialog.dismiss();
            }
        });

        //exibe na tela o dialog
        dialog.show();
    }

    public void uploadVideo(String video) {

        if (foto == null){
            Toast.makeText(getContext(), "Primeiro grave o video, só depois tente enviar", Toast.LENGTH_SHORT).show();
        }else {

            //Recuperar a ID do usuario no arquivo preferencias
            Preferencias preferencias = new Preferencias(getContext());
            String id = preferencias.getIdentificador();

            lado = video;

            Uri uri = foto;
            armazenagemFire = ConfiguracaoFirebase.getStorageFireB();
            StorageReference storageReference = armazenagemFire.getReferenceFromUrl("gs://fotoface-f8bf4.appspot.com/")
                    .child("videos")
                    .child(lado + "." + id + ".mp4");


            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Enviando");
            progressDialog.setCancelable(false);
            progressDialog.show();

            UploadTask uploadTask = storageReference.putFile(uri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "O vídeo não foi enviado. Confira a sua conexão com a internet. ", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Pega  url do video
                    String urlDoVideo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    cadastrarUrlVideo(urlDoVideo);
                    progressDialog.dismiss();

                    Toast.makeText(getContext(), "Vídeo enviado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //calculando a porcentagem
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    //dialogo com a porcentagem
                    progressDialog.setMessage("Enviado " + ((int) progress) + "%...");
                }
            });
        }
    }

    public void cadastrarUrlVideo(String url){

        databaseFireB = ConfiguracaoFirebase.getDatabaseFireB();

        //Recuperar a ID do usuario no arquivo preferencias
        Preferencias preferencias = new Preferencias(getContext());
        String id = preferencias.getIdentificador();

        //salva no nó videos
        databaseFireB.child("videos").child(id).child(lado).setValue(url);

    }

    public void captureVideo(){

        Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);


        /*File video_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + EXTENSAO);
        Uri video_uri = Uri.fromFile(video_file);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
        //startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
*/
        if (camera_intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == VIDEO_REQUEST_CODE) {
                if (resultCode == getActivity().RESULT_OK) {

                    if (data!=null){
                        foto = data.getData();
                        Toast.makeText(getContext(), "Video gravado com sucesso", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Seu aparelho não permite a gravação. Entre em contato com a " +
                                "equipe FotoFace", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Ocorreu um erro na gravação, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void playVideo(){
        if (foto!=null){
            videoView.setVideoURI(foto);
            videoView.start();
        }else{
            Toast.makeText(getContext(), "Primeiro grave o video", Toast.LENGTH_SHORT).show();
            videoView.setVisibility(View.GONE);
        }

    }



}
