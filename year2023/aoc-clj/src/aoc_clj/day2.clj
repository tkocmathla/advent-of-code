(ns aoc-clj.day2
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [medley.core :refer [map-vals]]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day2_test.txt" io/resource slurp))
(defonce input (->> "day2.txt" io/resource slurp))

(defn parse-game [i line]
  [(inc i)
   (->> (re-seq #"(\d+) (blue|red|green)" line)
        (map rest)
        (group-by second)
        (map-vals (fn [turns] (->> turns (map first) (map str-int) (apply max))))
        (into {}))])

(defn solve [f s]
  (reduce f 0 (map-indexed parse-game (string/split-lines s))))

(defn p1 [s]
  (let [f (fn [sum [id game]]
            (cond->> sum
              (and (<= (game "red" 0) 12) (<= (game "green" 0) 13) (<= (game "blue" 0) 14))
              (+ id)))]
    (solve f s)))

(defn p2 [s]
  (let [f (fn [sum [_ game]] (+ sum (apply * (vals game))))]
    (solve f s)))

(comment
  (= 8 (p1 test-input))
  (= 2006 (p1 input))

  (= 2286 (p2 test-input))
  (= 84911 (p2 input)))
