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

public class PlayerTest{

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
    }

    public void SetPlayer1(Player p){
        p.ch=GameCharacter.Magician;
        p.AIplayer=false;
        p.cards.clear();
        p.building.clear();
        p.gold=1000;
        p.num_built=0;
        p.is_killed=true;
        //Library
    }
    @Test
    public void startturnTest1(){
        println("startturnTest1");
        int num=2;
        Player p=app.player[num];
        SetPlayer1(p);

        StringBuilder sb = new StringBuilder();
        //sb.append("gold\ncards\ncollect card 1\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        p.com_action("redraw");
        p.AIplayer=true;
        p.com_action("redraw");
        p.is_killed=false;
        p.ch=GameCharacter.Assassin;
        p.com_action("redraw");
        p.AIplayer=false;
        p.com_action("redraw");
        //test correctnees of com_action
    }
}

// gradle jar						Generate the jar file
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
