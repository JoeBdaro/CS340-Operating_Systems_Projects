import java.util.concurrent.locks.*;

public class DiningPhilosophers implements DiningServer {

    public enum State {THINKING, HUNGRY, EATING}


    public State[] states = new State[5];
    public Condition[] self = new Condition[5];
    public Lock lock = new ReentrantLock();


    public DiningPhilosophers() {
        for (int i = 0; i < 5; i++) {
            states[i] = State.THINKING;
            System.out.println("Philosopher " + i + " is Thinking");
            self[i] = lock.newCondition();
        }
    }

    @Override
    public void takeChopsticks(int i) {
        lock.lock();
        states[i] = State.HUNGRY;
        System.out.println("Philosopher " + i + " is hungry");
        test(i);
        if (states[i] != State.EATING) {
            try {

                self[i].await();

            } catch (InterruptedException e) {
                System.out.println("philosopher " + i + " is waiting for chopsticks to become available");
            }
        }
        lock.unlock();
    }

    @Override
    public void returnChopsticks(int i) {
        lock.lock();
        System.out.println("Philosopher " + i + " has released it's left and right chopsticks");
        states[i] = State.THINKING;
        System.out.println("philosopher " + i + " is now Thinking");
        test((i + 4) % 5);
        test((i + 1) % 5);
        lock.unlock();
    }

    private void test(int i) {

        if ((states[(i + 4) % 5] != State.EATING) && (states[i] == State.HUNGRY) && (states[(i + 1) % 5] != State.EATING)) {
            System.out.println("Philosopher " + i + " acquired it's left and right chopsticks");
            states[i] = State.EATING;
            System.out.println("Philosopher " + i + " is eating");
            self[i].signal();
        }
    }
}
