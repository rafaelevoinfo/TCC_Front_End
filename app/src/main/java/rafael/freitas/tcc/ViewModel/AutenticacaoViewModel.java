package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Cliente;
import rafael.freitas.tcc.Utils.Utils;

public class AutenticacaoViewModel extends ViewModel {

    public void autenticar(String email, String senha, CallbackModel callback){
        Cliente vaCliente = new Cliente();
        vaCliente.autenticar(email, senha,callback);
    }

}
