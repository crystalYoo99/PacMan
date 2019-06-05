import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Box extends Rectangle{

	public Box(int x, int y) {
		setBounds(x, y, 32, 32);
	}

	public void render(Graphics g) {
		g.drawImage(Character.box[0], x, y, null);
	}
}
