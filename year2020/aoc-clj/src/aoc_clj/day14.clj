(ns aoc-clj.day14
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input
  (->> "day14_test.txt"
       io/resource
       slurp
       string/split-lines
       (map #(string/split % #" = "))))

(def input
  (->> "day14.txt"
       io/resource
       slurp
       string/split-lines
       (map #(string/split % #" = "))))

(defn to-mask [mask]
  (->> (reverse mask)
       (keep-indexed (fn [i c] (case c \0 [i bit-clear] \1 [i bit-set] nil)))
       flatten
       (apply hash-map)))

(defn to-val [mask x]
  (reduce (fn [x' [i f]] (f x' i)) x mask))

(defn p1 [cmds]
  (->> [cmds 0 {}]
       (iterate
         (fn [[[[loc x] & cmds'] mask mem]]
           (if (= "mask" loc)
             [cmds' (to-mask x) mem]
             [cmds' mask (assoc mem (re-find #"\d+" loc) (to-val mask (read-string x)))])))
       (drop-while (comp seq first))
       first
       last vals (reduce +)))

(comment
  (= 165 (p1 test-input))
  (= 14862056079561 (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
