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

public class BuildingTest{

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
    public void SetPlayer(Player p){
        p.ch=GameCharacter.Architect;
        p.AIplayer=false;
        p.cards.clear();
        p.building.clear();
        p.gold=1000;
        p.num_built=0;
        p.building.add(map.get("harbor".toLowerCase()));
        p.building.add(map.get("trading post".toLowerCase()));
        p.building.add(map.get("docks".toLowerCase()));
    
        p.cards.add(map.get("dragon gate".toLowerCase()));
        p.cards.add(map.get("graveyard".toLowerCase()));
        p.cards.add(map.get("university".toLowerCase()));
        p.cards.add(map.get("library".toLowerCase()));
        p.cards.add(map.get("harbor".toLowerCase()));
        p.cards.add(map.get("harbor".toLowerCase()));
    }
    @Test
    public void buildingTest1(){
        println("buildingTest1");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        p.gold=1;
        p.com_build("1");
        //gold is not enough
    }
    @Test
    public void buildingTest2(){
        println("buildingTest2");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        p.ch=GameCharacter.Assassin;
        p.com_build("1");
        p.com_build("1");
        //cannot build more in one round
    }
    @Test
    public void buildingTest3(){
        println("buildingTest3");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        p.com_build("1");
        p.com_build("1");
        p.com_build("1");
        p.com_build("1");
        //for Architect, cannot build 4 cards in one round
    }
    @Test
    public void buildingTest4(){
        println("buildingTest4");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        p.gold=4;
        p.com_build("1");
        p.building.add(map.get("factory"));
        p.gold=5;
        p.com_build("1");
        //test correctness of factory
    }
    @Test
    public void buildingTest6(){
        println("buildingTest6");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        p.gold=5;
        p.com_build("1");
        p.building.add(map.get("factory"));
        p.gold=5;
        p.com_build("1");
         //test correctness of factory
    }
    @Test
    public void buildingTest5(){
        println("buildingTest5");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);
        
        p.com_build("5");
        p.building.add(map.get("quarry".toLowerCase()));
        p.com_build("5");
        p.com_build("5");
        // //test correctness of quarry
    }
    @Test
    public void buildingTest7(){
        println("buildingTest7");
        int num=4;
        Player p=app.player[num];
        SetPlayer(p);

        StringBuilder sb = new StringBuilder();
        sb.append("collect card 1\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        p.gold=8;
        p.cards.clear();
        p.cards.add(map.get("Lighthouse".toLowerCase()));
        p.com_hand();
        p.com_build("1");
        p.com_hand();
        
         //test correctness of Lighthouse

        p.gold=8;
        p.AIplayer=true;
        p.cards.clear();
        p.building.clear();
        p.cards.add(map.get("Lighthouse".toLowerCase()));
        p.com_build("1");
        //test correctness of Lighthouse
    }
}

// gradle jar						Generate the jar file
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
