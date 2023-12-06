(ns aoc-clj.day5
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]
   [taoensso.tufte :refer [p profile] :as tufte]))

(defonce test-input (->> "day5_test.txt" io/resource slurp))
(defonce input (->> "day5.txt" io/resource slurp))

(defn parse-seeds [s]
  (let [seeds (first (string/split-lines s))]
    (map str-int (re-seq #"\d+" seeds))))

(defn parse-map [m]
  (->> (map (partial re-seq #"\d+") m) (map (partial map str-int))))

(defn parse-maps [s]
  (let [raw-maps (->> (string/split-lines s) rest (partition-by empty?) (map rest) (filter seq))]
    (map parse-map raw-maps)))

(defn translate [x m]
  (let [within? (fn [mn sz x] (and (>= x mn) (< x (+ mn sz))))]
    (reduce
      (fn [result [to from size]]
        (if (within? from size x) (+ to (- x from)) result))
      x m)))

(defn p1 [s]
  (let [seeds (parse-seeds s), maps (parse-maps s)]
    (->> (map (fn [seed] (reduce translate seed maps)) seeds)
         (apply min))))

(comment
  (= 35 (p1 test-input))
  (= 324724204 (p1 input))

  (tufte/add-basic-println-handler! {})
  ; 79ms
  (profile {} (p :p1 (dotimes [_ 100] (p1 input)))))
