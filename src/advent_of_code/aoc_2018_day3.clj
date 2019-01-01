(ns advent-of-code.aoc-2018-day3
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def input
  (->> "2018-day3-input"
       io/resource
       slurp
       string/split-lines
       (map (fn [s] (zipmap [:id :x :y :w :h] (map read-string (rest (re-find #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)" s))))))))

(def cloth
  (vec (for [y (range 1000)] (vec (for [x (range 1000)] 0)))))

(defn coords [{:keys [x y w h]}]
  (for [i (range y (+ y h)), j (range x (+ x w))] [i j]))

(defn claim-cloth []
  (reduce
    (fn [c claim]
      (reduce (fn [c [i j]] (update-in c [i j] + 1))
              c (coords claim)))
    cloth input))

(defn part1 []
  (count (filter #(> % 1) (flatten (claim-cloth)))))

(defn part2 []
  (let [cloth (claim-cloth)]
    (->> input
         (filter (fn [claim] (every? (fn [coord] (= 1 (get-in cloth coord))) (coords claim))))
         first
         :id)))

(comment
  (= 107663 (part1))
  (= 1166 (part2)))
