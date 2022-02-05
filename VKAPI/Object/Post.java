package VKAPI.Object;

public class Post {
    int id;
    int from_id;
    int date;
    int owner_id;
    boolean marked_as_ads;
    String text;
    Attachments attachments;
    
    public Attachments getAttachments(){
        return attachments;
    }
    
    public int getDate(){
        return date;
    }
    @Override
    public String toString(){
        return "id: "+id+"\n"
                +"date: "+date+"\n"
                +"attach: "+attachments;
    }
    

}
