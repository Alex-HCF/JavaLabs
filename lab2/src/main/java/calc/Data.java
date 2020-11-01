package calc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Data {

    Set<String> getDefTokens();

    Optional<LOperator> getLOperator(String name);

    Optional<ROperator> getROperator(String name);

    Optional<BinOperator> getBinOperator(String name);

    Optional<Func> getFunction(String name);

    Optional<Const> getConst(String name);

    Optional<Comma> getComma(String name);

    Optional<Bracket> getLBracket(String name);

    Optional<Bracket> getRBracket(String name);

    List<LOperator> getLOperatorsList();

    List<ROperator> getROperatorsList();

    List<BinOperator> getBinOperatorsList();

    List<Func> getFunctionsList();

    List<Bracket> getBracketsList();

    List<Const> getConstsList();

    Optional<Token> getTokenByTokenMeta(TokenMeta tokenMeta);

    enum TokenType {NUM, CONST, L_BRACKET, R_BRACKET, COMMA, L_OPERATOR, BIN_OPERATOR, R_OPERATOR, FUNC, UNDEFINE}

    @AllArgsConstructor
    @Setter
    @Getter
    class TokenMeta {
        private String name;
        private TokenType tokenType = TokenType.UNDEFINE;
    }

    @AllArgsConstructor
    @Getter
    abstract class Token {
        private String name;
        private Integer priority;
    }

    @Getter
    class LOperator extends Token {
        private Compute compute;

        public LOperator(String name, Integer priority, Compute compute) {
            super(name, priority);
            this.compute = compute;
        }

        public interface Compute {
            Double compute(Double arg);
        }
    }

    @Getter
    class ROperator extends Token {
        private Compute compute;

        public ROperator(String name, Integer priority, Compute compute) {
            super(name, priority);
            this.compute = compute;
        }

        public interface Compute {
            Double compute(Double arg);
        }

    }

    @Getter
    class BinOperator extends Token {
        private Compute compute;

        public BinOperator(String name, Integer priority, Compute compute) {
            super(name, priority);
            this.compute = compute;
        }

        public interface Compute {
            Double compute(Double arg1, Double arg2);
        }
    }

    @Getter
    class Func extends Token {
        private Integer countArgs;
        private Compute compute;

        public Func(String name, Integer countArgs, Compute compute) {
            super(name, Integer.MAX_VALUE);
            this.countArgs = countArgs;
            this.compute = compute;
        }

        public interface Compute {
            Double compute(Double... args);
        }
    }

    @Getter
    class Const extends Token {
        private Double value;

        public Const(String name, Double value) {
            super(name, Integer.MAX_VALUE);
            this.value = value;
        }
    }

    @Getter
    class Bracket extends Token {
        String rightBracket;

        public Bracket(String LName, String RName) {
            super(LName, Integer.MAX_VALUE);
            this.rightBracket = RName;
        }

        public String getLeftBracket() {
            return this.getName();
        }

        public String getRightBracket() {
            return this.rightBracket;
        }
    }

    @Getter
    class Comma extends Token {
        public Comma(String name) {
            super(name, Integer.MAX_VALUE);
        }
    }
}
