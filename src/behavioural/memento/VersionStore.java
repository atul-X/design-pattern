package behavioural.memento;

import java.util.*;

//Here's the full problem with concrete data and a trace.
//Full problem statement
//You're building a versioned document store (think: a portfolio config or a security's reference data that changes over time). Two operations:
//
//put(docId, contents, timestamp) — save contents for docId at that timestamp.
// A docId can be written many times at different timestamps, so you keep every version.
//get(docId, timestamp) — return the contents that were "current" at timestamp:
// the version with the largest stored timestamp ≤ query timestamp. If the docId
// was never written, or the query is earlier than its first version, return null.
//
//Example data + trace
//put("AAPL", "price=150", 1)
//put("AAPL", "price=155", 5)
//put("AAPL", "price=160", 10)
//put("MSFT", "price=300", 4)
//So AAPL has versions at timestamps {1, 5, 10} and MSFT at {4}.
//CallReasoningReturnsget("AAPL", 1)exact hit at ts=1"price=150"get("AAPL", 3)largest ts ≤ 3
// is ts=1"price=150"get("AAPL", 5)exact hit at ts=5"price=155"get("AAPL", 7)largest ts ≤ 7 is ts=5"price=155"get("AAPL", 10)
// exact hit at ts=10"price=160"get("AAPL", 100)largest ts ≤ 100 is ts=10"price=160"get("AAPL", 0)query earlier than first version (ts=1)
// nullget("MSFT", 3)earlier than MSFT's first version (ts=4)nullget("TSLA", 5)docId never writtennull
//The key behavior to notice: get("AAPL", 7) returns the ts=5 version, not ts=10. You're finding the floor, not the nearest or the latest.
public class VersionStore {
    Map<String,TreeMap<Integer,String>> map=new HashMap<>();
    public void put(String docId, String contents, int timestamp) {
        map.computeIfAbsent(docId,k->new TreeMap<>()).put(timestamp,contents);
    }
    public String get(String docId, int timestamp) {
        TreeMap<Integer,String> version=map.get(docId);
        if (version==null){
            return null;
        }
        Map.Entry<Integer,String> e=version.floorEntry(timestamp);
        return e==null?null:e.getValue();
    }


//    Trades (ts, symbol, side, qty, price):
//    t=1  BUY  100 AAPL @ 150
//    t=2  BUY   50 MSFT @ 300
//    t=3  BUY   50 AAPL @ 160
//    t=4  SELL  30 AAPL @ 170
//    t=5  BUY   20 MSFT @ 310
//    t=6  BUY   10 TSLA @ 200
//    t=7  SELL  10 TSLA @ 220
    public class PortfolioTracker {

        enum Side { BUY, SELL }
        record Trade(long ts, String symbol, Side side, long qty, double price) {}

        private final List<Trade> log = new ArrayList<>(); // append-only source of truth

        public void record(Trade t) {
            log.add(t);
        }

        // Holdings projection as of a point in time (inclusive). O(events).
        public Map<String, Long> holdingsAsOf(long asOfTs) {
            Map<String, Long> pos = new HashMap<>();
            for (Trade t : log) {
                if (t.ts() > asOfTs) continue;
                long delta = t.side() == Side.BUY ? t.qty() : -t.qty();
                pos.merge(t.symbol(), delta, Long::sum);
            }
            pos.values().removeIf(q -> q == 0); // drop flat positions
            return pos;
        }

        public Map<String, Long> currentHoldings() {
            return holdingsAsOf(Long.MAX_VALUE);
        }

        // Aggregation via a pluggable strategy (here: market value from a price map).
        public double marketValue(long asOfTs, Map<String, Double> prices) {
            return holdingsAsOf(asOfTs).entrySet().stream()
                    .mapToDouble(e -> e.getValue() * prices.getOrDefault(e.getKey(), 0.0))
                    .sum();
        }
    }
}
