package rafael.freitas.tcc.Model;

import java.util.List;

/**
 * Created by rafae on 19/03/2018.
 */

public abstract class BasicoDaoCrud<T> extends BasicoDao<T> {
    public abstract void salvar(T obj, final CallbackModel<StatusRetorno> callback);
    public abstract void excluir(T obj, final CallbackModel<StatusRetorno> callback);
    public abstract void buscar(String filtro, final CallbackModels<StatusRetorno, List<T>> callback);
    public abstract void buscarPorPk(String pk, final CallbackModel<T> callback);

}
