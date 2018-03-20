package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import rafael.freitas.tcc.Model.BasicoDaoCrud;
import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;

/**
 * Created by rafae on 19/03/2018.
 */

public abstract class ViewModelCrud<T> extends ViewModelBasico {

    public abstract MutableLiveData<List<T>> buscar(String filtro);

    public abstract void salvar(T usuario, CallbackModel<StatusRetorno> callback);

    public abstract void excluir(T usuario, final CallbackModel<StatusRetorno> callback);
}
