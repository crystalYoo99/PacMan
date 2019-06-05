import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

import javax.imageio.ImageIO;

// Map : 씨앗, 팩맨, 유령, 벽의 위치 등을 관리 (유령, 씨앗, 팩맨 생성)
// map.png : 팩맨과 유령, 벽의 생성위치를 그려놓은 사진
public class Map {
	public int width; // 40
	public int height; //25

	public Tile[][] tiles;

	public List<Seed> seeds; // 씨앗 리스트
	public List<Ghost> Ghosts; // 고스트 리스트
	public List<SmartGhost> SmartGhosts; // 스마트 고스트 리스트
	public List<Box> box;

	private int location=30;

	public Map(String path) { // map(png)이 저장되어 있는 경로를 생성자로 받음
		try {
			//씨앗, 고스트, 스마트 고스트들의 새로운 배열 만들기
			seeds=new ArrayList<>();
			Ghosts=new ArrayList<>();
			SmartGhosts=new ArrayList<>();
			box = new ArrayList<>();
			ArrayList<Integer> boxi = new ArrayList<>();
			ArrayList<Integer> boxj = new ArrayList<>();

			// 이미지 객체 생성(Map이미지를 불러오기 위한 과정)
			// ImageIO를 이용한 이미지 로딩
			//* getClass() : 프로그램에서 Class 객체를 얻기 위해서 Object 클래스의 getClass()메소드 이용
			//* getResource(path) : 이미지 파일의 경로를 가져옴
			BufferedImage map = ImageIO.read(getClass().getResource(path));

			this.width=map.getWidth();
			this.height=map.getHeight();

			int[] pixels = new int[width*height]; // 포토샵으로 그려놓은 맵
			// pixels 배열에 map.png의 픽셀의 색을 일렬화(세로방향으로) 해서 저장함
			// *getRGB(startX,startY,width,height,rgb,offset,scansize)
			map.getRGB(0, 0, width, height, pixels, 0, width);

			tiles =new Tile[width][height]; // GUI에 구현될 맵

			for(int i=0;i<width;i++) {
				for(int j=0;j<height;j++) {
					//pixels의 값과 색을 비교해서 벽, 팩맨, 일반유령, 똑똑한 유령, 씨앗 위치 지정하기.
					int val = pixels[i+(j*width)];
					if(val==0xFF0000FF) {
						// 벽( 0xFF000A7C: 파란색)
						// tiles에 벽 깔기
						tiles[i][j]=new Tile(i*32+location, j*32+location);
					}
					else if(val==0xFFFFFF00) {
						// 팩맨( 0xFFFFD800: 노란색)
						Game.pacman.x=i*32+location;
						Game.pacman.y=j*32+location;
					}
					else if(val==0xFFFF0000) {
						// 일반 유령( 0xFFFF0000: 빨간색)
						Ghosts.add(new Ghost(i*32+location, j*32+location));
					}
					else if(val==0xFF00FFFF) {
						// 똑똑한 유령 ( 0xFF00FFFF: 민트색)
						SmartGhosts.add(new SmartGhost(i*32+location, j*32+location));
					}

					else{
						// 씨앗
						seeds.add(new Seed(i*32+location, j*32+location));
					}
					boxi.add(i);
					boxj.add(j);
				}
			}
			for(int i=0;i<4;i++){
				Collections.shuffle(boxi);
				Collections.shuffle(boxj);

				int boxx = boxi.get(0);
				int boxy = boxj.get(0);
				if(pixels[boxx+(boxy*width)]==0xFF0000FF){
					i--;
					continue;
				}
				else box.add(new Box(boxx*32+location, boxy*32+location));
			}
		} catch (IOException e) {
			e.printStackTrace(); // 에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
		}
	}

	public void tick() { // map에서 유령들의 상태를 바꾸고 랜더링 시킴
		for(int i=0;i<Ghosts.size();i++) {
			Ghosts.get(i).tick(); // Ghost 클래스 안에 tick()메소드 있음.
		}
		for(int i=0;i<SmartGhosts.size();i++) {
			SmartGhosts.get(i).tick();
		}
	}

	public void render(Graphics g) { // seeds와 tile은 상태가 바뀔 필요가 없으므로 tick함수가 존재하지 않음
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				if(tiles[i][j]!=null)
					tiles[i][j].render(g); // Tile 클래스에 render(Graphics g)메소드 있음.
			}
		}

		for(int i=0;i<seeds.size();i++) {
			seeds.get(i).render(g);
		}

		for(int i=0;i<Ghosts.size();i++) {
			Ghosts.get(i).render(g);
		}
		for(int i=0;i<SmartGhosts.size();i++) {
			SmartGhosts.get(i).render(g);
		}

		for(int i=0;i<box.size();i++) {
			box.get(i).render(g);
		}
	}
}
