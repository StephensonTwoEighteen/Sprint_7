package api;

import http.ApiOrder;
import http.model.Order;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(Parameterized.class)
public class OrderCreateParamsApiTest {

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1} {2} {3}")
    public static Object[][] data() {
        return new Object[][]{
                {"Вася", "Петров", "Рябиновая 55", "Сокол", "79182234455", 3, "01.04.2023", "Привет привет", new String[]{"black"}},
                {"Петя", "Anreev", "Новозаводская 14", "Восстания", "79223339910", 1, "07.28.2023", "Привет poka", new String[]{"grey"}},
                {"Андрей", "Сидоров", "Рябиновая 55", "Ярославская", "79182234455", 5, "03.04.2023", "Privet Privet", new String[]{"black", "grey"}},
                {"Аркадий", "Савинов", "Новозаводская 99", "Китай город", "7918223432", 6, "09.04.2024", "Привет пока", new String[]{""}}
        };
    }

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] colour;

    public OrderCreateParamsApiTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] colour) {
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

    @Test
    public void createOrderTest() {
        ApiOrder apiOrder = new ApiOrder();
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);

        //Создание заказа
        ValidatableResponse responseCreate = apiOrder.createOrder(order);
        String actualCreateBody = responseCreate.extract().body().asPrettyString();

        assertThat("\"Error: Что-то пошло не так. Тело ответа не соответствует ожидаемому\"", actualCreateBody, containsString("\"track\": "));
    }

}
