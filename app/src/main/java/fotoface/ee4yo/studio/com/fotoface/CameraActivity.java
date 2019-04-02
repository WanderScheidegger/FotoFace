package fotoface.ee4yo.studio.com.fotoface;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;
import fotoface.ee4yo.studio.com.fotoface.helper.Preferencias;


public class CameraActivity extends AppCompatActivity {

    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private Toolbar toolbar;
    private FirebaseAuth autenticao;
    private Bitmap enviar;
    private ImageButton bt_enviar;
    private FirebaseStorage armazenagemFire;
    private boolean contador;
    private String dataUp;

    private DatabaseReference databaseFireB;
    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCropImageView = (CropImageView)  findViewById(R.id.CropImageView);
        mCropImageView.setFixedAspectRatio(true);

        bt_enviar = (ImageButton) findViewById(R.id.botao_salvar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Fotos");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        //Metodo de suporte para a toolbar
        setSupportActionBar(toolbar);

        contador = true;

        bt_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enviar != null && contador){

                    contador = false;

                    //Recuperar a ID do usuario no arquivo preferencias
                    preferencias = new Preferencias(CameraActivity.this);
                    final String identificadorUsuarioLogado = preferencias.getIdentificador();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmssaa");
                    dataUp = dateFormat.format( new Date() );
                    String nomeImagem = identificadorUsuarioLogado + "." + dataUp + ".jpg" ;

                    armazenagemFire = ConfiguracaoFirebase.getStorageFireB();
                    StorageReference storageReference = armazenagemFire.getReferenceFromUrl("gs://fotoface-f8bf4.appspot.com/")
                            .child(nomeImagem);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    //Pega a imagem cortada e redimensiona
                    Bitmap imgRdef = getResizedBitmap(enviar,240,240);
                    //Transforma a imagem em uma matrizz de bytes com compressão jpeg
                    imgRdef.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);

                    byte[] data = outputStream.toByteArray();

                    //Mostrando o progress Dialog
                    final ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);
                    progressDialog.setTitle("Enviando");
                    progressDialog.show();

                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            contador = true;
                            Toast.makeText(CameraActivity.this, "Ocorreu um erro, a imagem não foi enviada." +
                                    " Tente novamente, por favor", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Pega  url da imagem
                            String urlDaImagem = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            cadastrarUrlImagens(urlDaImagem);

                            progressDialog.dismiss();
                            Toast.makeText(CameraActivity.this, "Imagem enviada com sucesso, obrigado!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
        });
    }

    public void cadastrarUrlImagens(String url){

        databaseFireB = ConfiguracaoFirebase.getDatabaseFireB();

        //Recuperar a ID do usuario no arquivo preferencias
        Preferencias preferencias = new Preferencias(CameraActivity.this);
        String id = preferencias.getIdentificador();

        //salva no nó usuarios
        databaseFireB.child("usuarios").child(id).child("foto").setValue(url);
        //salva no nó imagens
        databaseFireB.child("imagens").child(id).child(dataUp).setValue(url);


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }


    /** Inicio dos meus métodos para menu na toolbar
     ------------------------------------------------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //pega o menu criado e coloca no menu que o android
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //switch case, caso surjam mais itens
        switch (item.getItemId()){
            case R.id.item_sair:
                delogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void delogarUsuario(){
        autenticao.signOut();
        Intent intent = new Intent(CameraActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /** Inicio dos meus métodos
        ------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropImageClick(View view) {
        Bitmap cropped =  mCropImageView.getCroppedImage(600, 600);

        //pegando a imagem para envio.
        enviar = cropped;

        if (cropped != null)
            mCropImageView.setImageBitmap(cropped);

    }

    @Override
    protected void onActivityResult(int  requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri =  getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Você não tem permissão", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager =  getPackageManager();

// collect all camera intents
        Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new  Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}