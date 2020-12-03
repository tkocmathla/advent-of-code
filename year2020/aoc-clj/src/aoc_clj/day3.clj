(ns aoc-clj.day3
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day3.txt"
       io/resource
       slurp
       string/split-lines))

(defn p1
  ([lines] (p1 lines [3 1]))
  ([lines [dj di]]
   (let [w (count (first lines))
         h (count lines)
         move (fn [[n i j]]
                [(+ n (if (= \# (get-in lines [i j])) 1 0))
                 (+ i di)
                 (mod (+ j dj) w)])]
     (->> [0 0 0] (iterate move) (drop h) ffirst))))

(defn p2 [lines]
  (let [slopes [[1 1] [3 1] [5 1] [7 1] [1 2]]]
    (reduce * (map (partial p1 lines) slopes))))

(comment
  (= 225 (p1 input))
  (= 1115775000 (p2 input)))

