package br.com.kelvingcr.netflix.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.kelvingcr.netflix.R;
import br.com.kelvingcr.netflix.activity.DetalhePostActivity;
import br.com.kelvingcr.netflix.adapter.AdapterBusca;
import br.com.kelvingcr.netflix.helper.FirebaseHelper;
import br.com.kelvingcr.netflix.model.Post;


public class BuscaFragment extends Fragment {

    private final List<Post> postList = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView rvPosts;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busca, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciaComponentes(view);

        configRv(postList);

        recuperaPost();

        configSearchView();

    }

    private void configSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisa) {
                if (pesquisa.length() >= 3) {
                    buscarPosts(pesquisa);
                } else {
                    ocultarTeclado();
                    Toast.makeText(getContext(), "Mínimo 3 caracteres.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.findViewById(R.id.searchView).setOnClickListener(view -> {
            ocultarTeclado();
            recuperaPost();

            EditText edt = searchView.findViewById(R.id.searchView);
            edt.getText().clear();

            searchView.clearFocus();

        });
    }

    private void buscarPosts(String pesquisa) {
        List<Post> postListBusca = new ArrayList<>();

        ocultarTeclado();

        for (Post post : postList){
            if(post.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))){
                postListBusca.add(post);
            }
        }

        if(!postListBusca.isEmpty()){
            configRv(postListBusca);
        }else{
            configRv(new ArrayList<>());
            textInfo.setText("Nenhum post encontrado com o nome pesquisado.");
        }

    }

    private void recuperaPost() {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                    }
                    textInfo.setText("");
                } else {
                    textInfo.setText("Nenhum post cadastrado.");
                }

                progressBar.setVisibility(View.GONE);

                configRv(postList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv(List<Post> postList) {
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setHasFixedSize(true);
        AdapterBusca adapterBusca = new AdapterBusca(postList, getContext());
        rvPosts.setAdapter(adapterBusca);
        adapterBusca.notifyDataSetChanged();
    }

    private void iniciaComponentes(View view) {
        searchView = view.findViewById(R.id.searchView);
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        textInfo = view.findViewById(R.id.textInfo);
    }

    private void ocultarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}