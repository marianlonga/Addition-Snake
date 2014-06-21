package com.marianlonga.additionsnake;

public class Algorithms {

	// returns random number from the interval [beginPos, endPos]
	public static int randomFromRange(int beginPos, int endPos) {
		return (
			(int) (
				Math.round (
					Math.random() * (endPos - beginPos)
				)
			)
			+ beginPos
		);
	}

}
