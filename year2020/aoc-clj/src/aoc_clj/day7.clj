(ns aoc-clj.day7
 (:require
    [clojure.java.io :as io]
    [clojure.core.match :refer [match]]
    [clojure.set :as set]
    [clojure.string :as string]))

(defn parse [file]
  (->> file io/resource slurp string/split-lines))

(defonce input (parse "day7.txt"))
(defonce test-input (parse "day7_test.txt"))

(defn rgraph [lines]
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
  (dec (count (dfs (rgraph lines) "shiny gold"))))

; ------------------------------------------------------------------------------

(defn graph [lines]
  (reduce
    (fn [m s]
      (let [k (string/join #" " (take 2 (string/split s #" ")))
            xs (map (comp reverse rest) (re-seq #"(\d+) (\w+ \w+)" s))]
        (reduce (fn [m' [x n]] (update m' k conj [x (read-string n)])) m xs)))
    {} lines))

(defn dfs-sum [g n k]
  (if-let [ks (g k)]
    (reduce (fn [n' [k' v]] (+ n' v (* v (dfs-sum g 0 k')))) n ks)
    0))

(defn p2 [lines]
  (dfs-sum (graph lines) 0 "shiny gold"))

(comment
  (= 248 (p1 input))
  (= 32 (p2 test-input))
  (= 57281 (p2 input)))
