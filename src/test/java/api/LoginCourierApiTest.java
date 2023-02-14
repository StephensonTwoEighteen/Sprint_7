package api;

import com.google.gson.Gson;
import http.ApiCourier;
import http.model.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginCourierApiTest {

    Gson gson = new Gson();
    ApiCourier apiCourier = new ApiCourier();

    //Существующий курьер
    Courier courier = new Courier("ActualTestLogin", "ActualTestPassword", "ActualTestFirstName");

    @After
    public void deleteCourier() {
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        apiCourier.deleteCouriere(newCourier.id);
    }

    @Test
    @DisplayName("Check courier login action of /api/v1/courier/login")
    public void loginCourierTest() {
        //Создание курьера
        apiCourier.createCourier(courier);

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        int statusCode = responseLogin.extract().statusCode();

        //Проверка логина курьера
        assertThat("\"Код ответа не соответствует значению 200\"", statusCode, equalTo(200));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", loginBody, containsString("\"id\": "));
    }

    @Test
    @DisplayName("Check courier login action without login and password data of /api/v1/courier/login")
    public void loginCourierWithNoInputDataTest() {
        Courier courier = new Courier("", "", "");

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        int statusCode = responseLogin.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для входа\"\n}";

        //Проверка логина курьера
        assertThat("\"Код ответа не соответствует значению 400\"", statusCode, equalTo(400));
        assertEquals("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", loginBody, jsonExpectedResponse);
    }

    @Test
    @DisplayName("Check courier login action with no exist login and password of /api/v1/courier/login")
    public void loginCourierWithNotExistInputDataTest() {
        Courier courier = new Courier("notExistSamokattestLogin", "notExistSamokattestPassword", "notExistSamokattestFirstName");

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        int statusCode = responseLogin.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"code\": 404,\n    \"message\": \"Учетная запись не найдена\"\n}";

        //Проверка логина курьера
        assertThat("\"Код ответа не соответствует значению 404\"", statusCode, equalTo(404));
        assertEquals("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", loginBody, jsonExpectedResponse);
    }

}
