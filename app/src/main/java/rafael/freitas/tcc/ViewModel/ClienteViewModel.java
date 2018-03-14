package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.CallbackModels;
import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.Model.ClienteDao;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Utils.Utils;

public class ClienteViewModel extends ViewModel {
    private MutableLiveData<List<Cliente>> clientes;
    private MutableLiveData<Cliente> cliente;
    private ClienteDao dao;

    public ClienteViewModel(){
        dao = new ClienteDao();
    }

    public MutableLiveData<Cliente> getCliente(){
        if (cliente == null){
            cliente = new MutableLiveData<Cliente>();
        }
        return  cliente;
    }

    public MutableLiveData<List<Cliente>> pesquisarClientes(String cpfOuNome) {
        if (clientes == null){
            clientes = new MutableLiveData<List<Cliente>>();
        }

        dao.pesquisarClientes(cpfOuNome, new CallbackModels<List<Cliente>>() {
            @Override
            public void execute(StatusRetorno status, List<Cliente> resultados) {
                if (resultados != null){
                    clientes.setValue(resultados);
                }else{
                    clientes.setValue(new ArrayList<Cliente>());
                }
            }
        });

        return clientes;
    }

    public void pesquisarClienteCPF(String cpf){
        dao.carregarCliente(cpf, new CallbackModel<Cliente>() {
            @Override
            public void execute(Cliente resultado) {
                ClienteViewModel.this.getCliente().setValue(resultado);
            }
        });
    }

    public void salvar(Cliente cliente, CallbackModel<StatusRetorno> callback){
        if (!Utils.validarEmail(cliente.getEmail())){
            callback.execute(new StatusRetorno("E-Mail inválido"));
        }else if (!Utils.validarCPF(cliente.getCpf())){
            callback.execute(new StatusRetorno("CPF inválido"));
        }
        //Vamos criptografar a senha para enviala ao webservice
        cliente.setSenha(Utils.md5(cliente.getSenha()));
        dao.salvar(cliente,callback);
    }

    public void excluir(Cliente cliente, CallbackModel<StatusRetorno> callback){
        dao.excluir(cliente, callback);
    }
}
