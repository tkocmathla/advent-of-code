(ns aoc-clj.day6
 (:require
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string]))

(defonce input
  (->> "day6.txt"
       io/resource
       slurp
       string/split-lines))

(defn p1 [lines]
  (->> (partition-by empty? lines)
       (map (comp count set (partial apply concat)))
       (reduce +)))

(defn p2 [lines]
  (->> (partition-by empty? lines)
       (map (comp count (partial apply set/intersection) (partial map set)))
       (reduce +)))

(comment
  (= (p1 input))
  (= (p2 input)))
