import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	// There's a 67% chance that a philosopher does NOT want to think 
	// - 1 since it's 0-99, not 1-100
	private final int ODDS_YOU_A_YAPPER = 100 - 67 - 1; 


	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			// Following instructions above. Added some personality to the philosopherizerinators.
			System.out.printf("Philosopher %d has just started absolutely ravaging her plate!" + "\n", getTID());
			this.randomYield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
			System.out.printf("Philosopher %d left her plate absolutely spotless. Wow." + "\n", getTID());		
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			System.out.printf("Philosopher %d has begun to expand their mind towards enlightenment" + "\n", getTID());
			this.randomYield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			this.randomYield();
			System.out.printf("Philosopher %d has given up on thinking for the moment. It's probably just a fad anyways." + "\n", getTID());
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		// Implementing the art of yapping. No need for try-catch because
		// yapping is a tried and tested art form.
		System.out.printf("Philosopher %d is yapping!!. Listen up please." + "\n", getTID());
		this.randomYield();
		saySomething();
		this.randomYield();
		System.out.printf("Philosopher %d has begun to spout nonsense propaganda. We're cutting off his mic." + "\n", getTID());
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if(((int) Math.random() * 100) <= ODDS_YOU_A_YAPPER)
			{
				DiningPhilosophers.soMonitor.requestTalk(getTID());
				talk();
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}
			this.randomYield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 * 
	 * I very much feel free to add my own phrases. As a matter of fact, I think I'll ONLY use my own phrases, thank you very much.
	 */
	public void saySomething()
	{
		String[] superCoolAndInsightfulPhrases =
		{
			"DEAR GOD, WE WERE WRONG. IT'S 43! IT MUST BE!",
			"Did you know that 30% of deaths worldwide are caused by dying? Together we may have the power to change that number.",
			"Where was I when club pengiun was kill? I was at house eating dorito when phone ring... \"Club penguin is kil\". \"no\"",
			"So uhhh. Really nice weather we're having today, eh?",
			"You know, there's an art to flying. Or rather, a knack. All you need to do is throw yourself at the ground... and miss",
			"PLEASE GOD WOULD ONE OF THESE DAMNED CARNIVOROUS SAVAGES BESIDE ME RELEASE THEIR CHOPSTICKS! PLEASE! I HAVEN'T EATEN FOR DAYS!",
			"No!! I'm with the science team! What are you DOING Gordon?",
			"I don't care WHO the professor sends. I'm NOT doing my theory assignments!",
			"I really think I'd have better odds at a fulfilling career as a soundcloud rapper. I mean, just think about it. That's what we do right? We da thinkers fr fr. So yeah I just released my latest single yesterday and I would really appreciate it if you all gave it a listen. Actually, why don't we just listen to it right now! Oh, the speaker's aren't working? That's ok! I can do it accapella.",
			"I really think that the reason Stevie Wonder struggled to concretize his sound in his earlier career was due to the constraints imposed on him by his record label. I mean, they were really trying to capitalize from the profitability of hit singles in the music industry at the time, but quickly slapping together second-rate songs to pad out an entire album and using that to repackage one popular song really isn't a sustainable practice. It's no Wonder Stevie had so much leverage with his contract when he turned 21. For years that label had been the one thing keeping him from truly fleshing out a complete album, where he could experiment with his sound on his own terms."
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			superCoolAndInsightfulPhrases[(int)(Math.random() * superCoolAndInsightfulPhrases.length)]
		);
	}
}

// EOF
