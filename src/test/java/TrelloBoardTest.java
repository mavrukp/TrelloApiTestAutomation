import api.TrelloBoardApi;
import api.TrelloCardApi;
import api.TrelloOrganizationApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrelloBoardTest {

    final static Logger logger = LoggerFactory.getLogger(TrelloBoardTest.class);

    private static String orgId, boardId;
    private static ArrayList<String> cardIds = new ArrayList<>();


    @Test
    @Order(1)
    public void createOrganizationPostRequest(){
        TrelloOrganizationApi trelloOrganizationApi = new TrelloOrganizationApi();

        Response response = trelloOrganizationApi.createOrganization();
        try {
            assertNotNull(response.jsonPath().getString("id"));
            assertEquals(200,response.statusCode());
            response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/createOrgResponseSchema.json"));
            orgId = response.jsonPath().getString("id");
            //logger.info("org id " + orgId);
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in createOrganizationPostRequest ");
        }
    }

    @Test
    @Order(2)
    public void createBoardPostRequest(){
        TrelloBoardApi trelloBoardApi = new TrelloBoardApi();
        Response response = trelloBoardApi.createBoard(orgId);
        try {
            assertNotNull(response.jsonPath().getString("id"));
            assertEquals(200,response.statusCode());
            response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/createBoardResponseSchema.json"));
            boardId = response.jsonPath().getString("id");
            //logger.info("board id " + boardId);
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in createBoardPostRequest ");
        }
    }

    @Test
    @Order(3)
    public void createCardPostRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();
        TrelloBoardApi trelloBoardApi = new TrelloBoardApi();
        Response listResponse = trelloBoardApi.readListsFromBoard(boardId);
        try {
            assertEquals(200,listResponse.statusCode());
            assertTrue(listResponse.jsonPath().getList("").size() > 0);
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in createCardPostRequest ");
        }

        HashMap hm = (HashMap) listResponse.jsonPath().getList("").get(0);

        String idList = (String) hm.get("id");

        for (int i=0; i<2; i++){
            try {
                Response response = trelloCardApi.createCard(boardId,idList);
                assertEquals(200,response.statusCode());
                assertNotNull(response.jsonPath().getString("id"));
                cardIds.add(response.jsonPath().getString("id"));
                //logger.info("card id " + cardIds.get(i));
            }catch(NullPointerException e){
                logger.error("NullPointerException Exception in createCardPostRequest ");
            }
        }

    }

    @Test
    @Order(4)
    public void updateCardPutRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();
        int randomIndex = new Random().nextInt(2);
        Response response = trelloCardApi.updateCard(cardIds.get(randomIndex));

        try {
            assertEquals(200,response.statusCode());
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in updateCardPutRequest ");
        }
    }

    @Test
    @Order(5)
    public void deleteCardRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();
        try {

            for (int i=0; i<cardIds.size(); i++){
                Response response = trelloCardApi.deleteCard(cardIds.get(i));
                assertEquals(200,response.statusCode());
            }
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in deleteCardRequest ");
        }
    }

    @Test
    @Order(6)
    public void deleteBoardRequest(){
        TrelloBoardApi trelloBoardApi = new TrelloBoardApi();

        try {
                Response response = trelloBoardApi.deleteBoard(boardId);
                assertEquals( 200,response.statusCode());
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in deleteBoardRequest ");
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void deleteOrganizationRequest(){
        TrelloOrganizationApi trelloOrganizationApi = new TrelloOrganizationApi();

        try {
            Response response = trelloOrganizationApi.deleteOrganization(orgId);
            assertEquals( 200,response.statusCode());
        }catch(NullPointerException e){
            logger.error("NullPointerException Exception in deleteOrganizationRequest ");
        }
    }

    @Test
    public void createBoardInvalidPostRequest(){
        TrelloBoardApi trelloBoardApi = new TrelloBoardApi();
        Response response = trelloBoardApi.createBoard(null);

        assertEquals( 400,response.statusCode());
        assertTrue(response.getContentType().contains("text/plain"));
        assertEquals( "invalid value for name",response.then().extract().body().asString());
    }

    @Test
    public void createCardInvalidPostRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();
        Response response = trelloCardApi.createCard(boardId,null);

        assertEquals( 400,response.statusCode());
        assertTrue(response.getContentType().contains("text/plain"));
        assertEquals( "invalid value for idList",response.then().extract().body().asString());
    }

    @Test
    public void updateCardInvalidPutRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();
        Response response = trelloCardApi.updateCard("12345");

        assertEquals( 400,response.statusCode());
        assertTrue(response.getContentType().contains("text/plain"));
        assertEquals( "invalid id",response.then().extract().body().asString());
    }

    @Test
    public void updateCardNotExistingPutRequest(){
        TrelloCardApi trelloCardApi = new TrelloCardApi();

        Response response = trelloCardApi.updateCard("627e5eddd5418e8b4e2d15f8");

        assertEquals( 404,response.statusCode());
        assertTrue(response.getContentType().contains("text/plain"));
        assertEquals( "The requested resource was not found.",response.then().extract().body().asString());
    }
}
