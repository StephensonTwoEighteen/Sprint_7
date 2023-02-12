package api;

import http.ApiOrder;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class GetOrdersCountApiTest {

    @Test
    public void getCountOfOrdersTest(){
        ApiOrder apiOrder = new ApiOrder("1", "Сокольники", 5, 0);

        ValidatableResponse responseGet = apiOrder.getCountOfOrders();
        String actualResponseGetBody = responseGet.extract().body().asString();

        System.out.println(actualResponseGetBody);

        assertTrue(actualResponseGetBody.contains("\"orders\":[{\"id\":"));
    }
}
