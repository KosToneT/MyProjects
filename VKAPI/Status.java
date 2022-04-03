package VKAPI;

public class Status {
    private static String name = "status.";
    public static String get(Requests req, String user_ids){
        return req.createVKResponse(name+"get?"
                +"user_id="+user_ids);
    }
    /**
     * Необходимо text закодировать с помощью Requesrs.encodeTextFromHttp();
     * Need endcode text with Requesrs.encodeTextFromHttp();
     * @param req 
     * @param user_ids
     * @param text
     * @return
     */
    public static String set(Requests req, String user_ids, String text){
        return req.createVKResponse(name+"set?"
                +"text="+text
                +"&user_id="+user_ids);
    }

}
