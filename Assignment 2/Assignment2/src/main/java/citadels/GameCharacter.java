/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

import java.util.List;
/**
 * Enum representing the characters in the Citadels game.
 * Each character has a unique turn order number and a special ability description.
 */
public enum GameCharacter{
    /**
     * Assassin (1): Select another character to kill; the killed character loses their turn.
     */
    Assassin(1,"Select another character whom you wish to kill. The killed character loses their turn."){
         /**
         * Assassin ability: choose and assassinate another character, preventing them from taking their turn.
         *
         * @param p   the player invoking the ability
         * @param app the main application
         */
        @Override
        public void CharacterAbility(Player p, App app){
            String opt;
            int i, j;
            if(!p.AIplayer){
                p.print("Who do you want to kill? Choose a character from 2-8:\n> ");
                Object[] tmp=new Object[7];
                for(i=2;i<=8;i++) tmp[i-2]=i;
                opt=app.getInput_withoutcommand(tmp);
                j=Integer.parseInt(opt);
                p.println("You chose to kill the "+GameCharacter.values()[j-1]);
            }
            else j=App.rand.nextInt(7)+2;
            Integer goal=app.Character_player.getOrDefault(GameCharacter.values()[j-1], null);
            if(goal!=null) app.player[goal].is_killed=true;
        }
    },
    /**
     * Thief (2): Steal gold from another character.
     */
    Thief(2, "Select another character whom you wish to rob. When a player reveals that character to "+ 
                "take his turn, you immediately take all of his gold. You cannot rob the Assassin or the killed "+
                "character."){
        /**
         * Thief ability: steal all gold from the chosen character.
         *
         * @param p   the player invoking the ability
         * @param app the main application
         */
        @Override
        public void CharacterAbility(Player p, App app){
            String opt;
            int i, j;
            if(!p.AIplayer){
                p.print("Who do you want to steal from? Choose a character from 3-8:\n> ");
                Object[] tmp=new Object[6];
                for(i=3;i<=8;i++) tmp[i-3]=i;
                opt=app.getInput_withoutcommand(tmp);
                j=Integer.parseInt(opt);
                p.println("You chose to steal from the "+GameCharacter.values()[j-1]);
            }
            else j=App.rand.nextInt(6)+3;
            Integer goal=app.Character_player.getOrDefault(GameCharacter.values()[j-1], null);
            if(goal!=null) app.player[goal].is_stolen=true;
        }
    },
    /**
     * Magician (3): Exchange cards with another player or discard any number of cards and draw the same.
     */
    Magician(3, "Can either exchange their hand with another player’s, or discard any number of "+
                "district cards face down to the bottom of the deck and draw an equal number of cards from the "+
                "district deck (can only do this once per turn)."){
        public int last_skill_round=0;   //record the time of skill usage

        /**
         * Exchange cards
         *
         * @param p    the player invoking the ability
         * @param app  the main application
         */
        private void swap(Player p, App app){
            String opt;
            int i, j;
            if(!p.AIplayer){
                app.print("Who do you want to swap from? Choose a player from 2-"+App.n+":\n> ");
                Object[] tmp=new Object[App.n-1];
                for(i=2;i<=App.n;i++) tmp[i-2]=i;
                opt=app.getInput_withoutcommand(tmp);
                j=Integer.parseInt(opt);
            }
            else{
                j=App.rand.nextInt(App.n)+1;
                //maximum cards
                j=1;
                for(i=2;i<=App.n;i++) if(app.player[i].cards.size()>app.player[j].cards.size()) j=i;
                if(app.player[j]==p) {redraw(p,app);return;}
            }
            app.println("Player "+j+" exchange hands with the Magician");
            List<Card> tmp=app.player[j].cards;
            app.player[j].cards=p.cards;
            p.cards=tmp;
        }
        /**
         * Discard and redraw
         *
         * @param p    the player invoking the ability
         * @param app  the main application
         */
        private void redraw(Player p, App app){
            if(p.cards.size()==0) return;
            int i, j, num=0;
            String opt;
            if(!p.AIplayer) {
                while(true){
                    if(p.cards.size()==0) break;
                    app.println("Please choose a card to discard: \"discard <option>\" or \"q\" to finish discard");
                    for(i=0;i<p.cards.size();i++) p.println((i+1)+". "+p.cards.get(i).toString());
                    p.print("> ");
                    Object[] tmp=new Object[p.cards.size()+1];
                    for(i=0;i<p.cards.size();i++) tmp[i]="discard "+(i+1);
                    tmp[p.cards.size()]="q";
                    opt=app.getInput_withoutcommand(tmp);
                    if(opt.equals("q")) break;
                    j=Integer.parseInt(opt.split(" ")[1])-1;
                    num++;
                    App.deck.dis_push(p.cards.remove(j));
                }
            }
            else{
                App.deck.dis_push(p.cards.remove(0));
                num=1;
            }
            app.println("Player discard "+num+" cards and draw "+num+" cards");
            for(i=0;i<num;i++) p.drawCard();
        }
        /**
         * Magician ability with command: exchange cards with another player or discard and redraw.
         *
         * @param p    the player invoking the ability
         * @param app  the main application
         * @param com  additional command
         */
        @Override
        public void CharacterAbility(Player p, App app, String com){
            if(last_skill_round==App.round){
                app.println("You cannot use ability again");
                return;
            }
            if(com.equals("swap")) swap(p, app);
            else if(com.equals("redraw")) redraw(p, app);
            else return;
            last_skill_round=App.round;
        }
    },
     /**
     * King (4): Collect income from all yellow (noble) districts and take first pick next round.
     */
    King(4, "Gains one gold for each yellow (noble) district in their city. They receive the crown token "+ 
                "and will be the first to choose characters on the next round."){
        /**
         * King extra resource: collect gold income from noble districts.
         *
         * @param p the player invoking the ability
         */
        @Override
        public void ExtraResource(Player p){
            int sum=0;
            for(Card card: p.building) if(card.color.equals("yellow")) sum++;
            if(sum!=0) p.println("King collected "+sum+" gold from noble district");
            p.gold+=sum;
        }
        /**
         * King ability: Take the crown.
         *
         * @param p   the player invoking the ability
         * @param app the main application
         */
        @Override
        public void CharacterAbility(Player p, App app){
            app.println("Player take the crown");
            App.crown=app.Character_player.get(GameCharacter.King);
            Skill.Throne_Room.extraResource();
        }
    },
    /**
     * Bishop (5): Collect income from all religious districts and is protected from the Warlord.
     */
    Bishop(5, "Gains one gold for each blue (religious) district in their city. Their buildings cannot be "+
                "destroyed by the Warlord, unless they are killed by the Assassin."){
        /**
         * Bishop extra resource: collect gold income from religious districts.
         *
         * @param p the player invoking the ability
         */
        @Override
        public void ExtraResource(Player p){
            int sum=0;
            for(Card card: p.building) if(card.color.equals("blue")) sum++;
            if(sum!=0) p.println("Bishop collected "+sum+" gold from religious district");
            p.gold+=sum;
        }
    },
    /**
     * Merchant (6): Collect extra gold and gains one extra gold.
     */
    Merchant(6, "Gains one gold for each green (trade) district in their city. Gains one extra gold."){
        /**
         * Merchant extra resource: gain extra gold and collect gold income from trade districts.
         *
         * @param p the player invoking the ability
         */
        @Override
        public void ExtraResource(Player p){
            int sum=0;
            for(Card card: p.building) if(card.color.equals("green")) sum++;
            if(sum!=0) p.println("Merchant collected "+sum+" gold from trade district");
            p.println("Merchant gained extra 1 gold");
            p.gold+=sum+1;
        }
    },
    /**
     * Architect (7): Draw two extra cards and may build up to three districts.
     */
    Architect(7, "Gains two extra district cards. Can build up to 3 districts per turn."){
        /**
         * Architect extra resource: draw two additional cards.
         *
         * @param p the player invoking the ability
         */
        @Override
        public void ExtraResource(Player p){
            p.println("Architect gained extra 2 cards");
            p.drawCard();
            p.drawCard();
        }
    },
    /**
     * Warlord (8): Destroy a district by paying gold equal to its cost minus 1.
     */
    Warlord(8, "Gains one gold for each red (military) district in their city. You can destroy one district "+
                "of your choice by paying one fewer gold than its building cost. You cannot destroy a district in a " +
                "city with 8 or more districts."){
        /**
         * Warlord extra resource: collect gold income from military districts.
         *
         * @param p the player invoking the ability
         */
        @Override
        public void ExtraResource(Player p){
            int sum=0;
            for(Card card: p.building) if(card.color.equals("red")) sum++;
            if(sum!=0) p.println("Warlord collected "+sum+" gold from military district");
            p.gold+=sum;
        }   
        /**
         * Warlord ability: destroy an opponent's district at reduced cost.
         *
         * @param p   the player invoking the ability
         * @param app the main application
         */
        @Override
        public void CharacterAbility(Player p, App app, String com){
            if(!com.equals("destroy")) return;
            String opt;
            int i, j;
            if(!p.AIplayer){
                app.print("Who do you want to destroy? Choose a player from 1-"+App.n+":\n> ");
                Object[] tmp=new Object[App.n];
                for(i=1;i<=App.n;i++) tmp[i-1]=i;
                opt=app.getInput_withoutcommand(tmp);
                j=Integer.parseInt(opt);
            }
            else{
                j=1;
                for(i=2;i<=App.n;i++) if(app.player[j].ch!=GameCharacter.Warlord && 
                    app.player[j].building.size()<app.player[i].building.size()) j=i;
            }
            if(app.player[j].building.size()==0){
                if(!p.AIplayer) app.println("This player's city has not any district");
                return;
            }
            if(app.player[j].building.size()>=App.end_building_num){
                if(!p.AIplayer) app.println("You cannot destroy a completed city");
                return;
            }

            //execute delete
            int index=j;
            Player new_p=app.player[j];
            List<Card> goal=new_p.building;
            
            //Bishop
            if(new_p.ch==GameCharacter.Bishop && !new_p.is_killed){
                if(!p.AIplayer) p.println("You cannot destroy Bishop city");
                return;
            }

            if(!p.AIplayer){
                app.println("Please choose a district to destroy: \"destroy <option>\"");
                for(i=0;i<goal.size();i++) p.println((i+1)+". "+goal.get(i).toString());
                app.print("> ");
                String[] tmp=new String[goal.size()];
                for(i=0;i<goal.size();i++) tmp[i]="destroy "+(i+1);
                opt=app.getInput_withoutcommand(tmp);
                j=Integer.parseInt(opt.split(" ")[1])-1;
            }
            else{
                //minimum cost
                j=0;
                for(i=1;i<goal.size();i++) if(goal.get(i).cost<goal.get(j).cost) j=i;
            }

            Card card=goal.get(j);
            //Keep
            if(card.skill==Skill.Keep){
                card.skill.Special(new_p, app);
                return;
            }
            //Great Wall
            int spend=new_p.CardContain(new_p.building, "Great Wall")!=null?1:0;
            spend+=card.cost-1;

            if(spend>p.gold){
                if(!p.AIplayer) p.println("You don't have enough gold");
                return;
            }

            if(p.AIplayer && p.gold-spend<3) return;
            p.println("Player "+index+": "+card.toString()+" is destroyed");
            p.gold-=spend;
            card.skill.destroyed();

            if(Skill.Graveyard.Special(p, card)!=null) goal.remove(j); 
            else App.deck.dis_push(goal.remove(j));
        }
    };
    
    private int num;
    private String text;
    /**
     * Constructs a GameCharacter with the specified order and ability description.
     *
     * @param num  the turn order number of this character
     * @param text the description of the character's ability
     */
    private GameCharacter(int num, String text){
        this.num=num;
        this.text=text;
    }
    /**
     * Applies extra resource gain for characters that collect outside of actions.
     * Default implementation does nothing.
     *
     * @param p the player invoking the ability
     */
    public void ExtraResource(Player p){}
    /**
     * Executes the character's ability.
     * Default implementation does nothing.
     *
     * @param p    the player invoking the ability
     * @param app  the main application
     */
    public void CharacterAbility(Player p, App app){}
    /**
     * Executes the character's ability with a command string parameter.
     * Some characters require additional command input.
     * Default implementation does nothing.
     *
     * @param p    the player invoking the ability
     * @param app  the main application
     * @param com  additional command or target identifier
     */
    public void CharacterAbility(Player p, App app, String com){}

    /**
     * Returns the turn order number of this character.
     *
     * @return the character's order number
     */
    public int getnum() {return num;}
    /**
     * Returns the description text of this character's ability.
     *
     * @return the ability description
     */
    public String gettext() {return text;}
    /**
     * Finds a GameCharacter by its name (case-insensitive).
     *
     * @param name the name of the character to find
     * @return the matching GameCharacter or null if not found
     */
    public static GameCharacter findCharacter(String name){
        for(GameCharacter i: GameCharacter.values())
            if (i.name().equalsIgnoreCase(name)) return i;
        return null;
    }
}
