package br.com.kelvingcr.netflix.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.kelvingcr.netflix.R;
import br.com.kelvingcr.netflix.adapter.AdapterCategoria;
import br.com.kelvingcr.netflix.model.Categoria;
import br.com.kelvingcr.netflix.model.Post;


public class InicioFragment extends Fragment {

    private AdapterCategoria adapterCategoria;

    private List<Categoria> categoriaList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();

    private RecyclerView rvCategorias;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        configRv();

    }

    private void configRv(){
        rvCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategorias.setHasFixedSize(true);
        adapterCategoria = new AdapterCategoria(categoriaList, getContext());
        rvCategorias.setAdapter(adapterCategoria);
    }

     private void initComponents(View view){
         rvCategorias = view.findViewById(R.id.rvCategorias);
         progressBar = view.findViewById(R.id.progressBar);
     }
}