(ns aoc-clj.day1
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defonce test-input-1 (->> "day1_test1.txt" io/resource slurp))
(defonce test-input-2 (->> "day1_test2.txt" io/resource slurp))
(defonce input (->> "day1.txt" io/resource slurp))

(def digit-strs {"1" 1 "2" 2 "3" 3 "4" 4 "5" 5 "6" 6 "7" 7 "8" 8 "9" 9})
(def name-strs {"one" 1 "two" 2 "three" 3 "four" 4 "five" 5 "six" 6 "seven" 7 "eight" 8 "nine" 9})
(def all-strs (into digit-strs name-strs))

(defn first-num [nums s] (apply min-key (fn [num] (or (string/index-of s num) Integer/MAX_VALUE)) nums))
(defn last-num [nums s] (apply max-key (fn [num] (or (string/last-index-of s num) Integer/MIN_VALUE)) nums))

(defn solve [s nums]
  (->> s
       string/split-lines
       (map (juxt (partial first-num nums) (partial last-num nums)))
       (map (partial map all-strs))
       (map (partial apply str))
       (map #(Integer/parseInt %))
       (reduce +)))

(defn p1 [s] (solve s (keys digit-strs)))
(defn p2 [s] (solve s (keys all-strs)))

(comment
  (= 142 (p1 test-input-1))
  (= 54081 (p1 input))

  (= 281 (p2 test-input-2))
  (= 54649 (p2 input)))
