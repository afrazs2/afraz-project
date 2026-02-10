import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class PokerProject {
    public static void main(String[] args) //note that most of these comments seem redundant but it's how i'm keeping track of what each variable and array does since it gets relatively messy
    {
        int count;
        int five = 0; // Tpair and Opair are twopair and onepair - Afraz
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
                                checked[followingcard] = true;
                            }
                        }


                    }

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

    }








































}
