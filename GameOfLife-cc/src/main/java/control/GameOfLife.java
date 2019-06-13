package control;
public class GameOfLife {
	public static void go(int[][] arr) throws InterruptedException {
		
		//得到GameTool工具对象
		GameTool gt = GameTool.getGameTool();
		
		//根据当前细胞存活情况计算下一代细胞情况
		int[][] neighbour = new int[arr.length][arr[0].length];
			for(int i=0;i<arr.length;i++) {
				for(int j=0;j<arr[i].length;j++) {
					neighbour[i][j] = gt.getNeighbour(arr,i,j);
				}
			}
			gt.change(arr,neighbour);
	}
}


