package calc;

import lombok.Getter;
import org.apache.commons.math.util.MathUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Хранилище предопределенных токенов
 */
@Getter
public class DataImpl implements Data {

    private static final Map<String, Const> consts = Stream.of(
            new Const("pi", 3.14),
            new Const("e", 2.71828))
            .collect(Collectors.toMap(Const::getName, Function.identity() ));



    private static final Map<String, Comma> comma = Stream.of(
            new Comma(","))
            .collect(Collectors.toMap(Comma::getName, Function.identity()));



    private static final Map<String, LOperator> leftUnOperators = Stream.of(
            new LOperator("-",4, (arg) -> -arg)).collect(Collectors.toMap(LOperator::getName, Function.identity() ));



    private static final Map<String, ROperator> rightUnOperators = Stream.of(
            new ROperator("!",5, (arg) -> {
                if (arg - arg.intValue() != 0)
                    throw new RuntimeException("factorial of non integer num");
                return MathUtils.factorialDouble(arg.intValue()); } )
                ).collect(Collectors.toMap(ROperator::getName, Function.identity() ));



    private static final Map <String, BinOperator> binOperators = Stream.of(
            new BinOperator("+",1, ((arg1, arg2) -> arg1 + arg2)),
            new BinOperator("-",1, ((arg1, arg2) -> arg1 - arg2)),
            new BinOperator("*", 3,((arg1, arg2) -> arg1 * arg2)),
            new BinOperator("/",3, ((arg1, arg2) -> arg1 / arg2)),
            new BinOperator("^",6, ((arg1, arg2) -> Math.pow(arg1, arg2)))

    ).collect(Collectors.toMap(BinOperator::getName, Function.identity() ));



    private static Map <String, Func> functions = Stream.of(
            new Func("sin", 1, (Double ... args) -> Math.sin(args[0])),
            new Func("cos", 1, (Double ... args) -> Math.cos(args[0])),
            new Func("tan", 1, (Double ... args) -> Math.tan(args[0])),
            new Func("cot", 1, (Double ... args) -> 1./Math.tan(args[0])),
            new Func("sumFromTo",2, (Double ... args) ->
            {
                if(args[0] - args[0].intValue() != 0 || args[1] - args[1].intValue() != 0)
                    throw new RuntimeException("func sumFromTo need Int val");
                Integer sum = 0;
                for(int i = args[0].intValue(); i < args[1]; i++)
                    sum+=i;
                return sum.doubleValue();
            })
    ).collect(Collectors.toMap(Func::getName, Function.identity() ));



    private static Map <String, Bracket> brackets = Stream.of(
            new Bracket("(", ")"),
            new Bracket("[", "]")
    ).collect(Collectors.toMap(Bracket::getName, Function.identity() ));



    @Override
    public Set<String> getDefTokens() {
        Set<String> defTokenList = new HashSet<>();

        Stream.of(consts.keySet(), leftUnOperators.keySet(), binOperators.keySet(), rightUnOperators.keySet(),
                functions.keySet(), consts.keySet(), comma.keySet(),
                brackets.keySet(), brackets.values().stream().map(Bracket::getRightBracket).collect(Collectors.toSet())).forEach(defTokenList::addAll);

        return defTokenList;
    }

    @Override
    public Optional<LOperator> getLOperator(String name) {
        return Optional.ofNullable(leftUnOperators.get(name));
    }

    @Override
    public Optional<ROperator> getROperator(String name) {
        return Optional.ofNullable(rightUnOperators.get(name));
    }

    @Override
    public Optional<BinOperator> getBinOperator(String name) {
        return Optional.ofNullable(binOperators.get(name));
    }

    @Override
    public Optional<Func> getFunction(String name) {
        return Optional.ofNullable(functions.get(name));
    }

    @Override
    public Optional<Const> getConst(String name) {
        return Optional.ofNullable(consts.get(name));
    }

    @Override
    public Optional<Comma> getComma(String name) {
        return Optional.ofNullable(comma.get(name));
    }

    @Override
    public Optional<Bracket> getLBracket(String name) {
        return Optional.ofNullable(brackets.get(name));
    }

    @Override
    public Optional<Bracket> getRBracket(String name) {
        return brackets.values().stream().filter((val) -> val.getRightBracket().equals(name)).findFirst();
    }

    @Override
    public List<LOperator> getLOperatorsList() {
        return new ArrayList<>(leftUnOperators.values());
    }

    @Override
    public List<ROperator> getROperatorsList() {
        return new ArrayList<>(rightUnOperators.values());
    }

    @Override
    public List<BinOperator> getBinOperatorsList() {
        return new ArrayList<>(binOperators.values());
    }

    @Override
    public List<Func> getFunctionsList() {
        return new ArrayList<>(functions.values());
    }

    @Override
    public List<Bracket> getBracketsList() {
        return new ArrayList<>(brackets.values());
    }


    @Override
    public List<Const> getConstsList() {
        return new ArrayList<>(consts.values());
    }


    @Override
    public Optional<Token> getTokenByTokenMeta(TokenMeta tokenMeta){
        switch (tokenMeta.getTokenType()){
            case L_OPERATOR:
                return Optional.ofNullable(leftUnOperators.get(tokenMeta.getName()));
            case R_OPERATOR:
                return Optional.ofNullable(rightUnOperators.get(tokenMeta.getName()));
            case BIN_OPERATOR:
                return Optional.ofNullable(binOperators.get(tokenMeta.getName()));
            case FUNC:
                return Optional.ofNullable(functions.get(tokenMeta.getName()));
            case CONST:
                return Optional.ofNullable(consts.get(tokenMeta.getName()));
            case COMMA:
                return Optional.ofNullable(comma.get(tokenMeta.getName()));
            case L_BRACKET:
                return Optional.ofNullable((brackets.get(tokenMeta.getName())));
            case R_BRACKET:
                return Optional.of(brackets.values().stream().filter((val) -> val.getRightBracket().equals(tokenMeta.getName())).findFirst().get());
        }
        throw new RuntimeException("Token type" + tokenMeta.getName() +" not found");
    }
}
