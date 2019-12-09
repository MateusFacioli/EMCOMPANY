package com.tcc.easymeal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.tcc.easymeal.R;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.model.Comerciante;
import com.tcc.easymeal.model.Localizacao;

import java.util.Arrays;

public class Locais extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AutocompleteSupportFragment autocompleteFragment;
    PlacesClient placesClient ;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    private LatLng meuLocal;
    private LinearLayout LinearLayoutEndereco1;
    private LinearLayout LinearLayoutEndereco2;
    private String nomePedido;
    private String nomeLoja;
    private String idLoja;
    private Intent iConfirma;
    private Bundle bConfirma;
    private Button btnIrPagamento;
    private String endenreco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locais);
       // Toolbar toolbar = findViewById(R.id.toolbar);
       // toolbar.setTitle("Locais");
      //  setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        configurarComponentes();
        autoComplete();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

        btnIrPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(endenreco != null) {
                    Localizacao localizacao = new Localizacao();
                    localizacao.setLatitude(mLastLocation.getLatitude());
                    localizacao.setLongitude(mLastLocation.getLongitude());
                    Intent checkout = new Intent(Locais.this, ComercianteActivity.class);
                    startActivity(checkout);
                    UsuarioFirebase.atualizarDadosLocalizacao(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    localizacao.salvar();
                    finish();
                }
            }
        });
    }

    private void autoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCVA5Z6ZpycRP-NPtyKjvBbUSKXJ6aiD70");
        }

        placesClient = Places.createClient(this);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endenreco = place.getAddress();
                mLastLocation.setLatitude(place.getLatLng().latitude);
                mLastLocation.setLongitude(place.getLatLng().longitude);


                meuLocal = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                // Adicionamos um Marker com a posição...
                mMap.addMarker(new MarkerOptions().position(meuLocal).title("Minha Posição"));
                // Um zoom no mapa para a seua posição atual...
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(meuLocal, 18));

        /*// TODO: Get info about the selected place.
        Log.i("PlaceCerto", "Place: " + place.getName() + ", " + place.getId());
        Geocoder geocoder = new Geocoder(CheckoutActivity.this, Locale.getDefault());
        try {
          List<Address> listaEnderecos = geocoder.getFromLocationName(place.getName(), 1);
          if(listaEnderecos != null && listaEnderecos.size() >0){
            Address address = listaEnderecos.get(0);
            salvaLocal(address);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
               */



            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PlaceErrado", "An error occurred: " + status);
            }
        });
    }


    public void onConnected(Bundle bundle){
        //Também é preciso que você implemente para a solicitação de geolocalização.
        // você deverá tratar caso não consiga
        // pegamos a ultima localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if(mMap != null){
                // Criamos o LatLng através do Location




            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    private void inicializarComponentes(){

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        btnIrPagamento = findViewById(R.id.btnIrPagamento);

        iConfirma = getIntent();
        bConfirma = iConfirma.getExtras();
    }

    private void configurarComponentes(){

        if(bConfirma != null){
            nomePedido = bConfirma.get("nomePedido").toString();
            nomeLoja = bConfirma.get("nomeLoja").toString();
            idLoja = bConfirma.get("idLoja").toString();
        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
