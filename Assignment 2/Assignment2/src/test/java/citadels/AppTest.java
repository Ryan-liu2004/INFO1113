package citadels;

//import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthStyle;

public class AppTest{

    public void println(Object str) {System.out.println(str);}
    /*
    //@Test
    public void MainTest() {
        App.main(new String[]{});
    }*/
    /*
    @Test
    public void Test(){
        InputStream in= getClass().getResourceAsStream("/citadels/1");
        println(in);
        assertNotNull(in, "找不到 1 文件");
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        String firstLine = scanner.nextLine();
        assertEquals("7", firstLine);
    }*/

    @Test
    public void SetupTest(){
        String simulatedInput = "7\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        App app=new App();
        app.Setup();
        
        //System.setIn(originalIn);


        println(app.deck);
        //test run of deck
        int i;
        app.deck.dis_push(app.deck.draw());
        println(app.deck);
        for(i=1;i<=app.n;i++) app.player[i].com_hand();
    }


    @Test
    public void SaveLoadTest(){
        StringBuilder sb = new StringBuilder();
        sb.append("7\nload\nload 45454\nsave\nsave 3\ndebug\ndebug\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test run of save and load and debug command
    }
}

// gradle jar						Generate the jar file
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
