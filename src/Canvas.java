import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Canvas  extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    // Essential methods for all classes
    private int width = (int)Main.screenSize.getWidth();
    private int height = (int)Main.screenSize.getHeight();

    private int imageSize = 11; // Change this as required

    private Pixel[] pixelList;
    private JColorChooser jcc = new JColorChooser();
    private JPanel drawingArea;

    private JMenu file;
    private JMenuItem saveButton = new JMenuItem("Save");
    private JMenuItem saveRawButton = new JMenuItem("Save Raw Data");
    private JMenuItem loadRawButton = new JMenuItem("Load Raw Data"); // TODO: Non-functional
    private JMenuItem sizeButton = new JMenuItem("Size");


    private boolean leftMouseDown = false;
    private boolean rightMouseDown = false;

    public Canvas() {
        this.setTitle("Pixel Art Generator");
        this.setLayout(new BorderLayout());

        // Add color chooser

        jcc.setPreviewPanel(new JPanel());
        jcc.setColor(Color.WHITE);
        jcc.setSize(new Dimension(width, height/3));
        System.out.println(jcc.getHeight());
        this.add(jcc, BorderLayout.PAGE_END);

        this.setSize(height/2, height/2 + jcc.getHeight());
        this.setLocation(width/2-height/4, (height/2 - jcc.getHeight())/2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setBackground(Color.BLACK);

        // Creating the canvas

        resetCanvas();


        //// Menu Bar

        JMenuBar mb = new JMenuBar();
        file = new JMenu("File");
        file.add(saveButton);
        saveButton.addActionListener(this);
        file.add(sizeButton);
        sizeButton.addActionListener(this);
        file.add(saveRawButton);
        saveRawButton.addActionListener(this);
        file.add(loadRawButton);
        loadRawButton.addActionListener(this);
        mb.add(file);
        this.add(mb, BorderLayout.PAGE_START);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.setVisible(true);
    }

    // To call this again for resetting the canvas
    public void resetCanvas() {

        pixelList = new Pixel[(int) Math.pow(imageSize, 2)];

        try {
            this.remove(drawingArea);
        } catch (Exception e) {

        }

        // Create a 11x11 JLabel grid

        for (int i = 0; i < imageSize*imageSize; i++) {
            pixelList[i] = new Pixel();
        }

        // Drawing the actual canvas

        drawingArea = new JPanel();
        drawingArea.setBackground(Color.BLACK);

        drawingArea.setPreferredSize(new Dimension(height-50,height-50));
        drawingArea.setLayout(new GridLayout(imageSize, imageSize, 2, 2));

        for (int i = 0; i < imageSize*imageSize; i++) {

            pixelList[i].addMouseListener(this);
            pixelList[i].addMouseMotionListener(this);

            drawingArea.add(pixelList[i]);
        }

        this.add(drawingArea, BorderLayout.CENTER);

        validate();
        repaint();

    }

    private void save() throws IOException {
        BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {

                int p = pixelList[j*imageSize + i].getBackground().getRGB();

//                int a = (p>>24) & 0xff; //get alpha
//                int r = (p>>16) & 0xff; //get red
//                int g = (p>>8) & 0xff; //get green
//                int b = p & 0xff; //get blue
//                p = (a<<24) | (r<<16) | (g<<8) | b;

                img.setRGB(i, j, p);
            }
        }

        File f = new File("res/drawings/" + new SimpleDateFormat("hh-mm-ss yyyy-MM-dd'.png'").format(new Date()));
        ImageIO.write(img, "png", f);
    }

    private void saveRaw() {

        try{
            FileWriter fw = new FileWriter("res/rawData/" + new SimpleDateFormat("hh-mm-ss yyyy-MM-dd'.txt'").format(new Date()));
            for (int i = 0; i < imageSize*imageSize; i++) {
                fw.write(pixelList[i].getBackground().getRGB() + " ");
            }
            fw.close();
        } catch(Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveButton) {
            System.out.println("Save requested");
            try {
                save();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == sizeButton) {
            System.out.println("Size requested");

            String sizeInput = JOptionPane.showInputDialog("Enter size");
            int newSize = 3;

            // Only if number is an integer
            if (isNumeric(sizeInput)) {

                newSize = Integer.parseInt(sizeInput);

                // Only if the size is reasonable
                if (newSize >= 3 && newSize <= 17) {
                    imageSize = newSize;
                    resetCanvas();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a number between 3 and 17",
                            "Message", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid number",
                        "Message", JOptionPane.INFORMATION_MESSAGE);
            }

        }
        if (e.getSource() == saveRawButton) {
            System.out.println("Saving Raw Requested");
            saveRaw();
        }
        if (e.getSource() == loadRawButton) {
            System.out.println("Loading Raw Requested");
            // make loadRaw();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < imageSize*imageSize; i++) {
            if (e.getSource() == pixelList[i]) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    pixelList[i].setBackground(jcc.getColor());
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    pixelList[i].setBackground(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMouseDown = true;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseDown = true;
        }
        for (int i = 0; i < imageSize*imageSize; i++) {
            if (e.getSource() == pixelList[i]) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    pixelList[i].setBackground(jcc.getColor());
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    pixelList[i].setBackground(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMouseDown = false;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseDown = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < imageSize*imageSize; i++) {
            if (e.getSource() == pixelList[i]) {
                if (leftMouseDown) {
                    pixelList[i].setBackground(jcc.getColor());
                } else if (rightMouseDown) {
                    pixelList[i].setBackground(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) { }

//    @Override
//    public void mouseClicked(MouseEvent e) {
//
//        System.out.println("click" + e.toString());
//
//        for (int i = 0; i < imageSize*imageSize; i++) {
//            if (e.equals(pixelList[i])) {
//                pixelList[i].setBackground(jcc.getColor());
//                System.out.println(pixelList[i]);
//                drawingArea.repaint();
//                repaint();
//            }
//        }
//
//     }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
