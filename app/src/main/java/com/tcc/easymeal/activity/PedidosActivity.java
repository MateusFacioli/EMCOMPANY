package com.tcc.easymeal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.easymeal.R;
import com.tcc.easymeal.adapter.AdapterPedidos;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.listener.RecyclerItemClickListener;
import com.tcc.easymeal.model.Cardapio;
import com.tcc.easymeal.model.Pedidos;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterPedidos adapterPedidos;

    private List<Pedidos> pedidos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        inicializarComponentes();
        configurarComponentes();
        recuperarID();

        recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerProdutos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent visualizar = new Intent(PedidosActivity.this, VisualizarPedidoActivity.class);
                                Pedidos pedido = new Pedidos();
                                pedido =  pedidos.get(position);
                                String idpedido = pedido.getIdpedido();
                               // Toast.makeText(PedidosActivity.this, pedido.getIdpedido(), Toast.LENGTH_SHORT).show();

                                visualizar.putExtra("idPedido",idpedido);
                                startActivity(visualizar);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


    }

    private void recuperarID(){


        List<String> userIdList = new ArrayList();
        DatabaseReference idRef = firebaseRef
                .child("pedido");
        Query produtosQ = idRef.orderByChild(idRef.getKey());

        produtosQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIdList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    userIdList.add( ds.getKey());
                }
                   //recuperarProdutos();
                    recuperarPedidos(userIdList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarPedidos(List<String> userIdList) {

         DatabaseReference databaseReference=firebaseRef.child("comerciante")
                 .child(UsuarioFirebase.getIdentificadorUsuario())
                 .child("pedidos");

        // Query qDatabaseReference = databaseReference.orderByChild("pedidos");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pedidos.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //if(ds.getValue(Pedidos.class).getComerciante().getUid().equals(idComerciante))

                        pedidos.add(ds.getValue(Pedidos.class));
                    }

                     adapterPedidos.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    private void configurarComponentes(){
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterPedidos = new AdapterPedidos(pedidos);
        recyclerProdutos.setAdapter( adapterPedidos );
    }

    private void inicializarComponentes(){
        recyclerProdutos = findViewById(R.id.recyclerPedidos);
        autenticacao = ConfiguracaoFirebase.getFirebaseInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado().getUid();
        toolbar = findViewById(R.id.toolbarPedidos);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        Toast.makeText(PedidosActivity.this, "Clique no pedido para obter mais informações", Toast.LENGTH_LONG).show();
    }
}
