import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

enum State {START, STRING_BODY, CHRAR_BODY, CHRAR_SPECIAL_SYMBOL, SPECIAL_SYMBOL, SUSPECTED_STRING, SUSPECTED_COMMENT,
    ONE_STRING_COMMENT, DEFAULT_COMMENT, STAR}

public class Main{
    public static void main(String[] args) throws Exception {
        FileReader reader = new FileReader("in.txt");
        FileWriter writer = new FileWriter("out.txt");
        Scanner sc = new Scanner(reader);

        State state = State.START;

        StringBuilder input = new StringBuilder();
        StringBuilder output = new StringBuilder();

        while (sc.hasNextLine()) {
            input.append(sc.nextLine() + "\n");
        }

        String str = input.toString();

        for(char symbol : str.toCharArray()){
            switch (state) {
                case START:
                    if (symbol == '\'') {
                        output.append(symbol);
                        state = State.CHRAR_BODY;
                    } else if (symbol == '\"') {
                        output.append(symbol);
                        state = State.STRING_BODY;
                    } else if (symbol == '/')
                        state = State.SUSPECTED_COMMENT;
                    else {
                        output.append(symbol);
                        state = State.START;
                    }
                    break;

                case CHRAR_BODY:
                    if (symbol == '\'') {
                        output.append(symbol);
                        state = State.START;
                    } else if (symbol == '\\') {
                        output.append(symbol);
                        state = State.CHRAR_SPECIAL_SYMBOL;
                    }
                    else {
                        output.append(symbol);
                        state = State.CHRAR_BODY;
                    }
                    break;

                case STRING_BODY:
                    if (symbol == '/') {
                        state = State.SUSPECTED_COMMENT;
                    }
                    else if (symbol == '\\') {
                        output.append(symbol);
                        state = State.SPECIAL_SYMBOL;
                    } else {
                        output.append(symbol);
                        state = State.STRING_BODY;
                    }
                    break;

                case SPECIAL_SYMBOL:
                    output.append(symbol);
                    state = State.STRING_BODY;
                    break;
                case CHRAR_SPECIAL_SYMBOL:
                    output.append(symbol);
                    state = State.CHRAR_BODY;
                    break;
                case SUSPECTED_STRING:
                    if (symbol == '/') {
                        output.append(symbol);
                        state = State.SUSPECTED_STRING;
                    }
                    if (symbol == '\"') {
                        output.append(symbol);
                        state = State.STRING_BODY;
                    }
                    if(symbol=='\\'){
                        state=State.SUSPECTED_COMMENT;
                    }
                    else {
                        output.append(symbol);
                        state = State.START;
                    }
                    break;

                case SUSPECTED_COMMENT:
                    if (symbol == '/') {
                        state = State.ONE_STRING_COMMENT;
                    } else if (symbol == '*') {
                        state = State.DEFAULT_COMMENT;
                    } else if (symbol == '\'') {
                        output.append("/" + symbol);
                        state = State.CHRAR_BODY;
                    }
                    else {
                        output.append("/");
                        state = State.START;
                    }
                    break;

                case ONE_STRING_COMMENT:
                    if (symbol == '\n') {
                        output.append(symbol);
                        state = State.START;
                    } else {
                        state = State.ONE_STRING_COMMENT;
                    }
                    break;

                case DEFAULT_COMMENT:
                    if (symbol == '*') {
                        state = State.STAR;
                    } else {
                        state = State.DEFAULT_COMMENT;
                    }
                    break;

                case STAR:
                    if (symbol == '/') {
                        state = State.START;
                    } else {
                        state = State.DEFAULT_COMMENT;
                    }
                    break;
            }
        }
        writer.write(output.toString());
        writer.close();
    }
}

