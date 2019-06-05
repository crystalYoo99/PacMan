import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{

	private boolean isRunning = false;

	public static final int WIDTH = 1340; // 留� 媛�濡쒓만�씠
	public static final int HEIGHT = 960; // 留� �꽭濡쒓만�씠
	public static final String TITLE = "Pacman"; // 寃뚯엫 ���씠��

	private Thread thread; //�벐�젅�뱶

	public static Pacman pacman;
	public static Map map;
	public static Appearance appearance;
	public static Score score;

	// 寃뚯엫 �긽�깭 //
	public static final int GAMEOVER = 0; // 寃뚯엫�삤踰�
	public static final int GAME = 1; // 寃뚯엫 吏꾪뻾 以�
	public static final int NEWGAME = 2; // �깉濡쒖슫 寃뚯엫�떆�옉
	public static boolean DEAD = false; // 寃뚯엫�삤踰�-二쎌쓬 �뿬遺� �솗�씤
	public static boolean WIN =false; // 寃뚯엫�삤踰�-�듅由� �뿬遺� �솗�씤

	// 硫붾돱�꽑�깮 諛� 寃뚯엫 吏꾪뻾�긽�솴 �뵆�옒洹� //
	public boolean gameset = false; // 留�, 罹먮┃�꽣媛� 紐⑤몢 �꽑�깮�릺�뿀�뒗吏� 泥댄겕
	public boolean startMain = false;
	public boolean selectMap = false;
	public boolean selectChar = false;
	public boolean seeScore = false;
	public boolean seeRank = false;

	public String mapPath = "CSEE";
	public String charPath = "Jeon";


	public static String [] maps =  {"CSEE", "Handong", "Office"};
	public static String [] characters = {"Jeon", "You", "Park", "Kim", "Hwang", "Choi"};
	public static String [][] alpha = {
			{"A", "B", "C", "D", "E", "F", "G", "H", "I", },
			{"J", "K", "L", "M", "N", "O", "P", "Q", "R"},
			{"S", "T", "U", "V", "W", "X", "Y", "Z", "DONE"}
	};
	public String userName = "";
	StringBuilder sb = new StringBuilder();

	public int seedX = 160;
	public int seedY = 120;
	public int nameX = 20;
	public int nameY = 250;

	//<<<<<sujeong change>>>>>
	public static int itemSTATE = -1;
	public static final int ITEM_DEVIL = 2;
	public static final int ITEM_KING = 3;
	public static final int ITEM_STERN = 4;
	public static final int ITEM_BLACK = 5;

	public static int STATE = -1;

	public boolean isEnter = false;
	private boolean showText = true;
	public static final Color TRANSCLUENT_BLACK = new Color(0f, 0f, 0f, 0.7f);

	/********************************************************************************************************/

	public Game() {
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT); // �쐞�뿉�꽌 �꽕�젙�븳 媛�濡쒖꽭濡� 湲몄씠瑜� 媛�吏��뒗 dimension �겢�옒�뒪 �꽑�뼵(awt)
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		addKeyListener(this);

		STATE=NEWGAME;
		WIN=false;
		DEAD=false;

		score = new Score("scoreboard.txt"); // Score 媛앹껜瑜� �깮�꽦. �깮�꽦�옄 �씤�옄濡� score �뙆�씪�쓽 寃쎈줈瑜� �꽆寃⑥쨲
		pacman = new Pacman(Game.WIDTH/2, Game.HEIGHT/2); // 留� 媛�濡�/2, �꽭濡�/2 �젙蹂대�� �깮�꽦�옄�쓽 �씤�옄濡� �꽆寃⑥＜硫� �뙥留� 媛앹껜 �깮�꽦
		map=new Map("/"+mapPath+".png"); // 留� 媛앹껜 �깮�꽦. �깮�꽦�옄 �씤�옄濡� 留� �씠誘몄��뙆�씪 �쐞移섎�� �꽆寃⑥쨲
		//map=new Map("/map/CSEE.png"); // 留� 媛앹껜 �깮�꽦. �깮�꽦�옄 �씤�옄濡� 留� �씠誘몄��뙆�씪 �쐞移섎�� �꽆寃⑥쨲


	}

	/********************************************************************************************************/

	public synchronized void start() { // Start 硫붿냼�뱶
		if(isRunning) // 留뚯빟�뿉 寃뚯엫�씠 �떎�뻾以묒씠硫�
			return; // 硫붿냼�뱶 醫낅즺 -> �뼱李⑦뵾 �떎�뻾以묒씠湲� �븣臾몄뿉 �떎�떆 �떆�옉�븷 �븘�슂媛� �뾾�쑝誘�濡�
		isRunning = true; // �븘�땲硫� 寃뚯엫�쓣 �떎�뻾 以� �긽�깭濡� 諛붽씀怨�
		thread = new Thread(this); // �씠 媛앹껜瑜� �벐�젅�뱶濡� �꽆湲대떎
		thread.start(); // �벐�젅�뱶 �떆�옉
	}

	/********************************************************************************************************/

	public synchronized void stop() {
		if(!isRunning) // 留뚯빟 寃뚯엫�씠 �떎�뻾以묒씠 �븘�땲�씪硫�
			return; // 硫붿냼�뱶 醫낅즺
		isRunning = false; // �븘�땲硫� 寃뚯엫�쓣 �떎�뻾醫낅즺 �긽�깭濡� 諛붽퓞
		try {
			thread.join(); // �떎瑜� �뒪�젅�뱶媛� 醫낅즺�맆 �븣源뚯� ��湲고뻽�떎媛� �떎 醫낅즺�릺硫� 醫낅즺
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/********************************************************************************************************/

	private void render() {
		BufferStrategy bs = getBufferStrategy();

		if(bs==null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics(); // 洹몃┝洹몃━湲� 媛앹껜 �깮�꽦
		g.setColor(Color.black); // �깋源붿쓣 寃����깋�쑝濡� 蹂�寃�
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT); // 0,0 醫뚰몴�뿉 留� 媛�濡쒓만�씠, �꽭濡쒓만�씠 留뚰겮�쓽 �궗媛곹삎 梨꾩썙 洹몃━湲� -> 寃����깋�쑝濡� �꽕�젙�뻽湲� �븣臾몄뿉 寃����깋 �궗媛곹삎�쑝濡� 梨꾩슫

		int menu_width = 600; // 硫붾돱�솕硫� 媛�濡쒓만�씠
		int menu_height = 400; // 硫붾돱�솕硫� �꽭濡쒓만�씠
		int xx = Game.WIDTH/2 - menu_width/2; // �궗媛곹삎�쓽 �쇊履� �긽�떒 瑗�吏��젏�쓽 x醫뚰몴
		int yy = Game.HEIGHT/2 - menu_height/2; // �궗媛곹삎�쓽 �쇊履� �긽�떒 瑗�吏��젏�쓽 y醫뚰몴

		if(STATE==GAME) { // STATE 蹂��닔媛� GAME. 寃뚯엫 �떎�뻾 以� �긽�깭
			pacman.render(g); // �뙥留� �씠誘몄� 洹몃━湲�
			map.render(g); // 留� �씠誘몄� 洹몃━湲�
			score.render(g); // �뒪肄붿뼱 �씠誘몄� 洹몃━湲�
			
			if(itemSTATE == ITEM_BLACK) {
				g.setColor(Color.white);
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
				g.drawString("SCORE", 80, 60);

				Graphics gb = bs.getDrawGraphics();
				gb.setColor(TRANSCLUENT_BLACK);
				gb.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			}
			else{
				g.setColor(Color.white); //�븯���깋�쑝濡� �꽕�젙
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 25)); // 湲��뵪 �꽭�똿
				g.drawString("SCORE", 80, 60); // 湲��뵪 洹몃━湲�
			}
		}

		else if (STATE==GAMEOVER) {
			g.setColor(new Color(0, 0, 150)); // BLUE 濡� �깋源붾줈 �꽕�젙
			g.fillRect(xx, yy, menu_width, menu_height); // 硫붾돱 �솕硫� 洹몃━湲�

			g.setColor(Color.white); // �븯���깋 �꽕�젙
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25)); // 湲��뵪 洹몃━湲� �꽕�젙

			if(showText && (DEAD || WIN)) { // showText 媛� true�씪硫�
				if(DEAD){	// gameover-dead �긽�깭硫� 寃뚯엫�삤踰� 硫붿꽭吏� 異쒕젰
					g.drawString("YOU ARE DEAD", xx+200, yy+50);
				}
				else if(WIN) { // gameover-win �긽�깭硫� �듅由� 硫붿꽭吏� 異쒕젰
					g.drawString("!!! YOU WIN !!!", xx+215, yy+130);
				}

				if (!seeScore) {
					g.drawString("Your Score is "+score.score, xx+190, yy+90);
					g.drawString("Enter your name and Check your Rank!", xx+70, yy+130);

				// �궎蹂대뱶 諛곗뿴 諛� �엯�젰 //
					g.setColor(Color.yellow);
					g.drawString(">", xx+nameX, yy+nameY);
					g.setColor(Color.white);
					g.drawString("A", xx+50, yy+250); g.drawString("B", xx+100, yy+250); g.drawString("C", xx+150, yy+250); g.drawString("D", xx+200, yy+250); g.drawString("E", xx+250, yy+250);
					g.drawString("F", xx+300, yy+250); g.drawString("G", xx+350, yy+250); g.drawString("H", xx+400, yy+250); g.drawString("I", xx+450, yy+250);
					g.drawString("J", xx+50, yy+300); g.drawString("K", xx+100, yy+300); g.drawString("L", xx+150, yy+300); g.drawString("M", xx+200, yy+300); g.drawString("N", xx+250, yy+300);
					g.drawString("O", xx+300, yy+300); g.drawString("P", xx+350, yy+300); g.drawString("Q", xx+400, yy+300); g.drawString("R", xx+450, yy+300);
					g.drawString("S", xx+50, yy+350); g.drawString("T", xx+100, yy+350); g.drawString("U", xx+150, yy+350);g.drawString("V", xx+200, yy+350); g.drawString("W", xx+250, yy+350); g.drawString("X", xx+300, yy+350);
					g.drawString("Y", xx+350, yy+350); g.drawString("Z", xx+400, yy+350); g.drawString("DONE", xx+450, yy+350);

					// DONE �씠 �엯�젰�릺硫� �쑀�� �븘�씠�뵒 �셿�꽦, �븘�땶寃쎌슦�뿉�뒗 臾몄옄�뿴�뿉 怨꾩냽 遺숈씠湲� //
					if (isEnter) {
						String alphas = alpha[(nameY-249)/50][(nameX-19)/50];
						if (alphas.equals("DONE")) seeScore = true;
						else {
							sb.append(alphas);
							userName = sb.toString();
						}
					}
						// �쑀�� �븘�씠�뵒 怨꾩냽 �봽由고듃 //
						g.drawString(userName, xx+200, yy+180);
						isEnter = false;
				}
				else if (!seeRank) {
					g.drawString("RANK", xx+200, yy+100);
					Rank r = new Rank("rankboard.txt",score);
		               r.name = userName;
		               r.insertRank();
		               r.showRank(g);
		               if (isEnter) {
		            	   seeRank = true;
		            	   isEnter = false;
		               }
				}
				else {
					// seeScore媛� �셿�꽦�릺硫� 由ъ뒪���떚 硫붿꽭吏� 異쒕젰 //
					g.drawString("Press enter to restart the game", xx+115, yy+200);
					if (isEnter) {
						STATE = NEWGAME;
						isEnter = false;
						seeScore = false;
						seeRank = false;
						DEAD = false;
						WIN = false;
						seedY= 120;
						userName = "";
						sb.delete(0, sb.length());
					}
				}
			}
		}
		else if(STATE==NEWGAME) { // 寃뚯엫 �긽�깭媛� START. ��湲곗쨷 �긽�깭
			g.setColor(new Color(0, 0, 150)); // BLUE 濡� �깋源붾줈 �꽕�젙
			g.fillRect(xx, yy, menu_width, menu_height); // 硫붾돱 �솕硫� 洹몃━湲�
			g.setColor(Color.white); // �븯���깋 �꽕�젙
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25)); // 湲��뵪 洹몃━湲� �꽕�젙

			// 泥� �솕硫댁씠 �븳踰덈룄 蹂댁뿬吏� �쟻�씠 �뾾�떎硫� //
			if(showText && !startMain) { // showText 媛� true�씪硫�
				g.drawString("WELCOME TO PACMAN", xx+150, yy+160);
				g.drawString("Press enter to start the game", xx+125, yy+200);
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
				g.drawString("Made by Jeon, You, Hwang, Choi, Kim and Park. Team 6", xx+90, yy+300);
				if (isEnter == true) {
					startMain = true;
					isEnter = false;
				}
			}
			// 留듭씠 �꽑�깮�릺怨� �븘吏� 寃뚯엫�씠 �떆�옉�릺吏� �븡�븯�떎硫� //
			else if(showText && !selectMap && !gameset) {
				Seed seeds;
				g.drawString("Where do you want to travel?", xx+115, yy+70);
				g.drawString("CSEE", xx+205, yy+144);
				g.drawString("HGU", xx+205, yy+174);
				g.drawString("pf.Ahn's office", xx+205, yy+204);

				seeds = new Seed(xx+seedX, yy+seedY);
				seeds.render(g);

				if (isEnter == true) {
					mapPath = maps[(seedY-119)/30];
					selectMap = true;
					isEnter = false;
					seedY =100;
					nameX = 20;
					nameY = 250;

				}
			}
			else if (showText && !selectChar && !gameset) {
				Seed seeds;
				g.drawString("Select Character", xx+210, yy+60);
				g.drawString("Jeon", xx+205, yy+124);
				g.drawString("You", xx+205, yy+154);
				g.drawString("Park", xx+205, yy+184);
				g.drawString("Kim", xx+205, yy+214);
				g.drawString("Hwang", xx+205, yy+244);
				g.drawString("Choi", xx+205, yy+274);

				seeds = new Seed(xx+seedX, yy+seedY);
				seeds.render(g);

				// �뿏�꽣媛� �닃由щ㈃ �룄�듃 �쐞移섏뿉 �엳�뒗 罹먮┃�꽣媛� �꽑�깮�맂�떎. //
				if (isEnter == true) {

					charPath = characters[(seedY-99)/30];
					appearance = new Appearance(charPath+".png"); // appearance �뙆�씪�쓽 ���옣寃쎈줈瑜� �꽆寃⑥＜硫� 媛앹껜 �깮�꽦
					new Character(); // Character 媛앹껜 �깮�꽦
					selectChar = true;
					isEnter = false;
					seedY = 120;
					nameX = 20;
					nameY = 250;
				}
			}

			// 留듦낵 罹먮┃�꽣媛� 紐⑤몢 �꽑�깮�릺硫� 寃뚯엫�쓣 �떆�옉媛��뒫 �긽�깭濡� 留뚮뱺�떎 //
			if (showText && selectMap && selectChar) {
				selectMap = false;
				selectChar = false;
				gameset=true;
			}
		}
		g.dispose();
		bs.show(); //
	}

	/********************************************************************************************************/

	private void tick() {
		if(STATE==GAME) {
			pacman.tick(); // 寃뚯엫以� �긽�깭�씪 �븣,tick() 硫붿꽌�뱶 �떎�뻾
			map.tick(); // 寃뚯엫以� �긽�깭�씪 �븣, tick() 硫붿냼�뱶 �떎�뻾
		}
		else if(STATE == NEWGAME && gameset == true) { // 寃뚯엫 �떆�옉 ��湲� �긽�깭�씠硫�
			if(gameset) {  // �뿏�꽣媛� �닃�졇�떎硫�
				isEnter=false; // isEnter 瑜� false濡� 蹂�寃�
				gameset = false;
				pacman = new Pacman(Game.WIDTH/2, Game.HEIGHT/2);// �깉濡쒖슫 �뙥留� 媛앹껜 �깮�꽦
				map=new Map("/"+mapPath+".png");
				//map=new Map("/map/CSEE.png");
				appearance = new Appearance(charPath+".png");
				STATE=GAME;
			}
		}
	}

	/***************************************** �뒪�젅�뱶媛� �떆�옉�릺硫� �샇異쒕맖 ************************************************/

	public void run() {

		requestFocus(); //�샇硫댁뿉 focus媛� 媛��룄濡� �븳�떎.
		int fps = 0; // fps瑜� 0�쑝濡� 理쒓린�솕
		double timer = System.currentTimeMillis();// �쁽�옱�떆媛꾩쓣 millisecond 濡� �솚�궛�빐�꽌 timer�뿉 ���옣
		long lastTime = System.nanoTime(); // 寃쎄낵�맂 �떆媛꾩쓣 lasttime�뿉 ���옣
		double targetTick = 100.0; // 洹몃옒�뵿 �깉濡� �쓣�슱 �떆媛� 媛꾧꺽�쓣 �꽕�젙�븯湲� �쐞�븳 蹂��닔
		double delta = 0;
		double ns = 1000000000 / targetTick; // targettick�쓣 nanosecond 濡� �솚�궛�븯�뿬 ns�뿉 ���옣

		while(isRunning) { // 寃뚯엫�씠 �떎�뻾以� �긽�깭�씪硫�
			long now = System.nanoTime(); // 寃쎄낵�떆媛꾩쓣 now�뿉 ���옣
			delta = delta + ((now-lastTime)/ns); // 寃쎄낵�떆媛꾩뿉�꽌 �씠�쟾 寃쎄낵�떆媛꾩쓣 鍮쇱꽌 ns 濡� �굹�늻�뼱以�
			lastTime = now; //吏�湲� 寃쎄낵�떆媛꾩쓣 ���옣

			while(delta >=1) { // delta媛� 1�씠�긽�씪�븣�뒗 怨꾩냽 �깉濡쒖슫 洹몃옒�뵿�쓣 �깮�꽦�븳�떎.
				tick(); // 寃뚯엫�긽�깭 怨꾩냽 �솗�씤
				render(); // 洹몃┝ 洹몃━湲�
				fps++;
				delta--;
			}

			if(System.currentTimeMillis()-timer>=1000) {
				fps=0;
				timer = timer+1000;
			}

		}
		stop(); // �뒪�젅�뱶 醫낅즺
	}

	/****************************************************** �궎蹂대뱶 �엯�젰愿�由� *******************************************************/

	public void keyPressed(KeyEvent e) {

		if(STATE == GAME && itemSTATE == ITEM_DEVIL) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				pacman.left = true;
			if(e.getKeyCode()==KeyEvent.VK_LEFT)
				pacman.right=true;
			if(e.getKeyCode()==KeyEvent.VK_UP)
				pacman.down=true;
			if(e.getKeyCode()==KeyEvent.VK_DOWN)
				pacman.up=true;
		}
		else if(STATE == GAME) { // 寃뚯엫�씠 �떎�뻾 以� �긽�깭�씪 �븣,
			if(e.getKeyCode()==KeyEvent.VK_RIGHT) // �궎瑜� �엯�젰諛쏆븘�꽌 洹� �궎媛� �삤瑜몄そ �궎�씪硫�
				pacman.right=true; // �뙥留� �겢�옒�뒪�뿉 right �떆洹몃꼸�쓣 蹂대궡湲�
			if(e.getKeyCode()==KeyEvent.VK_LEFT)
				pacman.left=true; // �뙥留� �겢�옒�뒪�뿉 left �떆洹몃꼸�쓣 蹂대궡湲�
			if(e.getKeyCode()==KeyEvent.VK_UP)
				pacman.up=true; // �뙥留� �겢�옒�뒪�뿉 up �떆洹몃꼸�쓣 蹂대궡湲�
			if(e.getKeyCode()==KeyEvent.VK_DOWN)
				pacman.down=true; // �뙥留� �겢�옒�뒪�뿉 down �떆洹몃꼸�쓣 蹂대궡湲�
		}


		if (STATE == NEWGAME && startMain && !selectMap) { // 寃뚯엫�씠 ��湲곗쨷 �긽�깭�씪 �븣
			if(e.getKeyCode()==KeyEvent.VK_ENTER) // �뿏�꽣媛� �엯�젰�릺硫�
				isEnter = true; // �뿏�꽣蹂��닔瑜� true 濡� 蹂�寃�
			if(e.getKeyCode()==KeyEvent.VK_DOWN) { // �궎瑜� �엯�젰諛쏆븘�꽌 洹� �궎媛� �삤瑜몄そ �궎�씪硫�
				seedY+=30;
				if (seedY > 180) {
					seedY = 120;
				}
			}
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				seedY-=30;
				if (seedY < 120) {
					seedY = 180;
				}
			}
		}


		else if (STATE == GAMEOVER && !seeScore) { // 寃뚯엫�씠 ��湲곗쨷 �긽�깭�씪 �븣
			if(e.getKeyCode()==KeyEvent.VK_ENTER) // �뿏�꽣媛� �엯�젰�릺硫�
				isEnter = true; // �뿏�꽣蹂��닔瑜� true 濡� 蹂�寃�
			if(e.getKeyCode()==KeyEvent.VK_DOWN) { // �궎瑜� �엯�젰諛쏆븘�꽌 洹� �궎媛� �삤瑜몄そ �궎�씪硫�
				nameY+=50;
				// 吏��젙�맂 �쐞移섎�� 踰쀬뼱�굹硫� �떎�떆 留� �쐞濡� //
				if (nameY > 350)
					nameY = 250;
			}
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				nameY-=50;
				// 吏��젙�맂 �쐞移섎�� 踰쀬뼱�굹硫� �떎�떆 留� �븘�옒濡� //
				if (nameY < 250)
					nameY = 350;
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT) {
				nameX-=50;
				// 吏��젙�맂 �쐞移섎�� 踰쀬뼱�굹硫� 留� �삤瑜몄そ�쑝濡� //
				if (nameX < 20)
					nameX = 420;
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
				nameX+=50;
				// 吏��젙�맂 �쐞移섎�� 踰쀬뼱�굹硫� 留� �쇊履쎌쑝濡� //
				if (nameX / 50 == 9)
					nameX = 20;
			}
		}
		else {
			if(e.getKeyCode()==KeyEvent.VK_ENTER) // �뿏�꽣媛� �엯�젰�릺硫�
				isEnter = true; // �뿏�꽣蹂��닔瑜� true 濡� 蹂�寃�
			if(e.getKeyCode()==KeyEvent.VK_DOWN) { // �궎瑜� �엯�젰諛쏆븘�꽌 洹� �궎媛� �삤瑜몄そ �궎�씪硫�
				seedY+=30;
				if (seedY > 250)
					seedY = 100;
			}
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				seedY-=30;
				if (seedY < 100)
					seedY = 250;
			}
		}

	}
	public void keyReleased(KeyEvent e) { // �궎蹂대뱶瑜� �닃���떎媛� �뼹硫�
		// �쁽�옱 ��吏곸씠怨� �엳�뒗 �뙥留⑥쓣 硫덉텛寃� �븿 //
		if(itemSTATE == ITEM_DEVIL) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				pacman.left = false;
			if(e.getKeyCode()==KeyEvent.VK_LEFT)
				pacman.right=false;
			if(e.getKeyCode()==KeyEvent.VK_UP)
				pacman.down=false;
			if(e.getKeyCode()==KeyEvent.VK_DOWN)
				pacman.up=false;
		}
		else if(STATE == GAME) {
			if(e.getKeyCode()==KeyEvent.VK_RIGHT)
				pacman.right=false;
			if(e.getKeyCode()==KeyEvent.VK_LEFT)
				pacman.left=false;
			if(e.getKeyCode()==KeyEvent.VK_UP)
				pacman.up=false;
			if(e.getKeyCode()==KeyEvent.VK_DOWN)
				pacman.down=false;
		}

		}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
