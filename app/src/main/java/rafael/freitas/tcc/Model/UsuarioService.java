package rafael.freitas.tcc.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioService {

    @GET("usuarios/{cpf_nome}")
    Call<List<Usuario>> pesquisarUsuarios(@Path("cpf_nome") String cpfOuNome);

    @GET("usuarios")
    Call<List<Usuario>> pesquisarUsuarios();

    @GET("auth")
    Call<StatusRetorno> autenticar(@Header("Authorization") String authorization);

    @POST("usuarios")
    Call<StatusRetorno> salvar(@Body Usuario usuario);

    @DELETE("usuarios/{cpf}")
    Call<StatusRetorno> excluir(@Path("cpf")String cpf);

}
