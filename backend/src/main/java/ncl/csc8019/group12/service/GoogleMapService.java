package ncl.csc8019.group12.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author wei tan
 */

@Service
public class GoogleMapService {

    @Value("${google.cloud.map.key}")
    private String MAP_KEY;

    public void findRestaurantWithLocation(double latitude, double longitude) throws Exception {
        NearbySearchRequest request = PlacesApi.nearbySearchQuery(
                new GeoApiContext.Builder().apiKey(MAP_KEY).build(),
                new LatLng(latitude, longitude));

        PlacesSearchResponse response = request
                .radius(1000)
                .type(PlaceType.RESTAURANT)
                .keyword("chinese")
                .awaitIgnoreError();

//        List<String> name = Arrays.stream(response.results)
//                .map(r -> r.name).collect(Collectors.toList());

        Arrays.stream(response.results).forEach(r -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                System.out.println(objectMapper.writeValueAsString(r));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

//        while (response.nextPageToken != null) {
//            Thread.sleep(2000);
//            request = PlacesApi.nearbySearchQuery(
//                    new GeoApiContext.Builder().apiKey(MAP_KEY).build(),
//                    new LatLng(latitude, longitude));
//
//            response = request.pageToken(response.nextPageToken).await();
//
//            name.addAll(Arrays.stream(response.results)
//                    .map(r -> r.name)
//                    .collect(Collectors.toList()));
//            System.out.println(name.size());
//        }

//        name.forEach(System.out::println);
    }
}
