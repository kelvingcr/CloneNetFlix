package br.com.kelvingcr.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.kelvingcr.netflix.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.btn_continuar).setOnClickListener(view -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }
}