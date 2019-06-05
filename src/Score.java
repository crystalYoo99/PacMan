import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Score {

   String path;
   File scoreboard;
   private ArrayList<Integer> scores;
   public int score;
   private int temp;
   public int total;
   public int rank;

   Score(String path){
      this.path=path;
      scores = new ArrayList<>();
      score=0;
      scoreboard = new File(path);
      try { // �씠�쟾 �젏�닔 紐⑤몢 scores�씪�뒗 ArrayList�뿉 ���옣
         Scanner scan = new Scanner(scoreboard);
         while(scan.hasNextInt()) {
            scores.add(scan.nextInt());
         }
         scan.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void insertScore() {
      scores.add(this.score); // player媛� �깉濡� 諛쏆� �젏�닔瑜� 異붽��븯�뿬 �궡由쇱감�닚 �젙�젹
      Collections.sort(scores);
      total = scores.size();
      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(path));
         PrintWriter pw = new PrintWriter(bw);
         for(int i=total-1;i>=0;i--) {
            temp=scores.get(i);
            pw.printf("%d\n", temp);
            if(score==temp)
               rank=total-i;
         }
         pw.close();
         bw.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void render(Graphics g) {
     Pacman.tt/=1000;
     String sec = String.format("%.2f",Pacman.tt);
     g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
      if(Game.STATE==Game.GAME) {
        //<<<<<sujeong change>>>>>
    		if(Game.itemSTATE == Game.ITEM_KING) {
    			g.setColor(Color.YELLOW);
    			g.drawString("ITEM : KING("+sec+")", 80, 155);
    		}
    		else if(Game.itemSTATE == Game.ITEM_BLACK) {
    			g.setColor(Color.WHITE);
    			g.drawString("ITEM : BLACK("+sec+")", 80, 155);
    		}
    		else if(Game.itemSTATE == Game.ITEM_DEVIL) {
    			g.setColor(Color.RED);
    			g.drawString("ITEM : DEVIL("+sec+")", 80, 155);
    		}
    		else if(Game.itemSTATE == Game.ITEM_STERN) {
    			g.setColor(Color.CYAN);
    			g.drawString("ITEM : STERN("+sec+")", 80, 155);
    		}
    		//sujeogn finish
         g.setColor(Color.BLACK);
         g.fillRect(30, 860, 150, 70);
         g.setColor(Color.WHITE);
         g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
         g.drawString(String.valueOf(score), 190, 60);

         int size = scores.size();
         if(scores.get(0) > scores.get(size-1)) {
            if(getRank() == 1)   g.drawString("New Record !!", 80, 90);
            else    g.drawString("NEXT GOAL : " + String.valueOf(getRank()-1) + "(" + String.valueOf(scores.get(getRank()-2)) + ")", 80, 90);
            g.drawString("YOUR RANK : " + String.valueOf(getRank()), 80, 120);
         }
         else {
            if(getRank() == 1)   g.drawString("New Record !!", 80, 90);
            else    g.drawString("NEXT GOAL : " + String.valueOf(getRank()-1) + "(" + String.valueOf(scores.get(size-getRank()+1)) + ")", 80, 90);
            g.drawString("YOUR RANK : " + String.valueOf(getRank()), 80, 120);
         }
      }
   }

   public int getRank() {
      int i;
      int size = scores.size();
      if(scores.get(0) > scores.get(size-1)) {
         for(i=0; i<size; i++)
            if(score > scores.get(i)) break;
         return i+1;
      }
      else {
         for(i=0; i<size; i++)
            if(score < scores.get(i)) break;
         return size-i+1;
      }
   }

   public ArrayList<Integer> getList(){
      return scores;
   }
}
