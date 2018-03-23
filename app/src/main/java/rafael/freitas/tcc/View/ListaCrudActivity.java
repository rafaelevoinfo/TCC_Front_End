package rafael.freitas.tcc.View;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.ViewModelCrud;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class ListaCrudActivity<T> extends BasicaActivity<T> {
    protected ProgressBar pbProgressoPesquisa;
    protected SearchView searchView;
    protected ArrayAdapter<T> adapter;

    protected void showProgress(boolean show) {
        pbProgressoPesquisa.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected abstract void criarAdapter();
    protected abstract void addObservers();
    protected abstract void addListeners();

    protected abstract void alterar(T obj);
    protected abstract void incluir();
    protected void excluir(final T obj){
        showProgress(true);
        getViewModel().excluir(obj, new CallbackModel<StatusRetorno>() {
            @Override
            public void execute(StatusRetorno resultado) {
                showProgress(false);
                if (!resultado.getStatus().equals(Utils.STATUS_OK)) {
                    Toast.makeText(ListaCrudActivity.this, resultado.getStatus(), Toast.LENGTH_LONG).show();
                }else{
                    ListaCrudActivity.this.adapter.remove(obj);
                    ListaCrudActivity.this.adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    protected void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            pesquisar(query);
        }
    }

    protected void pesquisar(String filtro){
        showProgress(true);
        //Realizando a pesquisa pelo filtro especificado pelo usuario
        getViewModel().buscar(filtro);
    }

    protected void fecharPesquisa() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
    }

    @Override
    public ViewModelCrud<T> getViewModel(){
        return (ViewModelCrud<T>) super.getViewModel();
    }


}
