import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static int LIMIT = 10000000;
    private static int NUM_THREADS = 1;
    private static List<Integer> primes = new ArrayList<Integer>();

    public static void main(String[] args) {
        List<MyThread> threads = new ArrayList<>();
        long startTime, endTime; //Timer

        Scanner scanner = new Scanner(System.in);
        // Ask the user for the upper limit
        System.out.print("Enter the upper limit: ");
        LIMIT = scanner.nextInt();

        // Ask the user for the number of threads
        System.out.print("Enter the number of threads (default is 1): ");
        NUM_THREADS = scanner.nextInt();
        scanner.close();
        System.out.print("Code Running... ");


        //THREADS STARTS
        startTime = System.currentTimeMillis();
        
        int segmentSize = LIMIT / NUM_THREADS;
        int start = 2;
        int end = segmentSize;

        for (int i = 0; i < NUM_THREADS; i++) {
            if (i == NUM_THREADS - 1) {
                // Last thread takes care of remaining numbers
                end = LIMIT;
            }

            MyThread thread = new MyThread(i, start, end);
            thread.start();
            threads.add(thread);

            start = end + 1;
            end += segmentSize;
        }

        for (MyThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        endTime = System.currentTimeMillis();
        //THREADS END

        System.out.printf("%d primes were found.\n", primes.size());
        for (int i = 0; i < primes.size(); i++) {
            System.out.printf("%d,", primes.get(i));
        }

        System.out.println("\nTotal time taken: " + (endTime - startTime) + " milliseconds");
    }

    public static boolean check_prime(int n) {
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    //THREAD
    static class MyThread extends Thread {
        private int threadNum;
        private int start;
        private int end;


        public MyThread(int threadNum, int start, int end) {
            this.threadNum = threadNum;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {

                //System.out.println("Thread: " + threadNum + " is running ---> " + i);
                if (check_prime(i)) {
                    synchronized (primes) { // Mutual exclusion for accessing primes list
                        primes.add(i);
                    }
                }
            }
        }
    }




    
}