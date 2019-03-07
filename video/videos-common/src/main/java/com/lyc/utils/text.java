package com.lyc.utils;


import java.util.*;

public class text{

    public static void main(String[] args){
        String input;
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()) {
           input=scanner.nextLine();
           String[] s1=input.split(" ");
           String[] s2=s1[0].split(",");
           Integer m=Integer.valueOf(s2[0])+1;
           Integer n=Integer.valueOf(s2[1])+1;
           int[][] map=new int[m][n];
           s2=s1[1].split(",");
           Integer start1=Integer.valueOf(s2[0]);
           Integer start2=Integer.valueOf(s2[1]);
           local start=new local(start1,start2);
           s2=s1[2].split(",");
           Integer stop1=Integer.valueOf(s2[0]);
           Integer stop2=Integer.valueOf(s2[1]);
           local stop=new local(stop1,stop2);
           int num=Integer.valueOf(s1[3]);
           for(int i=0;i<num;i++){
               s2=s1[4+i].split(",");
               Integer f1=Integer.valueOf(s2[0]);
               Integer f2=Integer.valueOf(s2[1]);
               map[f1][f2]=1;
           }
           int[][] visit=new int[m][n];
           visit[0][0]=1;
           int[][] choose={{0,1},{1,0},{0,-1},{-1,0}};
            Queue<local> queue=new LinkedList<local>();
            ArrayList<local> arrayList=new ArrayList<local>();
            queue.offer(start);
            while(!queue.isEmpty()) {
                local now = queue.remove();
                arrayList.add(now);
                for (int i = 0; i < 4; i++) {
                    int x=now.a+choose[i][0];
                    int y=now.b+choose[i][1];
                    if(x>=0&&x<m&&y>=0&&y<n&&map[x][y]==0&&visit[x][y]==0){
                        local next=new local(x,y);
                        queue.offer(next);
                        visit[next.a][next.b] = 1;
                        next.front[0] = now.a;
                        next.front[1] = now.b;
                    }
                }
            }
                Stack<Integer> stack=new Stack<Integer>();
                int  front1=arrayList.get(arrayList.size()-1).front[0];
                int  front2=arrayList.get(arrayList.size()-1).front[1];
                stack.push(arrayList.size()-1);
                while (true){
                    if(front1==0&&front2==0) break;
                    for(int i=0;i<arrayList.size();i++){
                        if(arrayList.get(i).a==front1&&arrayList.get(i).b==front2){
                            front1=arrayList.get(i).front[0];
                            front2=arrayList.get(i).front[1];
                            stack.push(i);
                            break;
                        }

                    }

                }
                System.out.print("[0,0]");
                while (!stack.isEmpty()){
                    System.out.print("["+arrayList.get(stack.peek()).a+","+arrayList.get(stack.peek()).b+"]");
                    stack.pop();
                }
               }
           }
        }
     class local{
        public int a;
        public int b;
        public int[] next;
        public int[] front=new int[2];

        public local(int a,int b) {
            this.a=a;
            this.b=b;
        }

        public void  setterA(int a){
            this.a=a;
        }
        public void  setterB(int b){
            this.b=b;
        }
        public void  setterNext(int[] next){
            this.next=next;
        }
        public int getterA(){
            return this.a;
        }
        public int getterB(){
            return this.b;
        }
        public int[] getterNext(){
            return this.next;
        }

    }


