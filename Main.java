import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Main {
  public static void main(String[] args){
    new MyFrame();
  }
}

class CollisionSimulator extends Frame implements Runnable {

  int ObjectOneX = 0;
  int ObjectOneY = 0;
  int ObjectTwoX = 0;
  int ObjectTwoY = 0;
  int Diameter = 100;

  double ObjectOneVelX = 0;
  double ObjectOneVelY = 0;
  double ObjectTwoVelX = 0;
  double ObjectTwoVelY = 0;
  double ObjectOneVel = Math.hypot(ObjectOneVelX, ObjectOneVelY);
  double ObjectTwoVel = Math.hypot(ObjectTwoVelX, ObjectTwoVelY);

  int FrameWidth;
  int FrameHeight;

  public CollisionSimulator() {

    setTitle("Collision Simulator");
    setSize(600,300);
    setBackground(Color.LIGHT_GRAY);

    FrameWidth = getWidth();
    FrameHeight = getHeight();

    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
            System.exit(0);
        }
    });

    Thread animatorThread = new Thread(this);
    animatorThread.start();

    setVisible(true); 
    
  }

  @Override
  public void update(Graphics G) {

    if (offscreenImage == null || offscreenImage.getWidth(this) != getWidth() || offscreenImage.getHeight(this) != getHeight()) {
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = offscreenImage.getGraphics();
    }

    offscreenGraphics.setColor(getBackground());
    offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

    paint(offscreenGraphics);

    g.drawImage(offscreenImage, 0, 0, this);
  }

  @Override
  public void run() {

    while(true) {

      
    }
  }

  
}