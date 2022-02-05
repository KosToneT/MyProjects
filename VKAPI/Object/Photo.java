package VKAPI.Object;

import VKAPI.Object.Attachments;
import VKAPI.ParseJSON;

/**
 *
 * @author KosToneT
 */
public class Photo extends Attachments {

    String name = "photo";

    int height;
    String url;
    int width;
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    
    public String getName() {
        return name;
    }
    public String getUrl(){
        return url;
    }

    @Override
    public Photo parseAttach(String answer) {
        try {
            answer = ParseJSON.getArgs(name, answer);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Фото не обнаружено");
        }
        this.id = Integer.parseInt(ParseJSON.getArgs("id", answer));
        this.owner_id = Integer.parseInt(ParseJSON.getArgs("owner_id", answer));
        
        try{
            String sizes = ParseJSON.getArgs("sizes", answer);
            //String url = ParseJSON.getArgs("url", sizes);
            
            int end=-1;
            while((end = ParseJSON.findEnd(sizes, '{'))>1){
                
                if(height <= Integer.parseInt(ParseJSON.getArgs("height", sizes))){
                    height = Integer.parseInt(ParseJSON.getArgs("height", sizes));
                    url = ParseJSON.getArgs("url", sizes);
                    
                    try{
                        String width = ParseJSON.getArgs("width", sizes);
                        this.width = Integer.parseInt(width.substring(0, width.length()-1));
                    }catch(Exception ex){
                        
                    }
                    
                }
                sizes = sizes.substring(end+1);
                
                
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        

        
        
        
        return this;
    }
    
    
    public String toPost(){
        return name + owner_id + "_" + id;
    }
    @Override
    public String toString() {
        return name + owner_id + "_" + id;
    }

}