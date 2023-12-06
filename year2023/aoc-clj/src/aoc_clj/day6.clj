(ns aoc-clj.day6
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]
   [taoensso.tufte :refer [p profile] :as tufte]))

(defonce test-input (->> "day6_test.txt" io/resource slurp))
(defonce input (->> "day6.txt" io/resource slurp))

(defn parse [s]
  (->> (string/split-lines s)
       (map (partial re-seq #"\d+"))
       (map (partial map str-int))
       (apply map vector)))

(defn eval-race [race-time hold-time]
  (* hold-time (- race-time hold-time)))

(defn run-race [[race-time max-dist]]
  (->> (map (partial eval-race race-time) (range 1 race-time))
       (filter #(> % max-dist))))

(defn p1 [s]
  (->> (map run-race (parse s)) (map count) (reduce *)))

(defn p2 [race-time max-dist]
  (count (run-race [race-time max-dist])))

(comment
  (= 288 (p1 test-input))
  (= 2374848 (p1 input))

  ; p2
  (= 71503 (p2 71530 940200))
  (= 39132886 (p2 55999793 401148522741405)))
