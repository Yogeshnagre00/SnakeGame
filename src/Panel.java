import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
//panel inherits from jpanel &implementd actionlistener interface(dis gets triggered each 160ms &call dactionperformed function
public class Panel extends JPanel implements ActionListener {
    static int width = 1200;
    static int height = 600;
    static int unit = 50;
    //false-gameover true-game running
    boolean flag = false;
    int score =0;
    Timer timer;
    Random random;
    int fx,fy;
    int length = 3;
    char dir = 'R';
    //to store x&y cordinate in separate arraysfor each body part of d snake
    int xsnake[] =new int[288];
    int ysnake[] =new int[288];
    Panel(){
        this.setPreferredSize(new Dimension(width ,height));
        this.setBackground(Color.black);
        this.addKeyListener(new key());
        this.setFocusable(true);
        random = new Random();
        gamestart();
    }
    //sets d initial parameters for d game & spawns d food
    public void gamestart(){
        spawnfood();
        flag = true; //game is running or over
        timer = new Timer(160,this);//160 milisec,this-current object
        timer.start();
    }
    public void spawnfood(){
        //we get a random multipleof 50 bet 0-1200
        fx = random.nextInt(width/unit)*unit;//24*50 (0-24)
        //........0-600
        fy = random.nextInt(height/unit)*unit;//12*50 (0-12)
    }
    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic); //super keyword call func from parent cls
        draw(graphic);
    }
    public void draw(Graphics graphic){
        if(flag){
            //food particle
            graphic.setColor(Color.red);
            graphic.fillOval(fx,fy,50,50);

            //for d body of d snake
            for(int i=0;i<length;i++){ //head=0,tail=length-1
                graphic.setColor(Color.green);
                graphic.fillRect(xsnake[i],ysnake[i],50,50);
            }
            //to draw d score display
            graphic.setColor(Color.cyan);
            graphic.setFont(new Font("Comic sans",Font.BOLD,40));

            //helps to getd sizing of d font in pixels
            FontMetrics fme = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:"+score,(width - fme.stringWidth("Score:"+score))/2,graphic.getFont().getSize());
        }
        else{
            gameover(graphic);
        }
    }
    public void gameover(Graphics graphic) {
        //to display final score
        graphic.setColor(Color.cyan);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 40));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:" + score, (width - fme.stringWidth("Score:" + score)) / 2, graphic.getFont().getSize());
        //game over text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 80));
        fme = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER" , (width - fme.stringWidth("GAME OVER")) / 2, height/2);

        //to display d replay prompt
        graphic.setColor(Color.green);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 40));
        fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay", (width - fme.stringWidth("Press R to replay")) / 2,3*height/4);
    }
    public void eat(){
        if((fx == xsnake[0]) &&(fy == ysnake[0])){
            length++;
            score++;
            spawnfood();
        }
    }
    public void hit(){
        //to check hit with its own body
        for(int i=length-1;i>0;i--){
            if((xsnake[0]==xsnake[i]) && (ysnake[0]==ysnake[i])){
                flag = false;
            }
        }
        if(xsnake[0]<0 || xsnake[0]>width ){
            flag = false;
        }
        else if(ysnake[0]<0 || ysnake[0]>height){
            flag = false;
        }
        else if(flag==false){
            timer.stop();
        }

    }
    public void move(){
        //updating dcordinate of d body except d head
        for(int i=length-1;i>0;i--){
            xsnake[i]=xsnake[i-1];
            ysnake[i]=ysnake[i-1];
        }
        //to update d head
        switch(dir){
            case'U':
                ysnake[0]=ysnake[0]-unit;
                break;
            case'D':
                ysnake[0]=ysnake[0]+unit;
                break;
            case'R':
                xsnake[0]=xsnake[0]+unit;
                break;
            case'L':
                xsnake[0]=xsnake[0]-unit;
                break;

        }
    }
    public class key extends KeyAdapter{

        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if(dir!='D'){
                        dir='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir!='U'){
                        dir='D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(dir!='R'){
                        dir='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir!='L'){
                        dir='R';
                    }
                    break;
                case KeyEvent.VK_R:
                    if(flag == false){
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake,0);
                        Arrays.fill(ysnake,0);
                        gamestart();
                    }
                    break;
            }
        }
    }


    public void actionPerformed(ActionEvent e){
        if(flag){
            move();
            hit();
            eat();
        }
        repaint();
    }
}
