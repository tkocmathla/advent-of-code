(ns aoc-clj.day11
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input
  (->> "day11_test.txt"
       io/resource
       slurp
       string/split-lines
       (mapv vec)))

(def input
  (->> "day11.txt"
       io/resource
       slurp
       string/split-lines
       (mapv vec)))

(defn valid-seats [grid]
  (set
    (for [y (range (count grid)), x (range (count (first grid)))
          :when (#{\# \L} (get-in grid [y x]))]
      [y x])))

(defn n-neighbors [grid coord]
  (->> [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]
       (map (partial map + coord))
       (remove (partial some neg?))
       (map (partial get-in grid))
       (keep (partial #{\#}))
       count))

(defn p1 [grid]
  (let [step (fn [g]
               (let [op (fn [seat]
                          (let [n (n-neighbors g seat)]
                            (or (and (= \L (get-in g seat)) (zero? n) [seat \#])
                                (and (= \# (get-in g seat)) (>= n 4) [seat \L]))))
                     ops (filter identity (map op (valid-seats grid)))
                     g' (reduce (fn [g [seat c]] (assoc-in g seat c)) g ops)]
                 (when (not= g g') g')))]
    (->> (iterate step grid)
         (take-while seq)
         last
         (mapcat (partial filter #{\#}))
         count)))

(comment
  (= 37 (p1 test-input))
  (= 2251 (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
