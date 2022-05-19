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
public class Friends {
    private static String name = "friends.";
    public static String get(Requests req, String user_id){
        return req.createVKResponse(name+"get?"
                                    +"user_id="+user_id);
    }
    public static String get(Requests req, String user_id, int count, int offset){
        return req.createVKResponse(name+"get?"
                                    +"user_id="+user_id
                                    +"&count="+count
                                    +"&offset="+offset);
    }
}
