package com.solidarix.backend.service;

import com.solidarix.backend.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressApiService {

    /**
     * Récupère une adresse complète à partir d'une chaîne tapée par l'utilisateur,
     * en interrogeant l'API de data.gouv. Si aucune correspondance n’est trouvée,
     * retourne un objet Location minimal basé sur l’entrée manuelle.
     */
    public Location fetchLocationFromAddress(String fullAddress){
        String apiUrl = "https://api-adresse.data.gouv.fr/search/?q=" + fullAddress.replace(" ", "+");

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        JSONObject json = new JSONObject(response);
        JSONArray features = json.getJSONArray("features");

        Location location = new Location();

        if (features.length() > 0){
            // On extrait la première suggestion de l'API
            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");

            location.setFullAddress(properties.getString("label"));
            location.setNumber(properties.optString("houseNumber", "1"));
            location.setStreetName(properties.optString("street", ""));
            location.setPostalCode(properties.optString("postcode", ""));
            location.setCity(properties.optString("city", ""));
            location.setLatitude(geometry.getJSONArray("coordinates").getDouble(1));
            location.setLongitude(geometry.getJSONArray("coordinates").getDouble(0));

        } else {
            // Si pas de suggestion trouvée : on garde l'adresse saisie par l'utilisateur
            location.setFullAddress(fullAddress);
            location.setStreetName("");
            location.setPostalCode("");
            location.setCity("");
            location.setLatitude(0.0);
            location.setLongitude(0.0);
        }

        return location;
    }


    /**
     * Récupère une adresse complète à partir d'une chaîne tapée par l'utilisateur,
     * en interrogeant l'API de data.gouv.
     * retourne une liste de suggestiond 'adresses.
     */
    public List<String> getAdressSuggestions(String query){
        String apiUrl = "https://api-adresse.data.gouv.fr/search/?q=" + query.replace(" ", "+");

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        JSONObject json = new JSONObject(response);
        JSONArray features = json.getJSONArray("features");

        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < features.length(); i++){
            JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
            suggestions.add(properties.getString("label"));
        }

        return suggestions;
    }

}
