package com.example.practicapokeapi.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.practicapokeapi.models.Pokemon;
import com.example.practicapokeapi.pokeapi.PokeApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonViewModel extends ViewModel {
    private MutableLiveData<Pokemon> pokemon;
    private PokeApiService service;

    public PokemonViewModel() {
        pokemon = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PokeApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PokeApiService.class);
    }

    public MutableLiveData<Pokemon> getPokemon() {
        return pokemon;
    }

    public void loadPokemon(String id) {
        Call<Pokemon> call = service.getPokemonById(id);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    pokemon.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                // Handle the error
            }
        });
    }
}