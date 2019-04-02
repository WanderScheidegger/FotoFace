package fotoface.ee4yo.studio.com.fotoface.helper;


import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "fotoface.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_EMAIL = "emailUsuarioLogado";

    public Preferencias(Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDadosPref( String identificadorUsuario, String emailUsuario ){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_EMAIL, emailUsuario);
        editor.commit();

    }


    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR,null);
    }

    public  String getEmailUsuarioLogado(){
        return preferences.getString(CHAVE_EMAIL,null);
    }
}
