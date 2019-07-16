package com.dynasys.appdisoft.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ProgressDialog progresdialog;

    private TextView Servicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCodigo=(EditText) findViewById(R.id.view_login_codigo);
        mNroDocumento=(EditText)findViewById(R.id.view_login_nrodoc);
        btnIngresar=(Button)findViewById(R.id.view_btn_ingresar);
        textCodigo=(TextInputLayout)findViewById(R.id.view_texti_codigo);
        textNroDocumento=(TextInputLayout)findViewById(R.id.view_texti_nrodocumento);
        Servicio=(TextView)findViewById(R.id.id_login_lbl_Service);
        mCodigo.addTextChangedListener(new TextWatcherLabel(textCodigo));
        mNroDocumento.addTextChangedListener(new TextWatcherLabel(textNroDocumento));
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               mLoginPresenter.ValidarLogin(mCodigo.getText().toString(),mNroDocumento.getText().toString());
            }
        });
        ShowDialogSincronizando();
        new LoginPresenter(this,getApplicationContext());
        Servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambiarActividad();
            }
        });
    }
public void CambiarActividad(){
    startActivity(new Intent(this, WebServicesActivity.class));
}
    @Override
    public void showEmailError() {
        mCodigo.setError("Codigo Invalido");
        mCodigo.requestFocus();

    }

    @Override
    public void showDialogs() {
        progresdialog.show();
    }

    @Override
    public void showPasswordError() {
        mNroDocumento.setError("Numero de Documento Invalido");
        mNroDocumento.requestFocus();

    }

    @Override
    public void LoginSuccesfull() {
if (progresdialog.isShowing()){
    progresdialog.dismiss();
}
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
        if (progresdialog.isShowing()){
            progresdialog.dismiss();
        }
        Snackbar snackbar= Snackbar.make(mNroDocumento, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_iinfo,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
    private void ShowDialogSincronizando(){
        progresdialog=new ProgressDialog(this);
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Consultando Datos .....");

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
