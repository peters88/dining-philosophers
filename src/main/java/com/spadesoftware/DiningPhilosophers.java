package com.spadesoftware;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    private final Random rnd = new Random();
    private ReentrantLock[] forks = new ReentrantLock[5];

    public DiningPhilosophers() {
        for(int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {

        ReentrantLock rightFork = forks[philosopher];
        ReentrantLock leftFork = forks[(philosopher + 1) % 5];

        boolean lf = false, rf = false;

        do {
            rf = rightFork.tryLock(rnd.nextInt(100)+50, TimeUnit.MILLISECONDS);
            lf = leftFork.tryLock(rnd.nextInt(100)+50, TimeUnit.MILLISECONDS);
            if(rf && !lf) {
                rf = false;
                rightFork.unlock();
            }
        } while(!(lf && rf));

        try{
            pickRightFork.run();
            pickLeftFork.run();
            eat.run();

            putLeftFork.run();
            putRightFork.run();
        } finally {
            rightFork.unlock();
            leftFork.unlock();
        }

    }



    /*public DiningPhilosophers() {
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {

    }*/
}
