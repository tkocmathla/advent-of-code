(ns aoc-clj.day3
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day3.txt"
       io/resource
       slurp
       string/split-lines))

(defn p1 [lines [dx dy]]
  (let [w (count (first lines))
        h (count lines)
        move (fn [[n y x]]
               [(+ n (if (= \# (get-in lines [y x])) 1 0))
                (+ y dy)
                (mod (+ x dx) w)])]
    (->> [0 0 0] (iterate move) (drop h) ffirst)))

(defn p2 [lines]
  (let [slopes [[1 1] [3 1] [5 1] [7 1] [1 2]]]
    (reduce * (map (partial p1 lines) slopes))))

(comment
  (= 225 (p1 input [3 1]))
  (= 1115775000 (p2 input))
  )

