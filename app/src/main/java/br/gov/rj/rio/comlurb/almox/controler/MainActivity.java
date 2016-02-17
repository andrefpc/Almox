package br.gov.rj.rio.comlurb.almox.controler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;

import br.gov.rj.rio.comlurb.almox.R;
import br.gov.rj.rio.comlurb.almox.model.Usuario;
import br.gov.rj.rio.comlurb.almox.service.ServicoRestFul;
import br.gov.rj.rio.comlurb.almox.util.RestClient;

public class MainActivity extends AppCompatActivity implements RestClient.OnPostExecuteListener, NavigationView.OnNavigationItemSelectedListener {


    private final Context context = MainActivity.this;
    private final RestClient.OnPostExecuteListener onPost = MainActivity.this;
    private String totalRequisicoes = "";
    private String flagOnPost = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuario usuario = new Usuario();
        ServicoRestFul.totalRegistrosValidacao(usuario, context, onPost);
        flagOnPost = "total";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Você possui " + totalRequisicoes + " requisições para validar!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
    }


    @Override
    public void onPostExecute(String result) {
        if(flagOnPost == "total"){
            totalRequisicoes = result;
            Toast.makeText(context, "Você possui " + result + " requisições para validar!", Toast.LENGTH_LONG).show();
        }else if(flagOnPost == "validar"){
            Intent intent = new Intent(context, ListaValidacaoActivity.class);
            intent.putExtra("jsonRequisicoes", result);
            startActivity(intent);
        }else if(flagOnPost == "receber"){
            Intent intent = new Intent(context, ListaRecebimentoActivity.class);
            intent.putExtra("jsonRequisicoes", result);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(context, getString(R.string.estaNaTela), Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_validar) {
            Usuario usuario = new Usuario();
            ServicoRestFul.consultaValidacaoRestful(usuario, this, onPost);
            flagOnPost = "validar";
        }else if (id == R.id.nav_receber) {
            Usuario usuario = new Usuario();
            ServicoRestFul.consultaRecebimentoRestful(usuario, this, onPost, "1", "10");
            flagOnPost = "receber";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
