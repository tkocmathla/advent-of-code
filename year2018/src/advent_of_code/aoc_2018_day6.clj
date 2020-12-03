(ns advent-of-code.aoc-2018-day6
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def input
  (->> "2018-day6-input"
       io/resource
       slurp
       string/split-lines
       (map #(map read-string (string/split % #", ")))))

(defn manhattan [[x1 y1] [x2 y2]]
  (+ (Math/abs ^long (- x1 x2)) (Math/abs ^long (- y1 y2))))

(defn distance [goals coord]
  (zipmap goals (map (partial manhattan coord) goals)))

(defn infinites [grid]
  (distinct (concat (first grid) (last grid) (map first grid) (map last grid))))

(defn grid [goals pred]
  (for [x (range (inc (apply max (map first goals))))]
    (for [y (range (inc (apply max (map second goals))))
          :let [m (distance goals [x y])
                [coord i] (apply min-key val m)] ]
      (when (pred m i)
        coord))))

(defn part1 [goals]
  (let [g (grid goals (fn [m i] (<= ((frequencies (vals m)) i) 1)))]
    (->> (apply concat g)
         frequencies
         (#(apply dissoc % (infinites g)))
         vals
         (apply max))))

(defn part2 [goals]
  (let [g (grid goals (fn [m _] (< (reduce + (vals m)) 10000)))]
    (count (remove nil? (apply concat g)))))

(comment
  (= 3882 (part1 input))
  (= 43852 (part2 input)))
