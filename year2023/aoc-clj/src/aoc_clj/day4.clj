(ns aoc-clj.day4
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day4_test.txt" io/resource slurp))
(defonce input (->> "day4.txt" io/resource slurp))

(defn parse-card [card]
  (let [[_ winning mine] (re-find #"^Card +\d+:(.+)\|(.+)" card)]
    [(re-seq #"\d+" winning) (re-seq #"\d+" mine)]))

(defn count-card [card]
  (let [[winning mine] (parse-card card)]
    (->> (set/intersection (into (hash-set) winning) (into (hash-set) mine))
         (filter seq)
         count)))

(defn p1 [s]
  (->> (map count-card (string/split-lines s))
       (map #(int (Math/pow 2 (dec %))))
       (reduce +)))

(defn p2 [s]
  (->> (map count-card (string/split-lines s))
       (reduce
         (fn [[sum copies] card-wins]
           (let [card-copies (or (first copies) 0)
                 rest-copies (when (seq copies) (subvec copies 1))
                 next-copies (vec (repeat card-wins (inc card-copies)))
                 indexes (range (max (count rest-copies) (count next-copies)))]
             [(+ sum card-copies 1), (mapv #(+ (get rest-copies % 0) (get next-copies % 0)) indexes)]))
         [0 []])
       first))

(comment
  (= 13 (p1 test-input))
  (= 17803 (p1 input))

  (= 30 (p2 test-input))
  (= 5554894 (p2 input)))
