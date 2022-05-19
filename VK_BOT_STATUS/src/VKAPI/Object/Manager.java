package VKAPI.Object;

import VKAPI.Groups;
import VKAPI.ParseJSON;
import VKAPI.Requests;
import VKAPI.Wall;


public class Manager {
    Requests requests;
    public Manager(Requests req){
        this.requests = req;
    }

    public Group getGroup(String domain){
        return getGroup(requests, domain);
    }
    public static Group getGroup(Requests req, String domain){
        String answer = Groups.getById(req, domain);
        Group group = new Group();
        group.domain = domain;
        try{
            group.id = Integer.parseInt(ParseJSON.getArgs("id", answer));
            group.is_closed = Boolean.getBoolean(ParseJSON.getArgs("is_closed", answer));
            group.name = ParseJSON.getArgs("name", answer);
            group.screen_name = ParseJSON.getArgs("screen_name", answer);
        }catch (RuntimeException ex){
            System.out.println(answer);
        }

        return group;
    }
    public Post getPost(String domain, int offset){
        return getPost(requests, domain, offset);
    }
    public static Post getPost(Requests req, String domain, int offset){
        String answer = Wall.get(req, "", domain, offset, 1);
        Post post = new Post();
        try{
            post.id = Integer.parseInt(ParseJSON.getArgs("id", answer));
            post.from_id = Integer.parseInt((ParseJSON.getArgs("from_id", answer)));
            post.date = Integer.parseInt(ParseJSON.getArgs("date", answer));
            post.owner_id = Integer.parseInt(ParseJSON.getArgs("owner_id", answer));
            //post.marked_as_ads = Boolean.parseBoolean(ParseJSON.getArgs("marked_as_ads", answer));
            post.text = Requests.useControlSym(ParseJSON.getArgs("text", answer));
            String attachments = ParseJSON.getArgs("attachments", answer);
            try{
                String type = ParseJSON.getArgs("type", attachments);
                switch (type){
                    case "photo":
                        Photo photo = new Photo();
                        photo = (Photo)photo.parseAttach(attachments);
                        post.attachments = photo;
                        break;
                    case "video":
                        Video video = new Video();
                        video = (Video)video.parseAttach(answer);
                        post.attachments = video;
                        break;
                }
            }catch (RuntimeException ex){
                System.out.println(ex);
                throw ex;
            }
        }catch(RuntimeException ex){
            System.out.println(ex);
            throw ex;
        }
        return post;
    }

    public void sendPost(String group_id, Post post){
        sendPost(requests, group_id, post);
    }
    public static void sendPost(Requests req, String group_id, Post post){
        if(post.attachments==null){
            Wall.post(req, group_id, false, true, Requests.encodeTextFromHttp(post.text),
                    "", false,
                    (int)Math.random()*1000000,false);
        }else{
            Wall.post(req, group_id, false, true, Requests.encodeTextFromHttp(post.text),
                    post.attachments.toPost(), false,
                    (int)Math.random()*1000000,false);
        }
    }

    

    
}
