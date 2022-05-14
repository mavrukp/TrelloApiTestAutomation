package api;

import org.json.JSONObject;

import java.util.Random;

public class TrelloBase {
     static final String BASEURL = "https://api.trello.com";
     static final String key = System.getProperty("key");
     static final String token = System.getProperty("token");

     String createOrgJsonBody(){
          JSONObject jo = new JSONObject();
          jo.put( "displayName", "TestOrg" + new Random().nextInt(10));
          jo.put( "teamType", "engineering-it");
          return jo.toString();
     }

     String createBoardJsonBody(String id){
          JSONObject jo = new JSONObject();
          if (id != null) {
               jo.put("name", "TestBoard" + new Random().nextInt(10));
          }else if (id == null){
               jo.put("name", "");
          }
          jo.put("idOrganization", id);
          jo.put("defaultLists", true);
          return jo.toString();
     }

     String createCardJsonBody(String boardId, String listId){
          JSONObject jo = new JSONObject();
          jo.put( "idBoard", boardId);
          jo.put( "idList", listId);
          jo.put( "name", "test card name"+ new Random().nextInt(10));
          return jo.toString();
     }
}
