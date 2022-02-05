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
public class Users {
    private static String name = "users.";
    public static String get(Requests req, String user_ids){
        return req.createVKResponse(name+"get?"
                                    +"user_id="+user_ids);
    }
}
