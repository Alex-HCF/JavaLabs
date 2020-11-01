package calc;

import java.util.List;
import java.util.Set;

public class MyCalculator {
    private static Data data = new DataImpl();
    private static Analyzer analyzer = new AnalyzerImpl(data);
    private static Calc calc = new Calc(data);

    public static Double calculate(String expr){
        List<Data.TokenMeta> tokenMetaList = analyzer.getTokenMetaList(expr);
        return calc.compute(tokenMetaList);
    }

    private static Set<String> getAvailableTokens(){
        return data.getDefTokens();
    }

}
