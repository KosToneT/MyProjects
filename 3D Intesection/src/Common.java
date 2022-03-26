import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class Common{
    public static void main(String[] args) throws Exception {       
        new GUI();
    }
}
class GUI extends JFrame{
    Intersection isc;
    Canvas cs;

    byte forward;
    byte right; 
    byte up;

    byte forwardAn;
    byte rightAn; 
    byte upAn;

    double speed = 5;
    double speedAn = 0.1;


    double mouseX, mouseY;
    boolean mouseHIdden=false;
    Cursor cursordef;

    GUI gui;
    public GUI(){
        super("3D");
        setSize(100, 100);
        setDefaultCloseOperation(3);
        
        getContentPane().add(cs= new Canvas());
        gui = this;

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        cursordef = gui.getContentPane().getCursor();
        cs.addMouseMotionListener(new MouseMotionListener(){
            
            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(mouseHIdden){
                    int mx = e.getX() - cs.getWidth() / 2;
                    int my = e.getY() - cs.getHeight() / 2;
                    if((Math.abs(mx)<10)){
                        
                    }else{
                        mouseX -= mx;
                        mouseY -= my;
                        try {
                            Robot r = new Robot();
                            r.mouseMove((int)(gui.getLocation().getX()+cs.getWidth()/2),(int)(gui.getLocation().getY() +cs.getHeight()/2));
                            
                        } catch (AWTException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }   
                    }                   
                }
                             
            }
            
        });
        cs.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    mouseHIdden = !mouseHIdden;
                    if(mouseHIdden){
                        gui.getContentPane().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                            cursorImg, new Point(0, 0), "blank cursor"));                
                    }else{
                        gui.getContentPane().setCursor(cursordef);  
                    }

                    
                }

                if(e.getKeyCode()==KeyEvent.VK_W){
                    forward=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_S){
                    forward=-1;
                }
                if(e.getKeyCode()==KeyEvent.VK_D){
                    right=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_A){
                    right=-1;
                }
                if(e.getKeyCode()==KeyEvent.VK_SHIFT){
                    up=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_CONTROL){
                    up=-1;
                }



                if(e.getKeyCode()==KeyEvent.VK_I){
                    forwardAn=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_K){
                    forwardAn=-1;
                }
                if(e.getKeyCode()==KeyEvent.VK_J){
                    rightAn=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_L){
                    rightAn=-1;
                }
                if(e.getKeyCode()==KeyEvent.VK_Y){
                    upAn=1;
                }
                if(e.getKeyCode()==KeyEvent.VK_H){
                    upAn=-1;
                }



                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_W||e.getKeyCode()==KeyEvent.VK_S){
                    forward=0;
                }
                if(e.getKeyCode()==KeyEvent.VK_D||e.getKeyCode()==KeyEvent.VK_A){
                    right=0;
                }
                if(e.getKeyCode()==KeyEvent.VK_SHIFT||e.getKeyCode()==KeyEvent.VK_CONTROL){
                    up=0;
                }

                if(e.getKeyCode()==KeyEvent.VK_I||e.getKeyCode()==KeyEvent.VK_K){
                    forwardAn=0;
                }
                if(e.getKeyCode()==KeyEvent.VK_J||e.getKeyCode()==KeyEvent.VK_L){
                    rightAn=0;
                }
                if(e.getKeyCode()==KeyEvent.VK_Y||e.getKeyCode()==KeyEvent.VK_H){
                    upAn=0;
                }
                
                
            }

        });


        setVisible(true);
        new Drawing().start();
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("posCamera="+isc.posCamera);
                System.out.println("angCamera="+isc.angle);
            }
        });
        

    }
    class Drawing extends Thread{
        public void run(){
            long Time = 1000 / 30;
            long startTimeT;
            long sleepTimeT;
            

            isc = new Intersection();
            isc.screenSize = new Dimension(gui.getWidth(), gui.getHeight());
            
            int offset = 50;
            int X = +10;
            int Y = +20;
            int Z = -60;


            isc.object.add(new Sphere(new vec3(X,Y,Z+offset), 10, new vec3(0,0,1)));
            //isc.object.add(new Sphere(new vec3(X,Y+offset,Z+offset), 10, new vec3(1,1,0)));
            //isc.object.add(new Sphere(new vec3(X+offset,Y+offset,Z+offset), 10, new vec3(1,1,0)));
            //isc.object.add(new Sphere(new vec3(X+offset,Y,Z+offset), 10, new vec3(1,1,0)));

            isc.object.add(new Sphere(new vec3(X,Y,Z), 10, new vec3(1,1,1)));
            isc.object.add(new Sphere(new vec3(X,Y+offset,Z), 10, new vec3(0,1,0)));
            //isc.object.add(new Sphere(new vec3(X+offset,Y+offset,Z), 10, new vec3(1,0,0)));
            isc.object.add(new Sphere(new vec3(X+offset,Y,Z), 10, new vec3(1,0,0)));

            // for(int i=-2; i<3; i++){
            //     for(int j=-2; j<3; j++){
            //         isc.object.add(new Sphere(new vec3(X+offset*i,Y+offset*j,Z), 10, new vec3(1,0,0)));
            //     }
            // }
           




            while(true){
                isc.utime = (double)System.currentTimeMillis()/1000.f;

                startTimeT = System.currentTimeMillis();

                double x =(Math.cos(isc.angle.y));
                double y =(Math.sin(isc.angle.y));
                if(forward!=0){
                    //isc.posCamera.x += speed*forward; 
                    //isc.posCamera.x +=(Math.sin(isc.angle.y))*speed*forward;
                    isc.posCamera.x +=x*speed*forward;
                    isc.posCamera.y -=y*speed*forward;

                }
                if(right!=0){
                    //isc.posCamera.y += speed*right; 
                    //isc.posCamera.y +=(Math.cos(isc.angle.y))*speed*right;
                    isc.posCamera.x +=y*speed*right;
                    isc.posCamera.y +=x*speed*right;
                }
                if(up!=0){
                    isc.posCamera.z -= speed*up; 
                }

                

                isc.angle.y=(mouseX/cs.getWidth()-0.5f)*0.5;
                //isc.angle.x=(mouseY/cs.getHeight()-0.5f)*0.05;
                if(rightAn!=0){
                    isc.angle.y += speedAn*rightAn; 
                }
                if(upAn!=0){
                    isc.angle.z -= speedAn*upAn; 
                }
                if(forwardAn!=0){
                    isc.angle.x += speedAn*forwardAn; 
                }

                BufferStrategy bs = cs.getBufferStrategy();
                if(bs==null){
                    cs.createBufferStrategy(3);
                    continue;
                }
                Graphics g = bs.getDrawGraphics();

                double width = gui.getWidth();
                double height = gui.getHeight();
                for(int i=0; i<width; i++){
                    for(int j=0; j<height; j++){
                            double[] color= isc.shader(i/width,j/height);
                            int[] col = new int[3];
                            for(int c=0; c<3; c++){
                                int b = (int)(color[c]*255);
                                if(b>255){
                                    b=255;
                                }else if(b<0){
                                    b=0;
                                }
                                col[c]= b;
                            }   
                            Color col1 = new Color(col[0], col[1], col[2]);
                            g.setColor(col1);    
                            g.fillRect(i, j, 1, 1);
                    }
                }
                bs.show();



                sleepTimeT = Time-(System.currentTimeMillis() - startTimeT);
                try {
                    if (sleepTimeT > 0)
                        sleep(sleepTimeT);
                } catch (Exception e) {}
            }
        }
       
    }
}
class Cube{
    vec3 pos;
    vec3 boxSize;
    vec3 color;


    public Cube(vec3 pos, vec3 size, vec3 color){
        this.pos = pos;
        this.boxSize = size;
        this.color= color;
    }
    public double[] boxIntersection(vec3 ro, vec3 rd, vec3 outNormal){
        ro = ro.copy();
        ro.sub(pos);
        vec3 m = new vec3(1.0/rd.x, 1.0/rd.y, 1.0/rd.z); // can precompute if traversing a set of aligned boxes
        vec3 n = vec3.mul(m, ro);   // can precompute if traversing a set of aligned boxes
        vec3 k = new vec3(Math.abs(m.x),Math.abs(m.y),Math.abs(m.z));
        k.mul(boxSize);
       
        vec3 t1 = new vec3(-n.x-k.x, -n.y-k.y, -n.z-k.z);
        vec3 t2 =  new vec3(-n.x+k.x, -n.y+k.y, -n.z+k.z);
        double tN = Math.max( Math.max( t1.x, t1.y ), t1.z );
        double tF =Math.min(Math.min(t2.x,t2.y), t2.z);
        if( tN>tF || tF<0.0) return new double[]{-1.0,0.0}; // no intersection
        outNormal.x = -vec3.sign(rd.x)*vec3.step(t1.y, t1.x)*vec3.step(t1.z, t1.x);
        outNormal.y = -vec3.sign(rd.y)*vec3.step(t1.z, t1.y)*vec3.step(t1.x, t1.y);
        outNormal.z = -vec3.sign(rd.z)*vec3.step(t1.x, t1.z)*vec3.step(t1.y, t1.z);



        //outNormal = -sign(rd)*step(t1.yzx,t1.xyz)*step(t1.zxy,t1.xyz);
        return new double[]{tN, tF};
    }
}
class Sphere{
    vec3 pos;
    double size;
    vec3 color;
    public Sphere(vec3 pos, double size, vec3 color){
        this.pos = pos;
        this.size = size;
        this.color= color;
    }
    public double[] sphIntersect(vec3 ro, vec3 rd){
        vec3 ce = pos.copy();
        float ra = (float)size;
        vec3 ro1 = ro.copy();
        ro1.sub(ce);
        vec3 oc = ro1;
        double b = oc.dot(rd);
        double c = oc.dot(oc) - ra*ra;
        double h = b*b - c;
        if( h<0.0 ) 
            return new double[]{-1.0, 0}; // no intersection
        h = Math.sqrt(h);
        return new double[]{-b-h, -b+h};
    }
}
class vec3{
    double x,y,z;
    public vec3(){}
    public vec3(double value){
        this.x = value;
        this.y = value;
        this.z = value;
    }
    public vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    void add(vec3 v1){
        x+=v1.x;y+= v1.y;z+= v1.z;
    }
    static vec3 add(vec3 v0, vec3 v1){
        return new vec3(v0.x+v1.x, v0.y+v1.y, v0.z+v1.z);
    }
    void add(double v1){
        x+=v1;y+= v1;z+= v1;
    }
    static vec3 add(vec3 v0, double value){
        return new vec3(v0.x+value, v0.y+value, v0.z+value);
    }
    void sub(vec3 v1){
        x-=v1.x;y-= v1.y;z-= v1.z;
    }
    static vec3 sub(vec3 v0, vec3 v1){
        return new vec3(v0.x-v1.x,v0.y-v1.y,v0.z-v1.z);
    }
    void mul(vec3 v1){
        x*=v1.x;y*= v1.y;z*= v1.z;
    }
    static vec3 mul(vec3 v0, vec3 v1){
        return new vec3(v0.x*v1.x, v0.y*v1.y,v0.z*v1.z);
    }
    void mulS(double value){
        x*=value;y*= value;z*= value;
    }
    static vec3 mulS(vec3 v0, double value){
        return new vec3(v0.x*value,v0.y*value,v0.z* value);
    }
    double dot(vec3 v1){
        // vec3 v0 = this.copy();
        // v1 = v1.copy();
        // v0.norm();
        // v1.norm();
        // return v0.x*v1.x+ v0.y*v1.y+v0.z*v0.z;
        
        return x*v1.x+y*v1.y+z*v1.z;
    }
    double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }
    void norm(){
        this.mulS(1/length());
    }
    void reflect(vec3 n){
        n.mulS(2*n.dot(this));
        this.sub(n);
    }
    public static vec3 reflect(vec3 rd, vec3 n){
        vec3 v = vec3.sub(rd, vec3.mulS(n,2*rd.dot(n)));
        return v;
        
    }
    double[] toDouble(){
        return new double[]{x, y,z};
    }
    public vec3 copy(){
        return new vec3(x, y,z);
    }
    public static double step(double v, double value){
        return v>value?0.0:1.0;
    }
    public static vec3 sign(vec3 in){
        vec3 val = new vec3();
        val.x = in.x<0?-1:in.x>0?1:0;
        val.y = in.y<0?-1:in.y>0?1:0;
        val.z = in.z<0?-1:in.z>0?1:0;
        return val;
    }
    public static double sign(double in){
        return in<0?-1:in>0?1:0;
    }
    @Override
    public String toString(){
        return String.format("x=%.3f y=%.3f z=%.3f", x, y,z);
    }
}
class Intersection{
    Dimension screenSize = new Dimension(600,480);
    ArrayList<Sphere> object = new ArrayList<>();
    vec3 light = new vec3(-0.5,-0.75,-1);
    vec3 posCamera = new vec3(5,-20,-70);

    vec3 angle = new vec3(0, 0, 0);
    double utime =System.currentTimeMillis()/10000.f;

    double plaIntersect(vec3 ro,vec3 rd,vec3 p, float w){
        return -(ro.dot(p)+w)/rd.dot(p);
    }
    public double[] shader(double i, double j){
        double[] p = toPixel(screenSize, i, j);//Перевод пикселей в другое разрешение
        vec3 ro = posCamera.copy();//Копируем позицию камеры
        vec3 rd = new vec3(1,p[0], p[1]);//Вектор выходящий из камеры
        rd.norm();
        

        //Поворот относительно углов камеры
        //по оси x
        double s = Math.sin(angle.x);
        double c = Math.cos(angle.x);
        double z0, x0;
        z0 = rd.z*c+rd.x*(-s);
        x0 = rd.z*s+rd.x*c;
        rd.z = z0;
        rd.x = x0;
        //По оси y
        s = Math.sin(-angle.y);
        c = Math.cos(-angle.y);
        double x1,y1;
        x1 = rd.x*c-rd.y*s;
        y1 = rd.x*s+rd.y*c;
        rd.x = x1;
        rd.y = y1;

        double tmin = 1e10;//Максимальная длина прорисовки
        double tmin_shadow = 1e10;//Максимальная длина отрисовки тени
        vec3 col = new vec3(0.08, 0.08,0.08);//Цвет фона если луч ничего не коснеться
        vec3 n = new vec3();//Нормаль обьектов, нужно для просчета освещнности обьекта



        
        //Положение бесконечного света
        vec3 light = this.light.copy();
        //vec3 light = new vec3(Math.cos(utime),-0.75, Math.sin(utime)-1);
        light.norm();






        vec3 pl = new vec3(0,0,-1);//нормаль плоскости
        pl.norm();
        double t1 = plaIntersect(ro, rd, pl, 0);
        if(t1>0.0 && t1<tmin){
            tmin = t1;
            n = vec3.mulS(pl, t1);;
            //n = vec3.add(ro, vec3.mulS(rd, t1));
            col = new vec3(0.3,0.3,0.3);
        }



        //Проверка на пересечения луча и сфер
        for(Sphere sph:object){
            double[] res = sph.sphIntersect(ro, rd);
            double t2 = res[0];
            if(t2>0.0 && t2<tmin){
                tmin = t2;
                n = vec3.add(ro, vec3.mulS(rd, t2));//Точка из которой выходит нормаль обьекта, itPos
                n = vec3.sub(n, sph.pos);//Нормаль шара
                col = sph.color.copy();
            }
        }



        Cube cube = new Cube(new vec3(50,50,-100), new vec3(10,10,10), new vec3(0,1,1));
        vec3 norm = new vec3();
        double t3 = cube.boxIntersection(ro, rd, norm)[0];
        if(t3>0.0 && t3<tmin){
            tmin = t3;
            n = norm;
            col = cube.color.copy();
        }
        
        n.norm();//Вектор нормали
        double diffuse = Math.max(0, n.dot(light));//Освещение обьект от бесконечного цвета
        vec3 reflected = vec3.reflect(rd, n);
        reflected.norm();
        double specular = Math.pow(Math.max(0.0,light.dot(reflected)),32);//Отражение




        
        vec3 shadow = vec3.add(ro, vec3.mulS(rd, tmin));//Точка в которую мы пришли
        //Просчёт теней только для сфер
        for(Sphere sph:object){
            double[] res = sph.sphIntersect(shadow, light);
            double t2 = res[0];
            if(t2>0.0 && t2<tmin_shadow){
                tmin_shadow = t2;
                col = new vec3(0);
                break;
            }
        }
        //Просчёт теней только для кубов
        t3 = cube.boxIntersection(shadow, light, norm)[0];
        if(t3>0.0 && t3<tmin_shadow){
            tmin_shadow = t3;
            col = new vec3(0);
        }

       


        col = vec3.mulS(col,diffuse+specular);//Цвет умноженный на искажение от освещение и отражения
        
        //Гамма коррекция
        col.x = Math.pow(col.x, 0.45);
        col.y = Math.pow(col.y, 0.45);
        col.z = Math.pow(col.z, 0.45);

        

        return col.toDouble();
    }
    private double[] toPixel(Dimension dim, double x, double y){
        x = (x-0.5f)*dim.getWidth()/dim.getHeight();
        y = (y-0.5f)*dim.getHeight()/dim.getHeight();
        return new double[]{x, y};
    }

}