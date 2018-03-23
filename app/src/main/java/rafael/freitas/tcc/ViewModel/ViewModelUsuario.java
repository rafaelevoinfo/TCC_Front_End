package rafael.freitas.tcc.ViewModel;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import rafael.freitas.tcc.Model.BasicoDaoCrud;
import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.CallbackModels;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.Model.UsuarioDao;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Utils.Utils;

public class ViewModelUsuario extends ViewModelCrud<Usuario> {
    private MutableLiveData<List<Usuario>> clientes;
    private MutableLiveData<Usuario> cliente;

    @Override
    protected BasicoDaoCrud<Usuario> instanciarDao() {
        return new UsuarioDao();
    }

    @Override
    public UsuarioDao getDao(){
        return ((UsuarioDao) super.getDao());
    }

    public MutableLiveData<Usuario> getCliente(){
        if (cliente == null){
            cliente = new MutableLiveData<Usuario>();
        }
        return  cliente;
    }

    @Override
    public MutableLiveData<List<Usuario>> buscar(String cpfOuNome) {
        if (clientes == null){
            clientes = new MutableLiveData<List<Usuario>>();
        }

        getDao().buscar(cpfOuNome, new CallbackModels<StatusRetorno, List<Usuario>>() {
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

    @Override
    public void buscarPorPK(String pk){
        getDao().buscarPorPk(pk, new CallbackModel<Usuario>() {
            @Override
            public void execute(Usuario resultado) {
                ViewModelUsuario.this.getCliente().setValue(resultado);
            }
        });
    }

    @Override
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
        getDao().salvar(usuario,callback);
    }

    @Override
    public void excluir(Usuario usuario, final CallbackModel<StatusRetorno> callback){
        getDao().excluir(usuario, callback);
    }
}
