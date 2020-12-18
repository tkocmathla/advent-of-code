(ns aoc-clj.day17
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(defn parse [file] (->> file io/resource slurp string/split-lines))
(defonce test-input (parse "day17_test.txt"))
(defonce input (parse "day17.txt"))

(defn combos [cs]
  (if (empty? cs)
    '(())
    (for [xs (combos (rest cs)), x (first cs)]
      (cons x xs))))

(defn init-grid [lines dim]
  (let [pad (fn [xs] (take dim (concat xs (repeat 0))))]
    (->> (map-indexed (fn [y line] (map-indexed (fn [x c] [(pad [x y]) c]) line)) lines)
         (apply concat)
         (into {}))))

(defn deltas [dim] (remove #{(repeat dim 0)} (combos (repeat dim (range -1 2)))))
(def deltas-memo (memoize deltas))

(defn neighbors [dim coord] (map (partial mapv + coord) (deltas-memo dim)))

(defn solve [lines dim]
  (->> (init-grid lines dim)
       (iterate
         (fn [grid]
           (let [coords (distinct (mapcat (partial neighbors dim) (keys grid)))
                 grid' (reduce (fn [m coord] (cond-> m (nil? (m coord)) (assoc coord \.))) grid coords)
                 op (fn [[coord c]]
                      (let [n (count (keep (fn [coord'] (#{\#} (grid coord'))) (neighbors dim coord)))]
                        (cond
                          (and (= \# c) (not (<= 2 n 3))) [coord \.]
                          (and (= \. c) (= 3 n)) [coord \#])))]
             (reduce (fn [m [coord c]] (assoc m coord c)) grid (keep op grid')))))
       (drop 6)
       first
       (filter (comp #{\#} val))
       count))

(defn p1 [lines] (solve lines 3))
(defn p2 [lines] (solve lines 4))

(assert (= 26 (count (deltas-memo 3))))
(assert (= 80 (count (deltas-memo 4))))

(comment
  (= 112 (p1 test-input))
  (= 223 (p1 input))
  (= 848 (p2 test-input))
  (= 1884 (p2 input)))
