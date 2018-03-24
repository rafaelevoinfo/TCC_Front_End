package rafael.freitas.tcc.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.StatusRetorno;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.R;
import rafael.freitas.tcc.Utils.Utils;
import rafael.freitas.tcc.ViewModel.ViewModelAutenticacao;
import rafael.freitas.tcc.ViewModel.ViewModelBasico;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BasicaActivity<Usuario> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle(R.string.app_bemvindo);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    realizarLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void realizarLogin() {
        //pegandos os valores dos edits
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //Exibe um progress bar enquanto aguarda resposta do servidor
        showProgress(true);
        //chama o model que ira se encarregar de dizer se o usuario e senha sao validos
        getViewModel().autenticar(mEmailView.getText().toString(), mPasswordView.getText().toString(), new CallbackModel<StatusRetorno>() {
            @Override
            public void execute(StatusRetorno resultado) {
                if (resultado != null) {
                    if (resultado.getStatus().equals(Utils.STATUS_OK)) {
                        Intent intent = new Intent(LoginActivity.this, UsuarioActivity.class);
                        startActivity(intent);
                    } else {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this.getApplicationContext(), resultado.getStatus(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    showProgress(false);
                }
            }
        });
    }

    /**
     * Exibe um progress bar.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //Cria a instancia do ViewModel
    @Override
    protected ViewModelBasico<Usuario> instanciarViewModel() {
        return ViewModelProviders.of(this).get(ViewModelAutenticacao.class);
    }

    //retorna a instancia do viewModel
    public ViewModelAutenticacao getViewModel() {
        return (ViewModelAutenticacao) super.getViewModel();
    }
}

