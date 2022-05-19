/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VKAPI;

/**
 *
 * @author KosToneT
 */
public class Wall {
    private static final String name = "wall.";
    public static  String createComment(Requests requests, String owner_id, String post_id, String message,int guid ){
     return requests.createVKResponse(name+"createComment?owner_id="+owner_id
                                        +"&post_id="+post_id
                                        +"&message="+message
                                        +"&guid="+guid);
        
    }
    public static  String createComment(Requests requests, String owner_id,String from_group, String post_id, String message,int guid ){
    return requests.createVKResponse(name+"createComment?owner_id="+owner_id
                                        +"&from_group="+from_group
                                        +"&post_id="+post_id
                                        +"&message="+message
                                        +"&guid="+guid);
        
    }
    public static String createComment(Requests requests,String owner_id, String post_id, String from_group, String message,String reply_to_comment,String attachments, String sticker_id, int guid ){
        return requests.createVKResponse(name+"createComment?owner_id="+owner_id
                                        +"&post_id="+post_id
                                        +"&message="+message
                                        +"&guid="+guid);
        
    }
    public static String get(Requests requests, String owner_id, String domain, int offset, int count){
        return requests.createVKResponse(name+"get?owner_id="+owner_id
                                             +"&domain="+domain
                                             +"&offset="+offset
                                             +"&count="+count);
    }
    public static String getComment(Requests requests, String owner_id, String comment_id){
         return requests.createVKResponse(name+"getComment?owner_id="+owner_id
                                              +"&comment_id="+comment_id);
    }
    public static String getComments(Requests requests,String owner_id, String post_id, boolean need_likes,int start_comment_id, int offset, int count, String sort){
        return requests.createVKResponse(name+"getComments?owner_id="+owner_id
                                             +"&post_id="+post_id
                                             +"&need_likes="+ (need_likes?"1":"0")
                                             +"&start_comment_id="+start_comment_id
                                             +"&offset="+offset
                                             +"&count="+count
                                             +"&sort="+sort);
    }
    /**
     * Создает пост
     * attachmentsобъект или несколько объектов, приложенных к записи.
        К записи можно приложить медиа или ссылку на внешнюю страницу. Если объектов несколько, их нужно указать через запятую ",".

        Формат описания медиа-приложения:
        <type><owner_id>_<media_id>

        <type> — тип медиа-приложения:
        photo — фотография;
        video — видеозапись ;
        audio — аудиозапись;
        doc — документ;
        page — wiki-страница;
        note — заметка;
        poll — опрос;
        album — альбом;
        market — товар;
        market_album — подборка товаров;
        audio_playlist — плейлист с аудио.
        <owner_id> — идентификатор владельца медиа-приложения (обратите внимание, если объект находится в сообществе, значение должно быть отрицательным числом).

        <media_id> — идентификатор медиа-приложения.

        Формат описания ссылки:
        <protocol><URL>

        <protocol> — протокол HTTP или HTTPS.
        <URL> — оставшаяся часть URL.

        Формат для перечисления объектов:
        <type><owner_id>_<media_id>,<type><owner_id>_<media_id>,<protocol<URL>

        Обратите внимание, можно указать несколько медиа-приложений, но только одну ссылку. Если указать больше одной ссылки, будет возвращена ошибка.

        Примеры:
        photo100172_166443618,photo-1_265827614
        photo66748_265827614,https://example.ru
        https://example.ru

        Параметр attachments является обязательным, если не задано значение message.
        список слов, разделенных через запятую
     */
    public static String post(Requests requests,String owner_id, boolean friends_only, boolean from_group, String message, String attachments,boolean signed, int guid, boolean close_comments){
        return requests.createVKResponse(name+"post?owner_id=-"+owner_id
                                             +"&friends_only="+Requests.booleanTonumStr(friends_only)
                                             +"&from_group="+Requests.booleanTonumStr(from_group)
                                             +"&message="+message
                                             +"&attachments="+ attachments
                                             +"&signed="+Requests.booleanTonumStr(signed)
                                             +"&guid="+guid
                                             +"&close_comments="+Requests.booleanTonumStr(close_comments));
    }
    
    
}
