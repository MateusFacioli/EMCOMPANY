package com.tcc.easymeal.activity;

    import android.Manifest;
    import android.content.DialogInterface;
    import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
    import android.support.design.widget.Snackbar;
    import android.support.v4.app.ActivityCompat;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.support.v7.widget.Toolbar;
    import android.view.View;
    import android.widget.Toast;

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
    import com.google.firebase.database.DatabaseReference;
    import com.tcc.easymeal.R;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
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

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comerciante);

        inicializarComponentes();





       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().hide();

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

  /**  private void enviaParaCardapio(){


       if(mUser != null){

           Comerciante comercianteLogado = UsuarioFirebase.getDadosUsuarioLogado();
           DatabaseReference dados = firebaseRef.child("comerciante");
           Query dadosPesquisa = dados.orderByChild("uid").equalTo(comercianteLogado.getUid());

           dadosPesquisa.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   List<Comerciante> lista = new ArrayList<>();
                   for( DataSnapshot ds: dataSnapshot.getChildren() ){
                       lista.add( ds.getValue( Comerciante.class ) );
                   }

                   comerciante = lista.get(0);
                   loja.setComerciante(comerciante);



                   Bundle bundle = new Bundle();
                   bundle.putSerializable("loja", loja);
                   cadastrar.putExtras(bundle);






               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });



       }



        //String idToken = task.getResult().getToken();
        //                            Comerciante comerciante = new Comerciante();
        //                            comerciante.setUid(mUser.getUid());
        //                            loja.setComerciante(comerciante);
        //                            loja.salvar();


    }**/


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if(mMap != null){
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Posição"));
                 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                localizacao.setLatitude(latLng.latitude);
                localizacao.setLongitude(latLng.longitude);


            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comerciante, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 int n;
        switch (item.getItemId()){
            case R.id.menuCadastrarCardapio:
                Intent cadastrar = new Intent(ComercianteActivity.this, LojaActivity.class);
                startActivity(cadastrar);
                break;

            case R.id.menuOnline:
                n=1;
                Msg_alertas("Deseja ficar online para que os clientes possam te encontrar?", n);
                break;

            case R.id.menuOffiline:
                 n=2;
                Msg_alertas("Só é possível ficar indisponível para os clientes depois de entregar todos os pedidos",n);
                break;

            case R.id.menuPedidos:
                 n=3;
                Msg_alertas("Seus pedidos restantes para entregar",n);
                break;

            case R.id.menuSair:
                 n=4;
                Msg_alertas("Deseja sair do sistema ?",n);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void Msg_alertas(String texto, int n) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AVISO");
        builder.setMessage(texto);

   switch (n)
   {
       case 1:
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   localizacao.salvar();
               }
           });
           builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface arg0, int arg1) {

               }
           });
           break;
       case 2:

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
       case 3:
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
       case 4:
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   localizacao.remover();
                   FirebaseAuth.getInstance().signOut();
                   finish();
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
        String mUser2 = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        toolbar = findViewById(R.id.toolbarComerciante);

            toolbar.setTitle("Bem Vindo "+ mUser2);




        setSupportActionBar(toolbar);


    }

}
