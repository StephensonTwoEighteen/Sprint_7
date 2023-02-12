package api;

import com.google.gson.Gson;
import http.ApiCourier;
import http.model.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateCourierApiTest {

    Gson gson = new Gson();

    @Test
    @DisplayName("Check courier create action of /api/v1/courier")
    public void createCourierTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "SamokattestPassword", "SamokattestFirstName");

        ValidatableResponse response = apiCourier.createCourier(courier);
        String actualBody = response.extract().body().asPrettyString();

        int statusCode = response.extract().statusCode();
        String jsonExpectedResponse = "{\n    \"ok\": true\n}";

        System.out.println(statusCode);
        System.out.println(actualBody);



        //Проверка сощдания курьера
        assertThat(statusCode, equalTo(201));
        assertEquals(actualBody, jsonExpectedResponse);

        //Удаление курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        apiCourier.deleteCouriere(newCourier.id);
    }

    @Test
    @DisplayName("Check courier double not create action of /api/v1/courier")
    public void doubleCreateCourierTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "SamokattestPassword", "SamokattestFirstName");

        //Создание курьера
        apiCourier.createCourier(courier);

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        //Повторное создание курьера с идентичными данными
        ValidatableResponse secondCreateResponse = apiCourier.doubleCreateCourier(courier);
        String courierSecondCreateResponse = secondCreateResponse.extract().body().asPrettyString();

        String jsonExpectedResponse = "{\n    \"code\": 409,\n    \"message\": \"Этот логин уже используется. Попробуйте другой.\"\n}";
        int statusCodeNegative = secondCreateResponse.extract().statusCode();

        assertThat(statusCodeNegative, equalTo(409));
        assertEquals(courierSecondCreateResponse, jsonExpectedResponse);

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        apiCourier.deleteCouriere(newCourier.id);
    }

    @Test
    @DisplayName("Check courier delete action of /api/v1/courier/:id")
    public void deleteCourierTest(){
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "SamokattestPassword", "SamokattestFirstName");

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

        assertThat(statusCode, equalTo(200));
        assertThat(deleteMethodBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check courier not delete action without Id of /api/v1/courier/")
    public void deleteNotEnoughDataCourierTest(){
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "SamokattestPassword", "SamokattestFirstName");

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

        assertThat(deleteMethodBody, equalTo(jsonResponse));
        assertThat(statusCode, equalTo(400));
    }

    @Test
    @DisplayName("Check not delete action for non existent courier of /api/v1/courier/:id")
    public void deleteNotExistCourierNegativeTest(){
        ApiCourier apiCourier = new ApiCourier();
        Long notExistCourierId = Long.valueOf(444333);

        //Удаление курьера
        ValidatableResponse responseDelete = apiCourier.deleteCouriere(notExistCourierId);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();

        int statusCode = responseDelete.extract().statusCode();
        String jsonResponse = "{\n    \"code\": 404,\n    \"message\": \"Курьера с таким id нет.\"\n}";

        assertThat(statusCode, equalTo(404));
        assertThat(deleteMethodBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check create action for courier without password of /api/v1/courier")
    public void createCourierWithoutPasswordNegativeTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("SamokattestLogin", "", "SamokattestFirstName");

        ValidatableResponse response = apiCourier.createCourierNegative(courier);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();

        System.out.println(statusCode);
        System.out.println(actualBody);

        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для создания учетной записи\"\n}";

        assertThat(statusCode, equalTo(400));
        assertThat(actualBody, equalTo(jsonResponse));
    }

    @Test
    @DisplayName("Check create action for courier without login of /api/v1/courier")
    public void createCourierWithoutLoginNegativeTest() {
        ApiCourier apiCourierCreate = new ApiCourier("", "SamokattestPassword", "SamokattestFirstName");
        Courier courier = new Courier();

        ValidatableResponse response = apiCourierCreate.createCourierNegative(courier);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();

        System.out.println(statusCode);
        System.out.println(actualBody);

        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для создания учетной записи\"\n}";

        assertThat(statusCode, equalTo(400));
        assertThat(actualBody, equalTo(jsonResponse));
    }
}
