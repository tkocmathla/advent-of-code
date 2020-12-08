(ns aoc-clj.day7
 (:require
    [clojure.java.io :as io]
    [clojure.core.match :refer [match]]
    [clojure.set :as set]
    [clojure.string :as string]))

(defonce input
  (->> "day7.txt"
       io/resource
       slurp
       string/split-lines))

(defn graph [lines]
  (reduce
    (fn [m s]
      (let [k (string/join #" " (take 2 (string/split s #" ")))
            xs (mapcat rest (re-seq #"\d+ (\w+ \w+)" s))]
        (reduce (fn [m' x] (update m' x conj k)) m xs)))
    {} lines))

(defn dfs
  ([g x] (dfs g (list x) #{}))
  ([g q seen]
   (let [x (peek q)]
     (case x
       nil seen
       (recur g (into (pop q) conj (set/difference (set (g x)) seen)) (conj seen x))))))

(defn p1 [lines]
  (dec (count (dfs (graph lines) "shiny gold"))))

(comment
  (= 248 (p1 input))
  (= (p2 input)))
