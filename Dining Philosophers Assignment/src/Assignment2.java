public class Assignment2 {

    public static void main(String[] args) {
	DiningPhilosophers monitor = new DiningPhilosophers();

        for (int i = 0; i < 5; i++) {
            Philosopher philosopher = new Philosopher(i, monitor);
            Thread phil = new Thread(philosopher);
            phil.start();
        }
    }
}
