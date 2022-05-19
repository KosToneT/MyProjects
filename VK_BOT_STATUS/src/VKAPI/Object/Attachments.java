package VKAPI.Object;

import VKAPI.ParseJSON;

public abstract class Attachments {
    int id;
    int owner_id;
    public abstract Attachments parseAttach(String answer);
    public abstract String toPost();
    public abstract String getName();
}

class Video extends Attachments {
    String name = "video";
    public String getName() {
        return name;
    }

    @Override
    public Video parseAttach(String answer) {
        try {
            answer = ParseJSON.getArgs(name, answer);
        } catch (RuntimeException ex) {
            System.out.println("Видео нету" + ex);
            throw new RuntimeException("Видео не обнаружено");
        }
        this.id = Integer.parseInt(ParseJSON.getArgs("id", answer));
        this.owner_id = Integer.parseInt(ParseJSON.getArgs("owner_id", answer));
        return this;
    }

    @Override
    public String toString() {
        return name + owner_id + "_" + id;
    }

    @Override
    public String toPost() {
        return name + owner_id + "_" + id;
    }
}


