package rafael.freitas.tcc.Model;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by rafae on 11/03/2018.
 */

public interface CallbackModels<T,E> {
    public void execute(T status, E resultados);
}
