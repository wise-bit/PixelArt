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

    private Pixel[] pixelList = new Pixel[(int) Math.pow(imageSize, 2)];
    private JColorChooser jcc = new JColorChooser();
    private JPanel drawingArea = new JPanel();

    private JMenu file;
    private JMenuItem saveButton = new JMenuItem("Save");
    private JMenuItem saveRawButton = new JMenuItem("Save Raw Data");
    private JMenuItem loadRawButton = new JMenuItem("Load Raw Data"); // TODO: Non-functional
    private JMenuItem sizeButton = new JMenuItem("Size"); // TODO: Non-functional

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
        this.setLocation(width/2-height/4, height/2 - jcc.getHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setBackground(Color.BLACK);

        // Create a 11x11 JLabel grid

        for (int i = 0; i < imageSize*imageSize; i++) {
            pixelList[i] = new Pixel();
        }

        drawingArea.setBackground(Color.BLACK);

        drawingArea.setPreferredSize(new Dimension(height-50,height-50));
        drawingArea.setLayout(new GridLayout(imageSize, imageSize, 2, 2));

        for (int i = 0; i < imageSize*imageSize; i++) {

            pixelList[i].addMouseListener(this);
            pixelList[i].addMouseMotionListener(this);

            drawingArea.add(pixelList[i]);
        }

        this.add(drawingArea, BorderLayout.CENTER);

        drawingArea.setVisible(true);


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
//            imageSize = 5;
//            removeAll();
            System.out.println();
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
                System.out.println(i + " clicked");
                pixelList[i].setBackground(jcc.getColor());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < imageSize*imageSize; i++) {
            if (e.getSource() == pixelList[i]) {
                System.out.println(i + " clicked");
                pixelList[i].setBackground(jcc.getColor());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

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

}
