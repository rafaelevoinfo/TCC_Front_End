package rafael.freitas.tcc.Model;

import android.support.annotation.NonNull;
import android.util.Base64;

import rafael.freitas.tcc.Utils.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rafae on 20/03/2018.
 */

public class AutenticacaoDao extends BasicoDao<Usuario> {
    public void autenticar(String email, String senha, final CallbackModel<StatusRetorno> callback) {
        String vaAuth = montarAutenticacaoBasica(email, senha);

        Call<StatusRetorno> vaCall = new RetrofitConfig().getUsuarioService().autenticar(vaAuth);
        vaCall.enqueue(new retrofit2.Callback<StatusRetorno>() {
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

    //O webservice faz autenticacao via Basic Authentication, portanto monto ela aqui
    @NonNull
    private String montarAutenticacaoBasica(String email, String senha) {
        String vaSenhaCripto = Utils.md5(senha);
        String vaCredenciais = email + ":" + vaSenhaCripto;
        return "Basic " + Base64.encodeToString(vaCredenciais.getBytes(), Base64.NO_WRAP);
    }
}
