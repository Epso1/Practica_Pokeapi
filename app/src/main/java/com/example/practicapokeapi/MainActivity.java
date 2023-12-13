package com.example.practicapokeapi;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.practicapokeapi.models.Pokemon;
import com.example.practicapokeapi.models.PokemonList;
import com.example.practicapokeapi.pokeapi.PokeApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PokeApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService pokeService = retrofit.create(PokeApiService.class);

        Button iniciarListaButton = findViewById(R.id.iniciarListaButton);
        iniciarListaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int limit = 20;
                for (int offset = 0; offset < 807; offset += limit) {
                    pokemonList(pokeService, limit, offset);
                }
            }
        });

        Button pokemonAleatorioButton = findViewById(R.id.pokemonAleatorioButton);
        pokemonAleatorioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pokemonById(pokeService);
            }
        });

    }

    public void pokemonById(PokeApiService pokeService) {
        Call<Pokemon> pokeCall = pokeService.getPokemonById(Integer.toString((int) (Math.random() * 807 + 1)));
        pokeCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon foundPoke = response.body();
                if (foundPoke != null) {
                    Log.d("POKEMON NAME", foundPoke.getName());
                    Log.d("POKEMON HEIGHT", foundPoke.getHeight());
                    Log.d("POKEMON WEIGHT", foundPoke.getWeight());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pokemonList(PokeApiService pokeService, int limit, int offset) {
        Call<PokemonList> pokeListCall = pokeService.getPokemonList(limit, offset);
        pokeListCall.enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                PokemonList pokemonList = response.body();
                if (pokemonList != null) {
                    for (Pokemon pokemon : pokemonList.getResults()) {
                        if (pokemon.getName() != null) {
                            Log.d("POKEMON NAME", pokemon.getName());
                        }
                        if (pokemon.getHeight() != null) {
                            Log.d("POKEMON HEIGHT", pokemon.getHeight());
                        }
                        if (pokemon.getWeight() != null) {
                            Log.d("POKEMON WEIGHT", pokemon.getWeight());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}