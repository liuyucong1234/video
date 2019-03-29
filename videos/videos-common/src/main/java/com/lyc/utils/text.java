package com.lyc.utils;


import java.util.*;

public class text {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int[][] b=new int[a][1000];
        int[] max=new int[a];
        int[] mmax=new int[a];
        for(int i=0;i<a;i++){
            b[i][0]=scanner.nextInt();
            for(int j=1;j<=b[i][0];j++){
                b[i][j]=scanner.nextInt();
                if(max[i]<b[i][j]){
                    max[i]=b[i][j];
                    mmax[i]=j;
                }
            }
        }
        int d;
        int sum=0;
        for(int i=0;i<a;i++){
            d=max[i];
            for(int j=mmax[i];j>3;j--) {
                sum+=d;
                if(b[i][j]>b[i][j-1]&&b[i][j-1]>b[i][j-2])
                {
                    d--;
                    sum=sum+d;
                }
                else if(b[i][j]>b[i][j-1]&&b[i][j-1]<=b[i][j-2]){
                    d=1;
                    sum=d+sum;
                }
                else if(b[i][j]<b[i][j-1])
                {
                    d++;
                    sum=sum+d;
                }
                else
                    sum=sum+d;
            }
            if(b[i][2]>b[i][1])
                sum++;
            else if(b[i][2]==b[i][1])
                sum=sum+d;
            else
                sum=(d+1+sum);
            d=max[i];
            int N=b[i][0];
            for(int j=mmax[i];j<N-2;i++)
            {
                if(b[i][j]>b[i][j+1]&&b[i][j+1]>b[i][j+2])
                {
                    d--;
                    sum=d+sum;
                }
                else if(b[i][j]>b[i][j+1]&&b[i][j+1]<=b[i][j+2]){
                    d=1;
                    sum=d+sum;
                }
                else if(b[i][j]<b[i][j+1])
                {
                    d++;
                    sum=d+sum;
                }
                else
                    sum=d+sum;
            }
            if(b[i][N-2]>b[i][N-1])
                sum++;
            else if(b[i][N-2]==b[i][N-1])
                sum=sum+d;
            else
                sum=(d+1+sum);
            System.out.println(sum);
        }

    }
}
