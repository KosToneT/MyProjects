package VKAPI;

public class Groups {
    private static String name = "groups.";
    public static String getById(Requests req, String group_id){
        return req.createVKResponse(name+"getById?"
                +"group_ids="+group_id);
                //"&group_id="+group_id);
    }
}
