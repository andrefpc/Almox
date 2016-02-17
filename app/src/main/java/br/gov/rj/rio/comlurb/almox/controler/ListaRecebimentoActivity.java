package br.gov.rj.rio.comlurb.almox.controler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.gov.rj.rio.comlurb.almox.R;
import br.gov.rj.rio.comlurb.almox.model.Requisicao;
import br.gov.rj.rio.comlurb.almox.model.Usuario;
import br.gov.rj.rio.comlurb.almox.service.ServicoRestFul;
import br.gov.rj.rio.comlurb.almox.util.RestClient;

public class ListaRecebimentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RestClient.OnPostExecuteListener {

    private final Context context = ListaRecebimentoActivity.this;
    private final RestClient.OnPostExecuteListener onPost = ListaRecebimentoActivity.this;
    private String flagOnPost = "";
    private LinearLayout listaRequisicoesView;
    private LayoutInflater inflater;
    private String jsonRequisicoes;
    private String idRequisicao;
    private int pagina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recebimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        jsonRequisicoes = intent.getStringExtra("jsonRequisicoes");
        pagina = 1;

        criaListaRequisicoes(jsonRequisicoes);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button carregarMais = (Button) findViewById(R.id.btnCarregarMais);
        carregarMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagina = 2;

                Usuario usuario = new Usuario();
                ServicoRestFul.consultaRecebimentoRestful(usuario, context, onPost, String.valueOf(pagina), "10");
                flagOnPost = "receber";
            }
        });
    }

    private void criaListaRequisicoes(String jsonRequisicoes) {

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Requisicao>>() {
            }.getType();
            List<Requisicao> requisicoesList = gson.fromJson(jsonRequisicoes, listType);

            listaRequisicoesView = (LinearLayout) findViewById(R.id.lista_requisicoes_container);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            populaView(requisicoesList);

        }catch (Exception e){
            Toast.makeText(context, jsonRequisicoes, Toast.LENGTH_LONG).show();
        }
    }

    private void populaView(List<Requisicao> requisicoesList) {
        for (Requisicao requisicao: requisicoesList) {

            View itemLista = inflater.inflate(R.layout.template_item_lista_requisicoes, null);

            TextView codigoTextView = (TextView) itemLista.findViewById(R.id.txtCodigoValue);
            TextView urTextView = (TextView) itemLista.findViewById(R.id.txtURValue);
            TextView almoxarifadoTextView = (TextView) itemLista.findViewById(R.id.txtAlmoxarifadoValue);
            TextView dataRequisicaoTextView = (TextView) itemLista.findViewById(R.id.txtDataRequisicaoValue);

            codigoTextView.setText(requisicao.getCod());
            urTextView.setText(requisicao.getUnidadeRequisitante());
            almoxarifadoTextView.setText(requisicao.getAlmoxarifado());
            dataRequisicaoTextView.setText(requisicao.getDtRequisicao().getDate());

            itemLista.setTag(requisicao.getId());
            itemLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View item) {
                    //Toast.makeText(context, "Cliquei no item de id = "+ item.getTag().toString() , Toast.LENGTH_LONG).show();

                    flagOnPost = "materiais";
                    Usuario usuario = new Usuario();
                    idRequisicao = item.getTag().toString();
                    ServicoRestFul.materialRequisicao(usuario, context, onPost, idRequisicao);
                }
            });

            listaRequisicoesView.addView(itemLista);
        }
    }

    @Override
    public void onPostExecute(String result) {
        if(flagOnPost == "validar"){
            Intent intent = new Intent(context, ListaValidacaoActivity.class);
            intent.putExtra("jsonRequisicoes", result);
            startActivity(intent);
        }else if(flagOnPost == "receber"){
            criaListaRequisicoes(result);
            Toast.makeText(context, "Mais 10 requisições foram carregadas!", Toast.LENGTH_LONG).show();
        }else if(flagOnPost == "materiais"){
            Intent intent = new Intent(context, ListaMateriaisRecebimentoActivity.class);
            intent.putExtra("jsonMateriais", result);
            intent.putExtra("idRequisicao", idRequisicao);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_validar) {
            Usuario usuario = new Usuario();
            ServicoRestFul.consultaValidacaoRestful(usuario, this, onPost);
            flagOnPost = "validar";
        }else if (id == R.id.nav_receber) {
            Toast.makeText(context, getString(R.string.estaNaTela), Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
