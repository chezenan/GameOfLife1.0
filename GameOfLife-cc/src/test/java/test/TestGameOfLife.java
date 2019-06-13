package test;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import control.GameTool;
//import draw.GameOfLife;
public class TestGameOfLife {
	private int arr[][];
	GameTool gt;
	@Before
	public void init(){
		gt = GameTool.getGameTool();
		/*初始状态
		 *  1111
		 *  0000
		 *  0000
		 *  0000
		 */
		arr = new int[4][4];
		arr[0][0] = 1;
		arr[0][1] = 1;
		arr[0][2] = 1;
		arr[0][3] = 1;
		for(int i=1;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				arr[i][j]=0;
			}
		}
		
	}
	
	@Test
	public void testgetNeighbour(){
		int num = gt.getNeighbour(arr, 0, 1);
		
		if(num==2){
			System.out.println("pass");
		}else{
			fail("getNeighbour-->fail");
		}
	}
	
	@Test
	public void testChange(){
		/*	预期结果
		 *  0110
		 *  0110
		 *  0000
		 *  0000
		 */
		
		int [][] expResult = new int [arr.length][arr[0].length];
		expResult[0][1] = 1;
		expResult[0][2] = 1;
		expResult[1][1] = 1;
		expResult[1][2] = 1;
		
		for(int i=1;i<expResult.length;i++){
			for(int j=0;j<expResult[0].length;j++){
				if(arr[i][j]==1){
					continue;
				}else{
					arr[i][j]=0;
				}
			}
		}
		//调用测试过的getNeighbour函数获取每个细胞周围存活细胞
		int[][] neighbour = new int[4][4];
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				neighbour[i][j] = gt.getNeighbour(arr,i,j);
			}
		}
		
		gt.change(arr,neighbour);
		//比对测试
		for(int i=1;i<expResult.length;i++){
			for(int j=0;j<expResult[0].length;j++){
				if(expResult[i][j]!=arr[i][j]){
					fail("Change-->fail");
					break;
				}else{
					continue;
				}
			}
		}
		System.out.println("pass");
	}

}
