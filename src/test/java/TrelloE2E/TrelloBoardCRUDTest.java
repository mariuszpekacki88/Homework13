package TrelloE2E;
import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrelloBoardCRUDTest extends BaseTest  {

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String idCard;

    @Test
    @Order(1)
    public void createNewBoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board nazwa" + fakerName)
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("name")).isEqualTo("Board nazwa" + fakerName);

        boardId = json.getString("id");
    }

    @Test
    @Order(2)
    public void createFirstList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Lista nazwa" + fakerName)
                .when()
                .post(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("name")).isEqualTo("Lista nazwa" + fakerName);

        firstListId = json.getString("id");
    }

    @Test
    @Order(3)
    public void createSecondList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My second list" + fakerName)
                .when()
                .post(BASE_URL + "/" + BOARDS + "/" + boardId + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("name")).isEqualTo("My second list" + fakerName);

        secondListId = json.getString("id");
    }

    @Test
    @Order(4)
    public void addCardToFirstList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", firstListId)
                .queryParam("name", "My e2e car" + fakerName)
                .when()
                .post(BASE_URL + "/" + CARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("name")).isEqualTo("My e2e car" + fakerName);

        idCard = json.getString("id");
    }

    @Test
    @Order(5)
    public void moveCardToSecondList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + "/" + CARDS + "/" + idCard)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("idList")).isEqualTo(secondListId);
        Assertions.assertThat(json.getString("name")).isEqualTo("My e2e car" + fakerName);
    }

    @Test
    @Order(6)
    public void deleteBoard() {

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
