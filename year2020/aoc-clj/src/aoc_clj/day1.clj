(ns aoc-clj.day1
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day1.txt"
       io/resource
       slurp
       string/split-lines
       (map read-string)))

(defn p1 [nums]
  (let [xs (set nums)
        x (first (filter #(xs (- 2020 %)) xs))]
    (* x (- 2020 x))))

(defn p2 [nums]
  (let [xset (set nums)]
    (->> (iterate rest nums)
         (keep (fn [[x & xs]] (some (fn [y] (when-let [z (xset (- 2020 (+ x y)))] (* x y z))) xs)))
         first)))

(comment
  (= 1020036 (p1 input))
  (= 286977330 (p2 input)))
