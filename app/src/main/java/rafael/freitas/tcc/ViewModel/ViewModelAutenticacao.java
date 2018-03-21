package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.ViewModel;

import rafael.freitas.tcc.Model.AutenticacaoDao;
import rafael.freitas.tcc.Model.BasicoDao;
import rafael.freitas.tcc.Model.BasicoDaoCrud;
import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.Model.UsuarioDao;

public class ViewModelAutenticacao extends ViewModelBasico<Usuario> {

    public void autenticar(String email, String senha, CallbackModel<StatusRetorno> callback){

        getDao().autenticar(email, senha, callback);
    }

    @Override
    protected BasicoDao<Usuario> instanciarDao() {
        return new AutenticacaoDao();
    }

    @Override
    public AutenticacaoDao getDao(){
        return ((AutenticacaoDao)super.getDao());
    }
}
