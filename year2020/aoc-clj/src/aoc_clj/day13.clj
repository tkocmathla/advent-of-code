(ns aoc-clj.day13
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input
  (->> "day13_test.txt"
       io/resource
       slurp
       string/split-lines
       ((fn [[n ds]] [(read-string n) (string/split ds #",")]))))

(def input
  (->> "day13.txt"
       io/resource
       slurp
       string/split-lines
       ((fn [[n ds]] [(read-string n) (string/split ds #",")]))))


(defn p1 [n buses]
  (let [valid (map read-string (remove #{"x"} buses))
        ds (map (fn [d] [d (* d (inc (quot n d)))]) valid)
        [bus d] (apply min-key second ds)]
    (* bus (- d n))))

(comment
  (= 295 (apply p1 test-input))
  (= 370 (apply p1 input))
  (= (p2 (second test-input)))
  (= (p2 (second input))))
