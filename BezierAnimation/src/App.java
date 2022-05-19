import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        new GUI();
    }
}

class GUI extends JFrame{
    Canvas canvas;
    Player pl= new Player();
    Dimension dim = new Dimension(640,480);
    public GUI(){
        super("GUI");
        setDefaultCloseOperation(3);
        setSize(dim);
        canvas = new Canvas();
        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pl.posX = e.getX();
                pl.posY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                pl.posX = e.getX();
                pl.posY = e.getY();
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        getContentPane().add(canvas);


        setVisible(true);
        new Drawing().start();
    }
    class Drawing extends Thread{
        public void run(){
            LineBezier lbs[] = new LineBezier[200];
            for(int i=0; i<lbs.length; i++){
                float offset = (float)dim.getHeight()/lbs.length;
                lbs[i] = new LineBezier(new Point(0,(int)(i*offset)), new Point((int)dim.getWidth(), (int)(i*offset)), dim);
                

                //lbs[i] = new LineBezier(new Point(0,0), new Point((int)dim.getWidth(), (int)dim.getHeight()), dim);
                lbs[i].color = new Color((int)(Math.random()*0xFFFFFF));
            }


            while(true){
                BufferStrategy bs = canvas.getBufferStrategy();
                if(bs==null){
                    canvas.createBufferStrategy(3);
                }else{
                    Graphics g = bs.getDrawGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0, 0, dim.width, dim.height);
                    g.setColor(Color.red);


                    g.fillOval(pl.posX-5, pl.posY-5, 10, 10);

                    g.setColor(Color.white);


                    for(int i=0; i<45; i+=1){
                        double offs = i*Math.PI/180;
                        g.drawLine(pl.posX, pl.posY,(int)(pl.posX+100*Math.sin(pl.angle+offs)), (int)(pl.posY+100*Math.cos(pl.angle+offs)));

                    }

                    Graphics2D g2d = (Graphics2D)g;

                    
                    Color[] colors = {Color.RED,Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.PINK};
                    float[] dist = new float[colors.length];
                    for(int i=0; i<dist.length; i++){
                        dist[i] = (float)i/(dist.length-1);
                    }
                    // for(int i=0; i<dist.length; i++){
                    //     System.out.println((float)i/(dist.length-1));
                    //     dist[i] = 1;
                    // }
                    
                   LinearGradientPaint gradient =  new LinearGradientPaint(0, 0, 0, (float)dim.getHeight(), dist, colors);
                   // GradientPaint gradient = new GradientPaint(0,0,Color.red,175,175,Color.yellow,true);

                    g2d.setPaint(gradient);
                    for(int i=0; i<lbs.length; i++){
                        lbs[i].draw(g);
                        lbs[i].update();
                    }


                    bs.show();
                    
                }
                
            }
        }
    }
}
class LineBezier{
    Point start;
    Point end;
    Player p1; 
    Player p2;
    Color color;
    public LineBezier(Point start, Point end, Dimension size){
        this.start = start;
        this.end = end;

        p1 = new Player(size);
        p2 = new Player(size);
    }
    public void draw(Graphics g){
        drawBezier(g, start, end, p1.getPoint(), p2.getPoint());
    }
    public void drawWithColor(Graphics g){
        g.setColor(color);
        drawBezier(g, start, end, p1.getPoint(), p2.getPoint());
    }
    public void update(){
        p1.stepRand();
        p2.stepRand();
    }



    private void drawBezier(Graphics g, Point p1, Point p2, Point c1, Point c2){
        Graphics2D g2d = (Graphics2D)g;
        
        int step =200;//Кол-во вспомогательных узлов
        
        ArrayList<Point> res = new ArrayList<>();
        
        for (int k = 0; k < step; k++) {            
            Point p = new Point();

            double b = getBezierBasis(0, 3, (float)k/step);
                
            p.x += p1.x * b;
            p.y += p1.y * b;

            b = getBezierBasis(3, 3, (float)k/step);
            p.x += p2.x * b;
            p.y += p2.y * b;

            b = getBezierBasis(1, 3, (float)k/step);
            p.x += c1.x * b;
            p.y += c1.y * b;

            b = getBezierBasis(2, 3, (float)k/step);
            p.x += c2.x * b;
            p.y += c2.y * b;

            res.add(p);
        }
        for(int i=0; i<res.size()-1; i++){

            Point p = res.get(i);
            Point p0 = res.get(i+1);
            g2d.drawLine(p.x, p.y, p0.x, p0.y);
        }


    }
    private double getBezierBasis(int i, int n,float t) {
        // считаем i-й элемент полинома Берштейна
        return (f(n)/(f(i)*f(n - i)))* Math.pow(t, i)*Math.pow(1 - t, n - i);
    }
    private double f(int n) {
        return (n <= 1) ? 1 : n * f(n - 1);
    }
    class Player{
        int posX, posY;
        Dimension size;
        float angle;
    
        float value=10;//Координата смещения
        float step=0.001f;//Скорость изменения
    
        float a = (float)Math.random();
        float b = (float)Math.random();
        float c = (float)Math.random();
        float d = (float)Math.random();
        float e = (float)Math.random();

        public Player(){}
        public Player(Dimension size){
            this.size = size;
        }
        public Point getPoint(){
            return new Point(posX, posY);
        }
        public void stepRand(){
            value+= step;
            float x = value;
            posX = (int)(Math.abs(Math.sin(x*a)
                        +Math.sin(x*b)
                        +Math.sin(x*c)
                        +Math.sin(x*d)
                        +Math.sin(x*e)
                        )/5.f*size.width);
            posY = (int)(Math.abs(Math.cos(x*a)
                        +Math.cos(x*b)
                        +Math.cos(x*c)
                        +Math.cos(x*d)
                        +Math.cos(x*e)                        
                        )/5.f*size.height);
        }
        public String toString(){
            return "PosX="+posX+" PosY="+posY;
        }
    }
}
class Player{
    int posX, posY;
    Dimension size;
    float angle;

    float value;//Координата смещения
    float step=0.0001f;

    float a = (float)Math.random();
    float b = (float)Math.random();
    float c = (float)Math.random();
    public Player(){}
    public Player(Dimension size){
        this.size = size;
    }
    public Point getPoint(){
        return new Point(posX, posY);
    }
    public float stepRand(){
        value+= step;
        float x = value;
        posX = (int)(Math.abs(Math.sin(x*a)+Math.sin(x*b)+ Math.sin(x*c))/3.f*size.width/2);
        posY = (int)(Math.abs(Math.cos(x*a)+Math.cos(x*b)+ Math.cos(x*c))/3.f*size.height/2);



        return 0f;
    }
    public String toString(){
        return "PosX="+posX+" PosY="+posY;
    }
}
