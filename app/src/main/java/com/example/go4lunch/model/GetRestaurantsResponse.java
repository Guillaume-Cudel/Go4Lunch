package com.example.go4lunch.model;

import java.util.List;

public class GetRestaurantsResponse {

    private List<Restaurant> results;

    public List<Restaurant> getResults() {
        return results;
    }

    public void setResults(List<Restaurant> results) {
        this.results = results;
    }
}
