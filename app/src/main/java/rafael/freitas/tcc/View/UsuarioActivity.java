package rafael.freitas.tcc.View;

import android.app.SearchManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.UsuarioViewModel;

import static rafael.freitas.tcc.R.id;
import static rafael.freitas.tcc.R.layout;

public class UsuarioActivity extends AppCompatActivity {
    private UsuarioViewModel usuarioViewModel;
    private ListView lvClientes;
    //private RecyclerView lvClientes;
    private UsuarioAdapter adapter;
    private SearchView searchView;
    private List<Usuario> usuarios;
    private ProgressBar pbProgressoPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_usuario);


        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        //Pegando os componentes definidos na view
        pbProgressoPesquisa = (ProgressBar) findViewById(id.pbProgressoPesquisa);
        lvClientes = (ListView) findViewById(id.lvClientes);

        //Lista que ira armazenar todos os usuarios que sera exibidos na tela
        usuarios = new ArrayList<>();
        //Pegando a referencia ao ModelView
        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);
        //Solicita que todos os usuarios sejam carregados
        MutableLiveData<List<Usuario>> vaLiveData = usuarioViewModel.pesquisarClientes("");

        //Observer que sera notificado toda vez que a lista de usuarios for alterada
        final Observer<List<Usuario>> vaObserver = new Observer<List<Usuario>>() {
            @Override
            public void onChanged(@Nullable List<Usuario> clientes) {
                showProgress(false);
                UsuarioActivity.this.usuarios.clear();
                UsuarioActivity.this.usuarios.addAll(clientes);
                adapter.notifyDataSetChanged();
            }
        };

        vaLiveData.observe(this, vaObserver);

        adapter = new UsuarioAdapter(this, R.layout.list_text_img, usuarios, new CallbackModel<Usuario>() {
            @Override
            public void execute(final Usuario usuario) {
                new AlertDialog.Builder(UsuarioActivity.this)
                        .setMessage("Confirma a exclusão do Usuario?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgress(true);
                                usuarioViewModel.excluir(usuario, new CallbackModel<StatusRetorno>() {
                                    @Override
                                    public void execute(StatusRetorno resultado) {
                                        showProgress(false);
                                        if (!resultado.getStatus().equals(Utils.STATUS_OK)) {
                                            Toast.makeText(UsuarioActivity.this, resultado.getStatus(), Toast.LENGTH_LONG).show();
                                        }else{
                                            UsuarioActivity.this.adapter.remove(usuario);
                                            UsuarioActivity.this.adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        lvClientes.setAdapter(adapter);

        //Setando evento que sera disparado ao tocar em um item da lista
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Usuario usuario = (Usuario) adapterView.getItemAtPosition(i);
                Intent it = new Intent(UsuarioActivity.this, CadastroUsuarioActivity.class);
                it.putExtra(CadastroUsuarioActivity.CPF, usuario.getCpf());

                startActivityForResult(it, CadastroUsuarioActivity.RESULTADO);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioActivity.this, CadastroUsuarioActivity.class);
                startActivityForResult(intent, CadastroUsuarioActivity.RESULTADO);
            }
        });

        handleIntent(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        usuarioViewModel.pesquisarClientes("");
        fecharPesquisa();
    }

    private void fecharPesquisa() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Realizando a pesquisa pelo filtro especificado pelo usuario
            usuarioViewModel.pesquisarClientes(query);
        }
    }

    private void showProgress(boolean show) {
        pbProgressoPesquisa.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(id.toolbar);
        tb.inflateMenu(R.menu.usuario_list_menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) tb.getMenu().findItem(id.menu_edit_pesquisar).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showProgress(true);
                usuarioViewModel.pesquisarClientes(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    showProgress(true);
                    usuarioViewModel.pesquisarClientes("");
                    return true;
                } else {
                    return false;
                }
            }
        });

        return true;
    }


}