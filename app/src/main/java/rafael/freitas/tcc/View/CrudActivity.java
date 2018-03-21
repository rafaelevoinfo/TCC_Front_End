package rafael.freitas.tcc.View;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.ViewModelCrud;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class CrudActivity<T> extends BasicaActivity<T> {

    protected ProgressBar pbProgressoSalvar;

    protected abstract void addObservers();
    protected abstract boolean validarDados();

    @Override
    public ViewModelCrud<T> getViewModel(){
        return (ViewModelCrud)super.getViewModel();
    }

    public void salvar(T obj){
        if (validarDados()) {
            showProgress(true);
            getViewModel().salvar(obj, new CallbackModel<StatusRetorno>() {
                @Override
                public void execute(StatusRetorno resultado) {
                    showProgress(false);
                    if (!resultado.getStatus().equals(Utils.STATUS_OK)) {
                        Toast.makeText(CrudActivity.this, resultado.getStatus(), Toast.LENGTH_LONG).show();
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    protected void showProgress(boolean show) {
        pbProgressoSalvar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
