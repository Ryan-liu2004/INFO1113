/*
Haochen Liu
hliu0010
540243105
*/
package citadels;
/**
 * Represents a card in the Citadels game.
 * Each card has a name, color, cost, and other properties.
 */
public class Card {
     /**
     * The name of the card.
     * The color of the card (blue, red, green, yellow, purple).
     * The cost of building this card.
     * The ability of the card.
     */
    public String name, color, text;
    public int cost;
    public Skill skill;

    /**
     * Constructs a full card with specified properties.
     *
     * @param name  the name of the card
     * @param color the color category of the card
     * @param cost  the cost in gold to build this card
     * @param text  the descriptive text or ability of the card
     * @param skill the special Skill associated with the card
     */
    public Card(String name, String color, int cost, String text, Skill skill){
        this.name=name;
        this.color=color;
        this.cost=cost;
        this.text=text;
        this.skill=skill;
    }

     /**
     * Constructs a basic card without a skill and no descriptive text.
     *
     * @param name  the name of the card
     * @param color the color category of the card
     * @param cost  the cost in gold to build this card
     */
    public Card(String name, String color, int cost){
        this(name, color, cost, "", Skill.NonePurple);
    }

    /**
     * Returns a string representation of the card.
     * Format: "name [color cost]"
     *
     * @return formatted string with card details
     */
    @Override
    public String toString() {
        return String.format("%s [%s %d]", 
                              name, color, cost);
    }
}
