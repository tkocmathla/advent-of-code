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

(def test-input2
  (->> "day10_test2.txt"
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
  (->> [0 1 0 (set jolts)]
       (iterate
         (fn [[ones threes j js]]
           (let [one (js (+ j 1)), three (js (+ j 3))]
             (cond
               one [(inc ones) threes one (disj js one)]
               three [ones (inc threes) three (disj js three)]))))
       (drop-while (comp seq last))
       ((fn [[[ones threes]]] (* ones threes)))))

(comment
  (= 35 (p1 test-input))
  (= 2592 (p1 input))
  (= 8 (p2 test-input))
  (= (p2 input)))
