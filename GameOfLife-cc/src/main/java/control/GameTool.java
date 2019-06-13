package control;

import java.awt.TextField;

public class GameTool {
	
	private static GameTool gt = new GameTool();
	
	private GameTool(){}
	
	public static GameTool getGameTool() {
		return gt;
	}
	
	//得到当前细胞周围存活的细胞数目
	public int getNeighbour(int[][] arr,int i,int j) {
		int count = 0;
		for(int ii=i-1;ii<=i+1;ii++) {
			if(ii<0||ii>=arr.length) {
				continue;
			}
			for(int jj=j-1;jj<=j+1;jj++) {
				if(jj<0||jj>=arr[i].length) {
					continue;
				}
				if(ii==i&&jj==j) {
					continue;
				}
				if(arr[ii][jj]==1) {
					count++;
				}
			}
		}
		return count;	
	}
	
	//根据细胞周围细胞存活情况改变自身状态
	public void change(int[][] arr,int[][] neighbour) {
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				if(arr[i][j]==0) {
					if(neighbour[i][j]==3) {
						arr[i][j]=1;
					}
				}else {
					if(neighbour[i][j]>3||neighbour[i][j]<2) {
						arr[i][j] = 0;
					}
				}
			}
		}
	}
	
	//比较两个二位整型数组内容是否相同
	public boolean compare(int[][] oldArr, int[][] arr) {
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				if(oldArr[i][j] != arr[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	//将二维整型数组arr中的内容复制到oldArr中
	public void copy(int[][] oldArr, int[][] arr) {
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				oldArr[i][j] = arr[i][j];
			}
		}
		
	}
	
	//将输入值进行判断处理保证其为一个整型变量并输出该整型变量
	public  int stringToInt(TextField text) {
		String str =  text.getText();
		char[] chs = str.toCharArray();
		for(char ch:chs) {
			if(ch<48||ch>57) {
				text.setText("9");
				return 9;
			}
		}
		return Integer.parseInt(str);
	}
}
