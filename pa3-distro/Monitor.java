/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	// Task 2: Philosopher states 
	private static final int THINKING = 0;
	private static final int HUNGRY   = 1;
	private static final int EATING   = 2;
	private static final int TALKING  = 3;

	private int[] state;
	private int   numberOfPhilosophers;

	// Task 2: Boolean so systeme knows if anyone's tlaking already 
	private boolean someoneTalking = false;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// Task 2: initialise one state slot per philosopher; all start THINKING
		numberOfPhilosophers = piNumberOfPhilosophers;
		state = new int[numberOfPhilosophers];
		for (int i = 0; i < numberOfPhilosophers; i++)
			state[i] = THINKING;
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Task 2: Internal helper. Lets philosopher piTID eat if both neighbours are not eating and the philosopher itself is HUNGRY.
	 */
	private void test(final int piTID)
	{
		int i = piTID - 1;   //  start index 0
		int left  = (i - 1 + numberOfPhilosophers) % numberOfPhilosophers;
		int right = (i + 1) % numberOfPhilosophers;

		if (state[left] != EATING && state[i] == HUNGRY && state[right] != EATING)
		{
			
			state[i] = EATING;
			notifyAll();
		}
	}

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// Task 2: mark this philosopher as hungry and test whether it can eat
		int i = piTID - 1;  //  start index 0
		state[i] = HUNGRY;
		test(piTID);

		// Task 2: if we still cannot eat, block until notified and re-test
		while (state[i] != EATING && state[i] != TALKING)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				System.err.println("Monitor.pickUp():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// Task 2: philosopher is done eating; check if either neighbour can now eat
		int i     = piTID - 1;
		int left  = (i - 1 + numberOfPhilosophers) % numberOfPhilosophers;
		int right = (i + 1) % numberOfPhilosophers;

		state[i] = THINKING;
		test(left  + 1);   // convert back to 1-based TID for test()
		test(right + 1);
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		// Task 2: wait until no other philosopher is talking
		while (someoneTalking)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				System.err.println("Monitor.requestTalk():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}
		int i = piTID - 1;
		state[i] = TALKING;
		someoneTalking = true;
		notifyAll();
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		// Task 2: release the talking slot and wake waiting philosophers
		int i = piTID - 1;	
		state[i] = THINKING;
		someoneTalking = false;
		notifyAll();
	}
}

// EOF
