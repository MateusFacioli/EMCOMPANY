package com.tcc.easymeal.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.easymeal.R;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.model.Avaliacao;
import com.tcc.easymeal.model.Cardapio;
import com.tcc.easymeal.model.Comerciante;
import com.tcc.easymeal.model.Localizacao;

public class ComercianteActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;


    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference firebaseRef;
    private Location mLastLocation;
    private PlacesClient placesClient;
    private LatLng latLng;
    private Comerciante comerciante = new Comerciante();
    private Cardapio cardapio = new Cardapio();
    private Localizacao localizacao = new Localizacao();
    private AlertDialog alerta;
    private static final int ANIMATION_DURATION = 300;
    private static final float ROTATION_ANGLE = 90f;
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;
    private FloatingActionMenu btn_menu;

    private DatabaseReference mDatabase;
    private Avaliacao avaliacao = new Avaliacao();

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comerciante);

        inicializarComponentes();
        recuperarAvaliacao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private void recuperarAvaliacao(){

        Query avaliacaoRef = mDatabase
                .child("comerciante")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child("avaliacao");

        avaliacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    avaliacao = dataSnapshot.getValue(Avaliacao.class);
                Toast.makeText(ComercianteActivity.this, avaliacao.getAvaliacao().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
    }

    @Override
    public void onConnected(Bundle bundle)
    {


    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    public void ficaronline(View view)
    {
        Msg_alertas("Deseja ficar online para que os clientes possam te encontrar?",1);
    }

    public void ficaroffline(View view)
    {
        Msg_alertas("Só é possível ficar indisponível para os clientes depois de entregar todos os pedidos",2);
    }

    public void fechartudo (View view)
    {
        Msg_alertas("Deseja sair do sistema ?",4);
    }

    public void vaipedidos(View view)
    {
        Msg_alertas("Seus pedidos restantes para entregar",3);
    }

    public void vaiprodutos(View view)
    {
        Msg_alertas("Deseja cadastrar ou editar algum produto ?",5);
    }

    public void vaiscan(View view)
    {
        Msg_alertas("Deseja escanear um QR-Code?",6);
    }

    public void vaiagenda(View view)
    {
        Msg_alertas("Deseja cadastrar ou alterar algum horário semanal ?",7);
    }


    private void animation()
    {
        mOpenAnimatorSet = new AnimatorSet();
        mCloseAnimatorSet = new AnimatorSet();

        ObjectAnimator collapseAnimator =  ObjectAnimator.ofFloat(btn_menu.getMenuIconView(),
                "rotation",
                - 270f  +  ROTATION_ANGLE , 0f );
        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(btn_menu.getMenuIconView(),
                "rotation",
                0f , - 270f  +  ROTATION_ANGLE );
        //menu fica 45 graus nao consegui arrumar

        final Drawable plusDrawable = ContextCompat.getDrawable(this,
                R.drawable.menu);
        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_menu.getMenuIconView().setImageDrawable(plusDrawable);
                btn_menu.setIconToggleAnimatorSet(mCloseAnimatorSet);
            }
        });

        final Drawable mapDrawable = ContextCompat.getDrawable(this,
                R.drawable.menu);
        collapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_menu.getMenuIconView().setImageDrawable(mapDrawable);
                btn_menu.setIconToggleAnimatorSet(mOpenAnimatorSet);
            }
        });

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);

        btn_menu.setIconToggleAnimatorSet(mOpenAnimatorSet);
    }

    private void Msg_alertas(String texto, int n) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AVISO");
        builder.setMessage(texto);

        switch (n)
        {
            case 1://online
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (ActivityCompat.checkSelfPermission(ComercianteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ComercianteActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)
                        { return; }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mLastLocation != null)
                        {
                            if(mMap != null)
                            {
                                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Posição"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                localizacao.setLatitude(latLng.latitude);
                                localizacao.setLongitude(latLng.longitude);
                                UsuarioFirebase.atualizarDadosLocalizacao(localizacao.getLatitude(), localizacao.getLongitude());

                            }
                        }

                        localizacao.salvar();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 2://offline

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localizacao.remover();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 3://pedidos
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent pedidos = new Intent(ComercianteActivity.this,PedidosActivity.class);
                        startActivity(pedidos);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 4://sair
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // localizacao.remover();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 5://produtos
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cadastrar = new Intent(ComercianteActivity.this, LojaActivity.class);
                        startActivity(cadastrar);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 6://scan
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent Qr_code= new Intent(ComercianteActivity.this, Qr_codeActivity.class);
                        startActivity(Qr_code);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 7://agenda
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent agenda= new Intent(ComercianteActivity.this, CadastrarHorarioActivity.class);
                        startActivity(agenda);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
        }


        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void inicializarComponentes(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        toolbar = findViewById(R.id.toolbarComerciante);

        toolbar.setTitle("Bem Vindo comerciante "+ mUser.getDisplayName()+" ! ");

        mDatabase = ConfiguracaoFirebase.getFirebase();

        setSupportActionBar(toolbar);
        btn_menu=findViewById(R.id.menu_principal);
        final Drawable originalImage = btn_menu.getMenuIconView().getDrawable();
        btn_menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_menu.isOpened()) {
                    // We will change the icon when the menu opens, here we want to change to the previous icon
                    animation();
                    btn_menu.close(true);
                    btn_menu.getMenuIconView().setImageDrawable(originalImage);
                    //btn_menu.setIconAnimated(false);
                    //  btn_menu.setAnimation();
                } else {
                    // Since it is closed, let's set our new icon and then open the menu
                    // btn_menu.setIconAnimated(true);
                    btn_menu.getMenuIconView();
                    btn_menu.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.menu));
                    btn_menu.open(true);
                }
            }
        });



    }

}

//esse funciona online mas nao tem geocode crasha app
