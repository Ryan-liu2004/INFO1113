package citadels;
//import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class SaveFileTest1 {
    public void println(Object str) {System.out.println(str);}


    @Test
    public void Test_1(){
        println("---------------------");
        println("Test_1");

        StringBuilder sb=new StringBuilder();
        sb.append("admin\nload 1\nt\n");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test selection phase
    }
    
    @Test
    public void Test_2(){
        println("---------------------");
        println("Test_2");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload 2\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test selection phase
    }
    @Test
    public void Test_assassintest1(){
        println("---------------------");
        println("Test_assassintest1");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload assassintest1\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of assassin
    }
    @Test
    public void Test_assassintest2(){
        println("---------------------");
        println("Test_assassintest2");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload assassintest2\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of assassin
    }
    @Test
    public void Test_magiciantest1(){
        println("---------------------");
        println("Test_magiciantest1");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload magiciantest1\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of magician
    }
    @Test
    public void Test_magiciantest2(){
        println("---------------------");
        println("Test_magiciantest2");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload magiciantest2\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of magician
    }
    
    @Test
    public void Test_warlordtest1(){
        println("---------------------");
        println("Test_warlordtest1");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload warlordtest1\nall\nend\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of magician
    }
    @Test
    public void Test_warlordtest2(){
        println("---------------------");
        println("Test_warlordtest2");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload warlordtest2\nall\nt\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of magician
    }
}
