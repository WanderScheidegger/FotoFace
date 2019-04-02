package fotoface.ee4yo.studio.com.fotoface.config;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public final class ConfiguracaoFirebase {

    private static DatabaseReference databaseFireB;
    private static FirebaseAuth autenticacaoFireB;
    private static FirebaseStorage storageFireB;

    //Referencia do DatabaseFirebase
    public static DatabaseReference getDatabaseFireB(){

        if (databaseFireB == null){
            databaseFireB = FirebaseDatabase.getInstance().getReference();
        }
        return databaseFireB;
    }

    //Referencia do FirebaseAuth
    public  static FirebaseAuth getAutenticacaoFireB(){

        if (autenticacaoFireB == null){
            autenticacaoFireB = FirebaseAuth.getInstance();
        }
        return autenticacaoFireB;
    }

    //Referencia do FirebaseStorage
    public  static FirebaseStorage getStorageFireB(){

        if(storageFireB == null){
            storageFireB = FirebaseStorage.getInstance();

        }
        return storageFireB;
    }

}

