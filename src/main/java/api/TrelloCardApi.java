package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TrelloCardApi extends TrelloBase{

    final static Logger logger = LoggerFactory.getLogger(TrelloCardApi.class);

    public Response createCard(String boarddId, String idList) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","application/json")
                    .header("Accept","application/json" )
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .body(createCardJsonBody(boarddId,idList))
                    .baseUri(BASEURL)
                    .basePath("/1/cards/")
                    .when()
                    .post();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in post request ");
            //e.printStackTrace();
            return response;
        }
        return response;
    }

    public Response updateCard(String cardId) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","text/plain")
                    .header("Accept","application/json" )
                    .pathParam("id",cardId)
                    .queryParam("name", "new name" + new Random().nextInt(10))
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .baseUri(BASEURL)
                    .basePath("/1/cards/{id}")
                    .when()
                    .put();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in put request ");
            return response;
        }
        return response;
    }

    public Response deleteCard(String cardId) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","text/plain")
                    .header("Accept","application/json" )
                    .pathParam("id",cardId)
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .baseUri(BASEURL)
                    .basePath("/1/cards/{id}")
                    .when()
                    .delete();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in delete request ");
            return response;
        }
        return response;
    }
}
