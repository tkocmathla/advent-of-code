(ns aoc-clj.day16
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [queue]]))

(defonce test-input (->> "day16_test.txt" io/resource slurp))
(defonce input (->> "day16.txt" io/resource slurp))

; ------------------------------------------------------------------------------

(defn parse [s] (mapv vec (string/split-lines s)))

(defn outer-locs [grid]
  (let [rows (count grid), cols (count (first grid))]
    (concat (map vector (repeat 0) (range cols) (repeat :s))
            (map vector (repeat (dec rows)) (range cols) (repeat :n))
            (map vector (range rows) (repeat 0) (repeat :e))
            (map vector (range rows) (repeat (dec cols)) (repeat :w)))))

(def next-dirs {[:n \.] [:n], [:n \/] [:e], [:n \\] [:w], [:n \|] [:n],    [:n \-] [:w :e]
                [:s \.] [:s], [:s \/] [:w], [:s \\] [:e], [:s \|] [:s],    [:s \-] [:w :e]
                [:e \.] [:e], [:e \/] [:n], [:e \\] [:s], [:e \|] [:n :s], [:e \-] [:e]
                [:w \.] [:w], [:w \/] [:s], [:w \\] [:n], [:w \|] [:n :s], [:w \-] [:w]})

(defn next-loc [y x dir]
  (let [dydx {:n [-1 0] :e [0 1] :s [1 0] :w [0 -1]}]
    (conj (mapv + [y x] (dydx dir)) dir)))

(defn next-locs [grid y x dir]
  (let [valid? (fn [[y' x' _]] (try (get-in grid [y' x']) (catch Exception _)))]
    (->> (next-dirs [dir (get-in grid [y x])])
         (map (partial next-loc y x))
         (filter valid?))))

(defn step [grid [q seen]]
  (let [nexts (apply next-locs grid (peek q))]
    [(into (pop q) (remove seen nexts)) (into seen nexts)]))

(defn solve [grid start]
  (->> (iterate (partial step grid) [(queue [start]) #{start}])
       (drop-while ffirst)
       first
       ((fn [[_ seen]] (count (into #{} (map butlast seen)))))))

(defn p1 [s] (solve (parse s) [0 0 :e]))

(defn p2 [s]
  (let [grid (parse s)]
    (apply max (map (partial solve grid) (outer-locs grid)))))

; ------------------------------------------------------------------------------

(comment
  (= 46 (p1 test-input))
  (= 8125 (p1 input))

  (= 51 (p2 test-input))
  (= 8489 (p2 input)))
