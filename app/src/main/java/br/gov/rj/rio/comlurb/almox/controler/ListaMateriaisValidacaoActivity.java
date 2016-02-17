package br.gov.rj.rio.comlurb.almox.controler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.gov.rj.rio.comlurb.almox.R;
import br.gov.rj.rio.comlurb.almox.model.MaterialRequisicao;
import br.gov.rj.rio.comlurb.almox.model.Usuario;
import br.gov.rj.rio.comlurb.almox.service.ServicoRestFul;
import br.gov.rj.rio.comlurb.almox.util.RestClient;

public class ListaMateriaisValidacaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RestClient.OnPostExecuteListener {

    private final Context context = ListaMateriaisValidacaoActivity.this;
    private final RestClient.OnPostExecuteListener onPost = ListaMateriaisValidacaoActivity.this;

    private LinearLayout listMateriaisView;
    private LayoutInflater inflater;
    private List<MaterialRequisicao> materiaisList;
    private String jsonMateriais;
    private String idRequisicao;
    private String flagOnPost = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materiais_validacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        jsonMateriais = intent.getStringExtra("jsonMateriais");
        idRequisicao = intent.getStringExtra("idRequisicao");

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<MaterialRequisicao>>() {
            }.getType();
            List<MaterialRequisicao> materiaisList = gson.fromJson(jsonMateriais, listType);


            criaListaRequisicoes(materiaisList);
        }catch (Exception e){
            Toast.makeText(context, jsonMateriais, Toast.LENGTH_LONG).show();
        }
    }

    private void criaListaRequisicoes(List<MaterialRequisicao> materiaisList) {

        listMateriaisView = (LinearLayout) findViewById(R.id.lista_materiais_container);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (listMateriaisView.getChildCount() > 0) {
            listMateriaisView.removeViews(0, listMateriaisView.getChildCount());
        }

        populaView(materiaisList);
    }

    private void populaView(List<MaterialRequisicao> materiaisList) {
        for (MaterialRequisicao materialRequisicao: materiaisList) {

            View itemLista = inflater.inflate(R.layout.template_item_lista_materiais_validacao, null);

            TextView materialTextView = (TextView) itemLista.findViewById(R.id.txtMaterialValue);
            TextView qtdTextView = (TextView) itemLista.findViewById(R.id.txtQtdValue);

            materialTextView.setText(materialRequisicao.getMaterial());
            qtdTextView.setText(materialRequisicao.getQtd());

            itemLista.setTag(materialRequisicao);

            itemLista.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View item) {
                    MaterialRequisicao material = (MaterialRequisicao) item.getTag();
                    showQtdDialog(material);
                    //Toast.makeText(context, "Cliquei no item de id = " + item.getTag().toString(), Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            listMateriaisView.addView(itemLista);
        }
    }

    protected void showQtdDialog(final MaterialRequisicao material) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final TextView qtdTitle = (TextView) promptView.findViewById(R.id.qtdTitle);
        final EditText qtdEditText = (EditText) promptView.findViewById(R.id.qtdEditText);

        qtdTitle.setText("Entre com a nova quantidade para o material: \n\n\"" + material.getMaterial()+"\"");
        qtdEditText.setHint("Quantidade atual: " + material.getQtd());

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (qtdEditText.getText().toString().equals("")) {
                            Toast.makeText(context, "Campo quantidade não pode estar vazio!", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(context, "id = " + material.getId() + " | qtd = " + qtdEditText.getText(), Toast.LENGTH_LONG).show();
                            String idRequisicaoItem = String.valueOf(material.getId());
                            String qtd = qtdEditText.getText().toString();
                            Usuario usuario = new Usuario();
                            ServicoRestFul.editarQtdMaterial(usuario, context, onPost, idRequisicaoItem, qtd);
                            flagOnPost = "editar";
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onPostExecute(String result) {
        if(flagOnPost.equals("editar")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            Usuario usuario = new Usuario();
            ServicoRestFul.materialRequisicao(usuario, context, onPost, idRequisicao);
            flagOnPost = "popular";
        }else if(flagOnPost == "validar"){
            Intent intent = new Intent(context, ListaValidacaoActivity.class);
            intent.putExtra("jsonRequisicoes", result);
            startActivity(intent);
        }else if(flagOnPost == "receber"){
            Intent intent = new Intent(context, ListaRecebimentoActivity.class);
            intent.putExtra("jsonRequisicoes", result);
            startActivity(intent);
        }else{
            Intent intent = new Intent(context, ListaMateriaisValidacaoActivity.class);
            intent.putExtra("jsonMateriais", result);
            intent.putExtra("idRequisicao", idRequisicao);
            startActivity(intent);
            finish();
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
            Usuario usuario = new Usuario();
            ServicoRestFul.consultaRecebimentoRestful(usuario, this, onPost, "1", "10");
            flagOnPost = "receber";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
