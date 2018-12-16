import java.util.Random;

public class Philosopher implements Runnable {

    int phillisopherNum;
    DiningPhilosophers diningPhilosophers;

    public Philosopher(int phillisopherNum, DiningPhilosophers diningPhilisophers) {
        this.phillisopherNum = phillisopherNum;
        this.diningPhilosophers = diningPhilisophers;
    }

    public void threadSleep() {
        Random random = new Random();
        int min = 2000;
        int max = 5000;
        int time = random.nextInt(max - min) + min + max;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void run() {
        while (true) {
            threadSleep();
            this.diningPhilosophers.takeChopsticks(phillisopherNum);
            threadSleep();
            this.diningPhilosophers.returnChopsticks(phillisopherNum);
            threadSleep();
        }
    }

}
