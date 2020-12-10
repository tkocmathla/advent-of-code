(ns aoc-clj.day10
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input
  (->> "day10_test.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(def input
  (->> "day10.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(defn p1 [jolts]
  (->> [[] [] 0 (set jolts)]
       (iterate
         (fn [[ones threes j js]]
           (let [one (js (+ j 1)), two (js (+ j 2)), three (js (+ j 3))]
             (cond
               one [(conj ones one) threes one (disj js one)]
               two [ones threes two (disj js two)]
               three [ones (conj threes three) three (disj js three)]))))
       (drop-while (comp seq last))
       first
       ((fn [[ones threes]] (* (count ones) (inc (count threes)))))))

(comment
  (= 35 (p1 test-input))
  (= 2592 (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
