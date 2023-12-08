(ns aoc-clj.day8
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.math.numeric-tower :as math]
   [taoensso.tufte :refer [p profile] :as tufte]))

(defonce test-input-1 (->> "day8_test1.txt" io/resource slurp))
(defonce test-input-2 (->> "day8_test2.txt" io/resource slurp))
(defonce test-input-3 (->> "day8_test3.txt" io/resource slurp))
(defonce input (->> "day8.txt" io/resource slurp))

(defn parse-dirs [s]
  (->> s string/split-lines first seq cycle))

(defn parse-node [s]
  (let [[node left right] (re-seq #"\w+" s)]
    [node {\L left \R right}]))

(defn to-graph [s]
  (let [lines (drop 2 (string/split-lines s))]
    (into {} (map parse-node lines))))

(defn solve-step [g [steps node [dir & dirs]]]
  [(inc steps) (get-in g [node dir]) dirs])

(defn solve-node [g dirs node]
  (->> (iterate (partial solve-step g) [0 node dirs])
       (drop-while (comp not #(string/ends-with? % "Z") second))
       ffirst))

(defn p1 [s]
  (solve-node (to-graph s) (parse-dirs s) "AAA"))

(defn p2 [s]
  (let [g (to-graph s)
        nodes (filter #(string/ends-with? % "A") (keys g))
        steps (map (partial solve-node g (parse-dirs s)) nodes)]
    (reduce math/lcm steps)))

(comment
  (= 2 (p1 test-input-1))
  (= 6 (p1 test-input-2))
  (= 20221 (p1 input))

  (= 6 (p2 test-input-3))
  (= 14616363770447 (p2 input)))
