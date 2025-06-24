package lk.ijse.paymentservice.client;

import lk.ijse.userservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class UserClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public UserDTO isUserExists(int userId){

        try{
           UserDTO userResponse =  webClientBuilder.build().get()
                    .uri("http://localhost:8081/user-service/api/v1/user/getById/{userId}",userId)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();

            System.out.println(userResponse);

           return userResponse;



        }catch(WebClientResponseException.NotFound ex){
            return null;
        }
    }
}
