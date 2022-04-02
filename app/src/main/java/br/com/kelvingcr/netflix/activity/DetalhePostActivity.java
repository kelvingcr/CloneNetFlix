package br.com.kelvingcr.netflix.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.kelvingcr.netflix.R;
import br.com.kelvingcr.netflix.adapter.AdapterPost;
import br.com.kelvingcr.netflix.helper.FirebaseHelper;
import br.com.kelvingcr.netflix.model.Post;

public class DetalhePostActivity extends AppCompatActivity {

    private final List<String> downloadList = new ArrayList<>();
    private final List<Post> postList = new ArrayList<>();
    private AdapterPost adapterPost;

    private TextView textTitulo;
    private ImageView imagemPost;
    private ImageView imageFake;
    private TextView textAno;
    private TextView textDuracao;
    private TextView textElenco;
    private TextView textSinopse;
    private RecyclerView rvPosts;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(br.com.kelvingcr.netflix.R.layout.activity_detalhe_post);

        iniciaComponentes();

        configRv();

        configDados();

        configCliques();

        recuperaPost();

     //   recuperaDownloads();

    }

    private void configCliques() {
        findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());

        findViewById(R.id.btnBaixar).setOnClickListener(view -> efetuarDownload());
    }

    private void efetuarDownload(){
        if(!downloadList.contains(post.getId())){

            downloadList.add(post.getId());
           // Download.salvar(downloadList);

            Toast.makeText(getBaseContext(), "Download efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getBaseContext(), "Download já efetuado anteriormente!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperaDownloads() {
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        downloadRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    downloadList.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaPost() {
        DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
                .child("posts");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    postList.add(ds.getValue(Post.class));
                }

                adapterPost.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));
        rvPosts.setHasFixedSize(true);
        adapterPost = new AdapterPost(postList, this);
        rvPosts.setAdapter(adapterPost);
    }

    private void configDados() {
        post = (Post) getIntent().getSerializableExtra("postSelecionado");

        textTitulo.setText(post.getTitulo());

        imageFake.setVisibility(View.GONE);
        Picasso.get()
                .load(post.getImagem())
                .into(imagemPost);

        textAno.setText(post.getAno());
        textDuracao.setText(post.getDuracao());
        textSinopse.setText(post.getSinopse());
        textElenco.setText(post.getElenco());
    }

    private void iniciaComponentes() {
        textTitulo = findViewById(R.id.textTitulo);
        imagemPost = findViewById(R.id.imagemPost);
        imageFake = findViewById(R.id.imageFake);
        textAno = findViewById(R.id.textAno);
        textDuracao = findViewById(R.id.textDuracao);
        textElenco = findViewById(R.id.textElenco);
        textSinopse = findViewById(R.id.textSinopse);
        rvPosts = findViewById(R.id.rvPosts);
    }

}