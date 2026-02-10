import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class PokerProject {
    public static void main(String[] args)
    {
        int count;
        int five = 0; // Tpair and Opair are twopair and onepair
        int four = 0;
        int full = 0;
        int three = 0;
        int tpair = 0;
        int opair = 0;
        int high = 0;

        try {
            File f = new File("src/data");
            Scanner s = new Scanner(f);


            while (s.hasNextLine())
            {
                String totalhand = s.nextLine();
                String isohand = totalhand.substring(0, totalhand.indexOf('|')); // Reads up until | by creating a substring that goes up until that length
                String[] indicards = isohand.split(","); // splits up the hand into individual cards by seperating with ",". Indi cards is just individual cards while isohand is isolated hand without the comments after the |
                int[] duplicount = new int[5]; // this part is a little confusing but I would summarize it as counting whether or not a duplicate has popped up, meaning if a certain card shows up 4 times and another card shows up once it marks it. The question is how would I tell if it showed up before which I plan to use a for loop for where I compare the first card to the second card, second to the third and so on and using a check variable to make sure cards dont get counted twice.
                boolean[] checked = new boolean[5]; // I plan to use this to make sure each card only gets counted once.

                for(int i = 0; i < indicards.length; i++)
                {
                    if(checked[i] == false)//checks if this card is used before
                    {
                        int num = 1; // this is how i'm keeping track of how many times the card shows up while checking for duplicates
                        int followingcard = i+1; // I want to check the following card so this will always put it ahead of the current card.
                        while (followingcard < indicards.length)
                        {
                            if(indicards[i].equals(indicards[followingcard]))
                            {
                                num++; // a duplicate is found and marked and we count it using checked
                                checked[followingcard] = true; // we read this card so we dont check it again
                            }
                            followingcard = followingcard + 1; // move to the next following card as i continues.
                        }
                        duplicount[i] = num; //how many times that card is stored shows up, this is important for later because we need it to check for things such as 5 of a kind, 3 of a kind etc.
                        checked[i] = true; // don't check this card again

                    }

                }
                int highestamt = 0; //  checks for the highest amount of a singular card that shows up, meaning if king shows up 3 times it tracks that the card that showed up the most times showed up 3 times
                int pair = 0; // checks if there is a pair where 2 cards of the same, this is used for two pair and 1 pair, but most importantly to differentiate between a three of a kind and a full house.

                for (int i = 0; i < duplicount.length; i++) {
                    if (duplicount[i] > highestamt)
                    {
                        highestamt = duplicount[i]; // if duplicount which counts how many  duplicates is there is like if king shows up 3 times, itll mark it as 3 times and it takes into account only the card that shows up the most, we manage this by only taking the card if it is currently greater than the highest amount.
                    }
                    if (duplicount[i] == 2)
                    {
                        pair++; // if a card shows up only twice, it is a pair.
                    };
                }

                if (highestamt == 5)
                {
                    five++; // five of a kind
                }
                else if (highestamt == 4)
                {
                    four++; // four of a kind
                }
                else if (highestamt == 3 && pair == 1)
                {
                    full++; // full house which requires the highest amount to be a 3 and also require a pair, it is important it comes before three of a kind or else it could be swapped and cause issues.
                }
                else if (highestamt == 3)
                {
                    three++; // 3 of a kind.
                }
                else if (pair == 2)
                {
                    tpair++; // two pairs showed up.
                }
                else if (pair == 1)
                {
                    opair++; // singular pair
                }
                else
                {
                    high++; // no pairs and each card only shows up once, so it is set as highcard.
                }

            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }



        System.out.println("Number of five of a kind hands: " + five);
        System.out.println("Number of full house hands: " + full);
        System.out.println("Number of four of a kind hands: " + four);
        System.out.println("Number of three of a kind hands: " + three);
        System.out.println("Number of two pair hands: " + tpair);
        System.out.println("Number of one pair hands: " + opair);
        System.out.println("Number of high card hands: " + high);

    }}

