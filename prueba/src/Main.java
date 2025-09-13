import generated.pruebaParser;
import org.antlr.v4.runtime.*;
import java.io.IOException;
import java.util.ArrayList;
import generated.pruebaLexer;


public class Main {
    public static void main(String[] args) {
        pruebaLexer s;
        CharStream file;
        CommonTokenStream ct;
        pruebaParser p;

        try {
            file = CharStreams.fromFileName("tests.txt");
            s = new pruebaLexer(file);

            ct = new CommonTokenStream(s);
            p = new pruebaParser(ct);
            //parser.parseProgram();
            p.program();
            System.out.println("Compilación finalizada.");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}