package fotoface.ee4yo.studio.com.fotoface.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fotoface.ee4yo.studio.com.fotoface.R;

/**
 * Created by ee4yo on 14/07/2017.
 */
public class FotosAdapter extends ArrayAdapter<String>{

    private ArrayList<String> fotos;
    private Context context;

    public FotosAdapter(Context c, ArrayList<String> objects) {
        super(c, 0, objects);
        this.fotos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //verifica se a lista está vazia
        if (fotos != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_fotos, parent, false);

            ImageView imagemFoto = (ImageView) view.findViewById(R.id.img_lista_fotos);
            String urlImagem = fotos.get(position);

            Picasso.with(context).load(urlImagem).resize(224, 224).centerCrop().into(imagemFoto);

        }


        return view;
    }
}
