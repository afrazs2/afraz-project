import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PokerProject
{
    public static void main(String[] args)
    {
        int handindex = 0; // keeps track of which hand we are on and it goes in order from first hand to last hand
        int handcount = 0; // we are using this to find out how many hands there are in a file
        int five = 0; // Tpair and Opair are twopair and onepair
        int four = 0;
        int full = 0;
        int three = 0;
        int tpair = 0;
        int opair = 0;
        int high = 0;
        int totalbid = 0; // finding the total bid amount.

        try
        {
            File f = new File("src/data");
            Scanner s = new Scanner(f); //we need to know how many hands are in the file since it's not a set amount since we cant just use 79 due to the intended input file for the project testing to be an undisclosed amount. So we run tthrough the file completely once with handcount to keep track of how many hands there are by how many times it reads each hand
            while (s.hasNextLine())
            {
                s.nextLine();
                handcount++;
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

            int[] prevhandtype = new int[handcount]; // Basically, the way im ranking them is every hand we read is by keeping track of the previous type and using that to rank it in an array. We determine the handcount with the previous scan attempt. In particular, this is used to rank the handtype from 5oak, 4oak, fh, 3oak, 2 pair, one pair, highcard from 7 to 1 respectively
            int[] prevrank = new int[handcount]; // this is used to rank the hands by giving it a position and then how many hands are stronger than this hand.
            int[] prevcard1 = new int[handcount]; // first card in the hand
            int[] prevcard2 = new int[handcount]; // second card of the hand
            int[] prevcard3 = new int[handcount]; // third card of the hand
            int[] prevcard4 = new int[handcount]; // 4th card of the hand
            int[] prevcard5 = new int[handcount]; // 5th card of the hand.
            int[] bidValues = new int[handcount]; // bid value for the hand, important for calculating the total big value.

            while (s.hasNextLine())
            {
                String totalhand = s.nextLine();
                String isohand = totalhand.substring(0, totalhand.indexOf('|'));  // Reads up until | by creating a substring that goes up until that length
                String[] indicards = isohand.split(","); // splits up the hand into individual cards by seperating with ",". Indi cards is just individual cards while isohand is isolated hand without the comments after the |
                boolean[] checked = new boolean[5]; // I plan to use this to make sure each card only gets counted once.
                int[] duplicount = new int[5]; // this part is a little confusing but I would summarize it as counting whether or not a duplicate has popped up, meaning if a certain card shows up 4 times and another card shows up once it marks it. The question is how would I tell if it showed up before which I plan to use a for loop for where I compare the first card to the second card, second to the third and so on and using a check variable to make sure cards dont get counted twice
                int[] cardnumericvalue = new int[5]; // Since "Jack, King, Queen, Ace" Aren't actual numeric values, we convert it into numeric values with the cardranking object
                String bidstringform = totalhand.substring(totalhand.indexOf('|') + 1); // we get the bid from after | without including the |, it is in the form of a string
                int bidintform = Integer.parseInt(bidstringform); // we convert that string into an integer.

                bidValues[handindex] = bidintform; // we store the current hands bitvalue into our array

                for (int i = 0; i < indicards.length; i++)
                {
                    cardnumericvalue[i] = cardranking(indicards[i]);
                } // we essentially take the hand, parse through it and convert everything into numerical form using the card ranking object.

                for (int i = 0; i < indicards.length; i++)
                {
                    if (!checked[i]) //checks if this card is used before
                    {
                        int num = 1;  // this is how i'm keeping track of how many times the card shows up while checking for duplicates
                        int followingcard = i + 1;  // I want to check the following card so this will always put it ahead of the current card.
                        while (followingcard < indicards.length)
                        {
                            if (indicards[i].equals(indicards[followingcard]))
                            {
                                num++;  // a duplicate is found and marked and we count it using checked
                                checked[followingcard] = true; // we read this card so we dont check it again
                            }
                            followingcard++; // move to the next following card as i continues.
                        }
                        duplicount[i] = num; //how many times that card is stored shows up, this is important for later because we need it to check for things such as 5 of a kind, 3 of a kind etc.
                        checked[i] = true; // don't check this card again
                    }
                }

                int handranking = 0; // the hands need a hierarchy for the actual ranking so we use this variable to assign it depending on the hand type.
                int highestamt = 0; //  checks for the highest amount of a singular card that shows up, meaning if king shows up 3 times it tracks that the card that showed up the most times showed up 3 times
                int pair = 0; // checks if there is a pair where 2 cards of the same, this is used for two pair and 1 pair, but most importantly to differentiate between a three of a kind and a full house.

                for (int i = 0; i < duplicount.length; i++)
                {
                    if (duplicount[i] > highestamt)
                    {
                        highestamt = duplicount[i];  // if duplicount which counts how many  duplicates is there is like if king shows up 3 times, itll mark it as 3 times and it takes into account only the card that shows up the most, we manage this by only taking the card if it is currently greater than the highest amount.
                    }
                    if (duplicount[i] == 2)
                    {
                        pair++; // if a card shows up only twice, it is a pair.
                    }
                }

                if (highestamt == 5)
                {
                    five++; // five of a kind and the highest ranking of the 7 types so we assign it 7
                    handranking = 7;
                }
                else if (highestamt == 4)
                {
                    four++;
                    handranking = 6; // 4 of a kind and 2nd highest
                }
                else if (highestamt == 3 && pair == 1)
                {
                    full++;
                    handranking = 5; // full house which is 3rd highest
                }
                else if (highestamt == 3)
                {
                    three++;
                    handranking = 4; // 3 of a kind which is 4th highest
                }
                else if (pair == 2)
                {
                    tpair++;
                    handranking = 3; // 2 pair which is 5th highest
                }
                else if (pair == 1)
                {
                    opair++;
                    handranking = 2; // one pair which is 6th highest.
                }
                else
                {
                    high++;
                    handranking = 1; // highcard which is the lowest ranking
                }
                int rank = 1; // sets the current hand as the weakest rank, furthermore if it is the first hand of the handtype, it just stores its current data  and moves to the next hand, skipping this loop.

                for (int i = 0; i < handindex; i++) // compares this current hand to every other hand
                {
                    if (handranking > prevhandtype[i])
                    {
                        rank++; // if current hand is stronger than previous hand, rank goes up
                    }
                    else if (handranking < prevhandtype[i])
                    {
                        prevrank[i]++; // if current hand is weaker than previous hand, rank of previous hand goes up
                    }
                    else
                    {
                        if (cardnumericvalue[0] > prevcard1[i])
                        {
                            rank++; // if the handtypes are the same, compare the numerical value of the first card of the current hand with the numerical value of the first card of the previous hand, add 1 rank to whichever is stronger and we repeat this for all 5 cards if they are all the same.
                        }
                        else if (cardnumericvalue[0] < prevcard1[i])
                        {
                            prevrank[i]++;
                        }
                        else if (cardnumericvalue[1] > prevcard2[i])
                        {
                            rank++;
                        }
                        else if (cardnumericvalue[1] < prevcard2[i])
                        {
                            prevrank[i]++;
                        }
                        else if (cardnumericvalue[2] > prevcard3[i])
                        {
                            rank++;
                        }
                        else if (cardnumericvalue[2] < prevcard3[i])
                        {
                            prevrank[i]++;
                        }
                        else if (cardnumericvalue[3] > prevcard4[i])
                        {
                            rank++;
                        }
                        else if (cardnumericvalue[3] < prevcard4[i])
                        {
                            prevrank[i]++;
                        }
                        else if (cardnumericvalue[4] > prevcard5[i])
                        {
                            rank++;
                        }
                        else if (cardnumericvalue[4] < prevcard5[i])
                        {
                            prevrank[i]++;
                        }
                    }
                }

                prevhandtype[handindex] = handranking; // store the information of the current card into the array for the comparison of the next hands when ranking
                prevrank[handindex] = rank; // store current hands rank
                prevcard1[handindex] = cardnumericvalue[0]; // store the numerical values of the cards
                prevcard2[handindex] = cardnumericvalue[1];
                prevcard3[handindex] = cardnumericvalue[2];
                prevcard4[handindex] = cardnumericvalue[3];
                prevcard5[handindex] = cardnumericvalue[4];

                handindex++; // go to the next hand
            }

            for (int i = 0; i < handcount; i++) // since this comes after the while loop, previous rank contains the rank of each hand and bidvalues contains the bidvalue of each hand, and since they line up with total hand count we can just directly multiple and set it equal to itself
            {
                totalbid += prevrank[i] * bidValues[i];
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
        System.out.println("Total Bid Value: " + totalbid);
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
}