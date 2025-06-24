package lk.ijse.paymentservice.client;

import lk.ijse.parkingspaceservice.dto.ParkingSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ParkingSessionClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public ParkingSessionDTO isSessionExists(int userId){

        try{
            ParkingSessionDTO sessionResponse = webClientBuilder.build().get()
                    .uri("http://localhost:8083/parking-service/api/v1/parking/getByDriver/{userId}", userId)
                    .retrieve()
                    .bodyToMono(ParkingSessionDTO.class)
                    .block();

            System.out.println("session received now :" + sessionResponse);

            return sessionResponse;


        }catch(WebClientResponseException.NotFound ex){
            return null;
        }

    }
}
