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
 * 用户登录窗体
 * @author Admin
 *
 */
@SuppressWarnings("restriction")
public class LoginWindow extends JFrame {

    public static final long serialVersionUID = 1L;
    public Point origin; // 用于移动窗体
    public BufferedImage img; // 用来设定窗体不规则样式的图片

    public ImageIcon background1;
    public ImageIcon background2;
    public JTextField userText = new JTextField(30);
    public JButton exit = new JButton("");
    
    public JTextArea work_state=new JTextArea();
    public JScrollPane scrollPane = new JScrollPane();
    public JLabel back;

    /**
     * 初始化窗体
     */
    public LoginWindow() {
        super();
        background1 = new ImageIcon("src\\img\\green.png");
        background2 = new ImageIcon("src\\img\\red.png");
        
        back = new JLabel(background1);
        back.setBounds(0, 0, background1.getIconWidth(),
                background1.getIconHeight());
        /*
         * 首先初始化一张图片，我们可以选择一张有透明部分的不规则图片
         *  (要想图片能够显示透明，必须使用PNG格式的图片)
         */
        MediaTracker mt = new MediaTracker(this);

        try {
            img = ImageIO.read(new File("src\\img\\green.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        mt.addImage(img, 0);

        try {
            mt.waitForAll(); // 开始加载由此媒体跟踪器跟踪的所有图像
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialize(); // 窗体形状初始化
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMenu();
        getContentPane().add(back);
        this.setVisible(true);
    }

    /**
     * 组件初始化
     */
    public void addMenu() {
        getContentPane().setLayout(null);
        //设置字体
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

        userText.setText("状态：");
    }

    /**
     * 创建和图片形状一样的窗体
     * @throws IOException
     */
    private void initialize() throws IOException { // 窗体初始化
        // 设定窗体大小和图片一样大
        this.setSize(img.getWidth(null), img.getHeight(null));
        // 设定禁用窗体装饰，这样就取消了默认的窗体结构
        this.setUndecorated(true);
        // 初始化用于移动窗体的原点
        this.origin = new Point();

        // 调用AWTUtilities的setWindowShape方法设定本窗体为制定的Shape形状
        AWTUtilities.setWindowShape(this, getImageShape(img));
        // 设定窗体可见度
        AWTUtilities.setWindowOpacity(this, 0.8f);

        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        // 由于取消了默认的窗体结构，所以我们要手动设置一下移动窗体的方法
        this.addMouseListener(new OwnListener());
        //监听鼠标移动事件
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
                        - origin.y);
            }
        });
    }

    /**
     * 将Image图像转换为Shape图形
     * @param img
     * @return
     */
    public Shape getImageShape(Image img) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        int width = img.getWidth(null);// 图像宽度
        int height = img.getHeight(null);// 图像高度

        // 筛选像素
        // 首先获取图像所有的像素信息
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        try {
            pgr.grabPixels();
        } catch (InterruptedException ex) {
            ex.getStackTrace();
        }
        int pixels[] = (int[]) pgr.getPixels();

        // 循环像素
        for (int i = 0; i < pixels.length; i++) {
            // 筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中
            int alpha = getAlpha(pixels[i]);
            if (alpha == 0) {
                continue;
            } else {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        // 建立图像矩阵并初始化(0为透明,1为不透明)
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        // 导入坐标ArrayList中的不透明坐标信息
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

        /*
         * 由于Area类所表示区域可以进行合并，我们逐一水平"扫描"图像矩阵的每一行，
         * 将不透明的像素生成为Rectangle，再将每一行的Rectangle通过Area类的rec
         * 对象进行合并，最后形成一个完整的Shape图形
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
     * 取得透明度
     * @param pixel
     * @return
     */
    private int getAlpha(int pixel) {
        return (pixel >> 24) & 0xff;
    }

    /**
     * 事件监听
     * @author Admin
     *
     */
    private class OwnListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            origin.x = e.getX();
            origin.y = e.getY();
        }

        // 窗体上单击鼠标右键关闭程序
        public void mouseClicked(MouseEvent e) {
            //如果点击的区域位于右上角红色按钮，则关闭程序
           if (e.getSource() == exit) {
                //验证用户是否合法，合法打开主程序
            	System.exit(0);
            } 
        }

        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }
    }

}