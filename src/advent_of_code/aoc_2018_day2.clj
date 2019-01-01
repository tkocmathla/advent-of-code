(ns advent-of-code.aoc-2018-day2
  (:require
    [clj-fuzzy.metrics :refer [hamming]]
    [clojure.java.io :as io]
    [clojure.math.combinatorics :as combo]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(defonce input
  (->> "2018-day2-input"
       io/resource
       slurp
       string/split-lines))

(defn part1 []
  (let [counts (set (for [x input, [_ n] (frequencies x)
                          :when (#{2 3} n)]
                      [x n]))]
    (* (count (filter (comp #{2} second) counts))
       (count (filter (comp #{3} second) counts)))))

(defn part2 []
  (let [[[x y]] (filter (comp #{1} (partial apply hamming)) (combo/combinations input 2))]
    (apply str (map (fn [c1 c2] (if (= c1 c2) c1)) x y))))

(comment
  (= 7808 (part1))
  (= "efmyhuckqldtwjyvisipargno" (part2)))
