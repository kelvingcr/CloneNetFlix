package br.com.kelvingcr.netflix.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import br.com.kelvingcr.netflix.R;
import br.com.kelvingcr.netflix.helper.FirebaseHelper;
import br.com.kelvingcr.netflix.model.Post;


public class AddFragment extends Fragment {

    private EditText edt_titulo, edt_genero, edt_elenco, edt_ano, edt_duracao, edt_sinopse;
    private ProgressBar progressBar;


    private ImageView imagem;
    private static final int REQUEST_GALERIA = 100;
    private String caminhoImagem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        configCliques();

    }

    private void validaDados() {

        String titulo = edt_titulo.getText().toString().trim();
        String genero = edt_genero.getText().toString().trim();
        String elenco = edt_elenco.getText().toString().trim();
        String ano = edt_ano.getText().toString().trim();
        String duracao = edt_duracao.getText().toString().trim();
        String sinopse = edt_sinopse.getText().toString().trim();

        if (!titulo.isEmpty()) {
            if (!genero.isEmpty()) {
                if (!elenco.isEmpty()) {
                    if (!ano.isEmpty()) {
                        if (!duracao.isEmpty()) {
                            if (!sinopse.isEmpty()) {
                                if(caminhoImagem != null) {

                                    progressBar.setVisibility(View.VISIBLE);

                                    Post post = new Post();
                                    post.setTitulo(titulo);
                                    post.setGenero(genero);
                                    post.setElenco(elenco);
                                    post.setAno(ano);
                                    post.setDuracao(duracao);
                                    post.setSinopse(sinopse);
                                    salvarImagemFirebase(post);

                                }else{
                                    Toast.makeText(getActivity(), "Selecione uma imagem para sua postagem", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                edt_sinopse.requestFocus();
                                edt_sinopse.setError("Campo obrigatório.");
                            }

                        } else {
                            edt_duracao.requestFocus();
                            edt_duracao.setError("Campo obrigatório.");
                        }

                    } else {
                        edt_ano.requestFocus();
                        edt_ano.setError("Campo obrigatório.");
                    }

                } else {
                    edt_elenco.requestFocus();
                    edt_elenco.setError("Campo obrigatório.");
                }

            } else {
                edt_genero.requestFocus();
                edt_genero.setError("Campo obrigatório.");
            }

        } else {
            edt_titulo.requestFocus();
            edt_titulo.setError("Campo obrigatório.");
        }

    }

    private void salvarImagemFirebase(Post post) {

        StorageReference Storagereference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("posts")
                .child(post.getId() + "jpeg");

        UploadTask uploadTask = Storagereference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Storagereference.getDownloadUrl().addOnCompleteListener(task -> {

                String imgImagem = task.getResult().toString(); //Retorna a url da imagem salva no firebase
                post.setImagem(imgImagem);

                limparCampos();
                post.salvar();

            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });
        });


    }
    private void limparCampos(){
        imagem.setImageBitmap(null);

        edt_titulo.getText().clear();
        edt_ano.getText().clear();
        edt_duracao.getText().clear();
        edt_elenco.getText().clear();
        edt_genero.getText().clear();
        edt_sinopse.getText().clear();

        progressBar.setVisibility(View.GONE);
    }

    private void initComponents(View view) {
        imagem = view.findViewById(R.id.imagem);
        edt_titulo = view.findViewById(R.id.edt_titulo);
        edt_ano = view.findViewById(R.id.edt_ano);
        edt_duracao = view.findViewById(R.id.edt_duracao);
        edt_elenco = view.findViewById(R.id.edt_elenco);
        edt_genero = view.findViewById(R.id.edt_genero);
        edt_sinopse = view.findViewById(R.id.edt_sinopse);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void configCliques() {

        imagem.setOnClickListener(v -> verificaPermissacaoGaleria());
        getActivity().findViewById(R.id.btn_cadastrar).setOnClickListener(view -> validaDados());

    }


    public void verificaPermissacaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes) {
        TedPermission.create().setPermissionListener(permissionListener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Voce negou a permissão para acessar a galeria do aplicativo, deseja permitir?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes).check();
    }


    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {

                Uri localImagemSeleciona = data.getData();
                caminhoImagem = localImagemSeleciona.toString();

                try {

                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSeleciona);

                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), localImagemSeleciona);
                        bitmap = ImageDecoder.decodeBitmap(source);

                    }

                    imagem.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}