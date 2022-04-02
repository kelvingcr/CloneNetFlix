package br.com.kelvingcr.netflix.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

import br.com.kelvingcr.netflix.helper.FirebaseHelper;

public class Categoria {


    private String id;
    private String nome;
    private List<Post> postList;

    public Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());

        salvar();
    }

    public void salvar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("categorias")
                .child(getId());

        reference.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}
