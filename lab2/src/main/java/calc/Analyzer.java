package calc;

import java.util.List;

public interface Analyzer {
    List<Data.TokenMeta> getTokenMetaList(String expr);
}
