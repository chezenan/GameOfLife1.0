package view;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import control.GameOfLife;
import control.GameTool;

public class MainWindow extends JFrame implements ActionListener
{
	//小方格的边长x
	//图像的横坐标的起始偏移量offx
	//图像的纵坐标的起始偏移量offy
	int x,offy,offx ;
	
	//定义并获得数据处理和画图工具类对象dt，gt
	DrawTool dt = DrawTool.getDrawTool();
	GameTool gt = GameTool.getGameTool();
	
	//定义按钮:开始,暂停,初始化,清除图像 
	JButton Bstart = null;
	JButton BPause = null;
	JButton BInit = null;
	JButton BClear = null;
	
	//定义数组用于存放细胞存活状态
	static int[][] arr;
	
	//定义数组用于存放上一时刻细胞存活状态
	int [][] oldArr;
	
	//用于计算的线程
	Thread CoreThread = null;
	
	//创建各个text，用于输入输出  
	TextField rowNumText,colNumText,getRowNum,getColNum,cellNumText,getCellNum,timeText,timeText1,getMinTime,getMaxTime,sliderName;
	
	//暂停继续判断标签
	volatile Boolean  pauseFlag = true;
	
	//开始停止判断标签
	volatile Boolean  stopFlag = true;
	
	//计算间隔时间
	volatile int intervalTime = 1000;
	
	//控制计算时间
	Box sliderBox = new Box(BoxLayout.Y_AXIS);  
    //JTextField showVal = new JTextField();  
	
	//滑块滑动的监听事件
    ChangeListener listener; 
	public MainWindow ()
	{
		//初始化成员变量
		Bstart = new JButton ("开始");
		BPause = new JButton ("暂停");
		BInit = new JButton("生成初始状态");
		BClear = new JButton("清除图像");
		Bstart.addActionListener (this);
		BPause.addActionListener (this);
		BInit.addActionListener (this);
		BClear.addActionListener (this);
		rowNumText = new TextField("矩阵行数");
		colNumText = new TextField("矩阵列数");
		cellNumText = new TextField("初始细胞数");
		sliderName = new TextField("慢<---游戏进行速度--->快");
		timeText = new TextField("秒");
		timeText1 = new TextField("秒");
		getMinTime = new TextField("0.05");
		
		getMaxTime = new TextField("1");
		rowNumText.setEditable(false);
		colNumText.setEditable(false);
		cellNumText.setEditable(false);
		timeText.setEditable(false);
		timeText1.setEditable(false);
		sliderName.setEditable(false);
		getRowNum =new TextField("9");
		getColNum = new TextField("9");
		getCellNum = new TextField("5");
        JSlider slider = new JSlider();  
        addSlider(slider); 
        this.add(getMinTime);
        this.add(timeText);
        this.add(sliderName);
        this.add(getMaxTime);
        this.add(timeText1);
        this.add(sliderBox, BorderLayout.CENTER);  
        //this.add(showVal, BorderLayout.SOUTH);  
        this.pack();  

        this.addMouseListener(new MouseListener() { 
        	public void mouseClicked(MouseEvent e) { 
        		
        	}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			//鼠标点击屏幕中的小方格就改变其存活状态
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				int posx = e.getX();
        	    int posy = e.getY();
        	    int j = (posx-offx)/x;
        	    int i =(posy-offy)/x;
        	    if(i>=0&&i<arr.length&&j>=0&&j<arr[i].length)
        	    	arr[i][j]=(arr[i][j]==1?0:1);
        	    repaint();
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
        	
        });
		
		this.setLayout (new FlowLayout ());
		this.add (Bstart);
		this.add (BPause);
		this.add(BInit);
		this.add(cellNumText);
		this.add(getCellNum);
		this.add(BClear);
		this.add(rowNumText);
		this.add(getRowNum);
		this.add(colNumText);
		this.add(getColNum);
		
		this.setSize (900, 900);
		this.setLocationRelativeTo (null);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.setVisible (true);
	}
	
	//滑块的滑动事件监听
    public void addSlider(JSlider slider)  
    {          
    	//定义一个监听器
        listener = new ChangeListener()  
        {    
            public void stateChanged(ChangeEvent event)  
            {    
                //取出滑动条的值，并在文本中显示出来  
            	JSlider source = (JSlider) event.getSource();
                int value = source.getValue();
                double min = Double.parseDouble((getMinTime.getText()));
                double max = Double.parseDouble((getMaxTime.getText()));
                value = (int) (((max-min)/100*value+min)*1000);
                intervalTime = value;
            }  
        }; 
        slider.addChangeListener(listener);  
        Box box = new Box(BoxLayout.X_AXIS);  
        box.add(slider);  
        sliderBox.add(box); 
    }

    //根据窗口大小绘制图像
	public void paint ( Graphics g )
	{	
		super.paint (g);
		int windowWidth = this.getWidth(); //获得窗口宽
		int windowHeight = this.getHeight(); //获得窗口高
		int len = Math.min(windowWidth-offx, windowHeight-offy-20);
		int n=gt.stringToInt(getRowNum);
		int m=gt.stringToInt(getColNum);
		x = Math.min(len/m,len/n);
		int lenx = x*m;
		int leny = x*n;
		offy = getColNum.getY()+getColNum.getHeight()+50;
		offx = (windowWidth-lenx)/2;
		g.setColor(Color.white);
		g.fillRect (offx, offy, lenx, leny);
		g.setColor(Color.black);
		for(int j=0;j<=m;j++) {
			g.drawLine(offx+j*x, 0+offy, offx+j*x, n*x+offy);
		}
		for(int j=0;j<=n;j++) {
			g.drawLine(offx+0, j*x+offy, offx+m*x, j*x+offy);
		}

		
		if(arr==null) {
			arr = new int[n][m];
			oldArr = new int[n][m];
		}else {
			for(int i=0;i<arr.length;i++) {
				for(int j=0;j<arr[i].length;j++) {
					dt.drawARect(g,offx+j*x,offy+i*x,x,x,arr[i][j]);
				}
		}
		}
		g.dispose ();
	}

	//基本控制的监听事件
	public void actionPerformed ( ActionEvent e )
	{
		//开始按钮
		if (e.getSource () == Bstart)
		{
			if(stopFlag) {
				stopFlag = false;
				Bstart.setText("结束");
			}else {
				stopFlag = true;
				Bstart.setText("开始");
				pauseFlag = true;
				BPause.setText("暂停");
			}
			if(arr==null) return;
			CoreThread = new Thread(()->{
			
					while(!stopFlag) {
						repaint ();
						try {
							if(pauseFlag) {
								gt.copy(oldArr,arr);
								GameOfLife.go(arr);
								if(gt.compare(oldArr,arr)) {
									stopFlag = true;
									Bstart.setText("开始");
									pauseFlag=true;
									BPause.setText("暂停");
									JOptionPane.showMessageDialog(null, "游戏结束！");
								}
							}
							Thread.sleep(intervalTime);		
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
			});	
			CoreThread.start();
		}
		
		//暂停按钮
		if (e.getSource () == BPause) {
				if(pauseFlag) {
					pauseFlag=false;
					BPause.setText("暂停中...");
				}else {
					pauseFlag=true;
					BPause.setText("暂停");
				}
		}
		
		//初始化按钮
		if (e.getSource () == BInit) {
			int n,m;
			n=gt.stringToInt(getRowNum);
			m=gt.stringToInt(getColNum);
			arr = new int[n][m];
			oldArr = new int[n][m];
			for(int i=0;i<n;i++) {
				for(int j=0;j<m;j++) {
					arr[i][j] = 0;
				}
			}
			Random random = new Random();
			for(int i=1;i<=gt.stringToInt(getCellNum);i++) {
				if(i>m*n) {
					break;
				}
				int x = random.nextInt(n);
				int y = random.nextInt(m);
				if(arr[x][y]==1) {
					i--;
				}else {
					arr[x][y]=1;
				}
			}
			repaint();
		}
		
		//清除图像按钮
		if (e.getSource () == BClear) {
			for(int i=0;i<arr.length;i++) {
				for(int j=0;j<arr[i].length;j++) {
					arr[i][j] = 0;
				}
			}
			repaint();
		}

	}
}
