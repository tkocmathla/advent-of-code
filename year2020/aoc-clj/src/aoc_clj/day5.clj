(ns aoc-clj.day5
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day5.txt"
       io/resource
       slurp
       string/split-lines))

(defn seat [line]
  (let [step (fn [[v [c & s]]]
               (let [half (quot (count v) 2)]
                 (case c
                   (\F \L) [(subvec v 0 half) s]
                   (\B \R) [(subvec v half) s])))
        row (ffirst (nth (iterate step [(vec (range 128)) line]) 7))
        col (ffirst (nth (iterate step [(vec (range 8)) (take-last 3 line)]) 3))]
    (+ col (* row 8))))

(defn p1 [lines]
  (apply max (map seat lines)))

(defn p2 [lines]
  (->> (map seat lines)
       sort
       (reduce (fn [a b] (if (= b (inc a)) b a)))
       inc))

(comment
  (= 955 (p1 input))
  (= 569 (p2 input)))
