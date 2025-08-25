/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

/**
 * Enum representing purple card skills in the Citadels game.
 * Each skill may trigger on building a district, special activation, extra resource collection,
 * scoring calculation, or cleanup on destruction.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public enum Skill{
    /**
     * Haunted City skill: For the purposes of victory points, the Haunted City is conisdered to be of the color of your choice.  
     * You cannot use this ability if you built it during the last round of the game
     */
    Haunted_City("Haunted City"){
        String color;
        int build_round;
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return chosen color
         */
        @Override
        public Object Special(Player p, Object x){
            if(build_round!=App.round) return this.color;
            return "purple";
        }
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            if(!p.AIplayer) {
                App app=(App) x;
                p.println("Haunted City is triggered");
                p.print("Choose a color for Haunted City: \"red\", \"yellow\", \"green\", \"blue\", \"purple\"\n> ");
                String[] tmp=new String[]{"red", "yellow", "green", "blue", "purple"};
                this.color=app.getInput_withoutcommand(tmp);
            }
            else this.color="purple";
            build_round=App.round;
        }
    },
    Keep("Keep"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            p.println("Keep is is triggered");
            return null;
        }
    },
    Laboratory("Laboratory"){
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            Special(p, x);
        }
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            String opt;
            int i, j;
            App app=(App) x;
            if(p.cards.isEmpty()) return null;
            if(!p.AIplayer) {
                p.print("Laboratory is triggered. Do you want to discard a card to gain 1 gold?: Y(yes), N(no)\n> ");
                opt=app.getInput_withoutcommand(new Object[]{"Y", "N"},"Please enter Y/N");
            }
            else
                if(p.cards.size()>5) opt="y";
                else opt="n";
            
            if(opt.equals("n")) return null;

            if(!p.AIplayer) {
                p.println("Choose a card to discard: discard <option>");
                for(i=0;i<p.cards.size();i++) p.println((i+1)+". "+p.cards.get(i).toString());
                p.print("> ");
                Object[] tmp=new Object[p.cards.size()];
                for(i=0;i<p.cards.size();i++) tmp[i]="discard "+(i+1);
                opt=app.getInput_withoutcommand(tmp);
            }
            else opt="discard 1";
            j=Integer.parseInt(opt.split(" ")[1])-1;
            Card card=p.cards.remove(j);
            App.deck.dis_push(card);
            p.gold++;
            p.println("Player discard a card to gain 1 gold");
            return null;
        }
    },
    Smithy("Smithy"){
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
           Special(p, x);
        }
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            String opt;
            App app=(App) x;
            if(p.gold<2) return null;
            if(!p.AIplayer) {
                p.print("Smithy is triggered. Do you want to pay 2 gold to draw 3 cards?: Y(yes), N(no)\n> ");
                opt=app.getInput_withoutcommand(new Object[]{"Y", "N"},"Please enter Y/N");
            }
            else
                if(p.gold>7) opt="y";
                else opt="n";
            
            if(opt.equals("n")) return null;
            p.drawCard();
            p.drawCard();
            p.drawCard();
            p.gold-=2;
            p.println("Player pay 2 gold to draw 3 cards");
            return null;
        }
    },
    Observatory("Observatory"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return whether having this card
         */
        @Override
        public Object Special(Player p, Object x){
            return p.CardContain(p.building, "Observatory")!=null;
        }
    },
    Graveyard("Graveyard"){
        Player owner; //who have this card
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
           owner=p;
        }
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return return true if pay gold to gain this card, otherwise null
         */
        @Override
        public Object Special(Player p, Object x){
            if(owner==null || owner.AIplayer || owner.ch==GameCharacter.Warlord || owner.gold<1) return null;
            p.print("Graveyard is triggered. Do you want to pay 1 gold to gain this card?: Y(yes), N(no)\n> ");
            String opt=p.app.getInput_withoutcommand(new Object[]{"Y", "N"},"Please enter Y/N");
            Card card=(Card)x;
            if(opt.equals("y")){
                p.println("You gain this card");
                owner.cards.add(card);
                owner.gold--;
                return Boolean.TRUE;
            }
            return null;
        }
        /**
         * Handles cleanup when the card is destroyed.
         */
        @Override
        public void destroyed(){
            owner=null;
        }
    },
    Dragon_Gate("Dragon Gate"){
        /**
         * Provides extra Score
         *
         * @return 2
         */
        @Override
        public int extraScore(Player p){return 2;}
    },
    University("University"){
        /**
         * Provides extra Score
         *
         * @return 2
         */
        @Override
        public int extraScore(Player p){return 2;}
    },
    //Library,
    //Great Wall,
    School_Of_Magic("School Of Magic"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            if(p.ch==GameCharacter.King || p.ch==GameCharacter.Bishop || 
                p.ch==GameCharacter.Merchant || p.ch==GameCharacter.Warlord){
                p.println("School Of Magic is triggered");
                p.gold++;
            }
            return null;
        }
    },
    Lighthouse("Lighthouse"){
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            App app=(App) x;

            p.println("Lighthouse is triggered");
            int i, j, num=App.deck.deck.size();
            if(!p.AIplayer){
                p.println("Pick one of the following cards from the deck: 'collect card <option>'.");
                for(i=0;i<num;i++) p.println((i+1)+" "+App.deck.deck.get(i).toString());
                p.print("> ");
            }
            String opt;
            Object[] l=new Object[num];
            for(i=0;i<num;i++) l[i]="collect card "+(i+1);
            if(p.AIplayer) opt="collect card 1";
            else opt=app.getInput_withoutcommand(l);
            j=Integer.parseInt(opt.split(" ")[2]);
            p.addCard(App.deck.deck.remove(j-1));
            p.println("Player gained 1 card");
            App.deck.shuffle();
        }
    },
    Armory("Armory"){
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            Special(p, x);
        }
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            if(p.AIplayer) return null;
            p.print("Armory is triggered. Do you want to destroy it in order to destory any other district?: Y(yes), N(no)\n> ");
            App app=(App) x;
            String opt=app.getInput_withoutcommand(new Object[]{"Y", "N"},"Please enter Y/N");
            if(opt.equals("n")) return null;

            int i, j;
            app.print("Who do you want to destroy? Choose a player from 2-"+App.n+":\n> ");
            Object[] tmp=new Object[App.n-1];
            for(i=2;i<=App.n;i++) tmp[i-2]=i;
            opt=app.getInput_withoutcommand(tmp);
            j=Integer.parseInt(opt);
            if(app.player[j].building.size()==0){
                if(!p.AIplayer) app.println("This player's city has not any district");
                return null;
            }

            int index=j;
            Player new_p=app.player[j];
            List<Card> goal=new_p.building;
            app.println("Please choose a district to destroy: \"destroy <option>\"");
            for(i=0;i<goal.size();i++) p.println((i+1)+". "+goal.get(i).toString());
            app.print("> ");
            tmp=new Object[goal.size()];
            for(i=0;i<goal.size();i++) tmp[i]="destroy "+(i+1);
            opt=app.getInput_withoutcommand(tmp);
            j=Integer.parseInt(opt.split(" ")[1])-1;

            Card card=goal.get(j);
            p.println("Player "+index+": "+card.toString()+" is destroyed");
            card.skill.destroyed();
            App.deck.dis_push(goal.remove(j));
            Card itself=p.CardContain(p.building, "Armory");
            App.deck.dis_push(itself);
            p.building.remove(itself);
            return null;
        }
    },
    Museum("Museum"){
        List<Card> under=new ArrayList<>();
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            Special(p, x);
        }
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            App app=(App) x;
            int i, j, num;
            String opt;
            if(p.cards.size()==0 || p.AIplayer) return null;

            p.println("Museum is triggered");
            num=0;
            while(true){
                if(p.cards.size()==0) break;
                p.println("You can place some cards under Museum, \"select <id>\" to place and \"q\" to exit.");
                for(i=0;i<p.cards.size();i++) p.println((i+1)+". "+p.cards.get(i).toString());
                p.print("> ");
                Object[] tmp=new Object[p.cards.size()+1];
                for(i=0;i<p.cards.size();i++) tmp[i]="select "+(i+1);
                tmp[p.cards.size()]="q";
                opt=app.getInput_withoutcommand(tmp);
                if(opt.equals("q")) break;
                j=Integer.parseInt(opt.split(" ")[1])-1;
                under.add(p.cards.remove(j));
                num++;
            }
            p.println("Player placed "+num+" card under Museum");
            return null;
        }
        /**
         * Provides extra Score
         *
         * @return how many cards under Museum
         */
        @Override
        public int extraScore(Player p){return under.size();}
        /**
         * Handles cleanup when the card is destroyed.
         * put all cards under Museum into discard deck
         */
        @Override
        public void destroyed(){
            for(Card card: under) App.deck.dis_push(card);
            under.clear();
        }
    },
    Imperial_Treasury("Imperial Treasury"){
        /**
         * Provides extra Score
         *
         * @return player's gold
         */
        @Override
        public int extraScore(Player p){return p.gold;}
    },
    Map_Room("Map Room"){
        /**
         * Provides extra Score
         *
         * @return the number of cards in player's hand
         */
        @Override
        public int extraScore(Player p){return p.cards.size();}
    },
    Wishing_Well("Wishing Well"){
        /**
         * Provides extra Score
         *
         * @return how many other purple cards
         */
        @Override
        public int extraScore(Player p){
            int sum=-1;
            for(Card card: p.cards) if(card.color.equals("purple")) sum++;
            return sum;
        }
    },
    Quarry("Quarry"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return return true if there are duplicates, otherwise false
         */
        @Override
        public Object Special(Player p, Object x){
            return p.building.size()-(new HashSet<>(p.building)).size()>0;
        }
    },
    Poor_House("Poor House"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            if(p.gold==0){
                p.println("Poor House is triggered");
                p.gold++;
            }
            return null;
        }
    },
    Bell_Tower("Bell Tower"){
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            p.println("Bell Tower is triggered");
            App.end_building_num=7;
        }
        /**
         * Handles cleanup when the card is destroyed.
         */
        @Override
        public void destroyed(){
            App.end_building_num=8;
        }
    },
    //Factory,
    Park("Park"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return null
         */
        @Override
        public Object Special(Player p, Object x){
            if(p.cards.size()==0){
                p.println("Park is triggered");
                p.drawCard();
                p.drawCard();
            }
            return null;
        }
    },
    Hospital("Hospital"){
        /**
         * Activates the special ability.
         *
         * @param p the player invoking the skill
         * @param x additional context for the activation
         * @return return true if having Hospital, otherwise false
         */
        @Override
        public Object Special(Player p, Object x){
            return p.CardContain(p.building, "Hospital")!=null;
        }
    },
    Throne_Room("Throne Room"){
        private Player p;    //who have this card
        /**
         * Called when a player builds a district to apply the effect.
         *
         * @param p the player building a district
         * @param x additional context
         */
        @Override
        public void OnBuilding(Player p, Object x){
            this.p=p;
        }
        /**
         * Provides extra resources
         *
         * @return null
         */
        @Override
        public Object extraResource(){
            if(p!=null) {
                p.println("Throne_Room is triggered");
                p.gold++;
            }
            return null;
        }
        /**
         * Handles cleanup when the card is destroyed.
         */
        @Override
        public void destroyed(){p=null;}
    },
    NonePurple("");
    private String name;
    /**
     * Constructs a Skill enum constant with the specified display name.
     *
     * @param name the display name of the skill
     */
    private Skill(String name){
        this.name=name;
    }
    /**
     * Returns the display name of this skill.
     *
     * @return skill name
     */
    public String getname(){return name;}
    /**
     * Triggered when a player builds a district to execute skill logic.
     * Default implementation is nothing.
     *
     * @param p the player building a district
     * @param x additional context object
     */
    public void OnBuilding(Player p, Object x){}
    /**
     * Activates the skill's special ability.
     * Default implementation returns null.
     *
     * @param p the player invoking the ability
     * @param x additional parameters
     * @return result of the special ability, or null if none
     */
    public Object Special(Player p, Object x){return null;}
    /**
     * Provides extra resources when certain conditions are met.
     * Default implementation returns null.
     *
     * @return extra resource value, or null if none
     */
    public Object extraResource() {return null;}
    /**
     * Calculates additional score granted by this skill at game end.
     * Default implementation returns 0.
     *
     * @param p the player for whom to calculate extra score
     * @return extra score points
     */
    public int extraScore(Player p){return 0;}
    /**
     * Handles cleanup when a district associated with the skill is destroyed.
     * Default implementation is nothing
     */
    public void destroyed(){}
}