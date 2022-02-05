package VKAPI.Object;

public class Group {
    String domain;
    int id;
    String name;
    String screen_name;
    boolean is_closed;
    String photo_200;
    public Post getLastPost(Manager manager){
        return manager.getPost(domain, 0);
    }
    public Post getPost(Manager manager, int offset){
        return manager.getPost(domain, offset);
    }
    public int getId(){
        return id;
    }


}
