package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.ViewModel;

import rafael.freitas.tcc.Model.BasicoDaoCrud;
import rafael.freitas.tcc.Model.StatusRetorno;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class ViewModelBasico<T> extends ViewModel {
    protected BasicoDaoCrud<T, StatusRetorno> dao;

    public ViewModelBasico() {
        dao = instanciarDao();
    }

    protected abstract BasicoDaoCrud<T, StatusRetorno> instanciarDao();
}
