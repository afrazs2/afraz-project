import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PokerProject
{
    public static void main(String[] args)
    {
        int handIndex = 0; // keeps track of which hand we are on and it goes in order from first hand to last hand
        int handCount = 0; // we are using this to find out how many hands there are in a file
        int five = 0; // Tpair and Opair are twopair and onepair
        int four = 0;
        int full = 0;
        int three = 0;
        int tpair = 0;
        int opair = 0;
        int high = 0;
        int totalBid = 0; // finding the total bid amount.
        int totalBidWild = 0; // total bid for the wild amount

        try
        {
            File f = new File("src/data");
            Scanner s = new Scanner(f); //we need to know how many hands are in the file since it's not a set amount since we cant just use 79 due to the intended input file for the project testing to be an undisclosed amount. So we run tthrough the file completely once with handCount to keep track of how many hands there are by how many times it reads each hand
            while (s.hasNextLine())
            {
                s.nextLine();
                handCount++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }

        try
        {
            File f = new File("src/data");
            Scanner s = new Scanner(f);
            int[] prevRankWild = new int[handCount]; // new rankings aswell because of the fact that rankings will change if jack = 1 and theres a new highest ranking hierarchy.
            int[] prevHandTypeWild = new int[handCount]; // since in this one if there is a jack it acts as the card that makes the card the highest hand, we need to create a new handtype intake.
            int[] prevHandType = new int[handCount]; // Basically, the way im ranking them is every hand we read is by keeping track of the previous type and using that to rank it in an array. We determine the handCount with the previous scan attempt. In particular, this is used to rank the handtype from 5oak, 4oak, fh, 3oak, 2 pair, one pair, highcard from 7 to 1 respectively
            int[] prevRank = new int[handCount]; // this is used to rank the hands by giving it a position and then how many hands are stronger than this hand.
            int[] prevCard1 = new int[handCount]; // first card in the hand
            int[] prevCard2 = new int[handCount]; // second card of the hand
            int[] prevCard3 = new int[handCount]; // third card of the hand
            int[] prevCard4 = new int[handCount]; // 4th card of the hand
            int[] prevCard5 = new int[handCount]; // 5th card of the hand.
            int[] prevCard1Wild = new int[handCount]; // these are the same purpose as the above but for the wild version
            int[] prevCard2Wild = new int[handCount];
            int[] prevCard3Wild = new int[handCount];
            int[] prevCard4Wild = new int[handCount];
            int[] prevCard5Wild = new int[handCount];
            int[] bidValues = new int[handCount]; // bid value for the hand, important for calculating the total big value.

            while (s.hasNextLine())
            {
                int jackCount = 0; // for jacks are wild we need to track exactly how many jacks there are in a hand, we can do this by using a for loop that goes through the given hand and checks for jacks.
                String totalHand = s.nextLine();
                String isoHand = totalHand.substring(0, totalHand.indexOf('|'));  // Reads up until | by creating a substring that goes up until that length
                String[] indiCards = isoHand.split(","); // splits up the hand into individual cards by seperating with ",". Indi cards is just individual cards while isohand is isolated hand without the comments after the |
                boolean[] checked = new boolean[5]; // I plan to use this to make sure each card only gets counted once.
                int[] dupliCount = new int[5]; // this part is a little confusing but I would summarize it as counting whether or not a duplicate has popped up, meaning if a certain card shows up 4 times and another card shows up once it marks it. The question is how would I tell if it showed up before which I plan to use a for loop for where I compare the first card to the second card, second to the third and so on and using a check variable to make sure cards dont get counted twice
                int[] cardNumericValue = new int[5]; // Since "Jack, King, Queen, Ace" Aren't actual numeric values, we convert it into numeric values with the cardranking object
                int[] cardNumericValueWild = new int[5]; // card values similar to as above but now a jack is 1
                String bidStringForm = totalHand.substring(totalHand.indexOf('|') + 1); // we get the bid from after | without including the |, it is in the form of a string
                int bidIntForm = Integer.parseInt(bidStringForm); // we convert that string into an integer.

                bidValues[handIndex] = bidIntForm; // we store the current hands bit value into our array

                for (int i = 0; i < indiCards.length; i++)
                {
                    if (indiCards[i].equals("Jack"))
                    {
                        jackCount++; // we keep track of how many jacks there are in a hand
                    }
                }
                for (int i = 0; i < indiCards.length; i++)
                {
                    cardNumericValue[i] = cardranking(indiCards[i]);
                } // we essentially take the hand, parse through it and convert everything into numerical form using the card ranking object.

                for (int i = 0; i < indiCards.length; i++)
                {
                    cardNumericValueWild[i] = cardrankingWild(indiCards[i]); // same thing as above but for the wild cards.
                }
                for (int i = 0; i < indiCards.length; i++)
                {
                    if (!checked[i]) //checks if this card is used before
                    {
                        int num = 1;  // this is how i'm keeping track of how many times the card shows up while checking for duplicates
                        int followingCard = i + 1;  // I want to check the following card so this will always put it ahead of the current card.
                        while (followingCard < indiCards.length)
                        {
                            if (indiCards[i].equals(indiCards[followingCard]))
                            {
                                num++;  // a duplicate is found and marked and we count it using checked
                                checked[followingCard] = true; // we read this card so we dont check it again
                            }
                            followingCard++; // move to the next following card as i continues.
                        }
                        dupliCount[i] = num; //how many times that card is stored shows up, this is important for later because we need it to check for things such as 5 of a kind, 3 of a kind etc.
                        checked[i] = true; // don't check this card again
                    }
                }
                int highestAmtWild = 0; // same thing as the below but for the wild amount
                int handRankingWild = 0;
                int pairWild = 0;


                int handRanking = 0; // the hands need a hierarchy for the actual ranking so we use this variable to assign it depending on the hand type.
                int highestAmt = 0; //  checks for the highest amount of a singular card that shows up, meaning if king shows up 3 times it tracks that the card that showed up the most times showed up 3 times
                int pair = 0; // checks if there is a pair where 2 cards of the same, this is used for two pair and 1 pair, but most importantly to differentiate between a three of a kind and a full house.


                for (int i = 0; i < dupliCount.length; i++)
                {
                    if (dupliCount[i] > highestAmt)
                    {
                        highestAmt = dupliCount[i];  // if duplicount which counts how many  duplicates is there is like if king shows up 3 times, itll mark it as 3 times and it takes into account only the card that shows up the most, we manage this by only taking the card if it is currently greater than the highest amount.
                    }
                    if (dupliCount[i] == 2)
                    {
                        pair++; // if a card shows up only twice, it is a pair.
                        pairWild ++;
                    }
                }
                if (jackCount > 0)
                {
                    highestAmtWild = highestAmt;
                    highestAmtWild += jackCount; // if there is more than 1 jack, we set the highest amtwild for what the highest amount is but also adding in how many jacks there are

                    if (highestAmtWild > 5)
                    {
                        highestAmtWild = 5; // since it cant be greater than 5, if it is greater than 5 we just set it to 5
                    }
                }




                if (highestAmt == 5)
                {
                    five++; // five of a kind and the highest ranking of the 7 types so we assign it 7
                    handRanking = 7;
                }
                else if (highestAmt == 4)
                {
                    four++;
                    handRanking = 6; // 4 of a kind and 2nd highest
                }
                else if (highestAmt == 3 && pair == 1)
                {
                    full++;
                    handRanking = 5; // full house which is 3rd highest
                }
                else if (highestAmt == 3)
                {
                    three++;
                    handRanking = 4; // 3 of a kind which is 4th highest
                }
                else if (pair == 2)
                {
                    tpair++;
                    handRanking = 3; // 2 pair which is 5th highest
                }
                else if (pair == 1)
                {
                    opair++;
                    handRanking = 2; // one pair which is 6th highest.
                }
                else
                {
                    high++;
                    handRanking = 1; // highcard which is the lowest ranking
                }

                if (highestAmtWild == 5)
                {
                    handRankingWild = 7;
                }
                else if (highestAmtWild == 4)
                {
                    handRankingWild = 6;
                }
                else if (highestAmtWild == 3 && pairWild >= 1)
                {
                    handRankingWild = 5;
                }
                else if (highestAmtWild == 3)
                {
                    handRankingWild = 4;
                }
                else if (pairWild >= 2)
                {
                    handRankingWild = 3;
                }
                else if (pairWild >= 1)
                {
                    handRankingWild = 2;
                }
                else
                {
                    handRankingWild = 1;
                }

                int rank = 1; // sets the current hand as the weakest rank, furthermore if it is the first hand of the handtype, it just stores its current data  and moves to the next hand, skipping this loop.
                int rankWild = 1;// sets current hand as weakest rank similar to above but for hte wild cards.
                for (int i = 0; i < handIndex; i++) // compares this current hand to every other hand
                {
                    if (handRanking > prevHandType[i])
                    {
                        rank++; // if current hand is stronger than previous hand, rank goes up
                    }
                    else if (handRanking < prevHandType[i])
                    {
                        prevRank[i]++; // if current hand is weaker than previous hand, rank of previous hand goes up
                    }
                    else
                    {
                        if (cardNumericValue[0] > prevCard1[i])
                        {
                            rank++; // if the handtypes are the same, compare the numerical value of the first card of the current hand with the numerical value of the first card of the previous hand, add 1 rank to whichever is stronger and we repeat this for all 5 cards if they are all the same.
                        }
                        else if (cardNumericValue[0] < prevCard1[i])
                        {
                            prevRank[i]++;
                        }
                        else if (cardNumericValue[1] > prevCard2[i])
                        {
                            rank++;
                        }
                        else if (cardNumericValue[1] < prevCard2[i])
                        {
                            prevRank[i]++;
                        }
                        else if (cardNumericValue[2] > prevCard3[i])
                        {
                            rank++;
                        }
                        else if (cardNumericValue[2] < prevCard3[i])
                        {
                            prevRank[i]++;
                        }
                        else if (cardNumericValue[3] > prevCard4[i])
                        {
                            rank++;
                        }
                        else if (cardNumericValue[3] < prevCard4[i])
                        {
                            prevRank[i]++;
                        }
                        else if (cardNumericValue[4] > prevCard5[i])
                        {
                            rank++;
                        }
                        else if (cardNumericValue[4] < prevCard5[i])
                        {
                            prevRank[i]++;
                        }
                    }


                }
                for (int i = 0; i < handIndex; i++)
                {
                    if (handRankingWild > prevHandTypeWild[i])
                    {
                        rankWild++;
                    }
                    else if (handRankingWild < prevHandTypeWild[i])
                    {
                        prevRankWild[i]++;
                    }
                    else
                    {
                        if (cardNumericValueWild[0] > prevCard1Wild[i])
                        {
                            rankWild++;
                        }
                        else if (cardNumericValueWild[0] < prevCard1Wild[i])
                        {
                            prevRankWild[i]++;
                        }
                        else if (cardNumericValueWild[1] > prevCard2Wild[i])
                        {
                            rankWild++;
                        }
                        else if (cardNumericValueWild[1] < prevCard2Wild[i])
                        {
                            prevRankWild[i]++;
                        }
                        else if (cardNumericValueWild[2] > prevCard3Wild[i])
                        {
                            rankWild++;
                        }
                        else if (cardNumericValueWild[2] < prevCard3Wild[i])
                        {
                            prevRankWild[i]++;
                        }
                        else if (cardNumericValueWild[3] > prevCard4Wild[i])
                        {
                            rankWild++;
                        }
                        else if (cardNumericValueWild[3] < prevCard4Wild[i])
                        {
                            prevRankWild[i]++;
                        }
                        else if (cardNumericValueWild[4] > prevCard5Wild[i])
                        {
                            rankWild++;
                        }
                        else if (cardNumericValueWild[4] < prevCard5Wild[i])
                        {
                            prevRankWild[i]++;
                        }
                    }
                }
                prevHandType[handIndex] = handRanking; // store the information of the current card into the array for the comparison of the next hands when ranking
                prevRank[handIndex] = rank; // store current hands rank
                prevCard1[handIndex] = cardNumericValue[0]; // store the numerical values of the cards
                prevCard2[handIndex] = cardNumericValue[1];
                prevCard3[handIndex] = cardNumericValue[2];
                prevCard4[handIndex] = cardNumericValue[3];
                prevCard5[handIndex] = cardNumericValue[4];
                prevHandTypeWild[handIndex] = handRankingWild;
                prevRankWild[handIndex] = rankWild;
                prevCard1Wild[handIndex] = cardNumericValueWild[0];
                prevCard2Wild[handIndex] = cardNumericValueWild[1];
                prevCard3Wild[handIndex] = cardNumericValueWild[2];
                prevCard4Wild[handIndex] = cardNumericValueWild[3];
                prevCard5Wild[handIndex] = cardNumericValueWild[4];

                handIndex++; // go to the next hand
            }

            for (int i = 0; i < handCount; i++) // since this comes after the while loop, previous rank contains the rank of each hand and bidvalues contains the bidvalue of each hand, and since they line up with total hand count we can just directly multiple and set it equal to itself
            {
                totalBid += prevRank[i] * bidValues[i];
            }
            for (int i = 0; i < handCount; i++)
            {
                totalBidWild += prevRankWild[i] * bidValues[i];
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }

        System.out.println("Number of five of a kind hands: " + five);
        System.out.println("Number of full house hands: " + full);
        System.out.println("Number of four of a kind hands: " + four);
        System.out.println("Number of three of a kind hands: " + three);
        System.out.println("Number of two pair hands: " + tpair);
        System.out.println("Number of one pair hands: " + opair);
        System.out.println("Number of high card hands: " + high);
        System.out.println("Total Bid Value: " + totalBid);
        System.out.println("Total Bid Value With Jacks Wild: " + totalBidWild);


    }

    public static int cardranking(String card)
    {
        if (card.equals("Ace"))
        {
            return 14;
        }
        else if (card.equals("King"))
        {
            return 13;
        }
        else if (card.equals("Queen"))
        {
            return 12;
        }
        else if (card.equals("Jack"))
        {
            return 11;
        }
        else if (card.equals("10"))
        {
            return 10;
        }
        else if (card.equals("9"))
        {
            return 9;
        }
        else if (card.equals("8"))
        {
            return 8;
        }
        else if (card.equals("7"))
        {
            return 7;
        }
        else if (card.equals("6"))
        {
            return 6;
        }
        else if (card.equals("5"))
        {
            return 5;
        }
        else if (card.equals("4"))
        {
            return 4;
        }
        else if (card.equals("3"))
        {
            return 3;
        }
        else
        {
            return 2;
        }
    }
    public static int cardrankingWild(String card)
    {
        if (card.equals("Ace"))
        {
            return 14;
        }
        else if (card.equals("King"))
        {
            return 13;
        }
        else if (card.equals("Queen"))
        {
            return 12;
        }
        else if (card.equals("Jack"))
        {
            return 1; // for jacks are wild it has to be the weakest card which is 1
        }
        else if (card.equals("10"))
        {
            return 10;
        }
        else if (card.equals("9"))
        {
            return 9;
        }
        else if (card.equals("8"))
        {
            return 8;
        }
        else if (card.equals("7"))
        {
            return 7;
        }
        else if (card.equals("6"))
        {
            return 6;
        }
        else if (card.equals("5"))
        {
            return 5;
        }
        else if (card.equals("4"))
        {
            return 4;
        }
        else if (card.equals("3"))
        {
            return 3;
        }
        else
        {
            return 2;
        }
    }
}
