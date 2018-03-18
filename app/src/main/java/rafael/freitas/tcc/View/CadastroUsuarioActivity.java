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

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.UsuarioViewModel;

public class CadastroUsuarioActivity extends AppCompatActivity {

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

    private Usuario usuario;
    private UsuarioViewModel viewModel;
    private boolean inserindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);


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

        viewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);
        final Observer<Usuario> vaObserver = new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario cliente) {
                if (cliente!=null) {
                    CadastroUsuarioActivity.this.usuario = cliente;
                    preencherEdits();
                }else{
                    //Algo deu errado e nao achou o usuario
                    Toast.makeText(CadastroUsuarioActivity.this,"Nao foi possível carregar as informações do usuario",Toast.LENGTH_LONG).show();
                }
            }
        };

        Intent it = getIntent();
        inserindo = it.getStringExtra(CPF) == null;
        if (!inserindo) {
            //Carregando as informações do usuario
            MutableLiveData<Usuario> vaLiveData = viewModel.getCliente();
            vaLiveData.observe(this, vaObserver);

            viewModel.pesquisarClienteCPF(it.getStringExtra(CPF));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void preencherEdits() {
        if (usuario !=null) {
            edtCpf.setText(usuario.getCpf());
            edtNome.setText(usuario.getNome());
            edtEstado.setText(usuario.getEstado());
            edtMunicipio.setText(usuario.getMunicipio());
            edtEndereco.setText(usuario.getEndereco());
            edtTelefone.setText(usuario.getTelefone());
            edtEmail.setText(usuario.getEmail());
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
                usuario = new Usuario();

            usuario.setCpf(edtCpf.getText().toString());
            usuario.setNome(edtNome.getText().toString());
            usuario.setEstado(edtEstado.getText().toString());
            usuario.setMunicipio(edtMunicipio.getText().toString());
            usuario.setEndereco(edtEndereco.getText().toString());
            usuario.setTelefone(edtTelefone.getText().toString());
            usuario.setEmail(edtEmail.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.inflateMenu(R.menu.menu_cadastro_usuario);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_salvar_usuario:
                if (preencherCliente()) {
                    showProgress(true);
                    CadastroUsuarioActivity.this.viewModel.salvar(CadastroUsuarioActivity.this.usuario, new CallbackModel<StatusRetorno>() {
                        @Override
                        public void execute(StatusRetorno resultado) {
                            if (!resultado.getStatus().equals(Utils.STATUS_OK)) {
                                showProgress(false);
                                Toast.makeText(CadastroUsuarioActivity.this, resultado.getStatus(), Toast.LENGTH_LONG).show();
                            } else {
                                CadastroUsuarioActivity.this.finish();
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
