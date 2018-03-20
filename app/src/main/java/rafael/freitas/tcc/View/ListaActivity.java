package rafael.freitas.tcc.View;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class ListaActivity<T> extends BasicaActivity<T> {
    protected ProgressBar pbProgressoPesquisa;

    protected void showProgress(boolean show) {
        pbProgressoPesquisa.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected abstract void criarAdapter();
    protected abstract void addObservers();
    protected abstract void addListeners();


}
