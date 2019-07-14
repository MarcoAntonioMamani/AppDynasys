package com.dynasys.appdisoft;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dynasys.appdisoft.Clientes.ListClientesFragment;
import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.Login.LoginActivity;
import com.dynasys.appdisoft.Login.UsersListViewModel;
import com.dynasys.appdisoft.ShareUtil.LocationGeo;
import com.dynasys.appdisoft.SincronizarData.SincronizarFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Handler switchHandler = new Handler();
    private Context mContext;
    private NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;
    private Boolean bolBienvenida =true;
    private UsersListViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(UsersListViewModel.class);
        if(DataPreferences.getPrefLogin("isLogin",getApplicationContext())==null  ){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        if(DataPreferences.getPrefLogin("isLogin",getApplicationContext())==false  ){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        LocationGeo.getInstance(getApplicationContext(),this);
        LocationGeo.PedirPermisoApp();
        toolbar = (Toolbar) findViewById(R.id.id_main_toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.id_navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);
        //First start (Inbox Fragment)
       // setFragment(0);
        setupUserBox();
    }
    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               // String roles  = UserPrefs.getInstance(getApplicationContext()).getRoles();

                switch (item.getItemId()) {
                    case R.id.item_navigation_drawer_sincronizar:
                        item.setChecked(true);
                        setFragment(1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_navigation_drawer_clientes:

                        item.setChecked(true);
                        setFragment(2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_navigation_drawer_pedidos:

                        item.setChecked(true);
                        //setFragment(2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_navigation_drawer_help_and_feedback:
                        item.setChecked(true);
                        setFragment(21);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return true;
            }
        });
    }
    public void setFragment(int position){
        Fragment frag = null;
        String tag = "";
        boolean switchFrag = true;
        switch (position){
            case 0:
                loadWelcomeFragment();
                break;
            case 1:
                returnToMain();
                frag = new SincronizarFragment();
                tag = Constantes.TAG_SINCRONIZACION;
                break;
            case 2:
                returnToMain();
                frag = new ListClientesFragment();
                tag = Constantes.TAG_CLIENTES;
                break;
            case 21:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder
                        .setTitle("Disoft")
                        .setMessage("Está seguro(a) de finalizar la sesión?.")
                        .setIcon(R.drawable.ic_iinfo)
                        .setPositiveButton(mContext.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               DataPreferences.putPrefLogin("isLogin",false,getApplicationContext());
                              MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        finish();
                                    }
                            });
                            }
                        })
                        .setNegativeButton(mContext.getResources().getString(R.string.cancel), null)
                        .show();
                break;
        }
        if (switchFrag) {
            switchFragment(frag,tag);
            //addFragment(new WelcomeFragment(),frag,tag);
        }
    }
    public void switchFragment(final Fragment frag, final String tag){
        switchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    if (tag!=Constantes.TAG_CLIENTES){
                        ft.setCustomAnimations(R.transition.left_in,R.transition.left_out);
                    }
                    ft.addToBackStack(tag)
                            .replace(R.id.fragment, frag,tag)
                            .commit();
                }catch(Exception e){}
            }
        }, 100);
    }

    private void setupUserBox() {
        // TODO: Poblar más views, agregar más acciones

        // Poner email
        TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.id_nh_user);
        TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.id_nh_email);
       /* String userName = UserPrefs.getInstance(mContext).getUserName();
        String userEmail = UserPrefs.getInstance(mContext).getUserEmail();*/
       String nameRepartidor=DataPreferences.getPref("repartidor",getApplicationContext());
        userNameView.setText("Repartidor: ");
        userEmailView.setText(nameRepartidor);

        MenuItem menulcv = navigationView.getMenu().findItem(R.id.item_navigation_drawer_sincronizar);
        MenuItem menucli = navigationView.getMenu().findItem(R.id.item_navigation_drawer_clientes);
        MenuItem menuped = navigationView.getMenu().findItem(R.id.item_navigation_drawer_pedidos);

            menulcv.setVisible(true);
            menucli.setVisible(true);
            menuped.setVisible(true);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (getSupportFragmentManager().getBackStackEntryCount() > 1){
                    //getSupportFragmentManager().popBackStack("WELCOMEFRAGMENT", 0);
                    getSupportFragmentManager().popBackStack();
                } else {
                    if (getSupportFragmentManager().getFragments().size() >1){
                        removeAllFragments();
                        loadWelcomeFragment();
                        return true;
                    }
                    if (doubleBackToExitPressedOnce){
                        super.onBackPressed();
                        finish();
                        return true;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(mContext, "Click nuevamente para salir.", Toast.LENGTH_SHORT).show();
                    //Please click BACK again to exit
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 2000);
                    return true;
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.byte_code, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadWelcomeFragment(){
        /*switchHandler.post(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("WELCOMEFRAGMENT")
                        .replace(R.id.fragment, new WelcomeFragment())
                        .commit();
            }
        });*/
    }
    public  void returnToMain(){

        //getSupportFragmentManager().popBackStack("WELCOMEFRAGMENT",0);
        removeAllFragments();
        loadWelcomeFragment();
    }
    private void removeAllFragments() {

        if (getSupportFragmentManager().getFragments().size() > 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment activeFragment : getSupportFragmentManager().getFragments()) {
                fragmentTransaction.remove(activeFragment);
            }
            getSupportFragmentManager().getFragments().clear();
            fragmentTransaction.commit();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

}
