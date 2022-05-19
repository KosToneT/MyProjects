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
public class ParseJSON {
    public static int findEnd(String str, char start_sym){
        char end_sym=0;
        switch (start_sym){
            case '{':
                return findEnd(str);
            case '"':
                end_sym = '"';
                break;
            case '[':
                end_sym = ']';
                break;
            case '\'':
                end_sym = '\'';
                break;
            default:
                break;
        }

        int begin=0;
        int end =0;
        int end_pos=0;
        for (int i=0; i<str.length(); i++){
            if(str.charAt(i)==start_sym) begin++;
            if(str.charAt(i)==end_sym) end++;

            if((begin == end)&&(end!=0)){
                end_pos= i;
                break;

            }
        }
        return end_pos;

    }
    private static int findEnd(String str){
        int begin=0;
        int end =0;
        int end_pos=0;
        for (int i=0; i<str.length(); i++){
            if(str.charAt(i)=='{') begin++;
            if(str.charAt(i)=='}') end++;

            if(begin == end&& end!=0){
                end_pos= i;
                break;
                
            }
        }
        
        return end_pos;
    }
    private static char getEndSym(char start_sym){
        char end_sym=0;
        switch (start_sym){
            case '{':
                end_sym = '}';
                break;
            case '"':
                end_sym = '"';
                break;
            case '[':
                end_sym = ']';
                break;
            case '\'':
                end_sym = '\'';
                break;
            default:
                break;
        }
        return end_sym;
    }
    public static String getArgs(String args, String JSON){
        args = "\""+args+"\":";
        int begin = JSON.indexOf(args);
        if(begin==-1){
            System.out.println(args + "No correct argument");
            throw new RuntimeException(args + "No correct argument");
        }
        JSON = JSON.substring(begin);

        String text = JSON.substring(args.length());

        char end_sym = getEndSym(text.charAt(0));
        
        
        
        switch(end_sym){
            case ']':{
                int end = findEnd(text,'[');
                JSON = JSON.substring(args.length()+1, args.length()+end);
                break;
            }
            case '}':{
                int end = findEnd(text,'{');
                JSON = JSON.substring(args.length()+1, args.length()+end);
                break;
            }
            default:{
                if(end_sym!=0){
                    text = text.substring(1);
                    int end = text.indexOf(end_sym);
                    JSON = JSON.substring(args.length()+1, args.length()+1+end);
                }else{
                    end_sym = ',';
                    int end = text.indexOf(end_sym);
                    JSON = JSON.substring(args.length(), args.length()+end);
                }
            }
        }
        

        return JSON;
    }
}
