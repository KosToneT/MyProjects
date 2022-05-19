import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.LocalTime;
import javax.swing.*;

import VKAPI.*;




public class App {
    public static void main(String[] args) throws Exception {
        new GUI();
    }
}
class GUI extends JFrame{
    JLabel text;
    Bot1 b;
    boolean run = false;
    public GUI(){
        super("status");
        setDefaultCloseOperation(3);
        setSize(600,480);
        text = new JLabel(" ");
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(100,30));
        JButton btn = new JButton("set text");
        b = new Bot1("7f2ac62ca6994b6e3ccd8c433be9570129f9ce1c0a0b7c87cf817e4d6fce46f1b1610fcf199269ba859ce");

        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = txt.getText();
                text.setText(str);
                txt.setText("");
                b.setStatus(str);
                
            }            
        });

        JPanel jp = new JPanel();
        JButton btn1 = new JButton("not running");
        btn1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                run = !run;
                btn1.setText(run?"running":"not running");
            }            
        });

        
        jp.add(text);
        jp.add(txt);
        jp.add(btn);
        jp.add(btn1);
        setContentPane(jp);
        setVisible(true);
        new Th().start();
    }
    class Th extends Thread{
        public void run(){
            int i=40;
            while(true){
                if(run){
                    char x = (char)i++;
                    String str = "Time ";
                    str +=Time.valueOf(LocalTime.now()).toString()+ " "+x;
                    text.setText(str);
                    b.setStatus(str);
                    try {
                        sleep(30000);
                    }catch (InterruptedException e) {}
                }else{
                    try {
                        sleep(1000);
                    }catch (InterruptedException e) {}
                }
            }
        }
    }
}

class Bot1{
    VKAPI.Requests req = new Requests("");
    public Bot1(){}
    public Bot1(String key){
        req = new Requests(key);
    }
    public void setStatus(String test){
        test = Requests.encodeTextFromHttp(test);
        Status.set(req, "204550001",test);
    }
}