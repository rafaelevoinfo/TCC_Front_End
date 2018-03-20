package rafael.freitas.tcc.Model;

import java.util.List;

/**
 * Created by rafae on 19/03/2018.
 */

public abstract class BasicoDaoCrud<T,E> {
    public abstract void salvar(T obj, final CallbackModel<E> callback);
    public abstract void excluir(T obj, final CallbackModel<E> callback);
    public abstract void buscar(String filtro, final CallbackModels<E, List<T>> callback);

}
