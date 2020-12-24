(ns aoc-clj.day24
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

; https://www.redblobgames.com/grids/hexagons/#coordinates

(defn parse [file] (->> file io/resource slurp string/split-lines))
(def test-input (parse "day24_test.txt"))
(def input (parse "day24.txt"))

; ------------------------------------------------------------------------------

(def delta {"nw" [0 1 -1] "ne" [1 0 -1] "e" [1 -1 0] "se" [0 -1 1] "sw" [-1 0 1] "w" [-1 1 0]})

(defn dirs [line] (re-seq #"nw|ne|sw|se|e|w" line))
(defn move [dirs] (reduce (fn [coord dir] (map + coord (delta dir))) [0 0 0] dirs))
(defn install [lines] (reduce (fn [m line] (update m (move (dirs line)) not)) {} lines))

(defn p1 [lines]
  (count (filter (comp true? val) (install lines))))

; ------------------------------------------------------------------------------

(defn neighbors [coord] (map (partial map + coord) (vals delta)))
(defn flip? [m coord]
  (let [n (count (filter m (neighbors coord)))]
    (or
      (and (m coord) (or (zero? n) (> n 2)))
      (and (not (m coord)) (= n 2)))))

(defn p2 [lines]
  (->> (install lines)
       (iterate
         (fn [m]
           (let [coords (set (concat (keys m) (mapcat neighbors (keys m))))
                 flips (filter (partial flip? m) coords)]
             (reduce (fn [m' coord] (update m' coord not)) m flips))))
       (#(nth % 100))
       (filter (comp true? val))
       count))

; ------------------------------------------------------------------------------

(comment
  (= 10 (p1 test-input))
  (= 523 (p1 input))
  (= 2208 (p2 test-input))
  (= 4225 (p2 input)))
