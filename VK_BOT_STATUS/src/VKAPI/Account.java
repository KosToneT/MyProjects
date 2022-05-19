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
public class Account {
    private static String name = "account.";
    public static String ban(Requests requests, String owner_id){
        return requests.createVKResponse(name+"ban?" + "owner_id="+owner_id);
    }
    public static String changePassword(Requests requests, String restore_sid, String change_password_hash, String old_password, String new_password){
        return requests.createVKResponse(name+"changePassword?restore_sid="+restore_sid
                                           +"&change_password_hash="+change_password_hash
                                           +"&old_password="+old_password
                                           +"&new_password="+new_password);
    }
    public static String getActiveOffers(Requests requests, int offset, int count){
        return requests.createVKResponse(name+"getActiveOffers?offset="+offset+"&count="+count);
    }
    public static String getAppPermissions(Requests requests, String user_id){
        return requests.createVKResponse(name+"getAppPermissions?"+"user_id="+user_id);
    }
    public static String getBanned(Requests requests, int offset, int count){
        return requests.createVKResponse(name+"getBanned?"+"offset="+offset+"&count="+count);
    }
    public static String getCounters(Requests requests, String filter){
        return requests.createVKResponse(name+"getCounters?"+"filter="+filter);
    }
    public static String getInfo(Requests requests, String fields){
        return requests.createVKResponse(name+"getInfo?"+"fields="+fields);
    }
    public static String getProfileInfo(Requests requests){
        return requests.createVKResponse(name+"getProfileInfo?");
    }
    public static String getPushSettings(Requests requests, String device_id){
        return requests.createVKResponse(name+"getPushSettings?"+"device_id="+device_id);
    }
    public static String registerDevice(Requests requests, String token, String device_model, String device_year, String device_id, String settings, String sandbox){
        return requests.createVKResponse(name+"registerDevice?"
                                       +"token="+token
                                       +"&device_model="+device_model
                                       +"&device_year="+device_year
                                       +"&device_id="+ device_id
                                       +"&settings="+ settings
                                       +"&sandbox="+sandbox);
    }
    public static String saveProfileInfo(Requests requests, String first_name, String last_name, String maiden_name, String screen_name, String cancel_request_id, String sex, String relation,String relation_partner_id, String bdate, String bdate_visibility, String home_town, String country_id, String city_id, String status){
        return requests.createVKResponse(name+"saveProfileInfo?"
                                       +"first_name="+first_name
                                       +"&last_name="+last_name
                                       +"&maide_name="+maiden_name
                                       +"&screen_name="+screen_name
                                       +"&cancel_request_id="+cancel_request_id
                                       +"&sex="+sex
                                       +"&relation="+relation
                                       +"&relation_partner_id="+relation_partner_id
                                       +"&bdate="+bdate
                                       +"&bdate_visibility="+bdate_visibility
                                       +"&home_town="+home_town
                                       +"&country_id="+country_id
                
        );
    }
    public static String setOnline(Requests requests){
        return requests.createVKResponse(name+"setOnline?");
    }
    public static String setOffline(Requests requests){
        return requests.createVKResponse(name+"setOffline?");
    } 
}
