(ns advent-of-code.aoc-2018-day5
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def input
  (->> "2018-day5-input"
       io/resource
       slurp
       string/trim))

(defn opposite? [x y]
  (= 32 (Math/abs (- (short x) (short y)))))

(defn react [polymer]
  (reduce
    (fn [stack c]
      (if (and (seq stack) (opposite? (peek stack) c))
        (pop stack)
        (conj stack c)))
    '() polymer))

(defn part1 []
  (count (react input)))

(defn part2 []
  (->> (for [i (range 97 123)
             :let [c (char i), C (char (- i 32))
                   s (string/replace input (re-pattern (str "[" c C "]")) "")]]
         (count (react s)))
       (apply min)))

(comment
  (= 11540 (part1))
  (= 6918 (part2)))
