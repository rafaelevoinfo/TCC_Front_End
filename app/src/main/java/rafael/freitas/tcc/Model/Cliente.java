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

}
