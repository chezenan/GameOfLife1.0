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
	//С����ı߳�x
	//ͼ��ĺ��������ʼƫ����offx
	//ͼ������������ʼƫ����offy
	int x,offy,offx ;
	
	//���岢������ݴ���ͻ�ͼ���������dt��gt
	DrawTool dt = DrawTool.getDrawTool();
	GameTool gt = GameTool.getGameTool();
	
	//���尴ť:��ʼ,��ͣ,��ʼ��,���ͼ�� 
	JButton Bstart = null;
	JButton BPause = null;
	JButton BInit = null;
	JButton BClear = null;
	
	//�����������ڴ��ϸ�����״̬
	static int[][] arr;
	
	//�����������ڴ����һʱ��ϸ�����״̬
	int [][] oldArr;
	
	//���ڼ�����߳�
	Thread CoreThread = null;
	
	//��������text�������������  
	TextField rowNumText,colNumText,getRowNum,getColNum,cellNumText,getCellNum,timeText,timeText1,getMinTime,getMaxTime,sliderName;
	
	//��ͣ�����жϱ�ǩ
	volatile Boolean  pauseFlag = true;
	
	//��ʼֹͣ�жϱ�ǩ
	volatile Boolean  stopFlag = true;
	
	//������ʱ��
	volatile int intervalTime = 1000;
	
	//���Ƽ���ʱ��
	Box sliderBox = new Box(BoxLayout.Y_AXIS);  
    //JTextField showVal = new JTextField();  
	
	//���黬���ļ����¼�
    ChangeListener listener; 
	public MainWindow ()
	{
		//��ʼ����Ա����
		Bstart = new JButton ("��ʼ");
		BPause = new JButton ("��ͣ");
		BInit = new JButton("���ɳ�ʼ״̬");
		BClear = new JButton("���ͼ��");
		Bstart.addActionListener (this);
		BPause.addActionListener (this);
		BInit.addActionListener (this);
		BClear.addActionListener (this);
		rowNumText = new TextField("��������");
		colNumText = new TextField("��������");
		cellNumText = new TextField("��ʼϸ����");
		sliderName = new TextField("��<---��Ϸ�����ٶ�--->��");
		timeText = new TextField("��");
		timeText1 = new TextField("��");
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
			//�������Ļ�е�С����͸ı�����״̬
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
	
	//����Ļ����¼�����
    public void addSlider(JSlider slider)  
    {          
    	//����һ��������
        listener = new ChangeListener()  
        {    
            public void stateChanged(ChangeEvent event)  
            {    
                //ȡ����������ֵ�������ı�����ʾ����  
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

    //���ݴ��ڴ�С����ͼ��
	public void paint ( Graphics g )
	{	
		super.paint (g);
		int windowWidth = this.getWidth(); //��ô��ڿ�
		int windowHeight = this.getHeight(); //��ô��ڸ�
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

	//�������Ƶļ����¼�
	public void actionPerformed ( ActionEvent e )
	{
		//��ʼ��ť
		if (e.getSource () == Bstart)
		{
			if(stopFlag) {
				stopFlag = false;
				Bstart.setText("����");
			}else {
				stopFlag = true;
				Bstart.setText("��ʼ");
				pauseFlag = true;
				BPause.setText("��ͣ");
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
									Bstart.setText("��ʼ");
									pauseFlag=true;
									BPause.setText("��ͣ");
									JOptionPane.showMessageDialog(null, "��Ϸ������");
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
		
		//��ͣ��ť
		if (e.getSource () == BPause) {
				if(pauseFlag) {
					pauseFlag=false;
					BPause.setText("��ͣ��...");
				}else {
					pauseFlag=true;
					BPause.setText("��ͣ");
				}
		}
		
		//��ʼ����ť
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
		
		//���ͼ��ť
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
