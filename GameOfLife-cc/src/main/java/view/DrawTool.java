package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;

public class DrawTool {
	
	private static DrawTool dt = new DrawTool();
	
	private DrawTool() {}
	
	public static DrawTool getDrawTool() {
		return dt;
	}
	
	//根据坐标和大小画小方格
	public void drawARect(Graphics g,int x,int y,int lenx,int leny,int num) {
		if(num==1) {
			g.setColor(Color.GREEN);
			g.fillRect (x, y, lenx, leny);
			g.setColor(Color.BLACK);
			g.drawRect (x, y, lenx, leny);
		}
	}
}
