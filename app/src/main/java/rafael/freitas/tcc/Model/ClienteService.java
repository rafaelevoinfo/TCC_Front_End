package rafael.freitas.tcc.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClienteService {

    @GET("clientes/{cpf_nome}")
    Call<List<Cliente>> pesquisarClientes(@Path("cpf_nome") String cpfOuNome);

    @GET("clientes")
    Call<List<Cliente>> pesquisarClientes();

    @GET("auth")
    Call<StatusRetorno> autenticar(@Header("Authorization") String authorization);


}
