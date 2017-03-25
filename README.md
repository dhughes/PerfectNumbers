# PerfectNumbers

This project contains some experiments with calculating [perfect numbers](https://en.wikipedia.org/wiki/Perfect_number). A perfect number is one where the sum of its divisors (excluding itself) add up to the number. For example, 28 is a perfect number because it's divisors are:

* 1
* 2
* 4
* 7
* 14

If you add these up you get 28, the original number. However, consider 16. It's divisors are:

* 1
* 2
* 4
* 4
* 8

If you add these up you get 19. Therefore, 16 is not perfect.

This calculation was to be done via a static method that accepted `num`. The method returned a boolean indicating if the number provided was perfect or not. 

My first attempt was naive. I stared by using a for loop to loop backwards from the number-1 to 1. I tested each step to determine if the number was divisible by the step. If so, I collected a tuple into a `HashSet<Tupple>`. I then iterated over the set and summed the values in the tupple + 1. It worked, but was considerably inefficient.

It was pointed out to me that, because I was looping backwards, I was doing two times the amount of work as needed. If I looped forward to _half_ the number being tested I could simply create a sum of the valid divisors. Understanding this, I decided to try a few other approaches... 

I wanted to see if I could make a one-line solution to this puzzle. In particular, I wanted to experiment with streams of numbers in Java. My first attempt used a stream of integers, reduced this to those which were valid divisors of the number being tested and summed them. If this was equal to the number, the validation passed. If not, it didn't. This is the code:

```java
public static boolean reduceIsPerfect(long num) {
	return LongStream.rangeClosed(2, num / 2)
			.reduce(1, (sum, test) -> num % test == 0 ? sum + test : sum) == num;
}
```

Mission accomplished! It's one (wrapped) line of code. However, this led me to wonder if I could parallelize this. So, my next test was to make a parallel stream of numbers that I then filtered to those that cleanly divided the number being tested. I then summed these, checked for equality to the original number, and returned that.

```java
public static boolean parallelFilteredIsPerfect(long num) {
	return LongStream.rangeClosed(1, num / 2)
			// make this a parallel stream so we can find the whole divisors more quickly (ideally)
			.parallel()
			// filter out any non-whole divisors
			.filter(test -> num % test == 0)
			.sum() == num;
}
```

This too worked, though arguably it's more than one line of code. 

This success made me wonder how much (if at all) more efficient this method was than my first attempt. I wrote tests for the first six perfect numbers and found that the sixth one, 8,589,869,056, took quite a while to process. My first attempt with `reduceIsPerfect()` took about 45 secs to validate. My second attempt, `parallelFilteredIsPerfect()` took 23 seconds, so it was about 2x faster. But, this left a bad taste in my mouth. 23 seconds is a _really_ long time.

I tried a few other approaches, but found that, in general, most of my attempts tested in the 45 to 50 second range. Disappointing.

So, next, I did what any good programmer would do, I turned to Google. I reasoned that the part of the process that takes the most time is determining if a number is a divisor or not. Google led me to a more efficient algorithm. 

The way I was testing for perfection was by looping from 1 to half the number being tested and determining if the number cleanly divided the number being tested. For example, with 28 I was doing this:

Start with a `sum` of 0. Loop from 1 to 14 (inclusive).

1. `28 % 1`. Yes! Add 1 to `sum`. `sum` is now 1.
2. `28 % 2`. Yes! Add 2 to `sum`. `sum` is now 3.
3. `28 % 3`. No!
4. `28 % 4`. Yes! Add 4 to `sum`. `sum` is now 7.
3. `28 % 5`. No!
3. `28 % 6`. No!
3. `28 % 7`. Yes! Add 7 to `sum`. `sum` is now 14.
3. `28 % 8`. No!
3. `28 % 9`. No!
3. `28 % 10`. No!
3. `28 % 11`. No!
3. `28 % 12`. No!
3. `28 % 13`. No!
3. `28 % 14`. Yes! Add 14 to `sum`. `sum` is now 28.

So, 28 is perfect.

But, Google pointed out to me that half of this information is already known by the time I calculate it. The divisors are 1, 2, 4, 7, 14, 28. As soon as I discover 1 I can quickly determine that it's paired with 28. 2 is paired with 14, and 4 with 7. I don't need to continue working at all once I've gone to 4! This is less than the square root of 28, 5.291502622129181.

So, if I loop from 1 to the square root of the number being tested, I can just take the number being tested as well as it's paired divisor _at the same time_. Add these to the sum and I'm in business:

Start with a `sum` of 1. Loop from 2 to the square root of 28 (rounded down to 5) (inclusive).

2. `28 % 2`. Yes! Add 2 and 14 to `sum`. `sum` is now 17.
3. `28 % 3`. No!
4. `28 % 4`. Yes! Add 4 and 7 to `sum`. `sum` is now 28.
3. `28 % 5`. No!

And, again, I've confirmed that 28 is perfect.

With this in mind, I rewrote my original one line solution like this:

```java
public static boolean reduceSqrtIsPerfect(long num) {
	return LongStream.rangeClosed(2, (long) Math.sqrt(num))
			.reduce(1, (sum, test) -> num % test == 0 ? sum + test + (num / test) : sum) == num;
}
```

And viola, my test of 8,589,869,056 went from taking 23 seconds to _3 milliseconds_!! After all, rather than looping from 1 to 4294934528 and testing each step along the way, I am now looping from 2 to 92681! This is _significantly_ less work.

And so, I'm fairly happy with the result. That, and, after all that, it's just one line of code.



