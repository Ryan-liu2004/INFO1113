/*
Haochen Liu
hliu0010
540243105
*/
package citadels;

/**
 * Main application class that controls the game flow for Citadels.
 * It initializes the game, manages the main loop, and handles user commands.
 */
import java.io.*;
import java.util.*;

public class App extends Super{
    public static long seed;
    public static Random rand;
    public Player[] player;
    public static int n, crown, round, end_building_num=8;
    private boolean is_end;
    public boolean is_load_end, is_debug;
    public static Deck deck;
    public final String input_message="Please enter valid input",
        AIturn_message="It is not your turn. Press t to continue with other player turns.";
    public final String[] default_command=new String[]{
            "hand", "gold", "citadel", "list", "city", "info", 
            "all", "help", "save", "load", "debug"};
    private Map<String, String> command;
    public Map<String, Card> cardMap;
    public Map<String, Skill> skillMap;
    public Map<GameCharacter, Integer> Character_player;

     /**
     * Prints the given object without a newline.
     *
     * @param str the object to print
     */
    @Override
    public void print(Object str) {super.print(str);}
    /**
     * Prints the given object followed by a newline.
     *
     * @param str the object to print
     */
    @Override
    public void println(Object str) {super.println(str);}
    /**
     * Reads and returns the next line of user input.
     *
     * @return the next line of input
     */
    @Override
    public String nextLine() {return super.nextLine();}
    
    /**
     * Prompts the user for input from a set of allowed options with a custom message.
     *
     * @param key     an array of allowed input options
     * @param message the prompt message to display
     * @return the user's vaild input
     */
    public String getInput(Object[] key, String message){
        String res;
        boolean input_flag=false;
        do{
            res=nextLine().toLowerCase();
            for(Object i: key)
            {
                String tmp=i.toString().toLowerCase();
                if((res.startsWith(tmp) && command.containsKey(res)) || 
                   (tmp.equals("save") && res.startsWith("save ")) || 
                   (tmp.equals("load") && res.startsWith("load ")) ||
                   (res.startsWith("build ")))
                    input_flag=true;
            }
            if(!input_flag) print(message+"\n> ");   
        }while(!input_flag);
        return res;
    }
    /**
     * Handles input in 't' mode, processing commands until loading is complete.
     */
    public void getInput_t() {
        String[] vaild_command=Arrays.copyOf(default_command, default_command.length + 1);
        vaild_command[vaild_command.length-1]="t";
        String opt;
        while(true){
            opt=getInput(vaild_command, AIturn_message);
            Player_command(opt, player[1], vaild_command);
            if(!is_load_end || opt.equals("t")) return;
            print("> ");
        }
    }
    /**
     * Prompts the user for input from a set of allowed options with a custom message.
     *
     * @param key     an array of allowed input options
     * @return the user's input
     */
    public String getInput(Object[] key) {return getInput(key, input_message);}
    /**
     * Prompts the user for input from a set of allowed options without parsing commands, using a custom message.
     *
     * @param key     an array of allowed input options
     * @param message the prompt message to display
     * @return the user's vaild input
     */
    public String getInput_withoutcommand(Object[] key, String message){
        String res;
        boolean input_flag=false;
        do{
            res=nextLine().toLowerCase();
            for(Object i: key)
            {
                String tmp=i.toString().toLowerCase();
                if(res.equals(tmp))
                    input_flag=true;
            }
            if(!input_flag) print(message+"\n> ");   
        }while(!input_flag);
        return res;
    }
    /**
     * Prompts the user for input from a set of allowed options without parsing commands, using a custom message.
     *
     * @param key     an array of allowed input options
     * @return the user's vaild input
     */
    public String getInput_withoutcommand(Object[] key) {return getInput_withoutcommand(key, input_message);}

     /**
     * Processes a player command and performs the corresponding action.
     *
     * @param comm the command keyword
     * @param p    the current player
     * @param args the command arguments
     */
    public void Player_command(String comm, Player p, Object[] args){
        String[] opt=comm.split(" ");
        if(opt[0].equals("city") || opt[0].equals("list")) 
            opt[0]="citadel";
        switch (opt[0]){
            case "t":
                break;
            case "hand":
                p.com_hand();
                break;
            case "gold":
                p.com_gold();
                break;
            case "build":
                if(opt.length>1) p.com_build(opt[1]);
                else println(command.getOrDefault(opt[0], ""));
                break;
            case "citadel":
                int num=1;
                if(opt.length>1) num=Integer.parseInt(opt[1]);
                println("Player "+num+" has built:");
                player[num].com_citadel();
                break;
            //case "list"
            //case "city"
            case "action":
                if(opt.length==1) println(command.getOrDefault(opt[0], ""));
                else p.com_action(opt[1]);
                break;
            case "info":
                if(opt.length==1) println(command.getOrDefault(opt[0], ""));
                else{
                    GameCharacter tmp=GameCharacter.findCharacter(opt[1]);
                    if(tmp==null) println("Information about "+opt[1]+": "+cardMap.get(opt[1]).text);
                    else println("Information about "+tmp.name()+": "+tmp.gettext());
                }
                break;
            case "end":
                println("You ended your turn.");
                p.com_end();
                break;
            case "all":
                print("Player 1 (you): ");
                player[1].com_all();
                for(int i=2;i<=n;i++){
                    println("");
                    print("Player "+i+": ");
                    player[i].com_all();
                }
                break;
            case "save":
                if(opt.length==1) println("save failed, missing parameters");
                else super.com_save(opt[1], seed);
                break;
            case "load":
                Long new_seed;
                if(opt.length==1) {println("load failed, missing parameters");break;}
                else {
                    println("Loading data...");
                    new_seed=super.com_load(opt[1]);
                }
                if(new_seed==null){
                    println("load failed, parameter error");
                    break;
                }
                seed=new_seed;
                is_load_end=false;
                break;
            case "help":
                println("Available commands:");
                for(int i=0;i<args.length;i++) println(command.getOrDefault(args[i], ""));
                break;
            case "debug":
                if(is_debug){
                    is_debug=false;
                    println("Disabled debug mode. You will no longer see all player's hands.");
                }
                else{
                    is_debug=true;
                    println("Enabled debug mode. You can now see all player's hands.");
                }
                break;
            default:
                break;
        }
    }

    /**
     * Executes the phases of a single turn.
     */
    private void Turn_Phase(){
        println("Character choosing is over, action round will now begin.\n"+
            "================================\n"+
            "TURN PHASE\n"+
            "================================");

        int i, num;
        GameCharacter ch=null;
        
        Character_player=new HashMap<>();
        for(i=1;i<=n;i++) {
            player[i].is_killed=false;
            player[i].is_stolen=false;
            Character_player.put(player[i].ch, i);
        }
        for(num=1;num<=8;num++){
            for(GameCharacter g: GameCharacter.values()) if(g.getnum()==num) ch=g;
            println(num+": "+ch.name());
            i=Character_player.getOrDefault(ch, -1);
            if(i==-1){
                print("No one is the "+ch.name()+"\n> ");
                getInput_t();
                if(!is_load_end) return;
                continue;
            }
            println("Player "+i+" is the "+ch.name());
            Player p=player[i];
            if(p.is_killed){
                println("Player "+i+" loses their turn because they were assassinated.");
                //king need to gain crown
                if(p.ch==GameCharacter.King) p.ch.CharacterAbility(p, this);
                if((boolean)Skill.Hospital.Special(p, this)){
                    println("Hospital is triggered");
                }
                else continue;
            }
            else if(p.is_stolen){
                println("The Thief steals "+p.gold+" gold from the "+p.ch.name()+" (Player "+i+")");
                player[Character_player.get(GameCharacter.Thief)].gold+=p.gold;
                p.gold=0;
            }
            p.play_turn();
            if(!is_load_end) return;
        }
    } 

    /**
     * Manages the character selection phase.
     */
    private void Selection_Phase(){
        println("================================\n"+
            "SELECTION PHASE\n"+
            "================================");
        List<GameCharacter> l=new ArrayList<>(Arrays.asList(GameCharacter.values()));
        GameCharacter faceup, facedown=l.remove(rand.nextInt(l.size()));  //remove mystery character
        println("A mystery character was removed.");

        int i, j, num;
        String opt;
        String[] valid_command=default_command;
    
        for(i=0;i<6-n;i++){
            do{
                faceup=l.get(rand.nextInt(l.size()));
            }while(faceup==GameCharacter.King);
            l.remove(faceup);
            println(faceup.name()+" was removed.");
        }

        println("Player "+crown+" is the crowned player and goes first.");
        for(i=crown;i<n+crown;i++){
            if(i==n+crown-1 && n==7) l.add(facedown);
            num=i>n?i-n:i;
            println("Player "+num+" chose a character.");
            if(player[num].AIplayer) {
                print("> ");
                getInput_t();
                if(!is_load_end) return;
                opt=l.get(rand.nextInt(l.size())).toString().toLowerCase();
            }
            else{
                do{
                    println("Choose your character or enter commands. Available characters:");
                    print(l.get(0).name());
                    for(j=1;j<l.size();j++) print(", "+l.get(j).name());
                    print("\n> ");
                    Object[] tmp=new Object[valid_command.length+l.size()];
                    for(j=0;j<valid_command.length;j++) tmp[j]=valid_command[j];
                    for(j=valid_command.length;j<tmp.length;j++) tmp[j]=l.get(j-valid_command.length);
                    opt=getInput(tmp);
                    Player_command(opt, player[num], valid_command);
                    if(!is_load_end) return;

                }while(!command.getOrDefault(opt, "").equals("Character"));
            }
            player[num].setCharacter(opt);
            l.remove(GameCharacter.findCharacter(opt));
        }
    }

    /**
     * Calculates final scores and displays game end results.
     */
    private void Game_End(){
        println("Game End!");
        int i, j, maxn;
        Player p;
        Map<String, Boolean> map; 
        int[] score=new int[n+1];
        for(i=1;i<=n;i++)
        {
            score[i]=0;
            p=player[i];
            map=new HashMap<>();
            for(Card card: p.building) {
                score[i]+=card.cost+card.skill.extraScore(p);
                String tmp=card.color;
                //Haunted City
                if(card.skill==Skill.Haunted_City) tmp=(String)card.skill.Special(p, this);
                map.put(tmp, Boolean.TRUE);
            }
            if(map.size()==5) score[i]+=3;
            if(p.building.size()>=end_building_num) score[i]+=2;
        }
        for(GameCharacter g: GameCharacter.values()){
            j=Character_player.getOrDefault(g, 0);
            if(j!=0 && player[j].building.size()>=end_building_num) score[j]+=2;
        }
        for(i=1;i<=n;i++) println("Player "+i+" has total "+score[i]+" points");
        maxn=1;
        for(i=2;i<=n;i++)
            if(score[i]>score[maxn]) maxn=i;
            else if(score[i]==score[maxn] && player[i].ch.ordinal()>player[maxn].ch.ordinal()) maxn=i;
        println("Congratulations! Player "+maxn+" wins!");
    }

    /**
     * Initializes card and skill mappings.
     */
    private void CardSetup(){
        InputStream input = App.class.getResourceAsStream("/citadels/cards.tsv");
        if(input == null) {
            println("cards.tsv not found in resources.");
            return;
        }
        Scanner filescan = new Scanner(input, "UTF-8");

        if (filescan.hasNextLine()) filescan.nextLine();
        skillMap=new HashMap<>();
        for(Skill skill: Skill.values()) skillMap.put(skill.getname().toLowerCase(), skill);
        while (filescan.hasNextLine()){
            String line=filescan.nextLine();
            String[] parts=line.split("\t");
            if (parts.length<4) super.throwRE("uncomplete card");

            int num=Integer.parseInt(parts[1]), cost=Integer.parseInt(parts[3]);
            while(num--!=0) 
                if(parts.length==5) 
                    deck.push(new Card(parts[0], parts[2], cost, 
                        parts[4], skillMap.getOrDefault(parts[0].toLowerCase(), Skill.NonePurple)));
                else
                    deck.push(new Card(parts[0], parts[2], cost));
        }
        deck.shuffle();
        cardMap=new HashMap<>();
        for(Card card: deck.deck) cardMap.put(card.name.toLowerCase(), card);
        filescan.close();
    }

    /**
     * Initializes player array and loading state.
     */
    private void SetPlayer(){
        print("Enter how many players [4-7]:\n> ");
        String tmp=getInput_withoutcommand(new Object[]{4, 5, 6, 7,"admin"}, "Please enter valid number");

        if(tmp.equals("admin")){
            super.mode="admin";
            tmp="4";
        }

        n=Integer.parseInt(tmp);
        player=new Player[n+1];
        println("Shuffling deck...\n"+
              "Adding characters...\n"+
              "Dealing cards...\n"+
              "Starting Citadels with "+
              n+
              " players...\n"+
              "You are player 1\n");
        for(int i=2;i<=n;i++) player[i]=new Player(this);
        player[1]=new Player(this, false);
        crown=rand.nextInt(n)+1;
    }

    /**
     * Sets up the command lookup table for parsing user inputs.
     */
    private void SetCommand(){
        //set command list
        int i;
        command.put("t", "t: Process turns");
        command.put("hand", "hand: shows cards in hand");
        command.put("gold", "gold [p]: shows gold of a player");
        command.put("build", "build <place in hand> : Builds a building into your city");
        //for(Card card: deck.deck) command.put("build "+card.name.toLowerCase(), "build");
        command.put("citadel", "citadel [p]: shows districts built by a player");
        for(i=1;i<=n;i++) command.put("citadel "+i, "citadel");
        command.put("list", "list [p]: shows districts built by a player");
        for(i=1;i<=n;i++) command.put("list "+i, "list");
        command.put("city", "city [p]: shows districts built by a player");
        for(i=1;i<=n;i++) command.put("city "+i, "city");
        command.put("action", 
        "action : Gives info about your special action and how to perform it\n"+
            "   Magician: Magician may do \"action swap\" to swap their hand with the given player\n"+
            "   Or do \"action redraw\" to choose to discard from their hand and redraw from the deck\n"+
            "   Warlord: Warlord map do \"action destroy\" to destroy one district by paying one fewer gold than its building cost");
        command.put("action swap", "swap");
        command.put("action redraw", "redraw");
        command.put("action destroy", "redraw");
        
        command.put("info", "info [name/H]: show information about a character or building");
        for(String card: cardMap.keySet()) command.put("info "+card.toLowerCase(), "info");
        for(GameCharacter ch: GameCharacter.values()) command.put("info "+ch.name().toLowerCase(), "info");
        for(GameCharacter ch: GameCharacter.values()) command.put(ch.name().toLowerCase(), "Character");
        command.put("end", "end: Ends your turn");
        command.put("all", "all: shows all current game info");
        command.put("save", "save: Saves the current game state info");
        command.put("load", "load: Load the game state");
        command.put("debug", "debug: Toggles debug mode");
        command.put("help", "help: Display the help message");
    }

    /**
     * Sets up the game, including random seed initialization, deck initialization, flags setup,
     * player setup, and command registration.
     */
    public void Setup(){
        rand=new Random(seed); 

        deck=new Deck();

        is_end=false;
        is_debug=false;
        is_load_end=true;
        round=0;
        
        CardSetup();

        int i;
        command=new HashMap<>();
        for(i=1;i<=7;i++) command.put(i+"", "i");

        SetPlayer();

        SetCommand();
    } 

    /**
     * Runs the game loop, including setup, selection phase, and turn phase.
     */
    public void gamestart(){
        Setup();
        do{
            round++;
            println("================================\n"+
            "ROUND "+round+"\n"+
            "================================");
            Selection_Phase();
            if(!is_load_end) return;
            Turn_Phase();
            if(!is_load_end) return;
            
            for(int i=1;i<=n;i++) if(player[i].building.size()>=end_building_num) is_end=true;
            if(is_end) return;
            println("Everyone is done, new round!");
        }while(true);
    }
    
    /**
     * Constructs a new App instance.
     */
    public App(){
        super();
    }

    /**
     * Entry point for running the Citadels game.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Random tmp=new Random();
        seed=tmp.nextLong();
        
        try{
            App app = new App();
            while(!app.is_end) app.gamestart();
            app.Game_End();
        } catch(RuntimeException e){
            e.printStackTrace();
        }
    }
}