import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ghost extends Rectangle{

	private int right = 0;
	private int left = 1;
	private int up = 2;
	private int down = 3;
	private int direction = -1;

	private int imageIndex=0;

	public Random randomDirection;

	private int speed = 1;

	public Ghost(int x, int y) {
		randomDirection = new Random();
		setBounds(x, y, 32, 32);
		direction = randomDirection.nextInt(4);
	}

	public void tick() {
			if(direction==right) {
				if(canMove(x+speed, y)) {
					x=x+speed;
					imageIndex=0;
				}
				else
					direction = randomDirection.nextInt(4);
			}
			else if(direction==left) {
				if(canMove(x-speed, y)) {
					x=x-speed;
					imageIndex=1;
				}
				else
					direction = randomDirection.nextInt(4);
			}
			else if(direction==up) {
				if(canMove(x, y-speed)) {
					y=y-speed;
					imageIndex=2;
				}
				else
					direction = randomDirection.nextInt(4);
			}
			else if(direction==down) {
				if(canMove(x, y+speed)) {
					y=y+speed;
					imageIndex=3;
				}
				else
					direction = randomDirection.nextInt(4);
			}
	}
	
	private boolean canMove(int next_x, int next_y) {

		Rectangle bounds = new Rectangle(next_x, next_y, width, height);
		Map map = Game.map;

		for(int i=0;i<map.tiles.length;i++) {
			for(int j=0;j<map.tiles[0].length;j++) {
				if(map.tiles[i][j]!=null) {
					if(bounds.intersects(map.tiles[i][j])) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public void render(Graphics g) {
		g.drawImage(Character.ghost[imageIndex], x, y, null);
	}
}
