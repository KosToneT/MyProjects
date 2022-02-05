package VKAPI;


public class Messages {
    private static String name = "messages.";
    public static String getHistory(Requests requests, int offset, int count, String user_id){
        return "test";
    }
    public void sendMessage(Requests requests,String user_id, int random_id, String message){
        try{
           //message = URLEncoder.encode(message, "UTF-8");
           requests.createVKResponse("messages.send?"
                            + "user_id="+user_id
                            + "&random_id="+random_id
                            + "&message="+message
                    ); 
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}
