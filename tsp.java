import edu.princeton.cs.algs4.StdDraw;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class TSP {
    public static int N;
    public static int[] list;
    public static double[] x;
    public static double[] y;
    public static double[][] dist;
    public static double[][] mc;            // this is the minimum cost path from point 1 through set S (ending at point j);
    public static ArrayList<Integer> subProblemsOfNumber (int bits) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        for (int i = 0; i < bits; i++)
            list[i] = 1;
        relax();
        array.add(output());
        int cur = 0;// this is a pointer
        while (cur < list.length - 1) 
        {
            if (list[cur] == 1 && list[cur + 1] == 0)
            {
                exch(cur, cur + 1);
                reshuffle(0, cur - 1);
                relax();
                array.add(output());
                cur = 0;
            } else 
                cur++;
        }
        return array;
    }
    
    public static void relax() {
//        System.out.println("relax --->" );
        int child = output();
        ArrayList<Integer> bitsOfChild = new ArrayList<Integer>();         // position of 1s in child;
        for (int i = 0; i <= N - 2; i++)
            if (list[i] == 1)
                bitsOfChild.add(i);
        
//        for (int i : bitsOfChild)
//            System.out.println("bits : " + i);
        for (int i = 0; i <= N - 2; i++) 
        {
            if (list[i] == 1)
                continue;
            list[i] = 1;
            int parent = output();

            for (int j : bitsOfChild)         //
            {
//                System.out.println("child: " + child + "parent" + parent + " i: " + i + " j : " + j);
//                System.out.println("old value: " + mc[i][parent]);
//                System.out.println("compete: " + (mc[j][child] + dist[i + 1][j + 1]));
                if (mc[j][child] + dist[i + 1][j + 1] < mc[i][parent])
                {
//                    System.out.println("hha ------------------------ ");
                    mc[i][parent] = mc[j][child] + dist[i + 1][j + 1];
                }
            }
            list[i] = 0;
        }
    }
    public static void reshuffle(int lo, int hi)   // move all the 1s between lo and hi(inclusive)
    {
        if (hi <= lo)
            return;
        int i = lo;
        int j = hi;

        while (j >= i)
        {
            if (list[j] == 1 && list[i] == 0)
            {
                exch(i, j);
            }            
            while (i <= hi && list[i] == 1)    // find some place where list[i] == 0;
                i++;    
            while (j >= lo && list[j] == 0)
                j--;
        }
    }
    public static void exch(int i, int j)
    {
        int tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }
    public static int output () {
        int o = 0;
        for (int i = 0; i < list.length; i++)
            o = (o << 1) + list[i];
        return o;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        /*
        FileWriter writer = new FileWriter("data8.txt");
        Scanner s1 = new Scanner(new File("tsp.txt"));
        Scanner s2 = new Scanner(new File("num8.txt"));
        int N = s1.nextInt();
        double[] x = new double[N];
        double[] y = new double[N];
        
        for (int i = 0; i < N; i++)
        {
            x[i] = s1.nextDouble();
            y[i] = s1.nextDouble();
        }
        list = new int[24];
        int[] tag;
        while (s2.hasNext())
        {
            tag = new int[24];
            for (int i = 0; i < tag.length; i++)
            {
                tag[i] = s2.nextInt();
                writer.write(tag[i] + " ");
            }
            writer.write("\n");
            writer.write(22 + "\n");
            writer.write(x[0] + " " + y[0] + " " + "\n");
            for (int i = 0; i < tag.length; i++)
            {
                if (tag[i] == 1)
                    writer.write(x[i + 1] + " " + y[i + 1] + "\n");
            }            

        }

        writer.close();
        */
        /*
        FileWriter writer = new FileWriter("test.txt");
        double a = Double.POSITIVE_INFINITY;
        writer.write(a + " ");
        Scanner scanner = new Scanner(new File("test.txt"));
        double b = scanner.nextDouble();
        System.out.println(b);
        writer.close();
        */
    
        Scanner scanner = new Scanner(new File("data3.txt"));
        FileWriter writer = new FileWriter(new File("3-22_subset.txt"));
        for (int times = 0; times < 253; times++)
        {
            for (int i = 0; i < 24; i++)
                writer.write(scanner.nextInt() + " ");
            writer.write("\n");
        N = scanner.nextInt();
        System.out.println(" N = " + N );          // test;
        x = new double[N];
        y = new double[N];
        dist = new double[N][N];
//        StdDraw.setXscale(20000, 30000);
//        StdDraw.setYscale(9000, 20000);
//        StdDraw.setPenRadius(0.01);
//        StdDraw.setPenColor(StdDraw.BLUE);
        for (int i = 0; i < N; i++)
        {
            x[i] = scanner.nextDouble();
            y[i] = scanner.nextDouble();
        }
        
//        System.out.println("distance : ");
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                dist[i][j] = Math.sqrt(Math.pow((x[i] - x[j]), 2) + Math.pow(y[i] - y[j], 2));
//                System.out.print(dist[i][j] + " ");
            }
//            System.out.print("\n");
        }
        
        int c = (1 << (N - 1));
        ArrayList<Integer> array;
//        System.out.println("c = " + c);
        mc = new double[N - 1][c];
        for (int i = 0; i < mc.length; i++)
            for (int j = 0; j < mc[0].length; j++)
                mc[i][j] = Double.POSITIVE_INFINITY;           // initialize all the 
        
        list = new int[N - 1];
        for (int i = 1; i <= N - 1; i++)
            mc[i - 1][1 << (N - 1 - i)] = dist[0][i];            // initialize those with one vertex from S' (which is whole set - {Node 1});

//        for (int i = 0; i <= N - 2; i++)
//        {
//            for (int j = 0; j < (int)Math.pow(2, N - 1); j++)
//                System.out.print(mc[i][j] + " ");
//            System.out.print("\n");
//        }
       
        for (int i = 1; i <= N - 2; i++)          // number of subproblems in (N - 1, 0), (N - 1, 1), ... (N - 1, N - 1).
        {
            list = new int[N - 1];             // initialize array list[];
            array = subProblemsOfNumber(i);
//            int size = array.size();
//            System.out.println("size of array : " + size);
//            count += size;
        }
//        double min = Double.POSITIVE_INFINITY;
        int last = (1 << (N - 1)) - 1;
        for (int i = 0; i <= N - 2; i++)
            writer.write(mc[i][last] + "\n");
//        for (int i = 0; i <= N - 2; i++)
//        {
////            System.out.println(" i : " + i + " Last: " + last + " : " + mc[i][last]);
//            min = Math.min(min, mc[i][last] + dist[0][i + 1]);
//        }
//        System.out.println(" N = " + N);

//        for (int i = 0; i <= N - 2; i++)
//        {
//            for (int j = 0; j < (int)Math.pow(2, N - 1); j++)
//                System.out.print(i + " / " + j + " : " + mc[i][j] + " ");
//            System.out.print("\n");
//        }

//        System.out.println("minimum cost: " + min);
//        System.out.println("total length: " + count);

        }
        writer.close();
    }
    
}
