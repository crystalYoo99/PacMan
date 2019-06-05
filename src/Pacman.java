import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Pacman extends Rectangle{

	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	private int speed = 3;
	private int imageIndex = 0;
	public static double timer;
	public static double tt;

	Score curScore;

	public Pacman(int x, int y) {
		curScore=Game.score;
		curScore.score=0;
		setBounds(x, y, 30, 30);
	}

	private boolean canMove(int next_x, int next_y) {

		Rectangle bounds = new Rectangle(next_x, next_y, width, height);
		Map map = Game.map;

		for(int i=0;i<map.tiles.length;i++){
			for(int j=0;j<map.tiles[0].length;j++){
				if(map.tiles[i][j] != null){
					if(bounds.intersects(map.tiles[i][j])) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public void tick() {
		if(Game.itemSTATE == Game.ITEM_SPEED) speed = 6;
		else speed = 3;
		
		if(right&&canMove(x+speed, y)){
			x=x+speed;
			if(x%2==0) {
				imageIndex=4;
			}
			else {imageIndex = 0;}
		}
		if(left&&canMove(x-speed, y)) {
			x=x-speed;
			if(x%2==0) {
				imageIndex=5;
			}
			else{imageIndex = 1;}
		}
		if(up&&canMove(x, y-speed)) {
			y=y-speed;
			if(y%2==0) {
				imageIndex=6;
			}
			else{imageIndex = 2;}
		}
		if(down&&canMove(x, y+speed)) {
			y=y+speed;
			if(y%2==0) {
				imageIndex=7;
			}
			else{imageIndex = 3;}
		}

		Map map = Game.map;

		for(int i=0;i<map.seeds.size();i++){
			if(this.intersects(map.seeds.get(i))) {
				curScore.score+=10;
				map.seeds.remove(i);
				break;
			}
		}

		for(int i=0;i<map.box.size();i++){
			if(this.intersects(map.box.get(i))) {
				timer = System.currentTimeMillis();
				map.box.remove(i);

				Random random =new Random();
				int randomBox = random.nextInt(4)+2;
				//Game.itemSTATE=randomBox;
				Game.itemSTATE=randomBox;
				break;
			}
		}
		tt = 5000+(timer - System.currentTimeMillis());
		if(System.currentTimeMillis()-timer>=5000){
			Game.itemSTATE = -1;
		}

		if(map.seeds.size()==0) {

			Game.STATE=Game.GAMEOVER;
			return;
		}

		for(int i=0;i<Game.map.Ghosts.size();i++){

			Ghost temp = Game.map.Ghosts.get(i);

			if(temp.intersects(this) && Game.itemSTATE == Game.ITEM_KING) {
				curScore.score+=20;
			}
			else if(temp.intersects(this)) {
				Game.STATE=Game.GAMEOVER;
				Game.DEAD = true;
				curScore.insertScore();
				return;
			}
		}
		for(int i=0;i<Game.map.SmartGhosts.size();i++){

			SmartGhost temp=Game.map.SmartGhosts.get(i);

			if(temp.intersects(this) && Game.itemSTATE == Game.ITEM_KING) {
				curScore.score+=20;
			}
			else if(temp.intersects(this)) {
				Game.STATE=Game.GAMEOVER;
				Game.DEAD = true;
				curScore.insertScore();
				return;
			}
		}
	}

	public void render(Graphics g){
		g.drawImage(Character.pacman[imageIndex], x, y, width, height, null);
		// //<<<<<sujeong change>>>>>
		// if(Game.itemSTATE == Game.ITEM_KING) {
		// 	g.setColor(Color.YELLOW);
		// 	g.drawString("ITEM : KING", 80, 155);
		// }
		// else if(Game.itemSTATE == Game.ITEM_BLACK) {
		// 	g.setColor(Color.BLACK);
		// 	g.drawString("ITEM : BLACK", 80, 155);
		// }
		// else if(Game.itemSTATE == Game.ITEM_DEVIL) {
		// 	g.setColor(Color.RED);
		// 	g.drawString("ITEM : DEVIL", 80, 155);
		// }
		// else if(Game.itemSTATE == Game.ITEM_SPEED) {
		// 	g.setColor(Color.CYAN);
		// 	g.drawString("ITEM : SPEED", 80, 155);
		// }
		// //sujeogn finish
	}
}
