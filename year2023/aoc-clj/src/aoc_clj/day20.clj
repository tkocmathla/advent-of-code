(ns aoc-clj.day20
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [medley.core :refer [find-first map-kv-vals]]
   [aoc-clj.util :refer [queue]]))

(defonce test-input-1 (->> "day20_test1.txt" io/resource slurp))
(defonce test-input-2 (->> "day20_test2.txt" io/resource slurp))
(defonce input (->> "day20.txt" io/resource slurp))

; ------------------------------------------------------------------------------

(defn parse-line [line]
  (let [[l r] (string/split line #" -> ")
        [_ [kind] node] (re-find #"([&%]?)(.+)" l)
        kids (map keyword (string/split r #", "))]
    [(keyword node) {:kind kind :kids kids}]))

(defn parse [s] (into {} (map parse-line (string/split-lines s))))

(defn init [graph]
  (map-kv-vals
    (fn [node {:keys [kind] :as m}]
      (let [parents (reduce (fn [v [parent {:keys [kids]}]] (cond-> v (some #{node} kids) (conj parent))) [] graph)]
        (assoc m :state (case kind \% false \& (into {} (map vector parents (repeat false))) nil))))
    graph))

(defn propagate [from kind pulse state]
  (case kind
    \% (when (not pulse) (not (or state pulse)))
    \& (not (every? true? (vals (assoc state from pulse))))
    pulse))

(defn step [reached? [q graph nlo nhi _]]
  (let [[from to pin :as top] (peek q)
        {:keys [kind _ state]} (graph to)
        pout (propagate from kind pin state)]
    [(cond-> (pop q) (boolean? pout) (into (map vector (repeat to) (:kids (graph to)) (repeat pout))))
     (cond-> graph
       (and (not pin) (= kind \%)) (update-in [to :state] #(not (or % pin)))
       (= kind \&) (assoc-in [to :state] (assoc state from pin)))
     (cond-> nlo (false? pin) inc)
     (cond-> nhi (true? pin) inc)
     (reached? top)]))

(defn press [reached? [graph nlo nhi _]]
  (->> (iterate (partial step reached?) [(queue [[nil :broadcaster false]]) graph nlo nhi false])
       (find-first (fn [[q _ _ _ done?]] (or (empty? q) done?)))
       rest))

(defn p1 [s]
  (let [graph (init (parse s))
        [_ lo hi _] (nth (iterate (partial press (constantly false)) [graph 0 0 false]) 1000)]
    (* lo hi)))

(defn p2 [s]
  (let [graph (init (parse s))
        reached? (fn [node] (fn [[from _ pulse]] (and (= from node) (true? pulse))))
        solve-for (fn [node] (->> (iterate (partial press (reached? node)) [graph 0 0 false])
                                  (take-while (comp false? last))
                                  count))]
    (reduce * (map solve-for [:sb :nd :ds :hf]))))

; ------------------------------------------------------------------------------

(comment
  (= 32000000 (p1 test-input-1))
  (= 11687500 (p1 test-input-2))
  (= 791120136 (p1 input))
  (= 215252378794009 (p2 input)))
