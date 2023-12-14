(ns aoc-clj.day14
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defonce test-input (->> "day14_test.txt" io/resource slurp))
(defonce input (->> "day14.txt" io/resource slurp))

(defn parse [s] (mapv vec (string/split-lines s)))

(defn rotate- [grid] (mapv (comp vec reverse) (apply mapv vector grid)))
(def rotate (memoize rotate-))

(defn shift-rock [grid row col]
  (let [ch (get-in grid [row col])
        up-ch (get-in grid [(dec row) col])]
    (if (and (= \O ch) (= \. up-ch))
      (-> grid (assoc-in [row col] \.) (assoc-in [(dec row) col] \O))
      grid)))

(defn shift-row [grid row]
  (reduce (fn [g col] (shift-rock g row col)) grid (range (count (grid row)))))

(defn shift-rocks-once [grid]
  (reduce shift-row grid (range 1 (count grid))))

(defn shift-rocks- [grid]
  (->> (iterate shift-rocks-once grid)
       (partition 2 1)
       (drop-while (partial apply not=))
       ffirst))

(def shift-rocks (memoize shift-rocks-))

(defn score-rocks [grid]
  (->> (reverse grid)
       (map-indexed (fn [i row] (* (inc i) (count (filter #{\O} row)))))
       (reduce +)))

(defn spin-cycle [grid cycles]
  (->> (iterate (fn [g] (rotate (shift-rocks g))) grid)
       (drop (* 4 cycles))
       first))

(defn p1 [s] (score-rocks (shift-rocks (parse s))))
(defn p2 [s] (score-rocks (spin-cycle (parse s) 1000)))

(comment
  (= 136 (p1 test-input))
  (= 106990 (p1 input))

  (= 64 (p2 test-input))
  (= 100531 (p2 input)))
