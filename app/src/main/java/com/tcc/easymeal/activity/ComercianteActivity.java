package com.tcc.easymeal.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
import com.tcc.easymeal.model.Comerciante;
import com.tcc.easymeal.model.Localizacao;

import java.util.ArrayList;
import java.util.List;

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
    private Button btnOnline;
    private Comerciante comerciante = new Comerciante();



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

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latLng!=null){
                    Localizacao localizacao = new Localizacao();
                    localizacao.setLatitude(latLng.latitude);
                    localizacao.setLongitude(latLng.longitude);
                    salvaLocal(localizacao);

                  //  btnOnline.setBackgroundColor(Color.blue(2));
                }

            }
        });



    }

    private void salvaLocal(Localizacao localizacao){


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
                   localizacao.setComerciante(comerciante);
                   localizacao.salvar();



               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

       }



        //String idToken = task.getResult().getToken();
        //                            Comerciante comerciante = new Comerciante();
        //                            comerciante.setUid(mUser.getUid());
        //                            localizacao.setComerciante(comerciante);
        //                            localizacao.salvar();


    }

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

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void inicializarComponentes(){

        btnOnline = findViewById(R.id.btnOnline);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();


    }

}
