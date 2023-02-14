package http;

import http.model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ApiCourier extends SamokatClient {
    private String login;
    private String password;
    private String firstName;
    private Long id;
    private final String BASE_COURIER_URI = "/api/v1/courier/";
    private final String LOGIN_COURIER_URI = "/api/v1/courier/login/";

    public ApiCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public ApiCourier(Long id) {
        this.id = id;
    }

    public ApiCourier() {
    }

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post(BASE_COURIER_URI)
                .then();
    }

    @Step("Попытка создания курьера с уже зарегистрированным логином")
    public ValidatableResponse doubleCreateCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post(BASE_COURIER_URI)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post(LOGIN_COURIER_URI)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCouriere(Long id) {
        return given().spec(baseSpec())
                .when()
                .delete(BASE_COURIER_URI + id)
                .then();
    }

    @Step("Попытка удаления курьера без указания id")
    public ValidatableResponse deleteCouriereWithoutId(Courier courier) {
        return given().spec(baseSpec())
                .when()
                .delete(BASE_COURIER_URI)
                .then();
    }
}