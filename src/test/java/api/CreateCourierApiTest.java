package api;

import com.google.gson.Gson;
import http.ApiCourier;
import http.model.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateCourierApiTest {

    private Gson gson = new Gson();
    private ApiCourier apiCourier = new ApiCourier();
    //Курьер с полным набором входных данных
    private Courier courier = new Courier("ActualTestLogin", "ActualTestPassword", "ActualTestFirstName");
    //Курьер без логина
    private Courier courierWithoutLogin = new Courier("", "ProverkaProverkaPassword", "ProverkaProverkaFirstName");
    //Курьер без пароля
    private Courier courierWithoutPassword = new Courier("ProverkaProverkaLogin", "", "ProverkaProverkaFirstName");

    //Удаление созданного курьера
    @After
    public void deleteCourier() {
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        apiCourier.deleteCouriere(newCourier.id);
    }

    @Test
    @DisplayName("Check courier create action of /api/v1/courier")
    public void createCourierTest() {
        ValidatableResponse response = apiCourier.createCourier(courier);
        String actualBody = response.extract().body().asPrettyString();

        int statusCode = response.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"ok\": true\n}";

        //Проверки создания курьера
        assertThat("\"Код ответа не соответствует значению 201\"", statusCode, equalTo(201));
        assertEquals("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", actualBody, jsonExpectedResponse);
    }

    @Test
    @DisplayName("Check courier double not create action of /api/v1/courier")
    public void doubleCreateCourierTest() {
        //Создание курьера
        apiCourier.createCourier(courier);

        //Логин курьера
        apiCourier.loginCourier(courier);

        //Повторное создание курьера с идентичными данными
        ValidatableResponse secondCreateResponse = apiCourier.doubleCreateCourier(courier);
        String courierSecondCreateResponse = secondCreateResponse.extract().body().asPrettyString();

        String jsonExpectedResponse = "{\n    \"code\": 409,\n    \"message\": \"Этот логин уже используется. Попробуйте другой.\"\n}";
        int statusCodeNegative = secondCreateResponse.extract().statusCode();

        assertThat("\"Код ответа не соответствует значению 409\"", statusCodeNegative, equalTo(409));
        assertEquals("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", courierSecondCreateResponse, jsonExpectedResponse);
    }

    @Test
    @DisplayName("Check courier delete action of /api/v1/courier/:id")
    public void deleteCourierTest() {
        //Предварительное создание и логин курьером, для изъятия id курьера
        apiCourier.createCourier(courier);
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        ValidatableResponse responseDelete = apiCourier.deleteCouriere(newCourier.id);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();

        int statusCode = responseDelete.extract().statusCode();
        String jsonResponse = "{\n    \"ok\": true\n}";

        assertThat("\"Код ответа не соответствует значению 200\"", statusCode, equalTo(200));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", deleteMethodBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check courier not delete action without Id of /api/v1/courier/")
    public void deleteNotEnoughDataCourierTest() {
        //Предварительное создание и логин курьером, для изъятия id курьера
        apiCourier.createCourier(courier);
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        ValidatableResponse responseDelete = apiCourier.deleteCouriereWithoutId(newCourier);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();

        int statusCode = responseDelete.extract().statusCode();
        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для удаления курьера\"\n}";

        assertThat("\"Код ответа не соответствует значению 400\"", statusCode, equalTo(400));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", deleteMethodBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check not delete action for non existent courier of /api/v1/courier/:id")
    public void deleteNotExistCourierNegativeTest() {
        Long notExistCourierId = Long.valueOf(444333);

        //Удаление курьера
        ValidatableResponse responseDelete = apiCourier.deleteCouriere(notExistCourierId);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();

        int statusCode = responseDelete.extract().statusCode();
        String jsonResponse = "{\n    \"code\": 404,\n    \"message\": \"Курьера с таким id нет.\"\n}";

        assertThat("\"Код ответа не соответствует значению 404\"", statusCode, equalTo(404));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", deleteMethodBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check create action for courier without password of /api/v1/courier")
    public void createCourierWithoutPasswordNegativeTest() {
        ValidatableResponse response = apiCourier.createCourier(courierWithoutPassword);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();
        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для создания учетной записи\"\n}";

        assertThat("\"Код ответа не соответствует значению 400\"", statusCode, equalTo(400));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", actualBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check create action for courier without login of /api/v1/courier")
    public void createCourierWithoutLoginNegativeTest() {
        ValidatableResponse response = apiCourier.createCourier(courierWithoutLogin);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();
        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для создания учетной записи\"\n}";

        assertThat("\"Код ответа не соответствует значению 400\"", statusCode, equalTo(400));
        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", actualBody, equalTo(jsonResponse));
    }
}
