import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import  java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class AudioPlayer {
    public static void main(String[] args) throws Exception {
        new GUI();
    }
}
class GUI extends JFrame{
    Canvas canvas;
    JSlider s_timeline;
    JButton b_pause;
    JButton b_stop;
    JButton b_next;
    JCheckBox c_draw;
    JSlider s_volume;
    AudioReader ar;
    boolean play = true;

    boolean drawBoxMusic = false;
    
    public GUI(){
        super("AudioPlayer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);

     
        canvas = new Canvas();
        canvas.setMaximumSize(new Dimension(400,400));
        
        Box boxList = Box.createVerticalBox();
        ar = new AudioReader(16);
        Manager manager = new Manager(this, ar,boxList, Color.CYAN);
        manager.addMusic(new Music("C:\\Users\\Overthink\\Desktop\\aurora1.wav"));
        manager.nextSong();

        //JButton b_load = new JButton("Load");
        b_pause = new JButton("Pause");
        b_stop = new JButton("Stop");
        b_next = new JButton("Next");
        c_draw = new JCheckBox("Draw");
        c_draw.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoxMusic = c_draw.isSelected();
                
            }

        });

        b_next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.nextSong();
            }
        });

        JButton b_mute = new JButton("+");
        b_mute.addActionListener(new ActionListener() {
            float vol = 0;
            boolean state= false;
            @Override
            public void actionPerformed(ActionEvent e) {
                state = !state;
                if(state){
                    vol = ar.volume;
                    ar.volume = 0;  
                }else{
                    ar.volume = vol;
                }
                              
            }
            
        });
       
        s_volume = new JSlider(0,100,100);
        s_volume.setMaximumSize(new Dimension(80,30));
        s_timeline = new JSlider(0, 100,0);

       // Box jp = Box.createVerticalBox();
        
        Box jp1 = Box.createHorizontalBox();
        Box jp2 = Box.createHorizontalBox();


       


        //jp.add(canvas);
        //jp1.add(b_load);
        jp1.add(b_pause);
        jp1.add(b_stop);
        jp1.add(b_next);
        jp1.add(c_draw);
        jp1.add(b_mute);
        jp1.add(s_volume);
        jp1.add(Box.createHorizontalGlue());

        jp2.add(s_timeline);

        Box vertMain = Box.createVerticalBox();
        Box hor1 = Box.createHorizontalBox();

        hor1.add(canvas);
        hor1.add(boxList);
        

        vertMain.add(hor1);
        vertMain.add(jp2);
        vertMain.add(jp1);

        setContentPane(vertMain);


        //jp.add(jp2);
        //jp.add(jp1);

        //setContentPane(jp);


        this.setBackground(Color.BLACK);
        s_timeline.setBackground(Color.GRAY);
        s_timeline.setForeground(Color.GREEN);

        s_volume.setBackground(Color.GRAY);

        //boxList.setBackground(Color.WHITE);
        //boxList.setForeground(Color.CYAN);



        this.setDropTarget(new DropTarget(){
            public void drop(DropTargetDropEvent evt){
                try {
                    evt.acceptDrop(1);
                    List<File> droppedFile = (List)(evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    manager.addMusic(new Music(droppedFile.get(0).getAbsolutePath()));
                    //ar.Load(droppedFile.get(0).getAbsolutePath());
                } catch (Exception e) {
                    //TODO: handle exception
                }
            }
        });


        //SwingUtilities.updateComponentTreeUI(f);

        setVisible(true);

        
        
        new Drawing().start();

    }
    class Drawing extends Thread{

        public void run(){
            
            try {
                s_timeline.setMaximum(ar.getAllPlay());
                s_timeline.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                       ar.setPlayNow(s_timeline.getValue());
                    }
                    
                });
                b_pause.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        play = !play;
                        ar.play = !ar.play;
                    }
                    
                });
                b_stop.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        play = false;
                        ar.play = false;
                        ar.setPlayNow(0);
                        
                    }

                });
                s_volume.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                       ar.volume = s_volume.getValue()/100.f;
                        
                    }
                });

                ar.start();
            } catch (Exception e) {


                //TODO: handle exception
            }


            //Gistogram gs = new Gistogram(0,0,50,100, Color.GREEN);

            MusicLine musicDanceLine = new MusicLine(2048, canvas.getSize(), Color.CYAN);
            PulseCircle pulseCircle = new PulseCircle(100, Color.CYAN,2048, canvas.getWidth()/2, canvas.getHeight()/2);
            PictureBoxLine pictureBoxLine = new PictureBoxLine(canvas, (int)Math.pow(2,14));

            BufferStrategy bs;

            int keyFrame=0;
            while(true){
                bs=canvas.getBufferStrategy();
                if(bs==null){
                    canvas.createBufferStrategy(2);
                    continue;
                }
                Graphics g = bs.getDrawGraphics();
                g.setColor(new Color(0,0,0));
                g.fillRect(0, 0, canvas.getWidth(),  canvas.getHeight());


                //==============================KEY FRAME==================================
                keyFrame++;
                if(keyFrame>=30){
                    s_timeline.setMaximum(ar.getAllPlay());
                    s_timeline.setValue(ar.getPlayNow());
                    b_pause.setText(ar.play?"Play": "Pause");
                    keyFrame=0;
                }
                //==============================KEY FRAME==================================



                //GRAPHICS beatifule line
                if(play){//if play music then we added new value 
                    int current = (int) Statistic.max(ar.tempBuff);
                    current = Math.abs(current);
                    musicDanceLine.add(current);
                    pulseCircle.add(current);
                }

                if(drawBoxMusic)pictureBoxLine.draw(g, ar);
                else{
                    musicDanceLine.draw(g);
                pulseCircle.draw(g);
                }

                
                bs.show();
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}




class Statistic{
    public static double average(byte[] array){
        double s =0;
        for(int i=0; i<array.length; i++){
            s += (double)array[i];
        }
        s /= array.length;
        return s;
    }
    public static byte max(byte[] array){
        int s= 0;
        for(int i=0; i<array.length;i++){
            if(Math.abs(array[s])<Math.abs(array[i])){
                s = i;
            }
        }
        return array[s];
    }
}

class PictureBoxLine{
    double boxW, boxH;
    int sizeTemp;
    int sizeInRow;
    double sizeW, sizeH;
    public PictureBoxLine(Canvas cs, int sizeTemp){
        this.sizeTemp = sizeTemp;
        boxW = cs.getWidth();
        boxH = cs.getHeight();
        sizeInRow = (int)(Math.sqrt(sizeTemp));
        sizeW = boxW/sizeInRow;
        sizeH = boxH/sizeInRow;

    }
    public void draw(Graphics g, AudioReader ar){
        int nowPlay = ar.getPlayNow();

        
        int copyFrom =  Math.min(nowPlay*ar.tempBuff.length, ar.dataMusic.length);
        int copyTo = Math.min(nowPlay*ar.tempBuff.length+sizeTemp, ar.dataMusic.length);
        if(copyTo==ar.dataMusic.length){
            return;
        }
        byte [] temp = Arrays.copyOfRange(ar.dataMusic, copyFrom, copyTo);
        
        
        
        //DRaw picture sound frame
        for(int i=0; i<sizeInRow; i++){
            for(int j=0; j<sizeInRow; j++){
                int c = Math.abs(temp[i*sizeInRow+j]);
                
                int green=0;
                if(j==0){
                    green = Math.abs(temp[i*sizeInRow+j+1]);
                }else if(j==sizeInRow-1){
                    green = Math.abs(temp[i*sizeInRow+j-1]);

                }else{
                    green = Math.abs(temp[i*sizeInRow+j+1])+Math.abs(temp[i*sizeInRow+j-1]);
                }
                green = Math.max(Math.min(green, 254),0);

                int blue=0;
                if(i==0){
                    blue = Math.abs(temp[(i*sizeInRow+1)+j]);
                }else if(i==sizeInRow-1){
                    blue = Math.abs(temp[(i*sizeInRow-1)+j]);

                }else{
                    blue =  Math.abs(temp[(i*sizeInRow+1)+j]) + Math.abs(temp[(i*sizeInRow-1)+j]);
                }
                blue = Math.max(Math.min(blue, 254),0);
                g.setColor(new Color(green, blue, c));
                //index++;
                g.fillRect((int)(j*sizeW), (int)(i*sizeH), (int)(sizeW<1?1:sizeW), (int)(sizeH<1?1:sizeH));
            }
        }
    }
}

class PulseCircle{
    int countSides = 256;
    LinkedList<Integer> values = new LinkedList<>();
    double radius;
    Color col;
    double posX, posY;
    public PulseCircle(){}
    public PulseCircle(double radius, Color col, int countSides, double posX, double posY){
        this.radius = radius;
        this.col = col;
        this.posX = posX;
        this.posY = posY;
        this.countSides = countSides;
        for(int i=0; i<countSides;i++){
            values.add(0);
        }

    }
    public void add(int value){
        values.removeFirst();
        values.add(value);
    }
    public void draw(Graphics g){
        g.setColor(col);

        double Step = Math.PI*2.0/countSides;
        int count = 0;
        int xPoints[] = new int[countSides+1];
        int yPoints[] = new int[countSides+1];

        for(float a=0; a<Math.PI*2.0;a+=Step){
            float v = values.get(count);
            v = v/255;
            xPoints[count] = (int)(radius*Math.cos(a)+posX+Math.cos(a)*v*radius);
            yPoints[count++] = (int)(radius*Math.sin(a)+posY+Math.sin(a)*v*radius);
        }

        g.drawPolyline(xPoints, yPoints, countSides);
        
    }
    

}

class MusicLine{
    int countPoint;
    LinkedList<Integer> xPoints = new LinkedList<>();
    LinkedList<Integer> yPoints = new LinkedList<>();

    Dimension size;
    Color col;

    public MusicLine(int countPoint, Dimension cs, Color col){
        this.countPoint = countPoint;
        this.size = cs;
        this.col = col;

        for(int i=0; i<countPoint; i++){
            this.xPoints.add((int)((cs.getWidth()/countPoint)*i));
            this.yPoints.add((int)(size.getHeight()/2));
        }
       
    }
    public void add(int value){
        yPoints.removeFirst();
        yPoints.add((int)(size.getHeight()/2+value));
    }
    public void draw(Graphics g){
        g.setColor(col);

        int posX[] = xPoints.stream().mapToInt(i->i).toArray();
        int posY[] = yPoints.stream().mapToInt(i->i).toArray();
        g.drawPolyline(posX, posY, countPoint);
        
    }



}

class Gistogram{
    int posX, posY;
    int width, height;
    Color color;
    float force;
    public Gistogram(){

    }
    public Gistogram(int posX, int posY, int width, int height, Color color){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.color = color;
    }
    public void update(float force){
        if(force>this.force){
            this.force = force;
        }
        this.force -=0.005;
        //this.force = force;
    }
    public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(posX, posY, width, (int)(height*force));
    }

}



class Manager{
    Box musicL;
    ArrayList<Music> musicList = new ArrayList<>();
    int nowSong = 0;

    Color colorText;

    GUI gui;
    AudioReader ar;

    char endType = 0;

    public Manager(GUI gui, AudioReader ar, Box musicL, Color colorText){
        this.gui = gui;
        this.ar = ar;
        this.musicL = musicL;
        this.colorText = colorText;
    }
    public void addMusic(Music music){
        musicL.removeAll();
        musicList.add(music);

        JLabel lbl = new JLabel("Название песен");
        lbl.setForeground(colorText);
        musicL.add(lbl);
        for(Music i:musicList){

            String name = i.getName();
            int len = name.length()>15?15:name.length();
            
            lbl = new JLabel(i.getName().substring(0, len));
            lbl.setForeground(colorText);
            lbl.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    try {
                        if(ar.isLoad())
                            ar.unLoad();
                        gui.setTitle(i.getName());
                        ar.load(i);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            musicL.add(lbl);
        }
        musicL.add(Box.createVerticalGlue());
        gui.revalidate();
    }

    public void endSong(){
        switch(endType){
            case 1:
                ar.setPlayNow(0);
                break;
            case 2:
                try {
                    nextSong();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    public void nextSong(){
        Music music = getNextSong();
        nowSong++;
        if(nowSong>musicList.size()){
            nowSong=0;
        }
        try {
            if(ar.isLoad())
                ar.unLoad();
            ar.Load(music.getPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Music getNextSong(){
        if(nowSong+2>musicList.size()){
            return musicList.get(0);
        }else{
            return musicList.get(nowSong+1);
        }
    }
}
class Music{
    private String path;
    private String name;
    private String format;
    private String duration;
    public Music(String path){
        File file = new File(path);
        this.path = file.getAbsolutePath();
        this.name = file.getName();
        int t = name.lastIndexOf(".");
        this.format = name.substring(t, name.length());
        this.name = name.substring(0, t);
        this.duration = file.length()+"";
    }
    public String getName(){
        return name;
    }
    public String getPath(){
        return path;
    }
    public String getFormat(){
        return format;
    }
    
}


class AudioReader extends Thread{
    private FileInputStream fis;
    private SourceDataLine sdl;

    byte dataMusic[];
    byte tempBuff[] = new byte[256];
    
    private int allPlay;
    
    private int nowPlay = 0;
    boolean play = true;
    private boolean load = false;
    float volume =1.f;
    public void run(){
        while(true){
            if(play){
                playClip();
            }else{
                try {
                    sleep(10);
                } catch (InterruptedException e) {}
            }
        }
    }
    public AudioReader(){}
    public AudioReader(int clipSize){
        tempBuff = new byte[clipSize];
    }
    public synchronized void load(Music music) throws Exception{
        File file = new File(music.getPath());
        String format = music.getFormat();
        if(!format.equals(".wav")){
            return;
        }
        fis = new FileInputStream(file);
        AudioFormat af = AudioSystem.getAudioFileFormat(file).getFormat();
        sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        dataMusic = fis.readAllBytes();
        nowPlay = 0;
        allPlay = dataMusic.length/tempBuff.length;
        play = true;
        load = true;
    }
    public synchronized void Load(String path) throws Exception{
        File file = new File(path);
        String formatFile = file.getName();
        formatFile = formatFile.substring(formatFile.lastIndexOf("."), formatFile.length());
        fis = new FileInputStream(file);
        AudioFormat af = AudioSystem.getAudioFileFormat(file).getFormat();
        sdl = AudioSystem.getSourceDataLine(af);

        sdl.open();
        sdl.start();

        dataMusic = fis.readAllBytes();
        nowPlay = 0;
        allPlay = dataMusic.length/tempBuff.length;
        play = true;
        load = true;
    }
    public synchronized void unLoad() throws Exception{
        play = false;
        sdl.close();
        fis.close();
        load = false;
    }
    public void playClip(){
        try {
            tempBuff = Arrays.copyOfRange(dataMusic, nowPlay*tempBuff.length, nowPlay*tempBuff.length+tempBuff.length);
            nowPlay++;
            editClip(tempBuff);
            sdl.write(tempBuff,0, tempBuff.length);
        } catch (Exception e) {
        }

    }
    public void editClip(byte[] tempBuff){
        for(int i=0; i<tempBuff.length; i++){
            tempBuff[i] = (byte)(tempBuff[i]*volume);
        }
    }
    public void setPlayNow(int nowPlay){
        this.nowPlay = nowPlay;
    }
    public int getPlayNow(){
        return nowPlay;
    }
    public int getAllPlay(){
        return allPlay;
    }
    public boolean isLoad(){
        return load;
    }
}
