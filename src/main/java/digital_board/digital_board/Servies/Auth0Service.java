package digital_board.digital_board.Servies;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import digital_board.digital_board.Dto.CreateUserRequestDto;
import digital_board.digital_board.Dto.SignupRequestDto;
import digital_board.digital_board.Dto.SignupResponseDto;
import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Entity.User;
import digital_board.digital_board.Exception.ResourceNotFoundException;
import digital_board.digital_board.Repository.UserRepository;
import digital_board.digital_board.ServiceImpl.EmailServiceImpl;
import digital_board.digital_board.constants.ResponseMessagesConstants;

@Service
public class Auth0Service {
    @Autowired
    private EmailServiceImpl emailServices;
    @Autowired
    private UserRepository userRepo;
    private String auth0Domain = "dev-2v6nqrql62h5dwnv.us.auth0.com";
    private String clientId = "UkrepWEIKkn2CIYLmGIiuU2fdwU34WdH";
    private String connection = "Username-Password-Authentication";
    private RestTemplate restTemplate = new RestTemplate();

    public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
        System.out.println("signUp service");
        String apiUrl = "https://" + auth0Domain + "/dbconnections/signup";
        // SignupRequestDto.getEmail(), SignupRequestDto.getPassword()
        User userAvailable = userRepo.getbyemail(signupRequestDto.getEmail());
        User superAdminAvailable = userRepo.getbyemail(signupRequestDto.getCreatedBy());
        if (superAdminAvailable != null && "SuperAdmin".equals(superAdminAvailable.getRole())) {
            if (userAvailable == null) {
                System.out.println("signUp if block");
                String randomPasswrod = RandomStringUtils.random(12, true, true);
                System.out.println(randomPasswrod);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                CreateUserRequestDto request = new CreateUserRequestDto(clientId, signupRequestDto.getEmail(),
                        randomPasswrod,
                        connection);
                HttpEntity<CreateUserRequestDto> requestEntity = new HttpEntity<>(request, headers);
                // restTemplate.postForLocation(apiUrl, requestEntity);
                ResponseEntity<SignupResponseDto> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity,
                        SignupResponseDto.class);
                SignupResponseDto signupResponseDto = responseEntity.getBody();

                try {
                    if (signupResponseDto != null && signupResponseDto.getEmail() != null) {
                        User user = new User();
                        user.setUserName(signupRequestDto.getUserName());
                        user.setEmail(signupResponseDto.getEmail());
                        user.setRole("Admin");
                        user.setDepartmentName(signupRequestDto.getDepartmentName());
                        user.setCategory(signupRequestDto.getCategory());
                        user.setStatus("enable");
                        user.setAddress(signupRequestDto.getAddress());
                        user.setContact(signupRequestDto.getContact());
                        userRepo.save(user);
                        emailServices.sendSimpleMessageForPassword(signupResponseDto.getEmail(),
                                signupRequestDto.getUserName(),
                                randomPasswrod);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }

                return signupResponseDto;

            } else {
                throw new ResourceNotFoundException(ResponseMessagesConstants.messagelist.stream()
                        .filter(exceptionResponse -> "MESSAGE_REGISTER_ERRROR"
                                .equals(exceptionResponse.getExceptonName()))
                        .map(ExceptionResponse::getMassage)
                        .findFirst()
                        .orElse("Default message if not found"));
            }
        } else {
            throw new ResourceNotFoundException(ResponseMessagesConstants.messagelist.stream()
                    .filter(exceptionResponse -> "NOT_SUPERADMIN"
                            .equals(exceptionResponse.getExceptonName()))
                    .map(ExceptionResponse::getMassage)
                    .findFirst()
                    .orElse("Default message if not found"));
        }
    }

    // private String clientIdV2 = "zkodNoGjWY7QAbBChJJ1fyAtlBCbO2Re";
    // private String clientSecretV2 =
    // "3ks2A-bzkBW5_lLBZZ04GBIF0mfY4moqCjsF7nrhbJR-sIJuXsi0dWSTJS9VBoLR";

    // public String getManagementApiToken() {
    // String tokenUrl = "https://" + auth0Domain + "/oauth/token";

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON);

    // HttpEntity<String> request = new HttpEntity<>(
    // "{\"grant_type\":\"client_credentials\",\"client_id\":\"" + clientIdV2 +
    // "\",\"client_secret\":\""
    // + clientSecretV2 + "\",\"audience\":\"https://" + auth0Domain +
    // "/api/v2/\"}",
    // headers);

    // ResponseEntity<String> response = new RestTemplate().postForEntity(tokenUrl,
    // request, String.class);

    // if (response.getStatusCode().is2xxSuccessful()) {
    // // Parse the response to get the access token
    // String accessToken = response.getBody();
    // System.out.println("Management API Token: " + accessToken);
    // return accessToken;
    // } else {
    // System.out.println("Failed to obtain Management API token. Response: " +
    // response.getBody());
    // return null;
    // }
    // }

    private final String clientIdV2 = "zkodNoGjWY7QAbBChJJ1fyAtlBCbO2Re";
    private final String clientSecretV2 = "3ks2A-bzkBW5_lLBZZ04GBIF0mfY4moqCjsF7nrhbJR-sIJuXsi0dWSTJS9VBoLR";

    // public void deleteUser() {
    // String userIdToDelete = "auth0|6561be5a022a9fc3bea11ada"; // Replace with the
    // actual user ID to delete
    // String tokenUrl = "https://" + auth0Domain + "/oauth/token";
    // String apiUrl = "https://" + auth0Domain + "/api/v2/users/" + userIdToDelete;

    // // Request a Management API token
    // MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
    // tokenRequest.add("grant_type", "client_credentials");
    // tokenRequest.add("client_id", clientIdV2);
    // tokenRequest.add("client_secret", clientSecretV2);
    // tokenRequest.add("audience", "https://" + auth0Domain + "/api/v2/");

    // RestTemplate restTemplate = new RestTemplate();
    // String managementApiToken = restTemplate.postForObject(tokenUrl,
    // tokenRequest, String.class);

    // JSONObject jsonObject = new JSONObject(managementApiToken);

    // // Extract the access_token
    // String accessToken = jsonObject.getString("access_token");

    // // Now 'accessToken' variable contains the extracted access_token
    // System.out.println("Access Token: " + accessToken);
    // if (managementApiToken != null) {
    // System.out.println("Management API Token: " + managementApiToken);

    // // Prepare headers for the delete request
    // MultiValueMap<String, String> deleteHeaders = new LinkedMultiValueMap<>();
    // deleteHeaders.add("Authorization", "Bearer " + accessToken);

    // // Delete the user using the obtained token
    // restTemplate.exchange(apiUrl, HttpMethod.DELETE, new
    // HttpEntity<>(deleteHeaders), String.class);
    // System.out.println("User deleted successfully");
    // } else {
    // System.out.println("Failed to obtain Management API token.");
    // }
    // }
    public boolean deleteUser(String userEmail) {
        // Request a Management API token
        String tokenUrl = "https://" + auth0Domain + "/oauth/token";
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "client_credentials");
        tokenRequest.add("client_id", clientIdV2);
        tokenRequest.add("client_secret", clientSecretV2);
        tokenRequest.add("audience", "https://" + auth0Domain + "/api/v2/");

        RestTemplate restTemplate = new RestTemplate();
        String managementApiToken = restTemplate.postForObject(tokenUrl, tokenRequest, String.class);

        JSONObject jsonObject = new JSONObject(managementApiToken);

        // Extract the access_token
        String accessToken = jsonObject.getString("access_token");
        // Get the user_id using the email address
        String userId = getUserIdByEmail(userEmail, accessToken);

        if (userId != null) {

            String apiUrl = "https://" + auth0Domain + "/api/v2/users/" + userId;

            // Prepare headers for the delete request
            HttpHeaders deleteHeaders = new HttpHeaders();
            deleteHeaders.add("Authorization", "Bearer " + accessToken);

            // Delete the user using the obtained token
            restTemplate.exchange(apiUrl, HttpMethod.DELETE, new HttpEntity<>(deleteHeaders), String.class);
            System.out.println("User with email '" + userEmail + "' deleted successfully");
            return true;
        } else {
            System.out.println("User with email '" + userEmail + "' not found.");
            return false;
        }
    }

    private String getUserIdByEmail(String email, String managementApiToken) {
        String usersByEmailUrl = "https://" + auth0Domain + "/api/v2/users-by-email?email=" + email;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + managementApiToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(usersByEmailUrl, HttpMethod.GET, entity,
                String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Parse the response and extract the user_id
            String responseBody = responseEntity.getBody();

            // Assuming the response is a JSON array
            JSONArray jsonArray = new JSONArray(responseBody);

            if (jsonArray.length() > 0) {
                JSONObject userObject = jsonArray.getJSONObject(0);
                String userId = userObject.getString("user_id");
                System.out.println("UserId??????: " + userId);
                return userId;
            } else {
                // No user found
                System.out.println("User not found for email: " + email);
                return null;
            }
        } else {
            // Handle error cases
            System.out.println("Error retrieving user ID. HTTP Status: " + responseEntity.getStatusCodeValue());
            return null;
        }
    }
}