package com.dynasys.appdisoft.Login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.R;
import com.google.common.base.Preconditions;


public class LoginActivity extends AppCompatActivity implements LoginMvp.View {

    private EditText mCodigo;
    private EditText mNroDocumento;
    private TextInputLayout textCodigo;
    private TextInputLayout textNroDocumento;
    private Button btnIngresar;
    private LoginMvp.Presenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCodigo=(EditText) findViewById(R.id.view_login_codigo);
        mNroDocumento=(EditText)findViewById(R.id.view_login_nrodoc);
        btnIngresar=(Button)findViewById(R.id.view_btn_ingresar);
        textCodigo=(TextInputLayout)findViewById(R.id.view_texti_codigo);
        textNroDocumento=(TextInputLayout)findViewById(R.id.view_texti_nrodocumento);

        mCodigo.addTextChangedListener(new TextWatcherLabel(textCodigo));
        mNroDocumento.addTextChangedListener(new TextWatcherLabel(textNroDocumento));
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mLoginPresenter.ValidarLogin(mCodigo.getText().toString(),mNroDocumento.getText().toString());
            }
        });
        new LoginPresenter(this,getApplicationContext());

    }

    @Override
    public void showEmailError() {
        mCodigo.setError("Codigo Invalido");
        mCodigo.requestFocus();

    }

    @Override
    public void showPasswordError() {
        mNroDocumento.setError("Numero de Documento Invalido");
        mNroDocumento.requestFocus();

    }

    @Override
    public void LoginSuccesfull() {

        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void setPresenter(LoginMvp.Presenter presenter) {
        mLoginPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ShowMessageResult(String message) {
        Snackbar snackbar= Snackbar.make(mNroDocumento, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    class TextWatcherLabel implements TextWatcher {

        private final TextInputLayout mFloatingLabel;

        public TextWatcherLabel(TextInputLayout floatingLabel){
            mFloatingLabel = floatingLabel;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mFloatingLabel.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    }
}
