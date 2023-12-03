(ns aoc-clj.day3
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day3_test.txt" io/resource slurp))
(defonce input (->> "day3.txt" io/resource slurp))

(def num? #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9})
(def sym? (complement (into num? #{\.})))

(def dirs [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]])
(defn cell [grid row col] (get (grid row) col))

(defn num-at? [grid row col [dy dx]]
  (when (num? (cell grid (+ row dy) (+ col dx)))
    [(+ row dy) (+ col dx)]))

(defn coords [grid]
  (for [row (range (count grid)), col (range (count (grid 0)))]
    [row col]))

(defn coords-to-num [grid]
  (->> (map-indexed
         (fn [i row]
           (reduce
             (fn [[j m] group]
               (let [next-j (+ j (count group))]
                 (if (some num? group)
                   (let [num (str-int (apply str group))]
                     [next-j (reduce (fn [m' j'] (assoc m' [i j'] num)) m (range j next-j))])
                   [next-j m])))
             [0 {}]
             (partition-by (complement num?) row)))
         grid)
       (map second)
       (into {})))

(defn unique-nums [lut nums]
  (->> (filter identity (sort nums))
       (reduce (fn [[prev v] [y x]]
                 [[y x]
                  (cond-> v
                    (or (nil? prev) (not= (prev 0) y) (> (- x (prev 1)) 1))
                    (conj (lut [y x]))
                    )])
               [nil []])
       second))

(defn p1 [s]
  (let [grid (string/split-lines s)
        lut (coords-to-num grid)]
    (->> (for [[row col] (coords grid)
               :when (sym? (cell grid row col))]
           (unique-nums lut (map (partial num-at? grid row col) dirs)))
         (apply concat)
         (reduce +))))

(defn p2 [s]
  (let [grid (string/split-lines s)
        lut (coords-to-num grid)]
    (->> (for [[row col] (coords grid)
               :when (#{\*} (cell grid row col))
               :let [nums (unique-nums lut (map (partial num-at? grid row col) dirs))]
               :when (= 2 (count nums))]
           (apply * nums))
         (reduce +))))

(comment
  (= 4361 (p1 test-input))
  (= 517021 (p1 input))

  (= 467835 (p2 test-input))
  (= 81296995 (p2 input)))
