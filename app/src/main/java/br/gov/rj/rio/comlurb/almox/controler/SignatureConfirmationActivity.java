package br.gov.rj.rio.comlurb.almox.controler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.gov.rj.rio.comlurb.almox.R;
import br.gov.rj.rio.comlurb.almox.model.MaterialRequisicao;
import br.gov.rj.rio.comlurb.almox.model.Usuario;
import br.gov.rj.rio.comlurb.almox.service.ServicoRestFul;
import br.gov.rj.rio.comlurb.almox.util.RestClient;

public class SignatureConfirmationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RestClient.OnPostExecuteListener {

    private final Context context = SignatureConfirmationActivity.this;
    private final RestClient.OnPostExecuteListener onPost = SignatureConfirmationActivity.this;
    private String flagOnPost = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        Bitmap signatureImage = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("signatureByteArray"), 0, getIntent().getByteArrayExtra("signatureByteArray").length);

        ImageView signatureImageView = (ImageView) findViewById(R.id.imageSignature);
        signatureImageView.setImageBitmap(signatureImage);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button newSignature = (Button) findViewById(R.id.btnNewSignature);
        newSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button confirmar = (Button) findViewById(R.id.btnConfirm);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }

    protected void showConfirmDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Deseja realmente confirmar o recebimento da infração?");

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onPostExecute(String result) {
        if(flagOnPost == "validar"){
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
