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
import java.util.Comparator;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Rank {
   String path;
   String name;
   File rankboard;
   Score score;
   public int y = 400;
   private ArrayList<Integer> scores = new ArrayList();
   private Map<String,Integer> map;

   Rank(String path, Score score){
      this.path=path;
      this.score = score;
      scores = score.getList();
      map = new HashMap<String, Integer>();
      rankboard = new File(path);
      try { // �씠�쟾 �젏�닔 紐⑤몢 scores�씪�뒗 ArrayList�뿉 ���옣
         Scanner scan = new Scanner(rankboard);
         while(scan.hasNext()) {
            String temp1 = scan.next();
            int temp2 = scan.nextInt();
            map.put(temp1, temp2);
         }
         scan.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void insertRank() {
      map.put(name,score.score);
      Iterator it = Rank.sortByValue(map).iterator();
      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(path));
         PrintWriter pw = new PrintWriter(bw);
         while(it.hasNext()) {
            String temp = (String) it.next();
            pw.printf("%s %d\n", temp, map.get(temp));
         }
         pw.close();
         bw.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void showRank(Graphics g) {
      //臾댁“嫄� 4�벑源뚯� �굹�삤怨� �쑀���벑�닔�뒗 �몴�떆媛� �븞�맂�떎. 愿쒖갖��吏�??
         Iterator<String> it = Rank.sortByValue(map).iterator();
         int n = 0;
         while(it.hasNext() && n<5) {
            String temp = it.next();
            g.setColor(Color.white);
            if(name.equals(temp)) {
               g.setColor(Color.yellow);
               g.drawString(String.valueOf(n+1) + ". " + temp + "(" + String.valueOf(map.get(temp)) + ")" + "    <-- You    *(^3^)*", 550, y+=50);
            }
            else
               g.drawString(String.valueOf(n+1) + ". " + temp + "(" + String.valueOf(map.get(temp)) + ")", 550, y+=50);
            n++;
         }
         g.setColor(Color.white);
         g.setFont(new Font(Font.DIALOG, Font.BOLD, 50));
         g.drawString(name + "'s SCORE : " + String.valueOf(score.score), 200, 220);
         g.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
         g.drawString("RANK #" + String.valueOf(score.getRank()), 200, 260);
   }

   public static List sortByValue(final Map map){
      List<String> list = new ArrayList();
       list.addAll(map.keySet());
       Collections.sort(list, new Comparator(){

          public int compare(Object o1,Object o2){
                   Object v1 = map.get(o1);
                   Object v2 = map.get(o2);
                   return ((Comparable) v1).compareTo(v2);
           }
       });
       Collections.reverse(list); // 二쇱꽍�떆 �삤由꾩감�닚
       return list;
   }
}
