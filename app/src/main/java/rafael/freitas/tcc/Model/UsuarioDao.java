package rafael.freitas.tcc.Model;

import android.util.Base64;

import java.util.List;

import rafael.freitas.tcc.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rafae on 12/03/2018.
 */

public class UsuarioDao {
    public void autenticar(String email, String senha, final CallbackModel callback) {
        String vaSenhaCripto = Utils.md5(senha);
        String vaCredenciais = email + ":" + vaSenhaCripto;
        String vaAuth = "Basic " + Base64.encodeToString(vaCredenciais.getBytes(), Base64.NO_WRAP);

        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().autenticar(vaAuth);
        vaCall.enqueue(new Callback<StatusRetorno>() {
            @Override
            public void onResponse(Call<StatusRetorno> call, Response<StatusRetorno> response) {
                callback.execute(response.body());
            }

            @Override
            public void onFailure(Call<StatusRetorno> call, Throwable t) {
                StatusRetorno vaStatus = new StatusRetorno();
                vaStatus.setStatus(t.getMessage());
                callback.execute(vaStatus);
            }
        });
    }

    public void pesquisarClientes(String cpfOuNome, final CallbackModels<List<Usuario>> callback) {
        Call<List<Usuario>> vaCall = null;
        if (cpfOuNome.trim().isEmpty()) {
            vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios();
        } else {
            vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios(cpfOuNome);
        }
        vaCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                callback.execute(new StatusRetorno(Utils.STATUS_OK), response.body());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.execute(new StatusRetorno(t.getMessage()), null);
            }
        });
    }

    public void carregarCliente(String cpf, final CallbackModel<Usuario> callback) {
        Call<List<Usuario>> vaCall = new RetrofitConfig().getUsuarioService().pesquisarUsuarios(cpf);
        //NAO SE PODE USAR O .EXECUTE PQ NAS VERSOES 4.0 OU MAIOR DO ANDROID DA UMA EXCEPTION DE NetworkOnMainThreadException
        vaCall.enqueue(new Callback<List<Usuario>>() {
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

    public void salvar(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().salvar(usuario);
        vaCall.enqueue(new Callback<StatusRetorno>() {
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

    public void excluir(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().excluir(usuario.getCpf());
        vaCall.enqueue(new Callback<StatusRetorno>() {
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