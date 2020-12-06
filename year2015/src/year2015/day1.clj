(ns year2015.day1
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day1.txt"
       io/resource
       slurp))

(defn part1 [s]
  (reduce (fn [n x] ((case x \( inc \) dec) n)) 0 s))

(defn part2 [s]
  (->> (iterate (fn [[i n [c & s]]] [(inc i) ((case c \( inc \) dec) n) s]) [0 0 s])
       (drop-while (comp (complement neg?) second))
       ffirst))

(comment
  (= 138 (p1 input))
  (= 1771 (p2 input)))
