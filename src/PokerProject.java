import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PokerProject {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner Scan = new Scanner(new File("src/data"));

        int five = 0;
        int four = 0;
        int full = 0;
        int three = 0;
        int two = 0;
        int one = 0;
        int high = 0;

        while (Scan.hasNextLine()) {

            String line = Scan.nextLine().trim();

            if (!line.isEmpty()) {

                int bar = line.indexOf('|');
                if (bar != -1) {
                    line = line.substring(0, bar);
                }

                String[] cards = line.split(",");

                String[] seen = new String[5];
                int[] count = new int[5];
                int used = 0;

                for (int i = 0; i < 5; i++) {
                    String card = cards[i].trim();

                    int pos = -1;
                    for (int j = 0; j < used; j++) {
                        if (seen[j].equals(card)) {
                            pos = j;
                            break;
                        }
                    }

                    if (pos == -1) {
                        seen[used] = card;
                        count[used] = 1;
                        used++;
                    } else {
                        count[pos]++;
                    }
                }

                int max = 0;
                for (int c : count) {
                    if (c > max) max = c;
                }

                if (used == 1) {
                    five++;
                } else if (used == 2) {
                    if (max == 4) four++;
                    else full++;
                } else if (used == 3) {
                    if (max == 3) three++;
                    else two++;
                } else if (used == 4) {
                    one++;
                } else {
                    high++;
                }
            }
        }

        System.out.println(" Number of five of a kind hands: " + five);
        System.out.println(" Number of full house hands: " + full);
        System.out.println(" Number of four of a kind hands: " + four);
        System.out.println("Number of three of a kind hands: " + three);
        System.out.println(" Number of two pair hands: " + two);
        System.out.println("Number of one pair hands: " + one);
        System.out.println("Number of high card hands: " + high);
    }
}
