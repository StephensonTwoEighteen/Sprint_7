package api;

import com.google.gson.Gson;
import http.ApiCourier;
import http.model.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginCourierApiTest {

    Gson gson = new Gson();

    @Test
    @DisplayName("Check courier login action of /api/v1/courier/login")
    public void loginCourierTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "SamokattestPassword", "SamokattestFirstName");

        //Создание курьера
        apiCourier.createCourier(courier);

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        int statusCode = responseLogin.extract().statusCode();

        //Проверка логина курьера
        assertThat(statusCode, equalTo(200));
        assertThat(loginBody, containsString("\"id\": "));

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        apiCourier.deleteCouriere(newCourier.id);
    }

    @Test
    @DisplayName("Check courier login action without login and password data of /api/v1/courier/login")
    public void loginCourierWithNoInputDataTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("", "", "");

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        int statusCode = responseLogin.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для входа\"\n}";

        //Проверка логина курьера
        assertThat(statusCode, equalTo(400));
        assertEquals(loginBody, jsonExpectedResponse);

    }

    @Test
    @DisplayName("Check courier login action with no exist login and password of /api/v1/courier/login")
    public void loginCourierWithNotExistInputDataTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("notExistSamokattestLogin", "notExistSamokattestPassword", "notExistSamokattestFirstName");

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        int statusCode = responseLogin.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"code\": 404,\n    \"message\": \"Учетная запись не найдена\"\n}";

        //Проверка логина курьера
        assertThat(statusCode, equalTo(404));
        assertEquals(loginBody, jsonExpectedResponse);
    }

}
