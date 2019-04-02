package fotoface.ee4yo.studio.com.fotoface.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fotoface.ee4yo.studio.com.fotoface.R;
import fotoface.ee4yo.studio.com.fotoface.model.Usuario;

/**
 * Created by ee4yo on 14/07/2017.
 */
public class CadastradosAdapter extends ArrayAdapter<Usuario> {

    private Context context;
    private ArrayList<Usuario> usuarios;
    private ImageView imagem;
    private TextView textoNome;
    private TextView textoSobreNome;


    public CadastradosAdapter(Context C, ArrayList<Usuario> objects) {
        super(C, 0, objects);
        this.usuarios = objects;
        this.context = C;

    }

    public Usuario getItem(int position){
        return usuarios.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (usuarios != null){
            //Inicializar objetos para a montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_cadastrados, parent, false);

            textoNome = (TextView) view.findViewById(R.id.tv_nome_cadastrado);
            textoSobreNome = (TextView) view.findViewById(R.id.tv_sobrenome_cadastrado);
            imagem = (ImageView) view.findViewById(R.id.imagem_lista);

            Usuario usuario = usuarios.get(position);
            textoNome.setText(usuario.getNome());
            textoSobreNome.setText(usuario.getSobrenome());
            String urlDaImagem = usuario.getFoto();

            if (urlDaImagem!=null){
                Picasso.with(context).load(urlDaImagem).resize(70, 70).centerCrop().into(imagem);
            }

        }

        return view;
    }
}
