package com.mycompany.musicplay;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;   


    


/**
 *
 * @author Overthink
 */
public class Common {
    public static void main(String[] args){
        new GUI();
        
        
    }
}
class GUI extends JFrame{
    Canvas canvas;
    File file = new File("C:\\Users\\Overthink\\Desktop\\aurora1.wav");
    JButton btn;
    JButton btn1;
    JButton btn2;
    Box box;
    Box box1;

    
    public GUI(){
        super("MusicPlayer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        box = Box.createVerticalBox();
        box.add(canvas = new Canvas());
        GUI gui = this;
        canvas.setDropTarget(new DropTarget(){
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List)(evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    file = droppedFiles.get(0);
                    gui.setTitle(file.getName());
                    //System.out.println(droppedFiles);
                    for (File file : droppedFiles) {
                        System.out.println(file);
                    }
                } catch (Exception ex) {
                    
                }
            }
        });
        
        
        

        box1 = Box.createHorizontalBox();
        btn = new JButton("Pause");
        btn1 = new JButton("Load");
        Player player = new Player();
        
        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (player.pause) {
                    btn.setText("Pause");
                }else{
                    btn.setText("Play");
                }
                player.pause = !player.pause;
               
            }
        });
        btn.setEnabled(false);
        btn.enableInputMethods(false);
        btn1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
                if(player.playing){
                    
                    player.unload();
                    player.pause = true;
                    player.playing = false;
                    btn.setEnabled(false);
                }else{
                   
                    player.load();
                    player.pause = false;
                    player.playing = true;
                    btn.setEnabled(true);
                }
            }
        });
        
        JTextField txt = new JTextField("1.0");
        
        btn2 = new JButton("SetValue");
        btn2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    edit = Float.parseFloat(txt.getText());
                }catch (Exception ed) {
                }
                
                
                
               
            }
        });
        txt.setMaximumSize(new Dimension(100,100));
        
        JLabel lbl = new JLabel("TEXT");
        canvas.addMouseListener(new MouseAdapter() {
            Point oldPoint;
            int mode=0;
            public void mousePressed(MouseEvent e) {
                if(mode ==0){
                    oldPoint = e.getPoint();
                    mode++;
                }
            }
            public void mouseReleased(MouseEvent e){
                mode =0;
                Point newPoint = e.getPoint();
                Point p = new Point(newPoint.x-oldPoint.x, newPoint.y - oldPoint.y);
                
                
                Point buf = getLocation();
                setLocation(buf.x + p.x, buf.y + p.y);
                oldPoint = newPoint;
                
            }
        });
       
        box1.add(txt);
        box1.add(btn2);
        box1.add(btn);
        box1.add(btn1);
        box1.add(lbl);
        box.add(box1);
        
        
        setContentPane(box);
        setVisible(true);
        player.start();
        
        
        
        
    }
    float edit=1;
    class Player extends Thread{
        boolean pause = true;
        boolean playing = false;
        SourceDataLine sdl;
        FileInputStream fis;        
        public void load(){
            try {
                fis = new FileInputStream(file);
                
                //AudioFormat af = getAudioFormat();
                AudioFormat af = AudioSystem.getAudioFileFormat(file).getFormat();
                sdl = AudioSystem.getSourceDataLine(af);
                sdl.open();
                sdl.start();
            } catch (Exception ex) {
            } 
        }
        public void unload(){
            try {
                sdl.close();
                fis.close();
            } catch (IOException ex) {
            }
        }
        
        
       
        public void run(){
            int buf;
            byte tempBuff[] = new byte[256];
            BufferStrategy bs;
            while(true){
                if((bs = canvas.getBufferStrategy())==null){
                    canvas.createBufferStrategy(2);
                }else{
                    Graphics g = bs.getDrawGraphics();
//                    for(Component i:canvas.getParent().getParent().getComponents()){
//                       
//                        i.setBackground(color[col]);
//                    }
                    btn.setBackground(color[col].brighter().brighter().brighter());
                    btn1.setBackground(color[col].brighter().brighter().brighter());
                    btn2.setBackground(color[col].brighter().brighter().brighter());
                    box.setBackground(color[col].darker().darker().darker());
                    box1.setBackground(color[col].darker().darker().darker());
                    
                    
                    
                    g.setColor(color[col].darker().darker().darker());
                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    
                    if(!pause){

                        try {
                            if((buf = fis.read(tempBuff))!=-1){
                                edit(tempBuff, edit, g);
                                draw(g, tempBuff);
                                sdl.write(tempBuff, 0, buf);
                                
                                
                                
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    bs.show();
                }
                
                
                
                
                
                
                
                
                
            }
        }
        int tickasd=0;
        boolean left= false;
        int size_oval = 0;
        int col=0;
        public void edit(byte tempBuffer[], float value, Graphics g){
            int check = 0;
            for(int i=1; i<tempBuffer.length; i++){
                
                if(Math.abs(tempBuffer[i]-tempBuffer[i-1])>200){
                    left = !left;
                    check++;
                }
                
                if(i%4<2){
                    //Левое ухо
                    if(left){
                        //tempBuffer[i] =0;
                    }
                    
                }else{
                    //Правое ухо
                    if(!left){
                       //tempBuffer[i] =0;
                    }
                }
                tempBuffer[i] *= value;
            }
            
            g.setColor(color[col]);
            if(check>32){
                size_oval =250;
                if(++col>6)col =0;
                
            }
            if(size_oval>0){
                size_oval--;
            }
            g.fillOval(canvas.getWidth()/2-size_oval/2, canvas.getHeight()/2-size_oval/2, size_oval, size_oval);   
//            tickasd++;
//            if(tickasd>=1250){
//                left = !left;
//                tickasd =0;
//            }
            
            
        }
        
        
        private LinkedList<Integer> list = new LinkedList<Integer>();
        int znach[] = new int[2];
        Color color[] = new Color[7];
        {
            list.add(0);
            color[0] = Color.red;
            color[1] = Color.orange;
            color[2] = Color.yellow;
            color[3] = Color.green;
            color[4] = Color.cyan;
            color[5] = Color.blue;
            color[6] = Color.MAGENTA;
        }
        
            
       
        int tick=0;
        public void draw(Graphics g, byte tempBuffer[]){
            if(tick++>=10){
                
                if(list.size()>256){
                    list.removeFirst();
                }
                int buf = sredArif(tempBuffer);
                znach[0] = znach[1];
                znach[1] = buf;
                list.add(Math.abs(znach[1]-znach[0]));
                //list.add(buf);
                tick=0;
            }
            
            
            int x =0;
            int size = canvas.getHeight();
            int size_w = canvas.getWidth();
            
            float sizeW = size_w/(list.size()*1.0f);
            g.setColor(Color.red);
            GradientPaint gp = new GradientPaint(0,(int)(canvas.getHeight()*(0.9)), color[color.length-col-1], 0,canvas.getHeight(), color[col]); 
            Graphics2D g2d = (Graphics2D)g;
            g2d.setPaint(gp);
            for(int i:list){
                
                int sizeH = Math.abs(i);
                
                g2d.fillRect((int)sizeW*x, size-sizeH, (int)sizeW, sizeH);
                x++;
            }
        }
        public int sredArif(byte tempBuffer[]){
            int g  = 0;
            for(byte i:tempBuffer){
                g +=i;
            }
            g = g/tempBuffer.length;
            return g;
        }
        public AudioFormat getAudioFormat(){
            float sampleRate = 44100;
            int sampleSizeInBits = 16;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = false;
            return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        }
    }
}