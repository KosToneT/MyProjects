import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.dnd.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;


public class Common{
    public static void main(String[] args) throws Exception {
        new GUI();
    }
}

class EditImage{
    NeuralNetwork inn;
    int count_train;
    public EditImage(){
        inn = new NeuralNetwork(2, 5, 3, 0.01);
        count_train = 10;
    }
    public void edit_image(BufferedImage img){
        for(int i=0; i<img.getHeight(); i++){
            for(int j=0; j<img.getWidth(); j++){
                double input[] = new double[]{(double)i/img.getHeight(),(double)j/img.getWidth()};
                double output[] = inn.query(input);
                img.setRGB(i, j, new Color((int)(output[0]*255),(int)(output[1]*255),(int)(output[2]*255)).getRGB());
            }
        }

    }
    public void setCount(int count){
        count_train = count;
    }
    public void learn_image(BufferedImage img, BufferedImage img2){
        for(int l = 0; l<count_train; l++){
            for(int i=0; i<img.getHeight(); i++){
                for(int j=0; j<img.getWidth(); j++){
                    double input[] = new double[]{(double)i/img.getHeight(),(double)j/img.getWidth()};
                    int color = img.getRGB(i, j);
                    int red = new Color(color).getRed();
                    int green = new Color(color).getGreen();
                    int blue = new Color(color).getBlue();
                    double output[] = new double[]{red/255.f, green/255.f, blue/255.f};
                    inn.train(input, output);
                    if(img2==null){

                    }else{
                        output = inn.query(input);
                        img2.setRGB(i, j, new Color((int)(output[0]*255),(int)(output[1]*255),(int)(output[2]*255)).getRGB());
                    }
                }
            }
        }
    }
}


class GUI extends JFrame{
    Canvas cs;
    BufferedImage start_img;
    BufferedImage img;
    Dimension size = new Dimension(400,400);
    EditImage edi = new EditImage();

    public GUI(){
        super("test");
        Box box = Box.createHorizontalBox();
        JLabel lbl = new JLabel();
        this.setDropTarget(new DropTarget(){
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List)(evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    File fi = droppedFiles.get(0);
                    start_img = toBufferedImage(new ImageIcon(fi.getAbsolutePath()).getImage());
                    start_img = imageResize(start_img, (int)size.getWidth(), (int)size.getHeight());
                    lbl.setIcon(new ImageIcon(start_img));
                    
                    img = start_img;
                    for (File file : droppedFiles) {
                        System.out.println(file);
                    }
                } catch (Exception ex) {
                    
                }
            }
        });
        box.add(lbl);
        
        cs = new Canvas();
        box.add(cs);
        Box box1 = Box.createVerticalBox();
        box1.add(box);


        JButton edit = new JButton("edit");
        edit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                edi.edit_image(img);
                
            }

        });
        JTextField txt = new JTextField("10");
        Dimension size = new Dimension(100,40);
        txt.setMinimumSize(size);
        txt.setSize(size);
        txt.setMaximumSize(size);
        JButton learn = new JButton("learn");
        learn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    edi.setCount(Integer.parseInt(txt.getText()));
                }catch(Exception ex){

                }
                edi.learn_image(start_img, img);
                
            }

        });
        JButton reset = new JButton("reset");
        reset.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                edi = new EditImage();
                
            }

        });
        Box panel = Box.createHorizontalBox();
        panel.add(edit);
        panel.add(txt);
        panel.add(learn);
        panel.add(reset);
        
        box1.add(panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,500);
        setContentPane(box1);
        setVisible(true);
        new Draw().start();
    }
    class Draw extends Thread{
        public void run(){
            while(true){
                BufferStrategy bs = cs.getBufferStrategy();
                if(bs ==null){
                    cs.createBufferStrategy(2);
                }else{
                    Graphics g = bs.getDrawGraphics();
                    g.drawImage(img, 0, 0, null);
                    bs.show();
                }
            }
        }
    }
    public static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    public static BufferedImage imageResize(BufferedImage img, int width, int height){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
        if(g2d.drawImage(img, 0,0, width, height, null)){
            return bi;
        }
        return null;   
    }

}
class NeuralNetwork{
    private double lrate;
    private int inputnodes;
    private int hiddennodes;
    private int outputnnodes;
    private Matrix wih;
    private Matrix who;

    public NeuralNetwork(int input, int hidden, int output, double learnrate){
        lrate = learnrate;
        inputnodes = input;
        hiddennodes = hidden;
        outputnnodes = output;

        wih = Matrix.createRand(hiddennodes, inputnodes,0.f,1.f);
        wih.minus(0.5);
        who = Matrix.createRand(outputnnodes, hiddennodes,0.f,1.f);
        who.minus(0.5);
    }
    /**
     * Тренируем нейросеть
     * @param input входные данные
     * @param target данные которые нужно получить
     */
    public void train(double input[], double target[]){
        Matrix inputs = new Matrix(input);
        inputs.transp();
        Matrix targets = new Matrix(target);
        targets.transp();
        Matrix hidden_inputs = Matrix.dot(wih, inputs);
        Matrix hidden_outputs = activation(hidden_inputs);
        Matrix final_inputs = Matrix.dot(who, hidden_outputs);
        Matrix final_outputs = activation(final_inputs);
        Matrix output_errors = Matrix.minus(targets, final_outputs);
        Matrix hidden_errors = Matrix.dot(Matrix.transp(who), output_errors);
        Matrix p= Matrix.multi(Matrix.dot(Matrix.multi(Matrix.multi(output_errors, final_outputs), 
            Matrix.minus(1.0, final_outputs)),
            Matrix.transp(hidden_outputs)),lrate);
        who.plus(p);
        Matrix p1= Matrix.multi(Matrix.dot(Matrix.multi(Matrix.multi(hidden_errors, hidden_outputs), 
            Matrix.minus(1.0, hidden_outputs)),
            Matrix.transp(inputs)),lrate);
        wih.plus(p1);
    }
    /**
     * Опрос нейросети 
     * @param input входные данные
     * @return выходные данные
     */
    public double[] query(double input[]){
        Matrix input1 = new Matrix(input);
        input1.transp();
        Matrix hidden_inputs = Matrix.dot(wih, input1);
        Matrix hidden_outputs = activation(hidden_inputs);

        Matrix final_inputs = Matrix.dot(who, hidden_outputs);
        Matrix final_output = activation(final_inputs);
        final_output.transp();
        double out[] = new double[final_output.getColumn()];
        for(int i=0; i<out.length; i++){
            out[i] = final_output.getValue(0, i);
        }
        return out;
    }

    private Matrix activation(Matrix m){
        double m1[][] = new double[m.getRows()][m.getColumn()];
        for(int i=0; i<m.getRows();i++){
            for(int j=0; j<m.getColumn(); j++){
                m1[i][j] = activation_neuron(m.getValue(i,j));
            }
        }
        return new Matrix(m1);
    }
    private double activation_neuron(double x){
        return 1/(1+Math.pow(Math.E,-x));
    }



}
class Matrix{
    private double matrix[][];
    private int rows;
    private int columns;
    public Matrix(){}
    public Matrix(int rows, int column){
        matrix = new double[this.rows = rows][this.columns = column];
    }
    public Matrix(double m[][]){
        matrix = m;
        rows = matrix.length;
        columns = matrix[0].length;
    }
    public Matrix(double m[]){
        matrix = new double[rows = 1][columns = m.length];
        for(int i=0; i<m.length; i++){
            matrix[0][i] = m[i];
        }
    }
    public void fillRandomDouble(double min, double max){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                matrix[i][j] = Math.random()*(max-min)+min;
            }
        }
    }
    public void transp(){
        double m[][] = new double[columns][rows];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                m[j][i] = matrix[i][j];
            }
        }
        int buf= columns;
        columns = rows;
        rows = buf;
        matrix = m;
    }
    public static Matrix transp(Matrix m1){
        double m[][] = new double[m1.columns][m1.rows];
        for(int i=0; i<m1.rows; i++){
            for(int j=0; j<m1.columns; j++){
                m[j][i] =m1.matrix[i][j];
            }
        }
        return new Matrix(m);
    }
    public static Matrix createRand(int rows, int column, double min, double max){
        Matrix m = new Matrix(rows, column);
        m.fillRandomDouble(min, max);
        return m;
    }
    public double getValue(int row, int column){
        return matrix[row][column];
    }
    public int getRows(){
        return rows;
    }
    public int getColumn(){
        return columns;
    }
    public static Matrix minus(Double v, Matrix m){
        Matrix m2 = new Matrix(m.getRows(), m.getColumn());
        for(int i=0; i<m2.getRows(); i++){
            for(int j=0; j<m2.getColumn(); j++){
                m2.matrix[i][j] = v-m.matrix[i][j];
            }
        }
        return m2;
    }
    public static Matrix minus(Matrix m, Matrix m1){
        Matrix m2 = new Matrix(m.getRows(), m.getColumn());
        for(int i=0; i<m2.getRows(); i++){
            for(int j=0; j<m2.getColumn(); j++){
                m2.matrix[i][j] = m.matrix[i][j]-m1.matrix[i][j];
            }
        }
        return m2;
    }
    public void minus(double value){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns; j++){
                matrix[i][j] -=value;
            }
        }
    }
    public void plus(double value){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns; j++){
                matrix[i][j] +=value;
            }
        }
    }
    public void plus(Matrix m){
        for(int i=0; i<rows;i++){
            for(int j=0; j<columns; j++){
                matrix[i][j] +=m.matrix[i][j];
            }
        }
    }
    /**
     * Матричное умножение
     * @param mA первая матрица
     * @param mB вторая матрица
     * @return новая матрица
     */
    static Matrix dot(Matrix m, Matrix m1){
        return new Matrix(dot(m.matrix, m1.matrix));
    }
    /**
     * Матричное умножение на типе double[][]
     * @param mA первая матрица
     * @param mB вторая матрица
     * @return новая матрица
     */
    static double[][] dot(double[][]mA, double[][]mB){
        int m = mA.length;
        int n = mB[0].length;
        int o = mB.length;
        double[][] res = new double[m][n];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < o; k++) {
                    res[i][j] += mA[i][k] * mB[k][j]; 
                }
            }
        }
        return res;
    }
    /**
     * Поэлемнтое умножение матрицы на число
     * @param m матрица
     * @param value число
     * @return возращает новую матрицу
     */
    public static Matrix multi(Matrix m, Double value){
        Matrix m2 = new Matrix(m.getRows(), m.getColumn());
        for(int i=0; i<m2.getRows(); i++){
            for(int j=0; j<m2.getColumn(); j++){
                m2.matrix[i][j] = m.matrix[i][j]*value;
            }
        }
        return m2;
    }
    /**
     * Поэлемнтое умножение матрицы на матрицу
     * @param m матрица
     * @param m1 матрица
     * @return возращает новую матрицу
     */
    public static Matrix multi(Matrix m, Matrix m1){
        Matrix m2 = new Matrix(m.getRows(), m.getColumn());
        for(int i=0; i<m.rows; i++){
            for(int j=0; j<m.columns; j++){
                m2.matrix[i][j] = m.matrix[i][j]*m1.matrix[i][j];
            }
        }
        return m2;
    }

    @Override
    public String toString(){
        String str = "rows:"+rows+" columns:"+columns;
        for(int i=0; i<rows; i++){
            str+="\n";
            for(int j=0; j<columns;j++){
                str+= String.format("%.3f", matrix[i][j])+" ";
                
            }
        }
        return str;
    }

}
