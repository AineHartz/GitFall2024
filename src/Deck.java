import java.io.*;
import java.util.*;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Deck 
{
    private Map<String, Integer> cards;
    private List<String> invalidCards;
    private int totalCost;
    private int deckID;
    private Map<Integer, Integer> histogram;
    
    
    //A constructor which initializes the deck to a blank slate. The for loop just creates a slot in the map for each energy cost.
    public Deck() 
    {
        this.cards = new HashMap<>();
        this.invalidCards = new ArrayList<>();
        this.totalCost = 0;
        this.histogram = new HashMap<>();
        
        for (int i = 0; i <= 6; i++) 
        {
            this.histogram.put(i, 0);
        }
        this.deckID = generateDeckID();
    }
    
    // Method to generate 9-digit deck ID
    private int generateDeckID() 
    {
        Random rand = new Random();
        return rand.nextInt(900000000) + 100000000; // Ensure it's a 9-digit number
    }

    // Method to add valid cards to the deck. Takes a string with the card name and an int with the cost as input, returns nothing, but adds the card to the card list and the energy histogram.
    public void addCard(String card, int cost) 
    {
        if (isValidCard(card, cost)) 
        {
            cards.put(card, cost);
            totalCost += cost;
            histogram.put(cost, histogram.get(cost) + 1);
        } 
        
        else 
        {
            invalidCards.add(card);
        }
    }

    // Method to check if the card is valid. Takes the card name and the int cost as input, returns true or false for if the card is valid or not. 
    private boolean isValidCard(String card, int cost) 
    {
        // Check if card is null
        if (card == null) 
        {
            return false;
        }
        
        // Check if card name is empty or contains only spaces/tabs
        String trimmedCard = card.trim();
        if (trimmedCard.isEmpty()) 
        {
            return false;
        }
        
        // Check if cost is within valid range (0-6)
        if (cost < 0 || cost > 6) 
        {
            return false;
        }
        
        return true; // All checks passed, so the card is valid
    }
    
    // Method to read the deck from file
    public void readFromFile(String fileName) throws FileNotFoundException 
    {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) 
        {
            String[] cardData = scanner.nextLine().split(":");
            if (cardData.length == 2) 
            {
                try 
                {
                    String cardName = cardData[0];
                    int cost = Integer.parseInt(cardData[1]);
                    addCard(cardName, cost);
                } 
            
                catch (NumberFormatException e) 
                {
                    invalidCards.add(cardData[0]); // Handle invalid cost
                }
            }
        }
        
        scanner.close();
    }

    // Method to generate PDF report
    public void generatePDFReport(String fileName) throws DocumentException, FileNotFoundException 
    {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        
        if (invalidCards.size() > 10 || cards.size() > 1000) 
        {
            document.add(new Paragraph("VOID"));
        }
        
        else 
        {
            document.add(new Paragraph("Deck ID: " + deckID));
            document.add(new Paragraph("Total Energy Cost: " + totalCost));
            
            // Add histogram
            document.add(new Paragraph("Energy Cost Distribution:"));
            for (int i = 0; i <= 6; i++) 
            {
                document.add(new Paragraph(i + " Energy: " + histogram.get(i)));
            }
            
            // Add invalid cards
            if (!invalidCards.isEmpty()) 
            {
                document.add(new Paragraph("Invalid Cards:"));
                for (String card : invalidCards) 
                {
                    document.add(new Paragraph(card));
                }
            }
        }
        document.close();
    }
    
    public int getTotalCost() 
    {
        return totalCost;
    }
    
    public int getDeckID() 
    {
        return deckID;
    }
}