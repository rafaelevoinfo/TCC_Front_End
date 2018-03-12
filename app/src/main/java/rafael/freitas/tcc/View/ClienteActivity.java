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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.ViewModel.ClienteViewModel;

import static rafael.freitas.tcc.R.id;
import static rafael.freitas.tcc.R.layout;

public class ClienteActivity extends AppCompatActivity {
    private ClienteViewModel clienteViewModel;
    private ListView lvClientes;
    private ArrayAdapter<Cliente> adapter;
    private SearchView searchView;
    private List<Cliente> clientes;
    private ProgressBar pbProgressoPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_cliente);

        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        pbProgressoPesquisa = (ProgressBar) findViewById(id.pbProgressoPesquisa);

        lvClientes = (ListView) findViewById(id.lvClientes);

        clientes = new ArrayList<>();

        adapter = new ArrayAdapter<Cliente>(getBaseContext(), android.R.layout.simple_list_item_1, clientes);
        lvClientes.setAdapter(adapter);
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cliente cliente = (Cliente) adapterView.getItemAtPosition(i);
                Intent it = new Intent(ClienteActivity.this, CadastroClienteActivity.class);
                it.putExtra(CadastroClienteActivity.OBJETO, cliente);

                startActivity(it);
            }
        });

        clienteViewModel = ViewModelProviders.of(this).get(ClienteViewModel.class);
        MutableLiveData<List<Cliente>> vaClientes = clienteViewModel.pesquisarClientes("");

        final Observer<List<Cliente>> vaObserver = new Observer<List<Cliente>>() {
            @Override
            public void onChanged(@Nullable List<Cliente> clientes) {
                showProgress(true);
                ClienteActivity.this.clientes.clear();
                ClienteActivity.this.clientes.addAll(clientes);
                adapter.notifyDataSetChanged();
            }
        };

        vaClientes.observe(this, vaObserver);

        FloatingActionButton fab = (FloatingActionButton) findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteActivity.this, CadastroClienteActivity.class);
                startActivity(intent);
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
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
        /*tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.menu_pesquisar) {
                            ClienteActivity.this.onSearchRequested();
                        }
                        return true;
                    }
                });
*/
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
