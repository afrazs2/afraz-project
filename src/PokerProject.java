import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class PokerProject
{
    public static int handCount = 0;
    public static void main(String[] args)
    {








        try {
            File f = new File("src/data");
            Scanner s = new Scanner(f);




            while (s.hasNextLine()) {
                int jackCount = 0; // for jacks are wild we need to track exactly how many jacks there are in a hand, we can do this by using a for loop that goes through the given hand and checks for jacks.
                String totalHand = s.nextLine();
                String isoHand = totalHand.substring(0, totalHand.indexOf('|'));  // Reads up until | by creating a substring that goes up until that length
                String[] indiCards = isoHand.split(","); // splits up the hand into individual cards by seperating with ",". Indi cards is just individual cards while isohand is isolated hand without the comments after the |
                String bidStringForm = totalHand.substring(totalHand.indexOf('|') + 1);
                int bidIntForm = Integer.parseInt(bidStringForm);
                Hand hand = new Hand(indiCards, bidIntForm);


            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        Hand.calculateTotals();
        Hand.getHandTotals();




    }
    public static int gethandcount()
    {
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
        return handCount;
    }


}


