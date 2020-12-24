(ns aoc-clj.day24
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defn parse [file] (->> file io/resource slurp string/split-lines))
(def test-input (parse "day24_test.txt"))
(def input (parse "day24.txt"))

(def delta {"nw" [0 1 -1] "ne" [1 0 -1] "e" [1 -1 0] "se" [0 -1 1] "sw" [-1 0 1] "w" [-1 1 0]})

(defn dirs [line] (re-seq #"nw|ne|sw|se|e|w" line))
(defn move [dirs] (reduce (fn [coord dir] (map + coord (delta dir))) [0 0 0] dirs))

(defn p1 [lines]
  (->> (reduce (fn [m line] (update m (move (dirs line)) not)) {} lines)
       (filter (comp true? val))
       count))

(comment
  (= 10 (p1 test-input))
  (= 523 (p1 input))
  (= (p2 input)))
