package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.CallbackModels;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.Model.UsuarioDao;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Utils.Utils;

public class UsuarioViewModel extends ViewModel {
    private MutableLiveData<List<Usuario>> clientes;
    private MutableLiveData<Usuario> cliente;
    private UsuarioDao dao;

    public UsuarioViewModel(){
        dao = new UsuarioDao();
    }

    public MutableLiveData<Usuario> getCliente(){
        if (cliente == null){
            cliente = new MutableLiveData<Usuario>();
        }
        return  cliente;
    }

    public MutableLiveData<List<Usuario>> pesquisarClientes(String cpfOuNome) {
        if (clientes == null){
            clientes = new MutableLiveData<List<Usuario>>();
        }

        dao.pesquisarClientes(cpfOuNome, new CallbackModels<List<Usuario>>() {
            @Override
            public void execute(StatusRetorno status, List<Usuario> resultados) {
                if (resultados != null){
                    clientes.setValue(resultados);
                }else{
                    clientes.setValue(new ArrayList<Usuario>());
                }
            }
        });

        return clientes;
    }

    public void pesquisarClienteCPF(String cpf){
        dao.carregarCliente(cpf, new CallbackModel<Usuario>() {
            @Override
            public void execute(Usuario resultado) {
                UsuarioViewModel.this.getCliente().setValue(resultado);
            }
        });
    }

    public void salvar(Usuario usuario, CallbackModel<StatusRetorno> callback){
        if (!Utils.validarEmail(usuario.getEmail())){
            callback.execute(new StatusRetorno("E-Mail inválido"));
            return;
        }else if (!Utils.validarCPF(usuario.getCpf())){
            callback.execute(new StatusRetorno("CPF inválido"));
            return;
        }
        //Vamos criptografar a senha para enviala ao webservice
        usuario.setSenha(Utils.md5(usuario.getSenha()));
        dao.salvar(usuario,callback);
    }

    public void excluir(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        dao.excluir(usuario, callback);
    }
}
