package control;
public class GameOfLife {
	public static void go(int[][] arr) throws InterruptedException {
		
		//�õ�GameTool���߶���
		GameTool gt = GameTool.getGameTool();
		
		//���ݵ�ǰϸ��������������һ��ϸ�����
		int[][] neighbour = new int[arr.length][arr[0].length];
			for(int i=0;i<arr.length;i++) {
				for(int j=0;j<arr[i].length;j++) {
					neighbour[i][j] = gt.getNeighbour(arr,i,j);
				}
			}
			gt.change(arr,neighbour);
	}
}


