package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.ViewModel;
import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Model.UsuarioDao;

public class ViewModelAutenticacao extends ViewModel {

    public void autenticar(String email, String senha, CallbackModel<StatusRetorno> callback){
        UsuarioDao vaDaoCliente = new UsuarioDao();
        vaDaoCliente.autenticar(email, senha, callback);
    }

}
