package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.CallbackModels;
import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.Model.StatusRetorno;

public class ClienteViewModel extends ViewModel {
    private MutableLiveData<List<Cliente>> clientes;

    public MutableLiveData<List<Cliente>> pesquisarClientes(String cpfOuNome) {
        if (clientes == null){
            clientes = new MutableLiveData<List<Cliente>>();
        }

        Cliente vaCliente = new Cliente();
        vaCliente.pesquisarClientes(cpfOuNome, new CallbackModels<List<Cliente>>() {
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
    
   /* public LiveData<List<Cliente>> pesquisarClientes(String cpfOuNome, final CallbackModels callback) {
        Cliente vaCliente = new Cliente();
        vaCliente.pesquisarClientes(cpfOuNome, new CallbackModels() {
            @Override
            public void execute(StatusRetorno status, LiveData resultados) {
                if (resultados != null) {
                    clientes = resultados;
                }
                callback.execute(status,resultados);
            }
        });

        return clientes;
    }*/
}
