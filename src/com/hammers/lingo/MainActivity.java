package com.hammers.lingo;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener{

	private int nrOfBallsLeft = 0;
	private TextView nofBallsText, lingoProbText; 
	private boolean realCardBoolean[][] = new boolean[5][5];
	private static boolean virtualCardBoolean[][] = new boolean[5][5];
	private static ArrayList<Ball> balls = new ArrayList<Ball>();
	private static int numberOfCombinations;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nofBallsText = (TextView) findViewById(R.id.textView2);
		lingoProbText = (TextView) findViewById(R.id.textView3);
		initializeRealCardBoolean();
	}

	@Override 
	public void onSaveInstanceState(Bundle outState) 
	{
		outState.putInt("BallsLeft", nrOfBallsLeft);
		super.onSaveInstanceState(outState); 
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		nrOfBallsLeft = savedInstanceState.getInt("BallsLeft");
		nofBallsText.setText(String.valueOf(nrOfBallsLeft));
		lingoProbText.setText(roundTwoDecimals(binomialMethod()) + "%");
	}

	private void initializeRealCardBoolean() {
		for (int i = 0; i<5; i++) {
			for (int j = 0; j<5; j++) {
				String buttonID = "ToggleButton" + i + j;
				int resID = getResources().getIdentifier(buttonID, "id", "com.hammers.lingo");
				ToggleButton tb = (ToggleButton) findViewById(resID);
				if (tb.isChecked()) {
					realCardBoolean[i][j] = true;
				} else {
					realCardBoolean[i][j] = false;
				}
			}
		}
	}

	private void resetVirtualCard() {
		for (int i = 0; i<5; i++) {
			for (int j = 0; j<5; j++) {
				virtualCardBoolean[i][j] = realCardBoolean[i][j];
			}
		}
	}

	//	private double onlyFinalStates() {
	//		int totalNofStates = Binomial.binom(realFieldsLeft, nrOfBallsLeft); 
	//		int totalLingoCombinations = 0;
	//		int[] indexTracker = new int[nrOfBallsLeft];
	//		for(int i = 0; i < nrOfBallsLeft; i ++)
	//		{
	//			indexTracker[i] = i;
	//		}
	//		int changingIndex = nrOfBallsLeft - 1;
	//
	//		while(changingIndex >= 0)
	//		{
	//			Boolean indexesChanged = false;
	//			getCombinationForNumberOfBalls(nrOfBallsLeft, indexTracker);
	//			int n=0,m=0;
	//			for (int k = 0; k<5; k++) {
	//				for (int l = 0; l<5; l++) {
	//					while(!virtualCardBoolean[k][l]&&m<indexTracker.length) {
	//						if(indexTracker[m]==n) {
	//							virtualCardBoolean[k][l] = true;
	//							m++;
	//						}
	//						n++;
	//					}
	//				}
	//			}
	//			totalLingoCombinations += hasLingo(virtualCardBoolean) ? 1 : 0;
	//			resetVirtualCard();
	//
	//			while(!indexesChanged && changingIndex >= 0)
	//			{
	//				indexTracker[changingIndex]++;
	//				if(indexTracker[changingIndex] < 16 - ((indexTracker.length -1) - changingIndex))
	//				{
	//					for(int j = changingIndex; j < indexTracker.length; j++)
	//					{
	//						if(j != changingIndex)
	//							indexTracker[j] = indexTracker[j-1] + 1;
	//					}
	//					indexesChanged = true;
	//					changingIndex = nrOfBallsLeft - 1;
	//				}
	//				else
	//				{
	//					changingIndex--;
	//				}
	//			}
	//		}
	//		return totalLingoCombinations / totalNofStates;
	//	}

	public static Ball[] getCombinationForNumberOfBalls(int numberOfBalls, int[] indexArray)
	{
		Ball[] ballCombination = new Ball[numberOfBalls];
		int ballCombinationIndex = 0;
		for(int i : indexArray)
		{
			ballCombination[ballCombinationIndex] = balls.get(i);
			ballCombinationIndex++;
		}
		return ballCombination;
	}

	//	public double allSequincesAndFinalStates() {
	//		double result = 0.00;
	//		int virtualNrOfBalls = nrOfBallsLeft;
	//
	//		initializeRealCardBoolean();
	//		resetVirtualCard();
	//
	//		if(hasLingo(realCardBoolean)) {
	//			result = 100.0;
	//		} else {
	//			if (virtualNrOfBalls > 0) {
	//				result = 100.0 * placeBall(virtualCardBoolean, virtualNrOfBalls, realFieldsLeft);
	//			} else {
	//				result = 0.0;
	//			}
	//		}
	//		return result;
	//	}

	//	private double placeBall(boolean[][] virtualCardBoolean, int nofBallsLeft, int nofFieldsLeft) {
	//		double result = 0.0;
	//		double frac = 1.0 / (double) (nofFieldsLeft);
	//		for (int i = 0; i<5; i++) {
	//			for (int j = 0; j<5; j++) {
	//				if(!virtualCardBoolean[i][j]) {
	//					virtualCardBoolean[i][j] = true;
	//					nofBallsLeft = nofBallsLeft - 1;
	//					nofFieldsLeft = nofFieldsLeft - 1;
	//					if(hasLingo(virtualCardBoolean)) {
	//						result = result + frac;
	//					} else {
	//						if (nofBallsLeft > 0) {							
	//							double res = placeBall(virtualCardBoolean, nofBallsLeft, nofFieldsLeft);
	//							result = result + (frac * res);
	//						}
	//					}
	//					virtualCardBoolean[i][j] = false;
	//					nofBallsLeft = nofBallsLeft + 1;
	//					nofFieldsLeft = nofFieldsLeft + 1;
	//				}
	//			}
	//		}
	//		return result;
	//	}

	//	private boolean hasLingo(boolean testCardBoolean[][]) {
	//		boolean result = false;
	//		if (testCardBoolean[0][0] && testCardBoolean[1][1] && testCardBoolean[2][2] && testCardBoolean[3][3] && testCardBoolean[4][4]) {
	//			result = true;
	//		}
	//		if (testCardBoolean[4][0] && testCardBoolean[3][1] && testCardBoolean[2][2] && testCardBoolean[1][3] && testCardBoolean[0][4]) {
	//			result = true;
	//		}
	//		if (!result) {
	//			for (int i=0;i<5;i++) {
	//				if (testCardBoolean[i][0] && testCardBoolean[i][1] && testCardBoolean[i][2] && testCardBoolean[i][3] && testCardBoolean[i][4]) {
	//					result = true;
	//					break;
	//				}
	//				if (testCardBoolean[0][i] && testCardBoolean[1][i] && testCardBoolean[2][i] && testCardBoolean[3][i] && testCardBoolean[4][i]) {
	//					result = true;
	//					break;
	//				}
	//			}
	//		}
	//		return result;
	//	}

	public void onClick(View v) {
		if (v instanceof ToggleButton) {
			if (((ToggleButton) v).isChecked()) {
				if(nrOfBallsLeft > 0) {
					nrOfBallsLeft--;
				}	
			} else {
				//nrOfBallsLeft++;
			}
		} else {
			if(v.getId()==R.id.button1) {
				if(nrOfBallsLeft > 0) {
					nrOfBallsLeft--;
				} else {
					Toast toast = Toast.makeText(MainActivity.this, "Kan niet negatief zijn", Toast.LENGTH_SHORT);
					toast.show();
				}
			} else {
				nrOfBallsLeft++;
			}	
		}
		nofBallsText.setText(String.valueOf(nrOfBallsLeft));
		lingoProbText.setText(roundTwoDecimals(binomialMethod()) + "%");
		//		System.out.print(roundTwoDecimals(allSequincesAndFinalStates()) + "%");
	}

	private double binomialMethod() {
		double result = 0.0;
		initializeRealCardBoolean();
		numberOfCombinations = 0;
		resetVirtualCard();
		createBalls();

		int totalLingoCombinations = 0;
		int[] indexTracker = new int[nrOfBallsLeft];
		for(int i = 0; i < nrOfBallsLeft; i ++)
		{
			indexTracker[i] = i;
		}
		int changingIndex = nrOfBallsLeft - 1;

		while(changingIndex >= 0)
		{
			//count++;
			Boolean indexesChanged = false;
			totalLingoCombinations += checkIfLingo(getCombinationForNumberOfBalls(nrOfBallsLeft, indexTracker)) ? 1 : 0;

			while(!indexesChanged && changingIndex >= 0)
			{
				indexTracker[changingIndex]++;
				if(indexTracker[changingIndex] < balls.size() - ((indexTracker.length -1) - changingIndex))
				{
					for(int j = changingIndex; j < indexTracker.length; j++)
					{
						if(j != changingIndex)
							indexTracker[j] = indexTracker[j-1] + 1;
					}
					indexesChanged = true;
					changingIndex = nrOfBallsLeft - 1;
				}
				else
				{
					changingIndex--;
				}
			}
		}
		if (nrOfBallsLeft == 0) {
			if(checkIfLingo(null)) {
				result = 100.0;
			} else {
				result = 0.0;
			}
		} else if (numberOfCombinations == 0) {
			result = 0.0;
		} else {
			result = totalLingoCombinations * 100.0 / numberOfCombinations;
		}	
		return result;
	}

	private static Boolean checkIfLingo(Ball[] ballCombination)
	{
		Boolean isLingo = false;
		if (ballCombination != null) {
			for(Ball ball : ballCombination)
			{
				virtualCardBoolean[ball.getRow()][ball.getColumn()] = true;
			}
		}

		isLingo = checkRowForLingo(virtualCardBoolean);
		if(!isLingo)
			isLingo = checkColumnForLingo(virtualCardBoolean);
		if(!isLingo)
			checkDiagonalForLingo(virtualCardBoolean);

		if (ballCombination != null) {
			for(Ball ball : ballCombination)
			{
				virtualCardBoolean[ball.getRow()][ball.getColumn()] = false;
			}
			numberOfCombinations++;
		}
		return isLingo;
	}

	public static Boolean checkRowForLingo(boolean[][] virtualCardBoolean2)
	{
		Boolean isLingo = false;
		int i = 0;
		while(!isLingo && i < 5)
		{
			isLingo = virtualCardBoolean2[i][0] && virtualCardBoolean2[i][1] && virtualCardBoolean2[i][2] && virtualCardBoolean2[i][3] && virtualCardBoolean2[i][4];
			i++;
		}
		return isLingo;
	}

	public static Boolean checkColumnForLingo(boolean[][] lingoMap)
	{
		Boolean isLingo = false;
		int i = 0;
		while(!isLingo && i < 5)
		{
			isLingo = lingoMap[0][i] && lingoMap[1][i] && lingoMap[2][i] && lingoMap[3][i] && lingoMap[4][i];
			i++;
		}
		return isLingo;
	}

	public static Boolean checkDiagonalForLingo(boolean[][] lingoMap)
	{
		return (lingoMap[0][0] && lingoMap[1][1] && lingoMap[2][2] && lingoMap[3][3] && lingoMap[4][4]) || (lingoMap[0][4] && lingoMap[1][3] && lingoMap[2][2] && lingoMap[3][1] && lingoMap[4][0]);
	}

	public static void createBalls()
	{
		balls.clear();
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if(!virtualCardBoolean[i][j])
				{
					Ball ball = new Ball(i, j);
					balls.add(ball);
				}
			}
		}
	}

	public String roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return twoDForm.format(d);
	}
}
