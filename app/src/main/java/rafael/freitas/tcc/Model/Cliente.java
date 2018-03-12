package rafael.freitas.tcc.Model;

import android.arch.lifecycle.LiveData;
import android.util.Base64;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import rafael.freitas.tcc.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cliente extends Modelo implements Serializable{
    private String cpf;
    private String nome;
    private String endereco;
    private String estado;
    private String municipio;
    private String telefone;
    private String email;
    private String senha;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String toString(){
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return this.cpf.equals(((Cliente)obj).getCpf());
    }

    public void autenticar(String email, String senha, final CallbackModel callback){
        String vaSenhaCripto = Utils.md5(senha);
        String vaCredenciais = email+":"+vaSenhaCripto;
        String vaAuth = "Basic " + Base64.encodeToString(vaCredenciais.getBytes(), Base64.NO_WRAP);

        Call<StatusRetorno> vaCall = new RetrofitConfig().getClienteService().autenticar(vaAuth);
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

    public void pesquisarClientes(String cpfOuNome, final CallbackModels<List<Cliente>> callback){
        Call<List<Cliente>> vaCall = null;
        if (cpfOuNome.trim().isEmpty()){
            vaCall = new RetrofitConfig().getClienteService().pesquisarClientes();
        }else {
            vaCall = new RetrofitConfig().getClienteService().pesquisarClientes(cpfOuNome);
        }
        vaCall.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                callback.execute(new StatusRetorno(Utils.STATUS_OK), response.body());
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                callback.execute(new StatusRetorno(t.getMessage()),null);
            }
        });
    }


    public static Cliente carregarCliente(final String cpf){
        return null;
       /*final Cliente[] vaCliente = {null};
       Call<Cliente> vaCall = new RetrofitConfig().getClienteService().buscarClienteCPF(cpf);
        vaCall.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful()) {
                    vaCliente[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                // tratar algum erro
            }
        });
*/
    }
}
