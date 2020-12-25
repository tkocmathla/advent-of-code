(ns aoc-clj.day25
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defn parse [file] (->> file io/resource slurp string/split-lines (map read-string)))
(def test-input (parse "day25_test.txt"))
(def input (parse "day25.txt"))

(defn p1 [[cardkey doorkey]]
  (let [xform (fn [subject n] (mod (* subject n) 20201227))
        loop-n (fn [k] (count (take-while (partial not= k) (iterate (partial xform 7) 1))))]
    (nth (iterate (partial xform doorkey) 1) (loop-n cardkey))))

(comment
  (= 14897079 (p1 test-input))
  (= 448851 (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
