package com.tcc.easymeal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.easymeal.R;
import com.tcc.easymeal.adapter.AdapterProduto;
import com.tcc.easymeal.config.ConfiguracaoFirebase;
import com.tcc.easymeal.helper.UsuarioFirebase;
import com.tcc.easymeal.listener.RecyclerItemClickListener;
import com.tcc.easymeal.model.Cardapio;

import java.util.ArrayList;
import java.util.List;

public class LojaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;

    private List<Cardapio> produtos = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;

    private Toolbar toolbar;
    private FloatingActionMenu btn_add;

    private static final int ANIMATION_DURATION = 300;
    private static final float ROTATION_ANGLE = 360f;
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);


        //Configurações iniciais
        inicializarComponentes();
        configurarComponentes();


        //Recupera produtos para empresa
        recuperarProdutos();
      //  teste();

        //Adiciona evento de clique no recyclerview
        /*recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerProdutos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                             Cardapio produtoSelecionado = produtos.get(position);
                             produtoSelecionado.remover();
                            }


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        ); */

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuNovoProduto :
                abrirNovoProduto();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    */


    private void recuperarProdutos(){

        DatabaseReference produtosRef = firebaseRef
                .child("cardapio")
                .child( UsuarioFirebase.getDadosUsuarioLogado().getUid() );

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    produtos.add( ds.getValue(Cardapio.class) );
                }

                adapterProduto.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void teste(){

        Query produtosRef = firebaseRef
                .child("pedidos")
                .orderByChild("comerciante/uid").equalTo(UsuarioFirebase.getDadosUsuarioLogado().getUid());


        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    produtos.add( ds.getValue(Cardapio.class) );
                }

                adapterProduto.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuAcicionarItem:
                Intent cadastrar = new Intent(LojaActivity.this, CardapioActivity.class);
                startActivity(cadastrar);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
    */

    private void add_produto(View view)
    {

            Intent cadastrar = new Intent(LojaActivity.this, CardapioActivity.class);
            startActivity(cadastrar);


    }
    private void configurarComponentes()
    {
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos);
        recyclerProdutos.setAdapter( adapterProduto );
    }

    private void inicializarComponentes(){
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        autenticacao = ConfiguracaoFirebase.getFirebaseInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado().getUid();
        toolbar = findViewById(R.id.toolbarLoja);
        toolbar.setTitle(" Seus Produtos Cadastrados");
        setSupportActionBar(toolbar);
        btn_add=findViewById(R.id.add_produto);


        final Drawable originalImage = btn_add.getMenuIconView().getDrawable();
        btn_add.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_add.isOpened()) {
                    // We will change the icon when the menu opens, here we want to change to the previous icon
                    animation();
                    btn_add.close(true);
                    btn_add.getMenuIconView().setImageDrawable(originalImage);
                    add_produto(v);// tem que ser aqui para a pessoa ler o que significa o botao

                } else {
                    // Since it is closed, let's set our new icon and then open the menu
                    btn_add.getMenuIconView();
                    btn_add.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.add));
                    btn_add.open(true);
                }
            }
        });


    }

    private void animation()
    {
        mOpenAnimatorSet = new AnimatorSet();
        mCloseAnimatorSet = new AnimatorSet();

        ObjectAnimator collapseAnimator =  ObjectAnimator.ofFloat(btn_add.getMenuIconView(),
                "rotation",
                ROTATION_ANGLE , 0f );
        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(btn_add.getMenuIconView(),
                "rotation",
                0f ,ROTATION_ANGLE );


        final Drawable plusDrawable = ContextCompat.getDrawable(this,
                R.drawable.add);
        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_add.getMenuIconView().setImageDrawable(plusDrawable);
                btn_add.setIconToggleAnimatorSet(mCloseAnimatorSet);
            }
        });

        final Drawable mapDrawable = ContextCompat.getDrawable(this,
                R.drawable.add);
        collapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_add.getMenuIconView().setImageDrawable(mapDrawable);
                btn_add.setIconToggleAnimatorSet(mOpenAnimatorSet);
            }
        });

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);

        btn_add.setIconToggleAnimatorSet(mOpenAnimatorSet);
    }

}
