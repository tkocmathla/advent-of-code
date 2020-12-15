(ns aoc-clj.day15
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input [0, 3, 6])
(def input [9, 3, 1, 0, 8, 4])

(defn solve [nums end]
  (let [turn (inc (count nums))]
    (->> [turn (last nums) (into {} (map vector (butlast nums) (iterate inc 1)))]
         (iterate
           (fn [[i x m]]
             (if-let [prev (m x)]
               [(inc i) (- (dec i) prev) (assoc m x (dec i))]
               [(inc i) 0 (assoc m x (dec i))])))
         (drop-while (comp (partial >= end) first))
         first second)))

(defn p1 [nums] (solve nums 2020))
(defn p2 [nums] (solve nums 30000000))

(comment
  (= 436 (p1 test-input))
  (= 371 (p1 input))
  (= 175594 (p2 test-input))
  (= 352 (p2 input)))
