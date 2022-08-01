package TrelloE2E;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class OrganizationTest extends BaseTest {
    private static String newName;
    private static String organizationId;
    private static String newWebside;

    @Test
    @Order(1)
    public void createNewOrganization() {

        if(fakerWebsiteOrganization.contains("www.")) {
            newWebside = fakerWebsiteOrganization.replace("www.", "http://");
        }
        else {
            newWebside = fakerWebsiteOrganization;
        }

        newName  = ("nazwaorg" + fakerLastName).toLowerCase();

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", fakerNameOrganization)
                .queryParam("desc", fakerDescriptionOrganization)
                .queryParam("name", newName)
                .queryParam("website", newWebside)
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("displayName")).isEqualTo(fakerNameOrganization);
        Assertions.assertThat(json.getString("desc")).isEqualTo(fakerDescriptionOrganization);
        Assertions.assertThat(json.getString("name")).isEqualTo(newName);
        Assertions.assertThat(json.getString("website")).isEqualTo(newWebside);

        organizationId = json.getString("id");
    }

    @Test
    @Order(2)
    public void deleteOrganization() {

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(3)
    public void createNewOrganizationWithoutDisplayName() {

         given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", "")
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Order(4)
    public void createNewOrganizationWithTwoCharactersInFieldName() {       // ten test nie działa, proszę zobaczyć czemu, powinnien zwrócić status code 400 a zwraca 200
                                                                            // z jakiegoś pododu test dokleja mi w tym miejscu dodatkowe znaki  .queryParam("name", nameNewTest)
        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", fakerNameOrganization)
                .queryParam("desc", fakerDescriptionOrganization)
                .queryParam("name", randomText)         // nie ma znaczenia czy użuwam tego
              //  .queryParam("name", "cc")       // czy używam tego
                .queryParam("website", fakerWebsiteOrganization)
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)           // tu powinien być status code 400 ale na potrzeby prezentacji problemu zostawiłem 200
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("name")).isEqualTo("cc");

        System.out.println(response.prettyPrint());
    }

    @Test
    @Order(5)
    public void createNewOrganizationWithWrongFormatWebsite() {   // ta funkcjionalność zwraca 200, powinna 400 tak jak w moim teście, ta Walidacja w trello nie działa!

        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", fakerNameOrganization)
                .queryParam("desc", fakerDescriptionOrganization)
                .queryParam("name", fakerName)
                .queryParam("website", "xcvw://sdfsdfsdf.pl")     // zostawiłem tu wartośc na sztywno bo to i tak nie działa
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        System.out.println(response.prettyPrint());

        Assertions.assertThat(json.getString("website")).isEqualTo("xcvw://sdfsdfsdf.pl");
    }
}
