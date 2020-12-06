package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import com.assistclass.Circle;
//import com.data.Reader;
//import com.frame.MainFrame;
//import com.sqlservice.DriveSQL;
import com.sun.awt.AWTUtilities;

/**
 * �û���¼����
 * @author Admin
 *
 */
@SuppressWarnings("restriction")
public class LoginWindow extends JFrame {

    public static final long serialVersionUID = 1L;
    public Point origin; // �����ƶ�����
    public BufferedImage img; // �����趨���岻������ʽ��ͼƬ

    public ImageIcon background1;
    public ImageIcon background2;
    public JTextField userText = new JTextField(30);
    public JButton exit = new JButton("");
    
    public JTextArea work_state=new JTextArea();
    public JScrollPane scrollPane = new JScrollPane();
    public JLabel back;

    /**
     * ��ʼ������
     */
    public LoginWindow() {
        super();
        background1 = new ImageIcon("src\\img\\green.png");
        background2 = new ImageIcon("src\\img\\red.png");
        
        back = new JLabel(background1);
        back.setBounds(0, 0, background1.getIconWidth(),
                background1.getIconHeight());
        /*
         * ���ȳ�ʼ��һ��ͼƬ�����ǿ���ѡ��һ����͸�����ֵĲ�����ͼƬ
         *  (Ҫ��ͼƬ�ܹ���ʾ͸��������ʹ��PNG��ʽ��ͼƬ)
         */
        MediaTracker mt = new MediaTracker(this);

        try {
            img = ImageIO.read(new File("src\\img\\green.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        mt.addImage(img, 0);

        try {
            mt.waitForAll(); // ��ʼ�����ɴ�ý����������ٵ�����ͼ��
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialize(); // ������״��ʼ��
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMenu();
        getContentPane().add(back);
        this.setVisible(true);
    }

    /**
     * �����ʼ��
     */
    public void addMenu() {
        getContentPane().setLayout(null);
        //��������
        userText.setBounds(140, 130, 170, 30);
        exit.setBounds(558, 75, 100, 110);

		scrollPane.setBounds(741, 235, 225, 355);
		work_state.setFont(new Font("Monospaced", Font.PLAIN, 20));
		work_state.setForeground(Color.white);
		scrollPane.setViewportView(work_state);
		
		work_state.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
        
		getContentPane().add(scrollPane);
        getContentPane().add(userText);
        getContentPane().add(exit);

        exit.setContentAreaFilled(false);
        exit.addMouseListener(new OwnListener());

        userText.setText("״̬��");
    }

    /**
     * ������ͼƬ��״һ���Ĵ���
     * @throws IOException
     */
    private void initialize() throws IOException { // �����ʼ��
        // �趨�����С��ͼƬһ����
        this.setSize(img.getWidth(null), img.getHeight(null));
        // �趨���ô���װ�Σ�������ȡ����Ĭ�ϵĴ���ṹ
        this.setUndecorated(true);
        // ��ʼ�������ƶ������ԭ��
        this.origin = new Point();

        // ����AWTUtilities��setWindowShape�����趨������Ϊ�ƶ���Shape��״
        AWTUtilities.setWindowShape(this, getImageShape(img));
        // �趨����ɼ���
        AWTUtilities.setWindowOpacity(this, 0.8f);

        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        // ����ȡ����Ĭ�ϵĴ���ṹ����������Ҫ�ֶ�����һ���ƶ�����ķ���
        this.addMouseListener(new OwnListener());
        //��������ƶ��¼�
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
                        - origin.y);
            }
        });
    }

    /**
     * ��Imageͼ��ת��ΪShapeͼ��
     * @param img
     * @return
     */
    public Shape getImageShape(Image img) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        int width = img.getWidth(null);// ͼ����
        int height = img.getHeight(null);// ͼ��߶�

        // ɸѡ����
        // ���Ȼ�ȡͼ�����е�������Ϣ
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        try {
            pgr.grabPixels();
        } catch (InterruptedException ex) {
            ex.getStackTrace();
        }
        int pixels[] = (int[]) pgr.getPixels();

        // ѭ������
        for (int i = 0; i < pixels.length; i++) {
            // ɸѡ������͸�������ص�������뵽����ArrayList x��y��
            int alpha = getAlpha(pixels[i]);
            if (alpha == 0) {
                continue;
            } else {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        // ����ͼ����󲢳�ʼ��(0Ϊ͸��,1Ϊ��͸��)
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        // ��������ArrayList�еĲ�͸��������Ϣ
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

        /*
         * ����Area������ʾ������Խ��кϲ���������һˮƽ"ɨ��"ͼ������ÿһ�У�
         * ����͸������������ΪRectangle���ٽ�ÿһ�е�Rectangleͨ��Area���rec
         * ������кϲ�������γ�һ��������Shapeͼ��
         */
        Area rec = new Area();
        int temp = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix[i][j] == 1) {
                    if (temp == 0)
                        temp = j;
                    else if (j == width) {
                        if (temp == 0) {
                            Rectangle rectemp = new Rectangle(j, i, 1, 1);
                            rec.add(new Area(rectemp));
                        } else {
                            Rectangle rectemp = new Rectangle(temp, i,
                                    j - temp, 1);
                            rec.add(new Area(rectemp));
                            temp = 0;
                        }
                    }
                } else {
                    if (temp != 0) {
                        Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
                        rec.add(new Area(rectemp));
                        temp = 0;
                    }
                }
            }
            temp = 0;
        }
        return rec;
    }

    /**
     * ȡ��͸����
     * @param pixel
     * @return
     */
    private int getAlpha(int pixel) {
        return (pixel >> 24) & 0xff;
    }

    /**
     * �¼�����
     * @author Admin
     *
     */
    private class OwnListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            origin.x = e.getX();
            origin.y = e.getY();
        }

        // �����ϵ�������Ҽ��رճ���
        public void mouseClicked(MouseEvent e) {
            //������������λ�����ϽǺ�ɫ��ť����رճ���
           if (e.getSource() == exit) {
                //��֤�û��Ƿ�Ϸ����Ϸ���������
            	System.exit(0);
            } 
        }

        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }
    }

}