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

public class StartTurnTest2{

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
        p.AIplayer=true;
        p.cards.clear();
        p.building.clear();
        p.gold=0;
        p.num_built=0;
        p.building.add(map.get("Laboratory".toLowerCase()));
        p.building.add(map.get("Smithy".toLowerCase()));
        p.cards.add(map.get("Market".toLowerCase()));
        p.cards.add(map.get("Tavern".toLowerCase()));
        p.cards.add(map.get("Fortress".toLowerCase()));
        p.cards.add(map.get("Keep".toLowerCase()));
        p.cards.add(map.get("Laboratory".toLowerCase()));
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
        Skill.Laboratory.Special(p, app);
        p.cards.add(map.get("Laboratory".toLowerCase()));
        Skill.Laboratory.Special(p, app);
        p.cards.clear();
        Skill.Laboratory.Special(p, app);

        //test correctness of Laboratory, AIplayer
    }
    @Test
    public void startturnTest2(){
        println("startturnTest2");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);
        p.AIplayer=false;
        StringBuilder sb = new StringBuilder();
        sb.append("Y\ndiscard 3\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        p.com_hand();
        Skill.Laboratory.Special(p, app);
        p.com_hand();
        //test correctness of Laboratory, human player
    }
    @Test
    public void startturnTest3(){
        println("startturnTest3");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);
        p.AIplayer=true;
        StringBuilder sb = new StringBuilder();
        sb.append("Y\ndiscard 3\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        p.gold=3;
        println(p.gold);
        p.com_hand();
        Skill.Smithy.Special(p, app);
        println(p.gold);
        p.com_hand();

        p.gold=10;
        println(p.gold);
        p.com_hand();
        Skill.Smithy.Special(p, app);
        println(p.gold);
        p.com_hand();
        //test correctness of Laboratory, AI player
    }
    @Test
    public void startturnTest4(){
        println("startturnTest4");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);
        p.AIplayer=false;
        StringBuilder sb = new StringBuilder();
        sb.append("Y\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        p.gold=3;
        println(p.gold);
        p.com_hand();
        Skill.Smithy.Special(p, app);
        println(p.gold);
        p.com_hand();
        p.gold=0;
        println(p.gold);
        p.com_hand();
        Skill.Smithy.Special(p, app);
        println(p.gold);
        p.com_hand();
        //test correctness of Smithy, human player
    }
    public void SetPlayer2(Player p){
        p.ch=GameCharacter.Bishop;
        p.AIplayer=false;
        p.cards.clear();
        p.building.clear();
        p.gold=0;
        p.num_built=0;
        p.building.add(map.get("Museum".toLowerCase()));
        p.cards.add(map.get("Smithy".toLowerCase()));
        p.cards.add(map.get("Market".toLowerCase()));
        p.cards.add(map.get("Tavern".toLowerCase()));
        p.cards.add(map.get("Fortress".toLowerCase()));
        p.cards.add(map.get("Keep".toLowerCase()));
        p.cards.add(map.get("Laboratory".toLowerCase()));
    }
    
    @Test
    public void startturnTest5(){
        println("startturnTest5");
        int num=2;
        Player p=app.player[num];
        SetPlayer2(p);
        StringBuilder sb = new StringBuilder();
        sb.append("select 6\nselect 5\n select 4\nselect 3\nselect 2\nq\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        p.gold=3;
        p.com_hand();
        Skill.Museum.Special(p, app);
        println(Skill.Museum.extraScore(p));
        p.com_hand();

        p.cards.clear();
        Skill.Museum.Special(p, app);
        p.cards.add(map.get("Keep".toLowerCase()));
        p.AIplayer=true;
        Skill.Museum.Special(p, app);
        //test correctness of Museum, human player
    }
}

// gradle jar						Generate the jar file
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
