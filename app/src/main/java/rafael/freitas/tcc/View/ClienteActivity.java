package rafael.freitas.tcc.View;

import android.app.SearchManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.ClienteViewModel;

import static rafael.freitas.tcc.R.id;
import static rafael.freitas.tcc.R.layout;

public class ClienteActivity extends AppCompatActivity {
    private ClienteViewModel clienteViewModel;
    private ListView lvClientes;
    //private RecyclerView lvClientes;
    private ClienteAdapter adapter;
    private SearchView searchView;
    private List<Cliente> clientes;
    private ProgressBar pbProgressoPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_cliente);

        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        //Pegando os componentes definidos na view
        pbProgressoPesquisa = (ProgressBar) findViewById(id.pbProgressoPesquisa);
        lvClientes = (ListView) findViewById(id.lvClientes);

        //Lista que ira armazenar todos os clientes que sera exibidos na tela
        clientes = new ArrayList<>();
        //Pegando a referencia ao ModelView
        clienteViewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        //Solicita que todos os clientes sejam carregados
        MutableLiveData<List<Cliente>> vaLiveData = clienteViewModel.pesquisarClientes("");

        //Observer que sera notificado toda vez que a lista de clientes for alterada
        final Observer<List<Cliente>> vaObserver = new Observer<List<Cliente>>() {
            @Override
            public void onChanged(@Nullable List<Cliente> clientes) {
                showProgress(false);
                ClienteActivity.this.clientes.clear();
                ClienteActivity.this.clientes.addAll(clientes);
                adapter.notifyDataSetChanged();
            }
        };

        vaLiveData.observe(this, vaObserver);

        adapter = new ClienteAdapter(this, R.layout.list_text_img, clientes, new CallbackModel<Cliente>() {
            @Override
            public void execute(Cliente cliente) {
              clienteViewModel.excluir(cliente, new CallbackModel<StatusRetorno>() {
                  @Override
                  public void execute(StatusRetorno resultado) {
                      if (!resultado.getStatus().equals(Utils.STATUS_OK)){
                          Toast.makeText(ClienteActivity.this,resultado.getStatus(),Toast.LENGTH_LONG).show();
                      }
                  }
              });
            }
        });

        lvClientes.setAdapter(adapter);

        //Setando evento que sera disparado ao tocar em um item da lista
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cliente cliente = (Cliente) adapterView.getItemAtPosition(i);
                Intent it = new Intent(ClienteActivity.this, CadastroClienteActivity.class);
                it.putExtra(CadastroClienteActivity.CPF, cliente.getCpf());

                startActivityForResult(it, CadastroClienteActivity.RESULTADO);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteActivity.this, CadastroClienteActivity.class);
                startActivityForResult(intent,CadastroClienteActivity.RESULTADO);
            }
        });

        handleIntent(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clienteViewModel.pesquisarClientes("");
        fecharPesquisa();
    }

    private void fecharPesquisa(){
        if (!searchView.isIconified()){
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
            clienteViewModel.pesquisarClientes(query);
        }
    }

    private void showProgress(boolean show){
        pbProgressoPesquisa.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar tb = (Toolbar) findViewById(id.toolbar);
        tb.inflateMenu(R.menu.cliente_list_menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) tb.getMenu().findItem(id.menu_edit_pesquisar).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showProgress(true);
                clienteViewModel.pesquisarClientes(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    showProgress(true);
                    clienteViewModel.pesquisarClientes("");
                    return true;
                }else{
                    return false;
                }
            }
        });

        return true;
    }


}
