package rafael.freitas.tcc.View;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.ClienteViewModel;

public class CadastroClienteActivity extends AppCompatActivity {

    public static final String CPF = "CPF";
    public static final int RESULTADO = 0;

    private EditText edtNome;
    private EditText edtCpf;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtEndereco;
    private EditText edtEstado;
    private EditText edtMunicipio;
    private EditText edtTelefone;
    private ProgressBar pbProgressoSalvar;

    private Cliente cliente;
    private ClienteViewModel viewModel;
    private boolean inserindo;

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
        pbProgressoSalvar = (ProgressBar) findViewById(R.id.pbProgressoSalvar);

        viewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        final Observer<Cliente> vaObserver = new Observer<Cliente>() {
            @Override
            public void onChanged(@Nullable Cliente cliente) {
                if (cliente!=null) {
                    CadastroClienteActivity.this.cliente = cliente;
                    preencherEdits();
                }else{
                    //Algo deu errado e nao achou o cliente
                    Toast.makeText(CadastroClienteActivity.this,"Nao foi possível carregar as informações do cliente",Toast.LENGTH_LONG).show();
                }
            }
        };

        Intent it = getIntent();
        inserindo = it.getStringExtra(CPF) == null;
        if (!inserindo) {
            //Carregando as informações do cliente
            MutableLiveData<Cliente> vaLiveData = viewModel.getCliente();
            vaLiveData.observe(this, vaObserver);

            viewModel.pesquisarClienteCPF(it.getStringExtra(CPF));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void preencherEdits() {
        if (cliente!=null) {
            edtCpf.setText(cliente.getCpf());
            edtNome.setText(cliente.getNome());
            edtEstado.setText(cliente.getEstado());
            edtMunicipio.setText(cliente.getMunicipio());
            edtEndereco.setText(cliente.getEndereco());
            edtTelefone.setText(cliente.getTelefone());
            edtEmail.setText(cliente.getEmail());
        }
    }

    private boolean validarDados(){
        if (TextUtils.isEmpty(edtCpf.getText().toString())) {
            edtCpf.setError(getString(R.string.error_field_required));
            edtCpf.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtNome.getText().toString())) {
            edtNome.setError(getString(R.string.error_field_required));
            edtNome.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError(getString(R.string.error_field_required));
            edtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError(getString(R.string.error_field_required));
            edtSenha.requestFocus();
            return false;
        }
        return true;
    }

    public boolean preencherCliente(){
        if (validarDados()) {
            if (inserindo)
                cliente = new Cliente();

            cliente.setCpf(edtCpf.getText().toString());
            cliente.setNome(edtNome.getText().toString());
            cliente.setEstado(edtEstado.getText().toString());
            cliente.setMunicipio(edtMunicipio.getText().toString());
            cliente.setEndereco(edtEndereco.getText().toString());
            cliente.setTelefone(edtTelefone.getText().toString());
            cliente.setEmail(edtEmail.getText().toString());
            cliente.setSenha(edtSenha.getText().toString());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.menu_cadastro_cliente);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_salvar_cliente:
                if (preencherCliente()) {
                    showProgress(true);
                    CadastroClienteActivity.this.viewModel.salvar(CadastroClienteActivity.this.cliente, new CallbackModel<StatusRetorno>() {
                        @Override
                        public void execute(StatusRetorno resultado) {
                            if (!resultado.getStatus().equals(Utils.STATUS_OK)) {
                                showProgress(false);
                                Toast.makeText(CadastroClienteActivity.this, resultado.getStatus(), Toast.LENGTH_LONG).show();
                            } else {
                                CadastroClienteActivity.this.finish();
                            }
                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgress(boolean show) {
        pbProgressoSalvar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
