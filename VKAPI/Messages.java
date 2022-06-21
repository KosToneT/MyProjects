package VKAPI;


public class Messages {
    private static String name = "messages.";
    public static String getHistory(Requests requests, int offset, int count, String user_id){
        return requests.createVKResponse(name+"getHistory?"+
                                        "offset="+offset+
                                        "&count="+count+
                                        "&user_id="+user_id);
    }
    public static void sendMessage(Requests requests,String user_id, int random_id, String message){
        try{
           //message = URLEncoder.encode(message, "UTF-8");
           requests.createVKResponse(name+".send?"
                            + "user_id="+user_id
                            + "&random_id="+random_id
                            + "&message="+message
                    ); 
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    /*
     * offset - Смещение, необходимое для выборки определенного подмножества бесед.
     * count - Количество сообщений, которое необходимо получить (но не более 200).
     * filter - Типы бесед, которые нужно вернуть, перечисленные через запятую.
Может принимать следующие значения:
    •all — все беседы;
    •important — беседы, отмеченные важными (доступно только в сообщениях сообществ);
    •unanswered — беседы, отмеченные непрочтенными (доступно только в сообщениях сообществ);
    •unread — беседы, в которых есть непрочтенные сообщения.
    По умолчанию будут возвращены все беседы.
     */
    public static String getConversations(Requests requests, int offset, String filter){
        return requests.createVKResponse(name+"getConversations?"
                            + "offset="+offset
                            + "&count=1"
                            + "&filter="+filter);
    }
    /*
     * message_id - Уникальное id сообщения
     */
    public static String getById(Requests requests, String message_id){
        return requests.createVKResponse(name+"getById?"
                            + "message_ids="+message_id);
    }
}
