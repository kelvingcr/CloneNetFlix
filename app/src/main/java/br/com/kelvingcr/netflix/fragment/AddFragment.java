package br.com.kelvingcr.netflix.fragment;

import android.Manifest;
import android.content.Intent;
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
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import br.com.kelvingcr.netflix.R;
import br.com.kelvingcr.netflix.activity.MainActivity;


public class AddFragment extends Fragment {

    private EditText edt_titulo, edt_genero, edt_elenco, edt_ano, edt_duracao, edt_sinopse;

    private ImageView imagem;
    private static final int REQUEST_GALERIA = 100;

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

    }

    private void initComponents(View view) {
        imagem = view.findViewById(R.id.imagem);
    }

    private void configCliques() {

        imagem.setOnClickListener(v -> verificaPermissacaoGaleria());

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


    private void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }
}