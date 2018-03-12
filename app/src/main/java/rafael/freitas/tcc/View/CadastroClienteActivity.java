package rafael.freitas.tcc.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.R;

public class CadastroClienteActivity extends AppCompatActivity {

    public static final String OBJETO = "OBJETO";

    private EditText edtNome;
    private EditText edtCpf;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtEndereco;
    private EditText edtEstado;
    private EditText edtMunicipio;
    private EditText edtTelefone;

    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtCpf = (EditText) findViewById(R.id.edtCpf);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtMunicipio = (EditText) findViewById(R.id.edtMunicipio);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEstado = (EditText) findViewById(R.id.edtEstado);

        Intent it = getIntent();
        if (it.getSerializableExtra(OBJETO) != null) {
            cliente = (Cliente) it.getSerializableExtra(OBJETO);
            preencherEdits();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void preencherEdits() {
        edtCpf.setText(cliente.getCpf());
        edtNome.setText(cliente.getNome());
        edtEstado.setText(cliente.getEstado());
        edtMunicipio.setText(cliente.getMunicipio());
        edtEndereco.setText(cliente.getEndereco());
        edtTelefone.setText(cliente.getTelefone());
        edtEmail.setText(cliente.getEmail());
        edtSenha.setText(cliente.getSenha());
    }

}
