package com.dynasys.appdisoft.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.DescuentosListViewModel;
import com.dynasys.appdisoft.Login.DB.DetalleListViewModel;
import com.dynasys.appdisoft.Login.DB.PedidoListViewModel;
import com.dynasys.appdisoft.Login.DB.PreciosListViewModel;
import com.dynasys.appdisoft.Login.DB.StockListViewModel;
import com.dynasys.appdisoft.Login.DB.ZonaListViewModel;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.MainActivity;
import com.dynasys.appdisoft.R;
import com.dynasys.appdisoft.SincronizarData.DB.ClientesListViewModel;
import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.jetbrains.annotations.NotNull;


public class LoginActivity extends AppCompatActivity implements LoginMvp.View {

    private EditText mCodigo;
    private EditText mNroDocumento;
    private TextInputLayout textCodigo;
    private TextInputLayout textNroDocumento;
    private Button btnIngresar;
    private LoginMvp.Presenter mLoginPresenter;
    private CardView cardViewLogin;
    private TextView Servicio;
    Animation fromBottom;
    Animation fromTop;
    ImageView ivLogo;
    ///////Eliminar bd
    private ClientesListViewModel viewModel;
    private PreciosListViewModel viewModelPrecio;
    private ProductosListViewModel viewModelProducto;
    private PedidoListViewModel viewModelPedidos;
    private DetalleListViewModel viewModelDetalle;
    private StockListViewModel viewModelStock;
    private ZonaListViewModel viewModelZona;
    private DescuentosListViewModel viewModelDescuentos;
    LottieAlertDialog alertDialog;
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
        cardViewLogin=(CardView) findViewById(R.id.id_login_form_container);
        ivLogo=(ImageView) findViewById(R.id.id_login_logo);
        viewModel = ViewModelProviders.of(this).get(ClientesListViewModel.class);
        viewModelPrecio = ViewModelProviders.of(this).get(PreciosListViewModel.class);
        viewModelProducto = ViewModelProviders.of(this).get(ProductosListViewModel.class);
        viewModelPedidos = ViewModelProviders.of(this).get(PedidoListViewModel.class);
        viewModelDetalle = ViewModelProviders.of(this).get(DetalleListViewModel.class);
        viewModelStock = ViewModelProviders.of(this).get(StockListViewModel.class);
        viewModelZona= ViewModelProviders.of(this).get(ZonaListViewModel.class);
        viewModelDescuentos=ViewModelProviders.of(this).get(DescuentosListViewModel.class);
        mCodigo.addTextChangedListener(new TextWatcherLabel(textCodigo));
        mNroDocumento.addTextChangedListener(new TextWatcherLabel(textNroDocumento));
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                showDialogs();
                new ChecarNotificaciones().execute();

            }
        });
        ShowDialogSincronizando();
        new LoginPresenter(this,getApplicationContext(),viewModelZona);
        Servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambiarActividad();
            }
        });

        try{
            viewModelPedidos.deleteAllPedido();
            viewModelDetalle.deleteAllDetalles();
            viewModel.deleteAllClientes();
            viewModelStock.deleteAllStocks();
            viewModelZona.deleteAllZonas();
            viewModelDescuentos.deleteAllDescuentos();
        }catch(Exception e){

        }
        animationLogo();

    }


    public void animationLogo(){
        fromBottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        cardViewLogin.setAnimation(fromBottom);
        fromTop=AnimationUtils.loadAnimation(this,R.anim.fromtop);
        ivLogo.setAnimation(fromTop);

    }
public void CambiarActividad(){
    startActivity(new Intent(this, WebServicesActivity.class));
}
    @Override
    public void showEmailError() {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        mCodigo.setError("Codigo Invalido");
        mCodigo.requestFocus();

    }

    @Override
    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }

    @Override
    public void showPasswordError() {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        mNroDocumento.setError("Numero de Documento Invalido");
        mNroDocumento.requestFocus();

    }

    @Override
    public void LoginSuccesfull() {


        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
    private  void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    @Override
    public void setPresenter(LoginMvp.Presenter presenter) {
        mLoginPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ShowMessageResult(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        alertDialog=new LottieAlertDialog.Builder(this,DialogTypes.TYPE_WARNING)
                .setTitle("Advertencia")
                .setDescription(message)
                .setPositiveText("Aceptar")
                .setPositiveButtonColor(Color.parseColor("#008ebe"))
                .setPositiveTextColor(Color.parseColor("#ffffff"))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                }).build();
        alertDialog.show();
    }
    private void ShowDialogSincronizando(){
        /*progresdialog=new ProgressDialog(this);
        progresdialog.setCancelable(false);
        progresdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresdialog.setIndeterminate(false);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
        progresdialog.setIndeterminateDrawable(drawable);
        progresdialog.setMessage("Consultando Datos .....");*/

        try
        {

            alertDialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING).setTitle("Verificando Usuario")
                    .setDescription("Por Favor Espere ...")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }

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

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mLoginPresenter.ValidarLogin(mCodigo.getText().toString(),mNroDocumento.getText().toString());
                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
