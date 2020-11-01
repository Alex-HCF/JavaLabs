package calc;

import calc.Data.TokenMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static calc.Data.TokenType.*;


/**
 *  Получает на вход выражение в виде корректного списка список типизированных токенов,
 *  переводит его в польскую нотацию и вычисляет результат, при необходимости получая данные из объекта data.
 */
public class Calc {
    Data data;

    public Calc(Data data) {
        this.data = data;
    }

    public Double compute(List<TokenMeta> correctTokenMetaList) {
        var correctTokenMetaListInPolishNot = toPolishNot(correctTokenMetaList);

        Stack<Double> res = new Stack<>();

        for (TokenMeta token : correctTokenMetaListInPolishNot) {
            String tokenName = token.getName();
            switch (token.getTokenType()) {
                case NUM -> res.push(Double.parseDouble(tokenName));
                case CONST -> res.push(data.getConst(tokenName).orElseThrow().getValue());
                case L_OPERATOR -> res.push(data.getLOperator(tokenName).orElseThrow().getCompute().compute(res.pop()));
                case BIN_OPERATOR -> {
                    Double arg2 = res.pop();
                    Double arg1 = res.pop();
                    res.push(data.getBinOperator(tokenName).orElseThrow().getCompute().compute(arg1, arg2));
                }
                case R_OPERATOR -> res.push(data.getROperator(tokenName).orElseThrow().getCompute().compute(res.pop()));
                case FUNC -> {
                    Data.Func func = data.getFunction(tokenName).orElseThrow();
                    Double[] args = new Double[func.getCountArgs()];
                    // Считываем со стека нужно количество аргументов функции
                    for (int i = func.getCountArgs() - 1; i >= 0; i--) {
                        args[i] = res.pop();
                    }
                    res.add(func.getCompute().compute(args));
                }
            }
        }

        return res.peek();
    }

    private List<TokenMeta> toPolishNot(List<TokenMeta> tokenMetaList) {
        Stack<TokenMeta> stack = new Stack<>();
        List<TokenMeta> res = new ArrayList<>();

        for (TokenMeta token : tokenMetaList) {
            switch (token.getTokenType()) {
                case NUM, CONST -> res.add(token);
                case FUNC -> stack.push(token);
                case COMMA -> {
                    while (stack.peek().getTokenType() != L_BRACKET) {
                        res.add(stack.pop());
                    }
                }
                case L_OPERATOR, R_OPERATOR, BIN_OPERATOR -> {
                    Data.Token op = data.getTokenByTokenMeta(token).orElseThrow();
                    // Пока на вершине находится оператор с большим приоритетом, перекладываем элементы из стека в выходной список
                    while (!stack.empty() &&
                            (stack.peek().getTokenType() == L_OPERATOR || stack.peek().getTokenType() == BIN_OPERATOR || stack.peek().getTokenType() == R_OPERATOR)
                            && data.getTokenByTokenMeta(stack.peek()).orElseThrow().getPriority() > op.getPriority()) {
                        res.add(stack.pop());
                    }
                    stack.add(token);
                }
                case L_BRACKET -> stack.push(token);
                case R_BRACKET -> {
                    while (stack.peek().getTokenType() != L_BRACKET) {
                        res.add(stack.pop());
                    }
                    stack.pop();
                }
                default -> throw new RuntimeException("Bad token " + token.getName());
            }

        }

        Collections.reverse(stack);
        res.addAll(stack);

        return res;
    }


}
