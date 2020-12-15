(ns aoc-clj.day15
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input [0, 3, 6])
(def input [9, 3, 1, 0, 8, 4])

(defn p1 [nums]
  (let [turn (inc (count nums))]
    (->> [turn (last nums) (into {} (map (fn [x i] [x (list i)]) (butlast nums) (iterate inc 1)))]
         (iterate
           (fn [[i x m]]
             (if (nil? (m x))
               [(inc i) 0 (assoc m x (list (dec i)))]
               [(inc i) (- (dec i) (peek (m x))) (update m x conj (dec i))])))
         (drop-while (comp (partial >= 2020) first))
         first second)))

(comment
  (= 436 (p1 test-input))
  (= 371 (p1 input))
  (= (p2 test-input2))
  (= (p2 input)))
