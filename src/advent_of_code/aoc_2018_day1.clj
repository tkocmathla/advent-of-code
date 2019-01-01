(ns advent-of-code.aoc-2018-day1
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "2018-day1-input"
       io/resource
       slurp
       string/split-lines
       (map read-string)))

(defn part1 []
  (reduce + 0 input))

(defn part2 []
  (->> [0 #{} (cycle input)]
       (iterate
         (fn [[x seen [f & r]]]
           (let [next-x (+ x f)]
             (if (seen next-x)
               next-x
               [next-x (conj seen next-x) r]))))
       (drop-while vector?)
       first))

(comment
  (= 493 (part1))
  (= 413 (part2)))
