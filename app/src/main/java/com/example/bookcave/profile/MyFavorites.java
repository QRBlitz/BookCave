package com.example.bookcave.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookcave.BookInfoOrder;
import com.example.bookcave.R;
import com.example.bookcave.extras.Favorite;
import com.example.bookcave.extras.SellingBook;
import com.example.bookcave.ui.home.HomecFragment;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyFavorites extends AppCompatActivity {

    private RecyclerView recyler_favorite;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshFavorite);
        recyler_favorite = findViewById(R.id.recyler_favorite);
        final String userid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        showFavorites(userid);
        recyler_favorite.setHasFixedSize(true);
        recyler_favorite.setLayoutManager(new LinearLayoutManager(this));
        recyler_favorite.setAdapter(adapter);
        //End of adapter code

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFavorites(userid);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void showFavorites(String userid) {
        Query query = firebaseFirestore.collection("Favorites").whereEqualTo("customerid", userid);

        FirestoreRecyclerOptions<Favorite> options = new FirestoreRecyclerOptions.Builder<Favorite>()
                .setQuery(query, Favorite.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Favorite, MyFavorites.FavoritesViewHolder>(options) {
            @NotNull
            @Override
            public MyFavorites.FavoritesViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_book_list, parent, false);

                return new MyFavorites.FavoritesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NotNull FavoritesViewHolder viewHolder, int position, @NotNull final Favorite model) {
                viewHolder.row_price.setText(String.format("%d TNG", model.getSellingprice()));
                viewHolder.row_quantity.setText(String.format("%d cps available", model.getQuantities()));
                final String final_query=model.getBookid();
                viewHolder.row_title.setText(model.getTitle());
                viewHolder.row_author.setText(model.getAuthor());
                //load image from internet and set it into imageView using Glide
                Glide.with(getApplicationContext()).load(model.getThumbnail()).placeholder(R.drawable.loading_shape).dontAnimate().into(viewHolder.row_thumbnail);

                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext() , BookInfoOrder.class);

                        i.putExtra("book_id" ,final_query);
                        i.putExtra("book_author" ,model.getAuthor());
                        i.putExtra("book_title",model.getTitle());
                        i.putExtra("book_thumbnail",model.getThumbnail());
                        i.putExtra("book_desc",model.getDescription());
                        i.putExtra("book_cat",model.getCategory());

                        i.putExtra("link",model.getPreview());
                        i.putExtra("sellerbookid", model.getSellerbookid());
                        i.putExtra("seller",model.getSellerid());
                        i.putExtra("rp",model.getRentingprice());
                        i.putExtra("sp",model.getSellingprice());
                        i.putExtra("dc",model.getDeliverycharges());
                        i.putExtra("qu",model.getQuantities());

                        startActivity(i);
                    }
                });
            }
        };
        adapter.startListening();
        recyler_favorite.setAdapter(adapter);
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView row_thumbnail;
        LinearLayout container;
        TextView row_title,row_author,row_price,row_quantity;

        FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            container=mView.findViewById(R.id.container);
            row_thumbnail= mView.findViewById(R.id.row_thumbnail);
            row_title=mView.findViewById(R.id.row_title);
            row_author=mView.findViewById(R.id.row_author);
            row_price=mView.findViewById(R.id.row_price);
            row_quantity=mView.findViewById(R.id.row_quantity);

        }
    }

}
