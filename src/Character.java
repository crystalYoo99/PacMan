import java.awt.image.BufferedImage;

public class Character {
	public static BufferedImage[] pacman;
	public static BufferedImage[] ghost;
	public static BufferedImage[] smartGhost;
	public static BufferedImage[] box;
	public Character(){
		pacman = new BufferedImage[8];
		ghost = new BufferedImage[4];
		smartGhost = new BufferedImage[8];
		box = new BufferedImage[4];

		pacman[0]=Game.appearance.getAppearance(0, 0);
		pacman[1]=Game.appearance.getAppearance(32,0);
		pacman[2]=Game.appearance.getAppearance(64, 0);
		pacman[3]=Game.appearance.getAppearance(96, 0);
		pacman[4]=Game.appearance.getAppearance(128, 0);
		pacman[5]=Game.appearance.getAppearance(128, 32);
		pacman[6]=Game.appearance.getAppearance(128, 64);
		pacman[7]=Game.appearance.getAppearance(128, 96);

		ghost[0]=Game.appearance.getAppearance(0, 32);
		ghost[1]=Game.appearance.getAppearance(32, 32);
		ghost[2]=Game.appearance.getAppearance(64, 32);
		ghost[3]=Game.appearance.getAppearance(96, 32);

		smartGhost[0]=Game.appearance.getAppearance(0, 64);
		smartGhost[1]=Game.appearance.getAppearance(32, 64);
		smartGhost[2]=Game.appearance.getAppearance(64, 64);
		smartGhost[3]=Game.appearance.getAppearance(96, 64);
		smartGhost[4]=Game.appearance.getAppearance(0, 96);
		smartGhost[5]=Game.appearance.getAppearance(32, 96);
		smartGhost[6]=Game.appearance.getAppearance(64, 96);
		smartGhost[7]=Game.appearance.getAppearance(96, 96);

		box[0]=Game.appearance.getAppearance(0, 128);
		box[1]=Game.appearance.getAppearance(32,128);
		box[2]=Game.appearance.getAppearance(64, 128);
		box[3]=Game.appearance.getAppearance(96, 128);

	}

}
