import java.io.FileNotFoundException;
import com.itextpdf.text.DocumentException;

public class SpireCalc 
{	
    public static void main(String[] args) 
    {
        Deck deck = new Deck();
        
        try 
        {
            deck.readFromFile("deck.txt");  // Adjust file name here
            String fileName = "SpireDeck " + deck.getDeckID() + ".pdf";
            deck.generatePDFReport(fileName);
            System.out.println("PDF Report generated: " + fileName);
        } 
        
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found: " + e.getMessage());
        } 
        
        catch (DocumentException e) 
        {
            System.out.println("Error creating PDF: " + e.getMessage());
        }
    }
}