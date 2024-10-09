package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            // the function works with only one argument. If there are more than one
            // argument, it will print an error message and exit.
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            // if there is one argument, it will run the file
            runFile(args[0]);
        } else {
            // if there is no argument, it will run the prompt
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        // read the file from the path provided
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // run the file
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError)
            System.exit(65);
        if (hadRuntimeError)
            System.exit(70);
    }

    public static void runPrompt() throws IOException {
        // create a buffer reader to read the input from the user
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        // run the prompt for
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();

            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    static void run(String source) {
        // create instance of Scanner (which is a class still not implemented)
        Scanner scanner = new Scanner(source);
        // Create a list of tokens using the scanner
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError)
            return;

        System.out.println(new AstPrinter().print(expression));
    }

    static void error(int line, String message) {
        report(line, "", "message");
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line] " + line + "] Error" + where + ": " + message);
    }
}