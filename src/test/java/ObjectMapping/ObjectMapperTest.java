package ObjectMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperTest {

    public static void main(String[] args) {

            String jsonResponse = "{"
                    + "\"diagnosticTypes\": [{}, {}],"
                    + "\"rules\": {},"
                    + "\"summary\": {}"
                    + "}";

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                PortfolioDetails portfolioDetails = objectMapper.readValue(jsonResponse, PortfolioDetails.class);
                System.out.println("Mapping successful: " + portfolioDetails);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
