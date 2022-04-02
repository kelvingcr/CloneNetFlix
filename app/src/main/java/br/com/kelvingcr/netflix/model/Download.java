package br.com.kelvingcr.netflix.model;


import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.com.kelvingcr.netflix.helper.FirebaseHelper;

public class Download {

    public static void salvar(List<String> downloadList){
        DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
                .child("downloads");
        downloadRef.setValue(downloadList);
    }

}
