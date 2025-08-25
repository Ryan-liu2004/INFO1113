/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the Citadels game.
 * Manages the player's hand, constructed buildings, gold, and character state,
 * and provides command methods for various game actions.
 */
public class Player extends Super{
    public boolean AIplayer, is_killed, is_stolen;
    public int gold, num_built;
    public List<Card> cards, building;
    public GameCharacter ch;
    public App app;
    
    /**
     * Prints the given object without a newline, overriding Super.
     *
     * @param str the object to print
     */
    @Override
    public void print(Object str) {super.print(str);}
    /**
     * Prints the given object followed by a newline, overriding Super.
     *
     * @param str the object to print
     */
    @Override
    public void println(Object str) {super.println(str);}
    /**
     * Constructs a new player, defaults to AI control, and draws 4 starting cards.
     *
     * @param app reference to the main application instance
     */
    public Player(App app){
        AIplayer=true;
        gold=2;
        cards=new ArrayList<>();
        building=new ArrayList<>();
        for(int i=0;i<4;i++) drawCard();
        ch=null;
        this.app=app;
    }
    /**
     * Constructs a new human-player and draws 4 starting cards.
     *
     * @param app  reference to the main application instance
     * @param flag true if this player is AI-controlled, false if human
     */
    public Player(App app, boolean flag){
        this(app);
        AIplayer=flag;
    }
    /**
     * Sets the player's character for the round based on the character name.
     *
     * @param name the name of the character to assign
     */
    public void setCharacter(String name){
        ch=GameCharacter.findCharacter(name);
    }
    /**
     * Adds the specified card to the player's hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card){cards.add(card);}
    /**
     * Draws a card from the deck and adds it to the player's hand.
     */
    public void drawCard(){addCard(App.deck.draw());}
    /**
     * Searches for a card by name in the provided list.
     *
     * @param l    the list of cards to search
     * @param name the name of the card to find
     * @return the matching Card if found, otherwise null
     */
    public Card CardContain(List<Card> l, String name){
        for(Card card: l) if(card.name.equals(name)) return card;
        return null;
    }

    /**
     * Begins the player's turn: resets build count and allows resource collection or card draw.
     */
    public void StartTurn(){
        num_built=0;
        if(!AIplayer) print("Collect 2 gold or draw two cards and pick one [gold/cards].\n> ");
        String opt;
        if(AIplayer){
            if(gold<4) opt="gold";
            else if(cards.size()<3) opt="cards";
            else opt=new String[]{"gold", "cards"}[App.rand.nextInt(2)];
        }
        else opt=app.getInput_withoutcommand(new Object[]{"gold", "cards"});
        int num, i, j;

        //draw cards or gold
        if(opt.equals("gold")){
            println("Player collected 2 gold");
            gold+=2;
        }
        else{
            num=2;
            //Observatory
            if((boolean)Skill.Observatory.Special(this, app)){
                num=3;
                println("Observatory is triggered");
            }
            List<Card> tmp=new ArrayList<>();
            for(i=0;i<num;i++) tmp.add(App.deck.draw());
            //Library
            if(CardContain(building, "Library")!=null) println("Library is triggered");
            else{
                if(!AIplayer){
                    println("Pick one of the following cards: 'collect card <option>'.");
                    for(i=0;i<num;i++) println((i+1)+" "+tmp.get(i).toString());
                    print("> ");
                }
                Object[] l=new Object[num];
                for(i=0;i<num;i++) l[i]="collect card "+(i+1);
                if(AIplayer) opt="collect card 1";
                else opt=app.getInput_withoutcommand(l);
                j=Integer.parseInt(opt.split(" ")[2]);
                Card chosen=tmp.get(j-1);
                //recycle
                for(i=0;i<tmp.size();i++) if(i!=j-1) App.deck.dis_push(tmp.get(i));
                tmp.clear();
                tmp.add(chosen);
            }
            if(AIplayer) println("Player drawed "+tmp.size()+" card");
            for(Card card: tmp) {
                if(!AIplayer) println("You gained "+card.toString());
                addCard(card);
            }
        }

        //extra resource
        if(!is_killed) ch.ExtraResource(this);
        
        Skill[] after_start={Skill.Laboratory, Skill.Smithy, Skill.School_Of_Magic,
            Skill.Armory, Skill.Museum};
        for(Skill s: after_start) {
            Card card=CardContain(building, s.getname());
            if(card!=null) card.skill.Special(this, app);
        }
    }

    /**
     * Attempts to build a district card at the specified hand index.
     * Validates build limits, gold costs, and triggers related skill effects.
     *
     * @param place the index in the hand (0-based) of the card to build
     */
    public void OnBuilding(int place){
        //place [0,cards.size-1]
        if(is_killed){
            if(!AIplayer) println("build failed\nYou cannot build in this round because you were assassinated");
            return;
        }
        if(num_built==(ch==GameCharacter.Architect?3:1)) {
            println("build failed\nYou cannot build more districts in this round");
            return;
        }
        Card card=cards.get(place);
        //Factory
        int spend=card.cost-(card.color.equals("purple") && CardContain(building, "Factory")!=null?1:0);
        if(gold<spend){
            if(!AIplayer) println("build failed\nNot enough gold coins");
            return;
        }
        //Quarry
        Card Quarry=CardContain(building, "Quarry");
        if((Quarry==null && CardContain(building, card.name)!=null) || 
           (Quarry!=null && (boolean)Quarry.skill.Special(this, null))){
            if(!AIplayer) println("build failed\nNo more duplicate districts can be built");
            return;
        }
        println("Player built "+card.toString());
        building.add(card);
        cards.remove(card);
        gold-=spend;
        num_built++;


        Skill[] after_build={Skill.Haunted_City, Skill.Laboratory, Skill.Smithy, Skill.Graveyard, 
             Skill.Lighthouse, Skill.Armory, Skill.Museum, Skill.Bell_Tower, Skill.Throne_Room};
        for(Skill s: after_build) if(s==card.skill) card.skill.OnBuilding(this, app);
    }
    /**
     * Executes end-of-turn skill effects for the player.
     */
    public void EndTurn(){

        Skill[] end_turn={Skill.Poor_House, Skill.Park};
        for(Skill s: end_turn) {
            Card card=CardContain(building, s.getname());
            if(card!=null) card.skill.Special(this, app);
        }
    }

    /**
     * Executes a full play sequence for the player's turn,
     * including start, action, and end phases.
     */
    public void play_turn(){
        if(!AIplayer) println("Your turn.");
        StartTurn();
        if(!is_killed) ch.CharacterAbility(this, app);
        String opt;
        //int i, j;
        List<String> valid_command=new ArrayList<>(Arrays.asList(
            "hand", "gold", "build", "citadel", "list", "city", "action",
            "info", "end", "all", "save", "load", "help", "debug"));

            
        if(AIplayer){
            if(app.is_debug){
                print("Debug: ");
                for(Card card: cards) print(card.toString()+", ");
                println("");
            }
            if(ch==GameCharacter.Magician){
                if(App.rand.nextBoolean()) app.Player_command("action swap", this, null);
                else app.Player_command("action redraw", this, null);
            }
            if(ch==GameCharacter.Warlord){
                app.Player_command("action destroy", this, null);
            }
            if(cards.size()!=0) 
                app.Player_command("build "+(App.rand.nextInt(cards.size())+1), this, null);
            print("> ");
            app.getInput_t();
            if(!app.is_load_end) return;
            EndTurn();
            return;
        }
        
        do{
            print("> ");
            opt=app.getInput(valid_command.toArray());
            app.Player_command(opt, this, valid_command.toArray());
            if(!app.is_load_end) return;
        }while(!opt.equals("end"));

        EndTurn();
    }

    /**
     * Performs the player's character-specific action (Magician and Warlord),
     * unless the player has been assassinated this round.
     *
     * @param com the command or target identifier for the action
     */
    public void com_action(String com){
        if(is_killed){
            if(!AIplayer) println("Failed\nYou cannot use ability in this round because you were assassinated");
            return;
        }
        if(ch==GameCharacter.Magician) ch.CharacterAbility(this, app, com);
        else if(ch==GameCharacter.Warlord) ch.CharacterAbility(this, app, com);
        else if(!AIplayer) println("Your character no more action");
    }
    /**
     * Attempts to build a card from the player's hand into their city.
     * Validates the input index and processes the build action accordingly.
     *
     * @param place the string representing the index of the card to build
     */
    public void com_build(String place){
        int num;
        try{
            num=Integer.parseInt(place);
            if(num<1 || num>cards.size()) throw new NumberFormatException();
        }catch(NumberFormatException e){
            println("build failed, parameter error");
            return;
        }
        OnBuilding(num-1);
    }
    /**
     * Ends the player's turn and resets any temporary flags.
     */
    public void com_end(){}
    /**
     * Displays the player's current hand of cards in a numbered list.
     */
    public void com_hand(){
        println("You have "+gold+" gold. Cards in hand:");
        for(int i=0;i<cards.size();i++) println((i+1)+". "+cards.get(i).toString());
    }
    /**
     * Displays the amount of gold the player possesses.
     */
    public void com_gold(){
        println("You have "+gold+" gold.");
    }
    /**
     * Displays the list of buildings the player has constructed.
     */
    public void com_citadel(){
        for(int i=0;i<building.size();i++) println((i+1)+". "+building.get(i).toString());
    }
    /**
     * Displays a summary of the player's hand size, gold, and constructed city.
     */
    public void com_all(){
        print("cards="+cards.size()+" gold="+gold+" city=");
        if(building.size()>0) print(building.get(0).toString());
        for(int i=1;i<building.size();i++) print(", "+building.get(i).toString());
        println("");
    }
}