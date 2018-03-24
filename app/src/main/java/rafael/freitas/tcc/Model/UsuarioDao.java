package rafael.freitas.tcc.Model;

import android.util.Base64;

import java.util.List;

import rafael.freitas.tcc.Utils.Utils;
import retrofit2.Call;
import retrofit2.Response;


public class UsuarioDao extends BasicoDaoCrud<Usuario> {

    @Override
    public void buscar(String filtro, final CallbackModels<StatusRetorno, List<Usuario>> callback) {
        Call<List<Usuario>> vaCall = null;
        //Usa o retrofit para pegar o objeto que ira consumir o webservice
        if (filtro.trim().isEmpty()) {
            vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios();
        } else {
            vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios(filtro);
        }
        //Faz a requisicao ao webservice e quando finalizar invoca o metodo onResponse ou onFailure
        vaCall.enqueue(new retrofit2.Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                //faz um callback para quem chamou essa funcao avisando-o da resposta recebida
                callback.execute(new StatusRetorno(Utils.STATUS_OK), response.body());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.execute(new StatusRetorno(t.getMessage()), null);
            }
        });
    }

    @Override
    public void buscarPorPk(String pk, final CallbackModel<Usuario> callback) {
        Call<List<Usuario>> vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios(pk);
        //NAO SE PODE USAR O .EXECUTE PQ NAS VERSOES 4.0 OU MAIOR DO ANDROID DA UMA EXCEPTION DE NetworkOnMainThreadException
        vaCall.enqueue(new retrofit2.Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if ((response.isSuccessful())&&(response.body().size()>0))
                    callback.execute(response.body().get(0));
                else
                    callback.execute(null);
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.execute(null);
            }
        });
    }


    @Override
    public void salvar(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().salvar(usuario);
        vaCall.enqueue(new retrofit2.Callback<StatusRetorno>() {
            @Override
            public void onResponse(Call<StatusRetorno> call, Response<StatusRetorno> response) {
                callback.execute(response.body());
            }

            @Override
            public void onFailure(Call<StatusRetorno> call, Throwable t) {
                callback.execute(new StatusRetorno(t.getMessage()));
            }
        });
    }

    @Override
    public void excluir(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().excluir(usuario.getCpf());
        vaCall.enqueue(new retrofit2.Callback<StatusRetorno>() {
            @Override
            public void onResponse(Call<StatusRetorno> call, Response<StatusRetorno> response) {
                callback.execute(response.body());
            }

            @Override
            public void onFailure(Call<StatusRetorno> call, Throwable t) {
                callback.execute(new StatusRetorno(t.getMessage()));
            }
        });
    }
}
