package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import java.util.Random;

public class BaseTest {

    protected final static String BASE_URL = "https://api.trello.com/1";
    protected final static String BOARDS = "boards";
    protected final static String LISTS = "lists";
    protected final static String CARDS = "cards";
    protected final static String ORGANIZATIONS = "organizations";
    protected final static String KEY = "022025b4b327c076fa95079f685e018d";
    protected final static String TOKEN = "105026fec0fb815488115872acdcb02b00b430e6da013491a6f7d27ce23f7054";
    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;
    private static Faker faker;
    public static String fakerName;

    public static String fakerNameOrganization;

    public static String fakerDescriptionOrganization;

    public static String fakerWebsiteOrganization;

    public static String fakerLastName;

    public static String randomText;

    @BeforeAll
    public static void beforeAll (){
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);
        reqSpec = reqBuilder.build();
        faker = new Faker();
        fakerName = faker.name().username();
        fakerLastName = faker.name().lastName();
        fakerNameOrganization = faker.company().name();
        fakerDescriptionOrganization = faker.name().username();
        fakerWebsiteOrganization = faker.internet().url();
        randomText = GenerateRandomText(2);
    }

    public static String GenerateRandomText (int value){

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(value);
        Random random = new Random();
        for (int i = 0; i < value; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String randomText = sb.toString();

        return randomText;
    }
}
