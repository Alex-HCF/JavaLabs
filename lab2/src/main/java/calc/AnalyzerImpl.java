package calc;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Из выражения последовательно парсятся предопределенные токены и числа,
 * Полученный токен проверяется на соответствие текущей позиции,
 * При этом определяется его тип (число, константа, функция, левая скобка, правая скобка, запятая, бинарный оператор...),
 * Исходя из его типа происходит вызов функций для дальнейшего анализа
 * К примеру, после бинарного оператора можно ожидать число, функцию, левый унарный оператор и выражение в скобках
 * <p>
 * Код вышел не самый красивый
 */
public class AnalyzerImpl implements Analyzer {
    private final Data data;
    private final Set<String> defTokens;

    public AnalyzerImpl(Data data) {
        this.data = data;
        defTokens = data.getDefTokens();
    }

    @Override
    public List<Data.TokenMeta> getTokenMetaList(String inExpr) {
        inExpr = inExpr.replaceAll("\\s", "");

        if (inExpr.isEmpty()) {
            throw new RuntimeException("Empty expr");
        }

        List<Data.TokenMeta> tokenMetaList = new ArrayList<>();

        TokenIt tokenIt = new TokenIt(inExpr);
        tokenIt.next();

        if (expr(tokenMetaList, tokenIt) == Status.OK && tokenIt.getCountTokens() == tokenMetaList.size()) {
            return tokenMetaList;
        } else {
            throw new RuntimeException("Bad Expr: " + tokenMetaList.stream().map(Data.TokenMeta::getName).collect(Collectors.joining()) + "\uD83E\uDC17");
        }
    }

    boolean isEndOfSubExpr(TokenIt tokenIt) {
        return !tokenIt.hasNext() || data.getComma(tokenIt.next()).isPresent() || data.getRBracket(tokenIt.curr()).isPresent();
    }

    Status expr(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        if (lOp(tokenMetaList, tokenIt) == Status.OK || num(tokenMetaList, tokenIt) == Status.OK
                || constToken(tokenMetaList, tokenIt) == Status.OK || func(tokenMetaList, tokenIt) == Status.OK || bracket(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    Status num(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        if (!StringUtils.isNumeric(tokenIt.curr().replace(".", ""))) {
            return Status.ERROR;
        }
        tokenMetaList.add(new Data.TokenMeta(tokenIt.curr(), Data.TokenType.NUM));

        if (isEndOfSubExpr(tokenIt))
            return Status.OK;

        if (rOp(tokenMetaList, tokenIt) == Status.OK || binOp(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    Status func(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.Func> func = data.getFunction(tokenIt.curr());
        if (!func.isPresent()) {
            return Status.ERROR;
        }

        tokenMetaList.add(new Data.TokenMeta(tokenIt.curr(), Data.TokenType.FUNC));
        if (!tokenIt.hasNext())
            return Status.ERROR;
        tokenIt.next();

        Optional<Data.Bracket> brackets = data.getLBracket(tokenIt.curr());
        if (!brackets.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(brackets.get().getLeftBracket(), Data.TokenType.L_BRACKET));

        for (int i = 0; i < func.get().getCountArgs() - 1; i++) {
            if (!tokenIt.hasNext())
                return Status.ERROR;
            tokenIt.next();
            if (expr(tokenMetaList, tokenIt) == Status.ERROR) {
                return Status.ERROR;
            }
            if (!data.getComma(tokenIt.curr()).isPresent()) {
                return Status.ERROR;
            }
            tokenMetaList.add(new Data.TokenMeta(tokenIt.curr(), Data.TokenType.COMMA));
        }

        if (!tokenIt.hasNext())
            return Status.ERROR;
        tokenIt.next();

        if (expr(tokenMetaList, tokenIt) == Status.ERROR) {
            return Status.ERROR;
        }

        if (!tokenIt.curr().equals(brackets.get().getRightBracket())) {
            return Status.ERROR;
        }

        tokenMetaList.add(new Data.TokenMeta(tokenIt.curr(), Data.TokenType.R_BRACKET));

        if (isEndOfSubExpr(tokenIt))
            return Status.OK;

        if (rOp(tokenMetaList, tokenIt) == Status.OK || binOp(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    Status constToken(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.Const> constToken = data.getConst(tokenIt.curr());
        if (!constToken.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(constToken.get().getName(), Data.TokenType.CONST));

        if (isEndOfSubExpr(tokenIt))
            return Status.OK;

        if (rOp(tokenMetaList, tokenIt) == Status.OK || binOp(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    Status lOp(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.LOperator> lOperator = data.getLOperator(tokenIt.curr());
        if (!lOperator.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(lOperator.get().getName(), Data.TokenType.L_OPERATOR));

        if (isEndOfSubExpr(tokenIt))
            return Status.ERROR;

        if (func(tokenMetaList, tokenIt) == Status.OK || num(tokenMetaList, tokenIt) == Status.OK ||
                constToken(tokenMetaList, tokenIt) == Status.OK || bracket(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    Status rOp(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.ROperator> rOperator = data.getROperator(tokenIt.curr());
        if (!rOperator.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(rOperator.get().getName(), Data.TokenType.R_OPERATOR));

        if (isEndOfSubExpr(tokenIt))
            return Status.OK;

        if (rOp(tokenMetaList, tokenIt) == Status.OK || binOp(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    Status binOp(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.BinOperator> binOperator = data.getBinOperator(tokenIt.curr());
        if (!binOperator.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(binOperator.get().getName(), Data.TokenType.BIN_OPERATOR));

        if (isEndOfSubExpr(tokenIt))
            return Status.ERROR;

        if (expr(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    Status bracket(List<Data.TokenMeta> tokenMetaList, TokenIt tokenIt) {
        Optional<Data.Bracket> bracket = data.getLBracket(tokenIt.curr());
        if (!bracket.isPresent())
            return Status.ERROR;

        tokenMetaList.add(new Data.TokenMeta(bracket.get().getLeftBracket(), Data.TokenType.L_BRACKET));

        if (isEndOfSubExpr(tokenIt))
            return Status.ERROR;

        if (expr(tokenMetaList, tokenIt) == Status.OK && tokenIt.curr().equals(bracket.get().getRightBracket())) {
            tokenMetaList.add(new Data.TokenMeta(bracket.get().getRightBracket(), Data.TokenType.R_BRACKET));
        } else {
            return Status.ERROR;
        }

        if (isEndOfSubExpr(tokenIt))
            return Status.OK;

        if (rOp(tokenMetaList, tokenIt) == Status.OK || binOp(tokenMetaList, tokenIt) == Status.OK) {
            return Status.OK;
        }
        return Status.ERROR;
    }


    private enum Status {OK, ERROR}

    @Getter
    class TokenIt {
        private final String expr;
        private Integer pointer = 0;
        private String currToken;

        private int countTokens = 0;

        TokenIt(String expr) {
            this.expr = expr;
        }

        public String curr() {
            return currToken;
        }

        public String next() {
            if (!hasNext())
                throw new IndexOutOfBoundsException("next() must be called after hasNext()");

            char currChar = expr.charAt(pointer);
            String token;

            if (Character.isDigit(currChar)) {
                token = getNum();
            } else {
                token = getPredefinedToken();
            }

            pointer += token.length();
            currToken = token;
            countTokens++;

            return currToken;
        }

        public boolean hasNext() {
            return pointer < expr.length();
        }

        private String getNum() {

            if (pointer + 1 < expr.length() && expr.charAt(pointer) == '0' && Character.isDigit(expr.charAt(pointer + 1))) {
                throw new RuntimeException("Bad num: 0" + expr.charAt(pointer + 1));
            }

            StringBuilder num = new StringBuilder();

            boolean beforePointer = true;

            for (int i = pointer; i < expr.length() && (expr.charAt(i) == '.' || Character.isDigit(expr.charAt(i))); i++) {
                char currChar = expr.charAt(i);

                if (currChar == '.') {
                    if (beforePointer) {
                        beforePointer = false;
                    } else {
                        throw new RuntimeException("Bad num: two or more points");
                    }
                }
                num.append(currChar);
            }

            return num.toString();
        }

        private String getPredefinedToken() {

            String currToken = "";
            String lastValidToken = "";

            boolean contains = true;
            for (int i = pointer; i < expr.length() && contains; i++) {

                currToken += expr.charAt(i);

                contains = false;
                for (String str : defTokens) {
                    if (str.equals(currToken)) {
                        lastValidToken = currToken;
                        break;
                    } else if (str.contains(currToken)) {
                        contains = true;
                        break;
                    }
                }

            }

            if (lastValidToken.isEmpty()) {
                throw new RuntimeException("Bad token: " + currToken);
            }

            return lastValidToken;
        }

    }

}
