(ns aoc-clj.day15
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day15_test.txt" io/resource slurp))
(defonce input (->> "day15.txt" io/resource slurp))

(defn parse [s] (string/split (string/trimr s) #","))

(defn hash-char- [hash ch] (mod (* 17 (+ hash (int ch))) 256))
(def hash-char (memoize hash-char-))
(defn hash-str- [s] (reduce hash-char 0 s))
(def hash-str (memoize hash-str-))

(defn box-index [box label]
  (first (keep-indexed (fn [i [s _]] (when (= s label) i)) box)))

(defn remove-label [label box]
  (let [i (box-index box label)]
    (if i
      (into [] (concat (subvec box 0 i) (subvec box (inc i))))
      box)))

(defn merge-label [label len box]
  (let [i (box-index box label)]
    (if i
      (into [] (concat (subvec box 0 i) [[label len]] (subvec box (inc i))))
      (conj box [label len]))))

(defn score-box [box-num slot-num [_ len]]
  (* (inc box-num) (inc slot-num) len))

(defn score-boxes [boxes]
  (->> (map-indexed (fn [box-num box] (map-indexed (partial score-box box-num) box)) boxes)
       flatten
       (reduce +)))

(defn step [boxes s]
  (let [[_ label op len] (re-find #"(\w+)([-=])(\d+)?" s)
        box-num (hash-str label)]
    (case op
      "-" (update boxes box-num (partial remove-label label))
      "=" (update boxes box-num (partial merge-label label (str-int len))))))

(defn p1 [s] (reduce + (map hash-str (parse s))))

(defn p2 [s]
  (->> (parse s)
       (reduce step (vec (repeat 256 [])))
       score-boxes))

(comment
  (= 1320 (p1 test-input))
  (= 513172 (p1 input))

  (= 145 (p2 test-input))
  (= 237806 (p2 input)))
