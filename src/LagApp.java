import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class LagApp extends MIDlet implements CommandListener {
    private Display display;
    private Form form;
    private Command startCommand;
    private Command exitCommand;
    private boolean running = false;

    public LagApp() {
        display = Display.getDisplay(this);
        form = new Form("Performance Test");
        form.append("Press 'Start Test' to begin system load.");
        
        startCommand = new Command("Start Test", Command.SCREEN, 1);
        exitCommand = new Command("Exit", Command.EXIT, 2);
        
        form.addCommand(startCommand);
        form.addCommand(exitCommand);
        form.setCommandListener(this);
    }

    protected void startApp() {
        display.setCurrent(form);
    }

    protected void pauseApp() {}

    protected void destroyApp(boolean unconditional) {}

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            notifyDestroyed();
        } else if (c == startCommand) {
            form.append("\nAllocating resources...");
            running = true;
            
            // Background thread to ramp up memory pressure and CPU load
            new Thread(new Runnable() {
                public void run() {
                    Vector memoryHog = new Vector();
                    try {
                        while (running) {
                            // Allocate 512KB blocks gradually
                            byte[] block = new byte[512 * 1024];
                            memoryHog.addElement(block);
                            Thread.sleep(150); // Control speed of degradation
                        }
                    } catch (Throwable t) {
                        // Force CPU lock up once memory limit is hit
                        while (true) {
                            Math.sin(Math.random());
                        }
                    }
                }
            }).start();
        }
    }
}
