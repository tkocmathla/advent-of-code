(ns aoc-clj.day15
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day15_test.txt" io/resource slurp))
(defonce input (->> "day15.txt" io/resource slurp))

; ------------------------------------------------------------------------------

(defn parse [s] (string/split (string/trimr s) #","))

(defn hash-str [s] (reduce (fn [x ch] (mod (* 17 (+ x (int ch))) 256)) 0 s))

(defn slot [box label] (first (keep-indexed (fn [i [s _]] (when (= s label) i)) box)))

(defn update-box [label len box]
  (let [i (slot box label), lens (cond-> [] len (conj [label len]))]
    (if i
      (into [] (concat (subvec box 0 i) lens (subvec box (inc i))))
      (cond-> box len (into lens)))))

(defn step [boxes s]
  (let [[_ label len] (re-find #"(\w+)[-=](\d+)?" s)]
    (update boxes (hash-str label) #(update-box label (when len (str-int len)) %))))

(defn score-boxes [boxes]
  (let [f (fn [i j [_ len]] (* (inc i) (inc j) len))]
    (reduce + (flatten (map-indexed (fn [i box] (map-indexed (partial f i) box)) boxes)))))

(defn p1 [s] (reduce + (map hash-str (parse s))))
(defn p2 [s] (score-boxes (reduce step (vec (repeat 256 [])) (parse s))))

; ------------------------------------------------------------------------------

(comment
  (= 1320 (p1 test-input))
  (= 513172 (p1 input))

  (= 145 (p2 test-input))
  (= 237806 (p2 input)))
