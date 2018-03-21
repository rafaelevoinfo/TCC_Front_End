package rafael.freitas.tcc.View;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rafael.freitas.tcc.ViewModel.ViewModelBasico;

/**
 * Created by rafae on 20/03/2018.
 */

public abstract class BasicaActivity<T> extends AppCompatActivity {
    private ViewModelBasico<T> viewModel;

    protected abstract ViewModelBasico<T> instanciarViewModel();

    public ViewModelBasico<T> getViewModel() {
        if (viewModel == null) {
            viewModel = instanciarViewModel();
        }
        return viewModel;
    }
}
