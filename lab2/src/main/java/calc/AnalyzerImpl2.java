//package calc;
//
//import java.util.*;
//
//public class AnalyzerImpl2 implements Analyzer{
//    private final static String exprFJ2 = "E"; // reserve keyword
//
//    private Data data;
//    private Set<String> defTokens;
//
//    Map<String, Data.TokenType> rules = new HashMap<>();
//
//    public AnalyzerImpl2(Data data) {
//        this.data = data;
//        this.defTokens = data.getDefTokens();
//    }
//
//    @Override
//    public List<Data.TokenMeta> getTokenMetaList(String expr) {
//        List<Data.TokenMeta> tokenMetaList = new ArrayList<>();
//        TokenIt tokenIt = new TokenIt(expr.replaceAll("\\s", ""));
//        return tokenMetaList;
//    }
//
//    private void generateRules (){
//        data.getLOperatorsList().forEach((lOperator -> {
//            defTokens.add(lOperator.getName());
//            rules.put(lOperator.getName() + exprFJ2, Data.TokenType.L_OPERATOR);
//        }));
//
//        data.getBinOperatorsList().forEach((binOperator -> {
//            defTokens.add(binOperator.getName());
//            rules.put(exprFJ2 + binOperator.getName() + rules, Data.TokenType.BIN_OPERATOR);
//        }));
//
//        data.getROperatorsList().forEach((rOperator -> {
//            defTokens.add(rOperator.getName());
//            rules.put(exprFJ2 + rOperator, Data.TokenType.R_OPERATOR);
//        }));
//
//        data.getConstsList().forEach((aConst -> {
//            defTokens.add(aConst.getName());
//            rules.put(aConst.getName(), Data.TokenType.CONST);
//        }));
//
//        data.getFunctionsList().forEach((func -> {
//            defTokens.add(func.getName());
//            StringBuilder stringBuilder = new StringBuilder();
//            data.getBracketsList().forEach((bracket -> {
//                defTokens.add(bracket.getLeftBracket());
//                defTokens.add(bracket.getRightBracket());
//                rules.put(bracket.getLeftBracket() + exprFJ2 + bracket.getRightBracket(), Data.TokenType.L_BRACKET); ///
//
//                stringBuilder.append(func.getName());
//                stringBuilder.append(bracket.getLeftBracket());
//                for(int i = 0; i < func.getCountArgs() - 1; i++) {
//                    stringBuilder.append(exprFJ2);
//                }
//                stringBuilder.append(exprFJ2);
//                stringBuilder.append(bracket.getRightBracket());
//                rules.put(stringBuilder.toString(), Data.TokenType.FUNC);
//                stringBuilder.setLength(0);
//            }));
//        }));
//
//    }
//
//    private void analiz(TokenIt tokenIt){
//        List<Data.TokenMeta> tokenMetaList = new ArrayList<>();
//        Stack<Data.TokenMeta> stack = new Stack<>();
//
//        while (tokenIt.hasNext()){
//            Data.TokenMeta tokenMeta = new Data.TokenMeta(tokenIt.next());
//
//            stack.push(tokenMeta);
//
//            int lastSucsesInd = -1;
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = stack.size() - 1; i >= 0; i++){
//                stringBuilder.append(stack.get(i));
//                if (rules.containsKey(stringBuilder.toString())){
//                    lastSucsesInd = i;
//                }
//            }
//
//
//        }
//    }
//
//
//
//
//    class TokenIt {
//        private String expr;
//        private Integer point = 0;
//
//        TokenIt(String expr){
//            this.expr = expr;
//        }
//
//        public String next (){
//            if(!hasNext())
//                return "";
//
//            char currChar = expr.charAt(point);
//            String token;
//
//            if (Character.isDigit(currChar)) {
//                token = getNum();
//            } else {
//                token = getConstToken();
//            }
//
//            point += token.length();
//            return token;
//        }
//
//        public boolean hasNext (){
//            return point == expr.length();
//        }
//
//        public String getNum() {
//
//            if (point + 1 < expr.length() && expr.charAt(point) == '0' && Character.isDigit(expr.charAt(point + 1))) {
//                throw new RuntimeException("Bad num: 0" + expr.charAt(point + 1));
//            }
//
//            StringBuilder num = new StringBuilder();
//
//            boolean beforePoint = true;
//
//            for (int i = point; i < expr.length() && (expr.charAt(i) == '.' || Character.isDigit(expr.charAt(i))); i++) {
//                char currChar = expr.charAt(i);
//
//                if (currChar == '.') {
//                    if (beforePoint) {
//                        beforePoint = false;
//                    } else {
//                        throw new RuntimeException("Bad num: two or more points");
//                    }
//                }
//                num.append(currChar);
//            }
//
//            return num.toString();
//        }
//
//        public String getConstToken() {
//
//            String currToken = "";
//            String lastValidToken = "";
//
//            boolean contains = true;
//            for (int i = point; i < expr.length() && contains; i++) {
//
//                currToken += expr.charAt(i);
//
//                contains = false;
//                for (String str : defTokens) {
//                    if (str.equals(currToken)) {
//                        lastValidToken = currToken;
//                        break;
//                    } else if (str.contains(currToken)) {
//                        contains = true;
//                        break;
//                    }
//                }
//
//            }
//
//            if (lastValidToken.isEmpty()) {
//                throw new RuntimeException("Bad token: " + currToken);
//            }
//
//            return lastValidToken;
//        }
//
//    }
//
//}
