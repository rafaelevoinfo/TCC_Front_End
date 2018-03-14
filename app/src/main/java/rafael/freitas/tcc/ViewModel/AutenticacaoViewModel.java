package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.ViewModel;
import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.ClienteDao;

public class AutenticacaoViewModel extends ViewModel {

    public void autenticar(String email, String senha, CallbackModel callback){
        ClienteDao vaDaoCliente = new ClienteDao();
        vaDaoCliente.autenticar(email, senha, callback);
    }

}
