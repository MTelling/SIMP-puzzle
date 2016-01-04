import java.awt.Point;

public class Tile 
{

	protected Point where;
	protected int tileNum;
	
	
	public Tile (int x, int y, int num)
	{
		where = new Point(x,y);
		tileNum = num;
	}
	
	public int getx()
	{
		return where.x;
	}
	
	public int gety()
	{
		return where.y;
	}
	
}
