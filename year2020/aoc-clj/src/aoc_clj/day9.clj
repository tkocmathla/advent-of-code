(ns aoc-clj.day9
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def test-input
  (->> "day9_test.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(def input
  (->> "day9.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(defn p1 [nums]
  (->> (drop 25 nums)
       (map-indexed
         (fn [i x]
           (let [ys (set (subvec nums i (+ i 25)))]
             [x (some (fn [y] (contains? (disj ys y) (- x y))) ys)])))
       (drop-while (comp true? second))
       ffirst))

(comment
  (= 776203571 (p1 input))
  (= (p2 input)))
