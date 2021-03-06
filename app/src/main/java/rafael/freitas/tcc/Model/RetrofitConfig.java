package rafael.freitas.tcc.Model;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        //Configura o endereco do webservice
        this.retrofit = new Retrofit.Builder()
                //.baseUrl("http://10.0.2.2/tcc/index/") //Debug
                .baseUrl("http://rafaelevoinfodeveloper.esy.es")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public UsuarioService getUsuarioService() {
        return this.retrofit.create(UsuarioService.class);
    }

}
