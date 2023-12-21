(ns aoc-clj.day21
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [medley.core :refer [find-first]]
   [aoc-clj.util :refer [queue]]))

(defonce test-input (->> "day21_test.txt" io/resource slurp))
(defonce input (->> "day21.txt" io/resource slurp))

; ------------------------------------------------------------------------------

(defn parse [s] (mapv vec (string/split-lines s)))

(defn neighbors [grid loc steps]
  (let [dydx [[-1 0] [0 1] [1 0] [0 -1]]]
    (when (pos? steps)
      (->> (map (partial map + loc) dydx)
           (filter (fn [loc'] (try (#{\.} (get-in grid loc')) (catch Exception _))))))))

(defn step [grid [q seen plots]]
  (let [[loc steps] (peek q)
        nbrs (neighbors grid loc steps)]
    [(into (pop q) (map vector (remove seen nbrs) (repeat (dec steps))))
     (into seen nbrs)
     (cond-> plots (odd? steps) (into nbrs))]))

(defn p1 [s start steps]
  (let [grid (parse s)]
    (->> (iterate (partial step grid) [(queue [[start steps]]) #{start} #{start}])
         (find-first (comp empty? first))
         last
         count)))

; ------------------------------------------------------------------------------

(comment
  (= 16 (p1 test-input [5 5] 6))
  (= 3841 (p1 input [65 65] 64)))
