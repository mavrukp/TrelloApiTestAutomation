package api;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrelloBoardApi extends TrelloBase {

    final static Logger logger = LoggerFactory.getLogger(TrelloBoardApi.class);

    public TrelloBoardApi(){
        RestAssured.config= RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout",10000)
                        .setParam("http.connection.timeout", 10000));
        RestAssured.useRelaxedHTTPSValidation();
    }

    public Response createBoard(String id) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","application/json")
                    .header("Accept","application/json" )
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .body(createBoardJsonBody(id))
                    .baseUri(BASEURL)
                    .basePath("/1/boards/")
                    .when()
                    .post();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in post request ");
            //e.printStackTrace();
            return response;
        }
        return response;
    }


    public Response readListsFromBoard(String id) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","text/plain")
                    .header("Accept","application/json" )
                    .pathParam("id",id)
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .baseUri(BASEURL)
                    .basePath("/1/boards/{id}/lists")
                    .when()
                    .get();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in post request ");
            return response;
        }
        return response;
    }

    public Response deleteBoard(String boardId) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","text/plain")
                    .header("Accept","application/json" )
                    .pathParam("id",boardId)
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .baseUri(BASEURL)
                    .basePath("/1/boards/{id}")
                    .when()
                    .delete();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in post request ");
            return response;
        }
        return response;
    }
}
