/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the draw and discard piles for the Citadels game.
 * Provides methods to push cards to piles, shuffle, draw, and represent the deck state.
 */
public class Deck extends Super{
    public List<Card> deck, discard_deck;

     /**
     * Prints the given object followed by a newline.
     *
     * @param str the object to print
     */
    @Override
    public void println(Object str) {super.println(str);}

     /**
     * Constructs an empty Deck with separate draw and discard piles.
     */
    public Deck(){
        deck=new ArrayList<>();
        discard_deck=new ArrayList<>();
    }
    /**
     * Adds a card to the discard pile.
     *
     * @param card the card to discard
     */
    public void dis_push(Card card){
        discard_deck.add(card);
    }
    /**
     * Adds a card to the draw pile.
     *
     * @param card the card to add to the deck
     */
    public void push(Card card){
        deck.add(card);
    }
    /**
     * Shuffles the draw pile using the application's random generator.
     */
    public void shuffle() {
        Collections.shuffle(deck, App.rand);
    }
     /**
     * Draws the top card from the draw pile. If the draw pile is empty,
     * it refills by shuffling the discard pile back into the draw pile.
     *
     * @return the drawn Card
     */
    public Card draw(){
        Card tmp=deck.remove(0);
        if(deck.isEmpty()) {
            deck.addAll(discard_deck);
            discard_deck.clear();
            shuffle();
            println("The deck is empty and has been reshuffled from the discard deck.");
        }
        return tmp;
    }
    /**
     * Returns a formatted string representation of both draw and discard piles.
     *
     * @return a listing of cards in the draw pile and the discard pile
     */
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Deck:\n");
        for (Card i: deck) {
            sb.append(i).append("\n");
        }
        sb.append("Discard Deck:\n");
        for (Card i: discard_deck) {
            sb.append(i).append("\n");
        }
        return sb.toString();
    }
}