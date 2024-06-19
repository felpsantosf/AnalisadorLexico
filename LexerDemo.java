import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TokenType {
    NUMBER,
    IDENTIFIER,
    OPERATOR,
    PARENTHESIS,
    EOF
}

class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token[type=" + type + ", value='" + value + "']";
    }
}

class Lexer {
    private final String input;
    private int pos;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.currentChar = input.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos >= input.length()) {
            currentChar = '\0'; // EOF
        } else {
            currentChar = input.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private Token number() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return new Token(TokenType.NUMBER, result.toString());
    }

    private Token identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            result.append(currentChar);
            advance();
        }
        return new Token(TokenType.IDENTIFIER, result.toString());
    }

    private Token operator() {
        char op = currentChar;
        advance();
        return new Token(TokenType.OPERATOR, Character.toString(op));
    }

    private Token parenthesis() {
        char paren = currentChar;
        advance();
        return new Token(TokenType.PARENTHESIS, Character.toString(paren));
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                tokens.add(number());
                continue;
            }

            if (Character.isLetter(currentChar) || currentChar == '_') {
                tokens.add(identifier());
                continue;
            }

            if ("+-*/".indexOf(currentChar) != -1) {
                tokens.add(operator());
                continue;
            }

            if ("()".indexOf(currentChar) != -1) {
                tokens.add(parenthesis());
                continue;
            }

            // Tratamento de caracteres inesperados
            throw new RuntimeException("Erro léxico: " + currentChar);
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}

public class LexerDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a expressão para tokenizar: ");
        String input = scanner.nextLine();
        
        Lexer lexer = new Lexer(input);
        try {
            List<Token> tokens = lexer.tokenize();

            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
