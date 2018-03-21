package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.ViewModel;

import rafael.freitas.tcc.Model.BasicoDao;
import rafael.freitas.tcc.Model.BasicoDaoCrud;
import rafael.freitas.tcc.Model.StatusRetorno;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class ViewModelBasico<T> extends ViewModel {
    private BasicoDao<T> dao;

    public ViewModelBasico() {
        dao = instanciarDao();
    }

    public BasicoDao<T> getDao(){
        return dao;
    }

    protected abstract BasicoDao<T> instanciarDao();
}
