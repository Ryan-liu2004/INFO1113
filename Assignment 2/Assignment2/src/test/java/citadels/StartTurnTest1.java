package citadels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import javax.swing.plaf.synth.SynthStyle;

public class StartTurnTest1{

    static App app;
    static Map<String, Card> map;
    public static void println(Object str) {System.out.println(str);}

    @BeforeAll
    public static void Setup(){
        StringBuilder sb = new StringBuilder();
        sb.append("7\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);

        app=new App();
        app.Setup();
        map=new HashMap<>(app.cardMap);
        //System.setIn(originalIn);
    }

    public void SetPlayer1(Player p){
        p.ch=GameCharacter.Bishop;
        p.AIplayer=false;
        p.cards.clear();
        p.building.clear();
        p.gold=1000;
        p.num_built=0;
        p.cards.add(map.get("dragon gate".toLowerCase()));
        //Library
    }
    @Test
    public void startturnTest1(){
        println("startturnTest1");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);

        StringBuilder sb = new StringBuilder();
        sb.append("gold\ncards\ncollect card 1\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        //gold
        p.StartTurn();
        p.StartTurn();
        //test correctnees of draw cards
    }
    @Test
    public void startturnTest2(){
        println("startturnTest2");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);

        StringBuilder sb = new StringBuilder();
        sb.append("Cards\ncollect card 4\ncollect card 2\ncards\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        //error, draw a card
        p.building.add(map.get("observatory".toLowerCase()));
        println(p.cards.toString());
        p.StartTurn();
        p.building.add(map.get("library".toLowerCase()));
        p.StartTurn();
        //get 3 cards
        println(p.cards.toString());

        //test correctnees of library, observatory
    }
    @Test
    public void startturnTest3(){
        println("startturnTest3");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);
        p.AIplayer=true;
        StringBuilder sb = new StringBuilder();
        sb.append("gold\ncollect card 4\ncollect card 2\ncards\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        p.building.add(map.get("observatory".toLowerCase()));
        println(p.cards.toString());
        p.StartTurn();
        p.building.add(map.get("library".toLowerCase()));
        p.StartTurn();
        println(p.cards.toString());

        //test correctnees of library, observatory
    }


    public void SetPlayer2(Player p){
        p.AIplayer=true;
        p.cards.clear();
        p.building.clear();
        p.gold=0;
        p.num_built=0;
        p.building.add(map.get("school of magic".toLowerCase()));
        p.building.add(map.get("manor".toLowerCase()));
        p.building.add(map.get("tavern".toLowerCase()));
        p.building.add(map.get("temple".toLowerCase()));
        p.building.add(map.get("watchtower".toLowerCase()));
    }
    @Test
    public void startturnTest4(){
        println("startturnTest4");
        int num=2;
        Player p=app.player[num];
        SetPlayer2(p);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();

        p.ch=GameCharacter.Assassin;
        p.StartTurn();
        println("Assassin: "+p.gold);
        SetPlayer2(p);
        p.ch=GameCharacter.King;
        p.StartTurn();
        println("King: "+p.gold);
        SetPlayer2(p);
        p.ch=GameCharacter.Bishop;
        p.StartTurn();
        println("Bishop: "+p.gold);
        SetPlayer2(p);
        p.ch=GameCharacter.Merchant;
        p.StartTurn();
        println("Merchant: "+p.gold);
        SetPlayer2(p);
        p.ch=GameCharacter.Warlord;
        p.StartTurn();
        println("Warlord: "+p.gold);
        SetPlayer2(p);
        p.ch=GameCharacter.Architect;
        p.StartTurn();
        println("Architect: "+p.cards.toString());

        //test each character's the ability of gain extra gold
    }
    @Test
    public void startturnTest5(){
        println("startturnTest5");
        int num=2;
        Player p=app.player[num];
        SetPlayer2(p);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();

        p.building.clear();
        p.ch=GameCharacter.Assassin;
        p.StartTurn();
        println("Assassin: "+p.gold);
        p.ch=GameCharacter.King;
        p.StartTurn();
        println("King: "+p.gold);
        p.ch=GameCharacter.Bishop;
        p.StartTurn();
        println("Bishop: "+p.gold);
        p.ch=GameCharacter.Merchant;
        p.StartTurn();
        println("Merchant: "+p.gold);
        p.ch=GameCharacter.Warlord;
        p.StartTurn();
        println("Warlord: "+p.gold);
        p.ch=GameCharacter.Architect;
        p.StartTurn();
        println("Architect: "+p.cards.toString());
        //test each character's the ability of gain extra gold
    }
}

// gradle jar						Generate the jar file
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
