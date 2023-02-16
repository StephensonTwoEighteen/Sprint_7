package http;

import http.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ApiOrder extends SamokatClient {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] colour;
    private String courierId;
    private String nearestStation;
    private int limit;
    private int page;
    private final String BASE_ORDER_URI = "/api/v1/orders";

    public ApiOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] colour) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colour = colour;
    }

    public ApiOrder(String courierId, String nearestStation, int limit, int page) {
        this.courierId = courierId;
        this.nearestStation = nearestStation;
        this.limit = limit;
        this.page = page;
    }

    public ApiOrder() {
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given().spec(baseSpec())
                .body(order)
                .when()
                .post(BASE_ORDER_URI)
                .then().statusCode(201);
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getCountOfOrders() {
        return given().spec(baseSpec())
                .when()
                .get(BASE_ORDER_URI)
                .then().statusCode(200);
    }
}
