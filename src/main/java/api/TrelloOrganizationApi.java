package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrelloOrganizationApi extends TrelloBase{
    final static Logger logger = LoggerFactory.getLogger(TrelloOrganizationApi.class);

    public Response createOrganization() {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","application/json")
                    .header("Accept","application/json" )
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .body(createOrgJsonBody())
                    .baseUri(BASEURL)
                    .basePath("/1/organizations/")
                    .when()
                    .post();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in post request ");
            //e.printStackTrace();
            return response;
        }
        return response;
    }

    public Response deleteOrganization(String orgId) {

        Response response = null;

        try {
            response = RestAssured.given()
                    .header("Content-Type","text/plain")
                    .header("Accept","application/json" )
                    .pathParam("id",orgId)
                    .queryParam("key", key)
                    .queryParam("token", token)
                    .baseUri(BASEURL)
                    .basePath("/1/organizations/{id}")
                    .when()
                    .delete();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException Exception in delete request ");
            return response;
        }
        return response;
    }
}
